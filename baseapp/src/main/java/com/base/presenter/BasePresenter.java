package com.base.presenter;

import android.content.Context;

import com.base.view.BaseView;

public interface BasePresenter<V extends BaseView> {


    V getView();

    void attachView(V view);

    void detachView();

    Context getContext();

    String getString(int string);

    String getString(int string,Object... args);
}