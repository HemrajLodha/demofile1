package com.pws.pateast.api.observer;

import com.pws.pateast.api.exception.RetrofitException;
import com.pws.pateast.api.model.Response;
import com.pws.pateast.base.ServicePresenter;

import io.reactivex.observers.DisposableSingleObserver;

/**
 * Created by intel on 25-Jul-17.
 */

public abstract class ServiceSingleObserver<T extends Response> extends DisposableSingleObserver<T> {
    public abstract void onResponse(T response);

    public abstract ServicePresenter getPresenter();

    @Override
    public void onSuccess(T value) {
        dispose();
        if (value.isStatus()) {
            onResponse(value);
        } else {
            onError(new RetrofitException(value.getMessage()));
        }
    }

    @Override
    public void onError(Throwable e) {
        dispose();
        e.printStackTrace();
    }
}
