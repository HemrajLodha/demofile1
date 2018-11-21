package com.pws.pateast.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.base.ui.fragment.dialog.BaseDialogFragment;
import com.pws.pateast.MyApplication;
import com.pws.pateast.R;
import com.pws.pateast.di.component.ActivityComponent;
import com.pws.pateast.listener.AdapterListener;
import com.pws.pateast.listener.AppListener;
import com.pws.pateast.utils.DialogUtils;
import com.pws.pateast.widget.ServerResponseView;

/**
 * Created by intel on 24-Apr-17.
 */

public abstract class AppDialogFragment extends BaseDialogFragment {
    private CardView toolbar;
    private TextView tvTitle;
    private ImageView imgClear;
    private AppListener appListener;
    private ServerResponseView responseView;
    private ProgressBar progressBar;
    private ProgressDialogFragment progressDialog;
    private LinearLayout contentDialog;

    public static <T extends AppDialogFragment> T newInstance(Class<T> fragmentClass) {
        return newInstance(fragmentClass, null, new Bundle());
    }

    public static <T extends AppDialogFragment> T newInstance(Class<T> fragmentClass, AppListener appListener) {
        return newInstance(fragmentClass, appListener, new Bundle());
    }

    public static <T extends AppDialogFragment> T newInstance(Class<T> fragmentClass, AppListener appListener, Bundle bundle) {
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

    abstract protected int getContentLayout();

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_app_dialog;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        toolbar = (CardView) findViewById(R.id.toolbar);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        imgClear = (ImageView) findViewById(R.id.img_clear);
        progressDialog = DialogUtils.getProgressFragmentDialog(getContext(), R.string.loading);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        responseView = (ServerResponseView) findViewById(R.id.response_view);
        imgClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        contentDialog = (LinearLayout) findViewById(R.id.content_dialog);
        mInflater.inflate(getContentLayout(), contentDialog, true);
    }

    public void setToolbarVisibility(int visible) {
        toolbar.setVisibility(visible);
    }

    public void setTitle(String title) {
        if (tvTitle != null)
            tvTitle.setText(title);
        else
            toolbar.setVisibility(View.GONE);
    }

    @Override
    public Context getContext() {
        return super.getContext();
    }

    public void setTitle(int title) {
        setTitle(getString(title));
    }

    public void setAppListener(AppListener appListener) {
        this.appListener = appListener;
    }

    public ProgressDialogFragment getProgressDialog() {
        return progressDialog;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public AppListener getAppListener() {
        if (appListener == null)
            setAppListener((AppListener) getActivity());
        return appListener;
    }

    public View getContentDialog() {
        return contentDialog;
    }

    public ActivityComponent getComponent() {
        return getAppListener().getComponent();
    }


    public MyApplication getApp() {
        return getAppListener().getApp();
    }


    public void showAction(String text, boolean visible, View.OnClickListener onClickListener) {
        if (responseView != null) {
            responseView.setActionButtonText(text);
            responseView.setActionButtonVisibility(visible);
            responseView.setOnActionClickListener(onClickListener == null ? new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onActionClick();
                }
            } : onClickListener);
        }
    }


    public void showMessage(String message, boolean visible) {
        if (responseView != null) {
            responseView.setResponseMessage(message);
            responseView.setResponseMessageVisibility(visible);
        }
    }


    public void showIcon(int icon, boolean visible) {
        if (responseView != null) {
            responseView.setImgIcon(icon);
            responseView.setIconVisibility(visible);
        }
    }

    public void showActionButton(boolean visible) {
        if (responseView != null) {
            responseView.setActionButtonVisibility(visible);
        }
    }

    public void showResponseView(boolean visible) {
        if (responseView != null) {
            responseView.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    public void showProgress(boolean visible) {
        getContentDialog().setVisibility(visible ? View.GONE : View.VISIBLE);
        if (getProgressBar() != null)
            getProgressBar().setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void showProgressDialog(String title) {
        showProgress(false);
        getProgressDialog().setTitle(title);
        getProgressDialog().show(getChildFragmentManager(), AppDialogFragment.class.getSimpleName());
    }

    public void hideProgressDialog() {
        if (getProgressDialog().isAdded())
            getProgressDialog().dismiss();
    }

    public void showDialog(String title, String message, AdapterListener<String> listener, int... args) {
        getAppListener().showDialog(title, message, listener, args);
    }

    public void hideDialog() {
        getAppListener().hideDialog();
    }

    public void onActionClick() {
        showProgress(true);
        showResponseView(false);
    }


    public void showMessage(String message, final boolean show, int color) {
        DialogUtils.showDialog(getContext(), getString(R.string.app_name), message, new AdapterListener<String>() {
            @Override
            public void onClick(int id, String value) {
                if (show)
                    dismiss();
            }
        }, R.string.ok);
    }


    public void showMessage(int message, boolean show, int color) {
        showMessage(getString(message), show, color);
    }
}
