package com.pws.pateast.fragment.complaint;

import com.pws.pateast.R;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.exception.RetrofitException;
import com.pws.pateast.api.model.Complaint;
import com.pws.pateast.api.model.Tag;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.model.Ward;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.StudentService;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.utils.Preference;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.pws.pateast.api.exception.RetrofitException.ERROR_TYPE_MESSAGE;

/**
 * Created by intel on 09-Feb-18.
 */

public class ComplaintPresenter extends AppPresenter<ComplaintView> {

    @Inject
    Preference preference;

    @Inject
    ServiceBuilder serviceBuilder;
    private User user;
    private Ward ward;
    private StudentService apiService;
    private ComplaintView mView;

    @Override
    public ComplaintView getView() {
        return mView;
    }

    @Override
    public void attachView(ComplaintView view) {
        mView = view;
        getComponent().inject(this);
        user = preference.getUser();
        ward = preference.getWard();
    }

    public UserType getUserType() {
        return UserType.getUserType(user.getData().getUser_type());
    }

    public void getStudentComplaints() {
        apiService = serviceBuilder.createService(StudentService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        switch (getUserType()) {
            case PARENT:
                params.put("academicSessionId", String.valueOf(ward.getAcademicSessionId()));
                params.put("masterId", String.valueOf(ward.getMasterId()));
                params.put("studentId", String.valueOf(ward.getUserInfo().getUserId()));
                break;
            case STUDENT:
                params.put("academicSessionId", String.valueOf(user.getUserdetails().getAcademicSessionId()));
                params.put("masterId", String.valueOf(user.getData().getMasterId()));
                params.put("studentId", String.valueOf(user.getUserdetails().getUserId()));
                break;
        }

        disposable = apiService.getStudentComplaints(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Complaint>() {
                    @Override
                    public void onResponse(Complaint response) {
                        if (response.getData() == null || response.getData().isEmpty()) {
                            onError(new RetrofitException(getString(R.string.no_complaint_found), ERROR_TYPE_MESSAGE));
                        } else {
                            getView().setTags((ArrayList<Tag>) response.getTagsData());
                            getView().setComplaintAdapter(response.getData());
                        }
                    }

                    @Override
                    public ComplaintPresenter getPresenter() {
                        return ComplaintPresenter.this;
                    }

                });
    }
}
