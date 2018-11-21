package com.pws.pateast.api.observer;

import com.pws.pateast.R;
import com.pws.pateast.api.exception.NoConnectivityException;
import com.pws.pateast.api.exception.RetrofitException;
import com.pws.pateast.api.model.Response;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.base.AppView;

import io.reactivex.observers.DisposableSingleObserver;

import static com.pws.pateast.api.exception.RetrofitException.ERROR_TYPE_MESSAGE;

/**
 * Created by intel on 21-Apr-17.
 */

public abstract class AppSingleObserver<T extends Response> extends DisposableSingleObserver<T> {
    public abstract void onResponse(T response);

    public abstract AppPresenter getPresenter();

    public boolean openDialog() {
        return false;
    }

    public boolean isDismiss() {
        return true;
    }

    private AppView getView() {
        return (AppView) getPresenter().getView();
    }

    public void hide() {
        getView().showProgress(false);
        getView().hideProgressDialog();
        getView().showResponseView(false);
    }

    @Override
    public void onSuccess(T value) {
        dispose();
        if (value.isStatus()) {
            hide();
            onResponse(value);
        } else {
            onError(new RetrofitException(value.getMessage(), ERROR_TYPE_MESSAGE));
        }
    }

    @Override
    public void onError(Throwable e) {
        dispose();
        getView().showProgress(false);
        getView().hideProgressDialog();
        if (e instanceof RetrofitException && ((RetrofitException) e).getErrorType() == RetrofitException.ERROR_TYPE_UNAUTHORIZED) {
            hide();
            return;
        }
        if (!openDialog()) {
            getView().showAction(getView().getString(R.string.retry), true, null);
            getView().showMessage(e.getMessage(), true);
            if (e instanceof NoConnectivityException)
                getView().showIcon(R.drawable.ic_cloud_off, true);
            else
                getView().showIcon(R.drawable.ic_cloud_download, true);
            getView().showResponseView(true);
            // added new on 05/10/17
            if (e instanceof RetrofitException && ((RetrofitException) e).getErrorType() == RetrofitException.ERROR_TYPE_MESSAGE) {
                getView().showIcon(0, false);
                getView().showActionButton(false);
            }
        } else {
            getView().showMessage(e.getMessage(), isDismiss(), R.attr.colorPrimary);
        }
    }
}
