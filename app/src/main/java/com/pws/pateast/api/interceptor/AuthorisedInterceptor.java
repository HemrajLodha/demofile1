package com.pws.pateast.api.interceptor;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.pws.pateast.R;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.exception.NoConnectivityException;
import com.pws.pateast.api.exception.RetrofitException;
import com.pws.pateast.api.model.AccessToken;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.service.APIService;
import com.pws.pateast.di.component.ApplicationComponent;
import com.pws.pateast.events.AuthenticationErrorEvent;
import com.pws.pateast.utils.Preference;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;

import static com.pws.pateast.api.exception.RetrofitException.ERROR_TYPE_UNAUTHORIZED;

public class AuthorisedInterceptor extends ConnectivityInterceptor {

    @Inject
    public Preference preference;
    @Inject
    public ServiceBuilder serviceBuilder;
    @Inject
    EventBus eventBus;

    private AccessToken accessToken;
    private User user;

    public AuthorisedInterceptor(ApplicationComponent applicationComponent, Context context) {
        super(applicationComponent, context);
        applicationComponent.inject(this);
    }


    @Override
    public Response intercept(Chain chain) throws IOException {
        if (isNotConnected())
            throw new NoConnectivityException(getString(R.string.no_network));
        Response response = null;
        ResponseBody responseBody = null;
        String responseString;
        try {
            Request original = chain.request();
            accessToken = preference.getAccessToken();
            user = preference.getUser();
            if (accessToken != null) {
                response = chain.proceed(ServiceBuilder.requestBuild(original, accessToken.bearer()).build());
                if (response.code() == 401) {
                    if (user != null) {
                        APIService apiService = serviceBuilder.createToken(APIService.class);
                        Call<AccessToken> accessTokenCall = apiService.refreshAccessToken(
                                AccessToken.REFRESH_TOKEN,
                                ServiceBuilder.DEVICE_TOKEN,
                                user.getData().getUser_name(),
                                user.getData().getPassword(),
                                ServiceBuilder.CLIENT_ID,
                                ServiceBuilder.CLIENT_SECRET,
                                accessToken.getRefreshToken());
                        Response refreshResponse = chain.proceed(ServiceBuilder.requestBuild(accessTokenCall.request()).build());
                        if (refreshResponse.code() == 200) {
                            accessToken = new Gson().fromJson(refreshResponse.body().string(), AccessToken.class);
                            preference.setAccessToken(accessToken);
                            response = chain.proceed(ServiceBuilder.requestBuild(original, accessToken.bearer()).build());
                            if (response.code() == 401) {
                                unAuthorisedUser();
                            }
                        } else if (refreshResponse.code() == 400) {
                            unAuthorisedUser();
                        } else {
                            throw new RetrofitException(getString(R.string.something_went_wrong));
                        }
                    }
                } else if (response.code() != 200) {
                    throw new RetrofitException(getString(R.string.something_went_wrong));
                }
            } else {
                unAuthorisedUser();
            }
            responseBody = response.body();
            responseString = responseBody.string();
            return response.newBuilder()
                    .body(getResponseBody(responseBody, responseString))
                    .build();
        } catch (Exception e) {
            if (e instanceof RetrofitException)
                throw new RetrofitException(getString(R.string.something_went_wrong), ((RetrofitException) e).getErrorType());
            else
                throw new RetrofitException(getString(R.string.something_went_wrong));
        }
    }

    public void unAuthorisedUser() throws RetrofitException {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                eventBus.post(new AuthenticationErrorEvent());
            }
        });
        throw new RetrofitException("", ERROR_TYPE_UNAUTHORIZED);
    }

}