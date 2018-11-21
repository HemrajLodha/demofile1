package com.pws.pateast.base;

import android.content.Context;

import com.base.presenter.BasePresenter;
import com.pws.pateast.di.component.ActivityComponent;
import com.pws.pateast.MyApplication;

import io.reactivex.disposables.Disposable;

/**
 * Created by intel on 20-Apr-17.
 */

public abstract class AppPresenter<V extends AppView> implements BasePresenter<V>
{
    protected Disposable disposable;

    public ActivityComponent getComponent()
    {
        return getView().getComponent();
    }

    public MyApplication getApp()
    {
        return getView().getApp();
    }

    @Override
    public Context getContext()
    {
        return getView().getContext();
    }

    @Override
    public String getString(int string)
    {
        return getContext().getString(string);
    }

    @Override
    public String getString(int string,Object... args)
    {
        return getContext().getString(string,args);
    }

    @Override
    public void detachView() {
        dispose(getView());
    }

    public void dispose(V v)
    {
        dispose();
        v = null;
    }

    public void dispose()
    {
        if(disposable != null && !disposable.isDisposed())
        {
            disposable.dispose();
            disposable = null;
        }
    }


}
