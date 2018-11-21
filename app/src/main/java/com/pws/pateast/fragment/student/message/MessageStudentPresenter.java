package com.pws.pateast.fragment.student.message;

import android.text.TextUtils;

import com.pws.pateast.utils.Utils;
import com.pws.pateast.R;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.model.Response;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.TeacherService;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.utils.Preference;

import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by intel on 11-Jul-17.
 */

public class MessageStudentPresenter extends AppPresenter<MessageStudentView> {
    @Inject
    public Preference preference;

    @Inject
    public ServiceBuilder serviceBuilder;

    protected User user;

    protected TeacherService apiService;

    private MessageStudentView messageView;

    @Override
    public MessageStudentView getView() {
        return messageView;
    }

    @Override
    public void attachView(MessageStudentView view) {
        messageView = view;
        getComponent().inject(this);
        user = preference.getUser();
    }

    public void sendMessage() {
        if (getView().getMessage() == null || TextUtils.isEmpty(getView().getMessage())) {
            getView().setError(R.id.til_message, getString(R.string.validate_edittext, getString(R.string.menu_message)));
            return;
        } else {
            getView().setError(R.id.til_message, null);
        }
        getView().showProgressDialog(getString(R.string.wait));
        apiService = serviceBuilder.createService(TeacherService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("userId", String.valueOf(user.getData().getId()));
        params.put("academicSessionId", String.valueOf(user.getUserdetails().getAcademicSessionId()));
        params.put("masterId", String.valueOf(user.getData().getMasterId()));
        params.put("id", String.valueOf(getView().getStudentId()));
        params.put("message", getView().getMessage());

        disposable = apiService.sendMessageToStudent(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Response>() {
                    @Override
                    public boolean openDialog() {
                        return true;
                    }

                    @Override
                    public boolean isDismiss() {
                        return false;
                    }

                    @Override
                    public void onResponse(Response response) {
                        MessageStudentPresenter.this.getView().dismiss();
                        Utils.showToast(getContext(), response.getMessage());
                    }

                    @Override
                    public MessageStudentPresenter getPresenter() {
                        return MessageStudentPresenter.this;
                    }
                });
    }
}
