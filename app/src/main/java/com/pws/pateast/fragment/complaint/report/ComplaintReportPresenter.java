package com.pws.pateast.fragment.complaint.report;

import com.pws.pateast.R;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.model.Complaint;
import com.pws.pateast.api.model.Response;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.model.Ward;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.StudentService;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.utils.Preference;

import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by intel on 10-Feb-18.
 */

public class ComplaintReportPresenter extends AppPresenter<ComplaintReportView> {
    @Inject
    Preference preference;

    @Inject
    ServiceBuilder serviceBuilder;
    private User user;
    private Ward ward;
    private StudentService apiService;

    private ComplaintReportView mView;

    @Override
    public ComplaintReportView getView() {
        return mView;
    }

    @Override
    public void attachView(ComplaintReportView view) {
        mView = view;
        getComponent().inject(this);
        user = preference.getUser();
        ward = preference.getWard();
        getView().setReportDetail(getView().getComplaint());
    }

    public void sendEmailForComplaint(CharSequence message) {
        if (getView().isError(message)) {
            return;
        }
        getView().showProgressDialog(getString(R.string.wait));

        apiService = serviceBuilder.createService(StudentService.class);
        Complaint complaint = getView().getComplaint();

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("academicSessionId", String.valueOf(ward.getAcademicSessionId()));
        params.put("masterId", String.valueOf(ward.getMasterId()));
        params.put("studentId", String.valueOf(ward.getUserInfo().getUserId()));
        params.put("message", message.toString());
        params.put("complaintId", String.valueOf(complaint.getComplaintId()));
        params.put("emailId", complaint.getComplaint().getUser().getEmail());

        disposable = apiService.sendEmailForComplaint(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Response>() {

                    @Override
                    public boolean openDialog() {
                        return true;
                    }

                    @Override
                    public void onResponse(Response response) {
                        getView().navigateTpDetail(response.getMessage());
                    }

                    @Override
                    public ComplaintReportPresenter getPresenter() {
                        return ComplaintReportPresenter.this;
                    }

                });
    }
}
