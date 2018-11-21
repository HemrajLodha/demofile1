package com.pws.pateast.fragment.leave.teacher.approve;

import android.text.TextUtils;

import com.pws.pateast.R;
import com.pws.pateast.api.exception.RetrofitException;
import com.pws.pateast.api.model.Leave;
import com.pws.pateast.api.model.Response;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.TeacherService;
import com.pws.pateast.enums.LeaveType;
import com.pws.pateast.fragment.leave.LeavePresenter;
import com.pws.pateast.utils.DateUtils;
import com.pws.pateast.utils.Utils;

import java.util.Calendar;
import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.pws.pateast.api.exception.RetrofitException.ERROR_TYPE_MESSAGE;

/**
 * Created by intel on 29-Jun-17.
 */

public class ClassLeavePresenter extends LeavePresenter<ClassLeaveView> {

    public void getClassLeave() {
        teacherService = serviceBuilder.createService(TeacherService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("academicSessionId", String.valueOf(user.getUserdetails().getAcademicSessionId()));
        params.put("masterId", String.valueOf(user.getData().getMasterId()));
        params.put("userId", String.valueOf(user.getUserdetails().getUserId()));
        params.put("user_type", String.valueOf(user.getData().getUser_type()));

        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put("studentleave__leavestatus", String.valueOf(getLeaveStatus()));
        queryParams.put("page", String.valueOf(getView().getPage()));

        if (getView().getStudentName() != null && !TextUtils.isEmpty(getView().getStudentName()))
            queryParams.put("userdetail__fullname", getView().getStudentName());
        if (getView().getStartDate() != null && !TextUtils.isEmpty(getView().getStartDate()))
            queryParams.put("studentleave__start_date", getView().getStartDate());
        if (getView().getEndDate() != null && !TextUtils.isEmpty(getView().getEndDate()))
            queryParams.put("studentleave__end_date", getView().getEndDate());


        disposable = teacherService.getClassLeaves(params, queryParams)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Leave>() {

                    @Override
                    public void onResponse(Leave response) {
                        if (response.getData().isEmpty()) {
                            switch (LeaveType.getLeaveType(ClassLeavePresenter.this.getView().getLeaveStatus())) {
                                case PENDING:
                                    onError(new RetrofitException(getString(R.string.no_pending_leave), ERROR_TYPE_MESSAGE));
                                    break;
                                case APPROVED:
                                    onError(new RetrofitException(getString(R.string.no_approved_leave), ERROR_TYPE_MESSAGE));
                                    break;
                                case CANCELED:
                                    onError(new RetrofitException(getString(R.string.no_cancelled_leave), ERROR_TYPE_MESSAGE));
                                    break;
                                case REJECTED:
                                    onError(new RetrofitException(getString(R.string.no_rejected_leave), ERROR_TYPE_MESSAGE));
                                    break;
                                default:
                                    onError(new RetrofitException(getString(R.string.no_leave_applied), ERROR_TYPE_MESSAGE));
                                    break;
                            }
                        } else {
                            ClassLeavePresenter.this.getView().setPageCount(response.getPageCount());
                            ClassLeavePresenter.this.getView().setLeaveAdapter(response.getData());
                        }
                    }

                    @Override
                    public ClassLeavePresenter getPresenter() {
                        return ClassLeavePresenter.this;
                    }
                });
    }

    public void changeStudentLeaveStatus(int leaveId, int status) {
        getView().showProgressDialog(getString(R.string.wait));
        teacherService = serviceBuilder.createService(TeacherService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("masterId", String.valueOf(user.getData().getMasterId()));
        params.put("date", DateUtils.toDate(Calendar.getInstance().getTime(), "yyyy-MM-dd"));
        params.put("status_updatedby", String.valueOf(user.getData().getId()));
        params.put("status_updatedbytype", user.getData().getUser_type());


        disposable = teacherService.changeStudentLeaveStatus(params, String.valueOf(leaveId), String.valueOf(status))
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Response>() {
                    @Override
                    public boolean openDialog() {
                        return true;
                    }

                    @Override
                    public void onResponse(Response response) {
                        ClassLeavePresenter.this.getView().onActionClick();
                        Utils.showToast(getContext(), response.getMessage());
                    }

                    @Override
                    public ClassLeavePresenter getPresenter() {
                        return ClassLeavePresenter.this;
                    }
                });
    }
}
