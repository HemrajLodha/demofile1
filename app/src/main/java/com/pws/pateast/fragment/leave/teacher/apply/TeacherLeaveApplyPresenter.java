package com.pws.pateast.fragment.leave.teacher.apply;

import android.text.TextUtils;

import com.pws.pateast.R;
import com.pws.pateast.api.model.LeaveType;
import com.pws.pateast.api.model.Response;
import com.pws.pateast.api.model.Tag;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.TeacherService;
import com.pws.pateast.fragment.leave.LeaveApplyPresenter;
import com.pws.pateast.utils.DateUtils;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by intel on 23-Aug-17.
 */

public class TeacherLeaveApplyPresenter extends LeaveApplyPresenter<TeacherLeaveApplyView> {


    @Override
    public void attachView(TeacherLeaveApplyView view) {
        super.attachView(view);
        getLeaveReasonAndType();
    }


    public void getLeaveReasonAndType() {
        getView().showProgressDialog(getString(R.string.loading));
        teacherService = serviceBuilder.createService(TeacherService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("masterId", String.valueOf(user.getData().getMasterId()));
        params.put("academicSessionId", String.valueOf(user.getUserdetails().getAcademicSessionId()));
        params.put("userId", String.valueOf(user.getData().getId()));

        disposable = teacherService.getLeaveReasonAndType(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Tag>() {
                    @Override
                    public boolean openDialog() {
                        return true;
                    }

                    @Override
                    public void onResponse(Tag response) {
                        getView().setLeaveReasonAdapter(response.getTags());
                        getView().setLeaveTypeAdapter(response.getLeaveTypes());
                    }

                    @Override
                    public TeacherLeaveApplyPresenter getPresenter() {
                        return TeacherLeaveApplyPresenter.this;
                    }
                });
    }


    public void teacherLeaveApply(LeaveType leaveType, Tag tag, Tag typeTag, Tag timeTag, String startDate, String endDate, String comment) {
        getView().showProgressDialog(getString(R.string.loading));

        if (getView().isError()) {
            getView().hideProgressDialog();
            return;
        }

        teacherService = serviceBuilder.createService(TeacherService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("academicSessionId", String.valueOf(user.getUserdetails().getAcademicSessionId()));
        params.put("masterId", String.valueOf(user.getData().getMasterId()));
        params.put("userId", String.valueOf(user.getData().getId()));
        params.put("tagId", String.valueOf(tag.getId()));
        params.put("empLeaveTypeId", String.valueOf(leaveType.getId()));
        params.put("start_date", startDate);
        params.put("end_date", endDate);
        params.put("balance", String.valueOf(leaveType.getBalance()));
        params.put("user_type", user.getData().getUser_type());

        if (!TextUtils.isEmpty(comment)) {
            params.put("comment", comment);
        }

        if (typeTag != null) {
            params.put("duration", typeTag.getId() == 0 ? "1" : "0.5");
            if (timeTag != null && typeTag.getId() != 0) {
                params.put("halfday", timeTag.getId() == 0 ? "1" : "2");
            }
        } else {
            params.put("duration", String.valueOf(DateUtils.getDaysCountBetweenDate(startDate, endDate, "yyyy-MM-dd")));
        }


        disposable = teacherService.applyTeacherLeave(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Response>() {
                    @Override
                    public boolean openDialog() {
                        return true;
                    }

                    @Override
                    public void onResponse(Response response) {
                        getView().leaveApplied(response.getMessage());
                    }

                    @Override
                    public TeacherLeaveApplyPresenter getPresenter() {
                        return TeacherLeaveApplyPresenter.this;
                    }
                });
    }

}
