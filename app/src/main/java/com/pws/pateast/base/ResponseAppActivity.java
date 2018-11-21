package com.pws.pateast.base;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.pws.pateast.R;
import com.pws.pateast.service.socket.SocketService;
import com.pws.pateast.utils.DialogUtils;
import com.pws.pateast.widget.ServerResponseView;

/**
 * Created by planet on 5/13/2017.
 */

public abstract class ResponseAppActivity extends AppActivity {
    private ServerResponseView responseView;
    private ProgressDialog progressDialog;
    private ProgressBar progressBar;

    private SocketService mSocketService;
    boolean mBound = false;

    public abstract boolean isToolbarSetupEnabled();

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        if (isToolbarSetupEnabled()) {
            setupToolbar((Toolbar) findViewById(R.id.toolbar));
            if (getBaseActionBar() != null)
                getBaseActionBar().setDisplayHomeAsUpEnabled(true);
        }
        progressDialog = DialogUtils.getProgressDialog(this, R.string.loading, false);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        responseView = (ServerResponseView) findViewById(R.id.response_view);
    }

    @Override
    public ProgressDialog getProgressDialog() {
        return progressDialog;
    }

    @Override
    public ProgressBar getProgressBar() {
        return progressBar;
    }

    @Override
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

    @Override
    public void showMessage(String message, boolean visible) {
        if (responseView != null) {
            responseView.setResponseMessage(message);
            responseView.setResponseMessageVisibility(visible);
        }
    }


    @Override
    public void showIcon(int icon, boolean visible) {
        if (responseView != null) {
            if (icon != 0) {
                responseView.setImgIcon(icon);
            }
            responseView.setIconVisibility(visible);
        }
    }

    @Override
    public void showActionButton(boolean visible) {
        if (responseView != null) {
            responseView.setActionButtonVisibility(visible);
        }
    }

    @Override
    public void showResponseView(boolean visible) {
        if (responseView != null) {
            responseView.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    public void showProgress(boolean visible) {
        if (getProgressBar() != null)
            getProgressBar().setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void showProgressDialog(String title) {
        showProgress(false);
        getProgressDialog().setTitle(title);
        getProgressDialog().show();
    }

    public void hideProgressDialog() {
        getProgressDialog().dismiss();
    }

    @Override
    public void onActionClick() {
        showProgress(true);
        showResponseView(false);
    }

    @Override
    public void bindSocketService() {
        Intent mSocketServiceIntent = new Intent(this, SocketService.class);
        bindService(mSocketServiceIntent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void unBindSocketService() {
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            SocketService.SocketBinder binder = (SocketService.SocketBinder) service;
            mSocketService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
}
