package com.pws.pateast.base;

import android.content.Context;

import com.base.presenter.BasePresenter;
import com.pws.pateast.MyApplication;
import com.pws.pateast.di.component.DaggerServiceComponent;
import com.pws.pateast.di.component.ServiceComponent;
import com.pws.pateast.di.module.ServiceModule;

import io.reactivex.disposables.Disposable;

/**
 * Created by intel on 21-Jul-17.
 */

public class ServicePresenter<V extends ServiceView> implements BasePresenter<V> {
    protected Disposable disposable;

    private V view;

    @Override
    public V getView() {
        return view;
    }

    @Override
    public void attachView(V view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        dispose();
        view = null;
    }

    @Override
    public Context getContext() {
        return getView().getContext();
    }

    @Override
    public String getString(int string) {
        return getContext().getString(string);
    }

    @Override
    public String getString(int string, Object... args) {
        return getContext().getString(string, args);
    }

    public ServiceComponent getServiceComponent() {
        return DaggerServiceComponent
                .builder()
                .applicationComponent(getView().getApplicationComponent())
                .serviceModule(new ServiceModule(getContext()))
                .build();
    }

    public MyApplication getApp() {
        return getView().getApp();
    }

    public void dispose(V v) {
        dispose();
        v = null;
    }

    public void dispose() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            disposable = null;
        }
    }
}
