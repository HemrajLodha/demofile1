package com.pws.pateast.fragment.leave.teacher;

import android.view.View;

import com.pws.pateast.R;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.exception.RetrofitException;
import com.pws.pateast.api.model.Leave;
import com.pws.pateast.api.model.Response;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.TeacherService;
import com.pws.pateast.fragment.leave.LeavePresenter;
import com.pws.pateast.utils.DateUtils;
import com.pws.pateast.utils.Utils;

import java.util.Calendar;
import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by intel on 28-Jun-17.
 */

public class TeacherLeavePresenter extends LeavePresenter<TeacherLeaveView> {

    public void getTeacherLeave() {
        teacherService = serviceBuilder.createService(TeacherService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("academicSessionId", String.valueOf(user.getUserdetails().getAcademicSessionId()));
        params.put("masterId", String.valueOf(user.getData().getMasterId()));
        params.put("userId", String.valueOf(user.getData().getId()));

        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put("empleave__leavestatus__eq", String.valueOf(getLeaveStatus()));
        queryParams.put("currentPage", String.valueOf(getView().getPage()));
        queryParams.put("pageLimit", String.valueOf(ServiceBuilder.DATA_LIMIT));

        dispose();
        disposable = teacherService.getTeacherLeave(params, queryParams)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Leave>() {

                    @Override
                    public void onResponse(Leave response) {
                        if (response.getData().isEmpty()) {
                            if (getLeaveStatus() == Leave.PENDING) {
                                onError(new RetrofitException(getString(R.string.no_leave_applied), RetrofitException.ERROR_TYPE_MESSAGE));
                                /*getView().showAction(getString(R.string.apply), true, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        getView().leaveApply();
                                    }
                                });*/
                                getView().setApplyVisible(true);
                            } else {
                                switch (getLeaveStatus()) {
                                    case Leave.APPROVED:
                                        onError(new RetrofitException(getString(R.string.no_approved_leave), RetrofitException.ERROR_TYPE_MESSAGE));
                                        break;
                                    case Leave.REJECTED:
                                        onError(new RetrofitException(getString(R.string.no_rejected_leave), RetrofitException.ERROR_TYPE_MESSAGE));
                                        break;
                                    case Leave.CANCELED:
                                        onError(new RetrofitException(getString(R.string.no_cancelled_leave), RetrofitException.ERROR_TYPE_MESSAGE));
                                        break;
                                }
                            }
                        } else {
                            getView().setPageCount(response.getPageCount());
                            getView().setLeaveAdapter(response.getData());
                            getView().setApplyVisible(true);
                        }
                    }

                    @Override
                    public TeacherLeavePresenter getPresenter() {
                        return TeacherLeavePresenter.this;
                    }
                });
    }

    public void cancelTeacherLeave(int leaveId) {
        getView().showProgressDialog(getString(R.string.wait));
        teacherService = serviceBuilder.createService(TeacherService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("status_updatedby", String.valueOf(user.getData().getId()));
        params.put("status_updatedbytype", user.getData().getUser_type());
        params.put("date", DateUtils.toDate(Calendar.getInstance().getTime(), "yyyy-MM-dd"));


        disposable = teacherService.cancelTeacherLeave(params, String.valueOf(leaveId))
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Response>() {
                    @Override
                    public boolean openDialog() {
                        return true;
                    }

                    @Override
                    public void onResponse(Response response) {
                        getView().onActionClick();
                        Utils.showToast(getContext(), response.getMessage());
                    }

                    @Override
                    public TeacherLeavePresenter getPresenter() {
                        return TeacherLeavePresenter.this;
                    }
                });
    }
}
