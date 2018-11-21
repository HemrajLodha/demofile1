package com.pws.pateast.api.interceptor;

import android.content.Context;
import android.support.annotation.StringRes;

import com.pws.pateast.R;
import com.pws.pateast.api.exception.NoConnectivityException;
import com.pws.pateast.api.exception.RetrofitException;
import com.pws.pateast.di.component.ApplicationComponent;
import com.pws.pateast.utils.NetworkUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by intel on 20-Apr-17.
 */

public class ConnectivityInterceptor implements Interceptor {
    protected Context mContext;
    protected ApplicationComponent mApplicationComponent;

    public ConnectivityInterceptor(ApplicationComponent applicationComponent, Context context) {
        mApplicationComponent = applicationComponent;
        mContext = context;
    }

    protected String getString(@StringRes int resId) {
        return mContext.getString(resId);
    }

    protected boolean isNotConnected() {
        return !NetworkUtil.isOnline(mContext);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (isNotConnected())
            throw new NoConnectivityException(getString(R.string.no_network));
        try {
            return chain.proceed(chain.request());
        } catch (Exception e) {
            throw new RetrofitException(getString(R.string.something_went_wrong));
        }
    }

    protected ResponseBody getResponseBody(ResponseBody responseBody, String responseString) throws IOException {
        return ResponseBody.create(
                responseBody.contentType(),
                responseString.getBytes());
    }

}
