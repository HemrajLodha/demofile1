package com.pws.pateast.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ProgressBar;

import com.base.ui.fragment.BaseFragment;
import com.pws.pateast.MyApplication;
import com.pws.pateast.di.component.ActivityComponent;
import com.pws.pateast.listener.AdapterListener;
import com.pws.pateast.listener.AppListener;

/**
 * Created by intel on 18-Apr-17.
 */

public abstract class AppFragment extends BaseFragment {
    public static final String EXTRA_DATA = "bundle_extra_data";
    private AppListener appListener;

    public static <T extends AppFragment> T newInstance(Class<T> fragmentClass) {
        return newInstance(fragmentClass, null, new Bundle());
    }

    public static <T extends AppFragment> T newInstance(Class<T> fragmentClass, AppListener appListener) {
        return newInstance(fragmentClass, appListener, new Bundle());
    }

    public static <T extends AppFragment> T newInstance(Class<T> fragmentClass, AppListener appListener, Bundle bundle) {
        T fragment = null;
        try {
            fragment = fragmentClass.newInstance();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        fragment.setAppListener(appListener);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static <T extends AppFragment> T newInstance(Class<T> fragmentClass, Bundle bundle) {
        T fragment = null;
        try {
            fragment = fragmentClass.newInstance();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        fragment.setArguments(bundle);
        return fragment;
    }


    public void setAppListener(AppListener appListener) {
        this.appListener = appListener;
    }


    public AppListener getAppListener() {
        if (appListener == null)
            setAppListener((AppListener) getActivity());
        return appListener;
    }


    public ProgressDialog getProgressDialog() {
        return getAppListener().getProgressDialog();
    }

    public ProgressBar getProgressBar() {
        return getAppListener().getProgressBar();
    }

    public ActivityComponent getComponent() {
        return getAppListener().getComponent();
    }


    public MyApplication getApp() {
        return getAppListener().getApp();
    }


    public void showAction(String text, boolean visible, View.OnClickListener onClickListener) {
        getAppListener().showAction(text, visible, onClickListener);
    }


    public void showMessage(String message, boolean visible) {
        getAppListener().showMessage(message, visible);
    }


    public void showIcon(int icon, boolean visible) {
        getAppListener().showIcon(icon, visible);
    }

    public void showActionButton(boolean visible) {
        getAppListener().showActionButton(visible);
    }

    public void showResponseView(boolean visible) {
        getAppListener().showResponseView(visible);
    }

    public void showProgress(boolean visible) {

        if (getView() != null) {
            getView().setVisibility(visible ? View.GONE : View.VISIBLE);
        }

        if (getProgressBar() != null)
            getProgressBar().setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void showMessage(String message, boolean show, int color) {
        getAppListener().showMessage(message, show, color);
    }


    public void showMessage(int message, boolean show, int color) {
        getAppListener().showMessage(message, show, color);
    }

    public void showProgressDialog(String title) {
        showProgress(false);
        getProgressDialog().setTitle(title);
        getProgressDialog().show();
    }

    public void showDialog(String title, String message, AdapterListener<String> listener, int... args) {
        getAppListener().showDialog(title, message, listener, args);
    }

    public void hideDialog() {
        getAppListener().hideDialog();
    }

    public void hideProgressDialog() {
        getProgressDialog().dismiss();
    }

    public void onActionClick() {
        showProgress(true);
        showResponseView(false);
    }

    /**
     * override in child class and return true if need options menu
     *
     * @return
     */
    @Override
    protected boolean hasOptionMenu() {
        return false;
    }
}
