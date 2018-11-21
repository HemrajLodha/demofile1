package com.pws.pateast.fragment.leave.parent.apply;

import android.text.TextUtils;

import com.pws.pateast.R;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.model.Response;
import com.pws.pateast.api.model.Tag;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.model.Ward;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.StudentService;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.fragment.leave.LeaveApplyPresenter;
import com.pws.pateast.utils.DateUtils;
import com.pws.pateast.utils.Preference;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by intel on 19-Aug-17.
 */

public class WardLeaveApplyPresenter extends LeaveApplyPresenter<WardLeaveApplyView> {


    @Override
    public void attachView(WardLeaveApplyView view) {
        super.attachView(view);
        getView().setWardData(ward);
        getLeaveReason(ward.getMasterId());
    }


    public void wardLeaveApply(Tag reasonTag, Tag typeTag, Tag timeTag, String startDate, String endDate, String comment) {
        getView().showProgressDialog(getString(R.string.loading));

        if (getView().isError()) {
            getView().hideProgressDialog();
            return;
        }
        studentService = serviceBuilder.createService(StudentService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("academicSessionId", String.valueOf(ward.getAcademicSessionId()));
        params.put("masterId", String.valueOf(ward.getMasterId()));
        params.put("userId", String.valueOf(ward.getUserId()));
        params.put("bcsMapId", String.valueOf(ward.getBcsMapId()));
        params.put("tagId", String.valueOf(reasonTag.getId()));
        params.put("start_date", startDate);
        params.put("end_date", endDate);
        params.put("applied_by", String.valueOf(user.getData().getId()));

        if (!TextUtils.isEmpty(comment))
            params.put("comment", comment);

        if (typeTag != null) {
            params.put("duration", typeTag.getId() == 0 ? "1" : "0.5");
            if (timeTag != null && typeTag.getId() != 0) {
                params.put("halfday", timeTag.getId() == 0 ? "1" : "2");
            }
        } else {
            params.put("duration", String.valueOf(DateUtils.getDaysCountBetweenDate(startDate, endDate, "yyyy-MM-dd")));
        }

        disposable = studentService.studentLeaveApply(params)
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
                    public WardLeaveApplyPresenter getPresenter() {
                        return WardLeaveApplyPresenter.this;
                    }
                });
    }
}
