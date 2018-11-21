package com.pws.pateast.fragment.leave.teacher.info;

import com.pws.pateast.R;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.model.LeaveInfo;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.TeacherService;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.utils.Preference;
import com.pws.pateast.utils.Utils;

import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by intel on 28-Jun-17.
 */

public class TeacherLeaveInfoPresenter extends AppPresenter<TeacherLeaveInfoView> {
    @Inject
    Preference preference;

    @Inject
    ServiceBuilder serviceBuilder;

    private User user;

    private TeacherService apiService;

    private TeacherLeaveInfoView leaveView;

    @Override
    public TeacherLeaveInfoView getView() {
        return leaveView;
    }

    @Override
    public void attachView(TeacherLeaveInfoView view) {
        leaveView = view;
        getComponent().inject(this);
        user = preference.getUser();
    }


    public void getTeacherLeaveInfo(int leaveId) {
        getView().showProgressDialog(getString(R.string.wait));
        apiService = serviceBuilder.createService(TeacherService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("academicSessionId", String.valueOf(user.getUserdetails().getAcademicSessionId()));
        params.put("masterId", String.valueOf(user.getData().getMasterId()));
        params.put("userId", String.valueOf(user.getData().getId()));
        params.put("user_type", user.getData().getUser_type());

        disposable = apiService.getTeacherLeaveInfo(params, String.valueOf(leaveId))
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<LeaveInfo>() {
                    @Override
                    public boolean openDialog() {
                        return true;
                    }

                    @Override
                    public void onResponse(LeaveInfo response) {
                        if (response.isStatus()) {
                            TeacherLeaveInfoPresenter.this.getView().setLeaveInfoAdapter(response.getData());
                        } else {
                            Utils.showToast(getContext(), response.getMessage());
                        }
                    }

                    @Override
                    public TeacherLeaveInfoPresenter getPresenter() {
                        return TeacherLeaveInfoPresenter.this;
                    }
                });
    }
}
