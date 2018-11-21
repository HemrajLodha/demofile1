package com.pws.pateast.fragment.assignment.student;

import android.text.TextUtils;

import com.pws.pateast.R;
import com.pws.pateast.api.ServiceAction;
import com.pws.pateast.api.ServiceModel;
import com.pws.pateast.api.exception.RetrofitException;
import com.pws.pateast.api.model.Assignment;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.StudentService;
import com.pws.pateast.fragment.assignment.AssignmentDownloadPresenter;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by intel on 15-May-17.
 */

public class StudentAssignmentPresenter extends AssignmentDownloadPresenter<StudentAssignmentView>
{
    private StudentService apiService;


    public void getStudentAssignment(String subjectTitle, String assignmentTitle) {
        apiService = serviceBuilder.createService(StudentService.class);

        HashMap<String, String> params = serviceBuilder.getParams(ServiceModel.ASSIGNMENT, ServiceAction.VIEW);
        params.put("userId", String.valueOf(user.getUserdetails().getUserId()));
        params.put("bcsMapId", String.valueOf(user.getUserdetails().getBcsMapId()));
        params.put("userType", user.getData().getUser_type());
        params.put("academicSessionId", String.valueOf(user.getUserdetails().getAcademicSessionId()));
        params.put("masterId", String.valueOf(user.getData().getMasterId()));

        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put("page", String.valueOf(getView().getPage()));

        if (!TextUtils.isEmpty(subjectTitle))
            queryParams.put("subjectdetail__name", subjectTitle);

        if (!TextUtils.isEmpty(assignmentTitle))
            queryParams.put("assignmentdetail__title", assignmentTitle);


        disposable = apiService.getStudentAssignment(params, queryParams)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Assignment>() {
                    @Override
                    public void onResponse(Assignment response) {
                        if (response.getData().isEmpty()) {
                            onError(new RetrofitException(getString(R.string.no_assignment)));
                        } else {
                            StudentAssignmentPresenter.this.getView().setPageCount(response.getPageCount());
                            StudentAssignmentPresenter.this.getView().setAssignmentAdapter(response.getData());
                        }
                    }

                    @Override
                    public StudentAssignmentPresenter getPresenter() {
                        return StudentAssignmentPresenter.this;
                    }
                });
    }
}
