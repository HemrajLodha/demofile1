package com.pws.pateast.fragment.classes.report;

import android.text.TextUtils;

import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.utils.Preference;
import com.pws.pateast.utils.Utils;
import com.pws.pateast.R;
import com.pws.pateast.api.ServiceAction;
import com.pws.pateast.api.ServiceModel;
import com.pws.pateast.api.model.TeacherReport;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.TeacherService;
import com.pws.pateast.base.AppPresenter;

import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by intel on 04-Jul-17.
 */

public class TeacherReportPresenter extends AppPresenter<TeacherReportView>
{
    @Inject
    public Preference preference;

    @Inject
    public ServiceBuilder serviceBuilder;

    protected User user;

    protected TeacherService apiService;

    private TeacherReportView reportView;

    @Override
    public TeacherReportView getView() {
        return reportView;
    }

    @Override
    public void attachView(TeacherReportView view) {
        reportView = view;
        getComponent().inject(this);
        user = preference.getUser();
    }

    public void bind()
    {
        getView().bind(getView().getTeacherClass(),getView().getSchedule());
    }

    public void getClassReport()
    {
        getView().showProgressDialog(getString(R.string.wait));
        apiService = serviceBuilder.createService(TeacherService.class);

        HashMap<String,String> params = serviceBuilder.getParams(ServiceModel.CLASS_REPORT, ServiceAction.VIEW);
        params.put("userId", String.valueOf(user.getData().getId()));
        params.put("academicSessionId", String.valueOf(user.getUserdetails().getAcademicSessionId()));
        params.put("masterId", String.valueOf(user.getData().getMasterId()));

        disposable = apiService.getClassReport(params,getView().getTeacherClass().getBcsMapId(), getView().getSchedule().getSubjectId(),getView().getDate(),getView().getOrder())
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<TeacherReport>()
                {
                    @Override
                    public boolean openDialog() {
                        return true;
                    }

                    @Override
                    public void onResponse(TeacherReport response)
                    {

                        TeacherReportPresenter.this.getView().setTeacherReport(response.getReport());
                    }

                    @Override
                    public TeacherReportPresenter getPresenter()
                    {
                        return TeacherReportPresenter.this;
                    }
                });
    }

    public void saveClassReport()
    {
        if(getView().getContent()== null || TextUtils.isEmpty(getView().getContent()))
        {
            getView().setError(R.id.til_report, getString(R.string.validate_edittext, getString(R.string.dialog_menu_daily_report)));
            return;
        }
        getView().showProgressDialog(getString(R.string.wait));
        apiService = serviceBuilder.createService(TeacherService.class);

        HashMap<String,String> params = serviceBuilder.getParams(ServiceModel.CLASS_REPORT, ServiceAction.ADD);
        params.put("userId", String.valueOf(user.getData().getId()));
        params.put("academicSessionId", String.valueOf(user.getUserdetails().getAcademicSessionId()));
        params.put("masterId", String.valueOf(user.getData().getMasterId()));
        params.put("bcsMapId", String.valueOf(getView().getTeacherClass().getBcsMapId()));
        params.put("subjectId", String.valueOf(getView().getSchedule().getSubjectId()));
        params.put("date", getView().getDate());
        params.put("content", getView().getContent());
        params.put("time", getView().getTime());
        params.put("order", String.valueOf(getView().getOrder()));
        if(getView().getReportId() != 0)
            params.put("id", String.valueOf(getView().getReportId()));

        disposable = apiService.saveClassReport(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<TeacherReport>()
                {
                    @Override
                    public boolean openDialog() {
                        return true;
                    }

                    @Override
                    public boolean isDismiss() {
                        return false;
                    }

                    @Override
                    public void onResponse(TeacherReport response)
                    {
                        TeacherReportPresenter.this.getView().dismiss();
                        Utils.showToast(getContext(),response.getMessage());
                    }

                    @Override
                    public TeacherReportPresenter getPresenter()
                    {
                        return TeacherReportPresenter.this;
                    }
                });
    }
}
