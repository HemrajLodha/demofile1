package com.pws.pateast.base;

import android.view.View;

import com.base.view.BaseView;
import com.pws.pateast.MyApplication;
import com.pws.pateast.di.component.ActivityComponent;
import com.pws.pateast.listener.AdapterListener;

/**
 * Created by intel on 20-Apr-17.
 */

public interface AppView extends BaseView
{
    MyApplication getApp();

    ActivityComponent getComponent();

    void showMessage(String message, boolean visible);

    void showAction(String text, boolean visible,View.OnClickListener onClickListener);

    void showIcon(int icon, boolean visible);

    void showActionButton(boolean visible);

    void showResponseView(boolean visible);

    void showProgress(boolean visible);

    void showProgressDialog(String title);

    void hideProgressDialog();

    void showDialog(String title, String message, AdapterListener<String> listener, int... args);

    void hideDialog();

    void showMessage(String message, boolean show, int color);

    void showMessage(int message, boolean show, int color);

    void onActionClick();
}
