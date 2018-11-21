package com.pws.pateast.api.interceptor;


import android.content.Context;

import com.google.gson.Gson;
import com.pws.pateast.R;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.exception.NoConnectivityException;
import com.pws.pateast.api.exception.RetrofitException;
import com.pws.pateast.api.model.AccessToken;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.service.APIService;
import com.pws.pateast.di.component.ApplicationComponent;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.utils.Preference;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by intel on 07-Apr-17.
 */

public class LoginInterceptor extends ConnectivityInterceptor {

    @Inject
    public Preference preference;
    @Inject
    public ServiceBuilder serviceBuilder;

    private APIService apiService;

    public LoginInterceptor(ApplicationComponent applicationComponent, Context context) {
        super(applicationComponent, context);
        applicationComponent.inject(this);
    }


    @Override
    public Response intercept(Chain chain) throws IOException {
        if (isNotConnected())
            throw new NoConnectivityException(getString(R.string.no_network));
        Response originalResponse = null;
        ResponseBody responseBody = null;
        String responseString;
        try {
            Request original = chain.request();
            originalResponse = chain.proceed(ServiceBuilder.requestBuild(original, ServiceBuilder.basic()).build());
            responseBody = originalResponse.body();
            responseString = responseBody.string();
            User user = new Gson().fromJson(responseString, User.class);
            if (user.isStatus() && user.getData() != null) {
                switch (UserType.getUserType(user.getData().getUser_type())) {
                    case PARENT:
                    case STUDENT:
                    case TEACHER:
                    case DRIVER:
                        break;
                    default:
                        throw new RetrofitException(getString(R.string.invalid_login_detail));
                }

                apiService = serviceBuilder.createToken(APIService.class);
                Call<AccessToken> accessTokenCall = apiService.generateAccessToken(
                        AccessToken.ACCESS_TOKEN,
                        ServiceBuilder.DEVICE_TOKEN,
                        user.getData().getUser_name(),
                        user.getData().getPassword(),
                        ServiceBuilder.CLIENT_ID,
                        ServiceBuilder.CLIENT_SECRET);
                Response accessTokenResponse = chain.proceed(ServiceBuilder.requestBuild(accessTokenCall.request()).build());
                if (accessTokenResponse.code() == 200) {
                    AccessToken accessToken = new Gson().fromJson(accessTokenResponse.body().string(), AccessToken.class);
                    if (accessToken.getCode() == 0) {
                        preference.setUser(user);
                        preference.setNotification(user.getData().getIs_notification() == 1);
                        preference.setAccessToken(accessToken);
                    } else {
                        throw new RetrofitException(getString(R.string.something_went_wrong));
                    }
                } else {
                    throw new RetrofitException(getString(R.string.something_went_wrong));
                }
            }
            return originalResponse.newBuilder()
                    .body(getResponseBody(responseBody, responseString))
                    .build();
        } catch (Exception e) {
            throw new RetrofitException(getString(R.string.something_went_wrong));
        }
    }


}
