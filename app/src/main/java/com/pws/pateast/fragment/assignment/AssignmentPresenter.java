package com.pws.pateast.fragment.assignment;

import android.text.TextUtils;

import com.pws.pateast.R;
import com.pws.pateast.api.ServiceAction;
import com.pws.pateast.api.ServiceModel;
import com.pws.pateast.api.exception.RetrofitException;
import com.pws.pateast.api.model.Assignment;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.model.Ward;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.StudentService;
import com.pws.pateast.fragment.assignment.parent.ParentAssignmentView;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by intel on 15-May-17.
 */

public class AssignmentPresenter extends AssignmentDownloadPresenter<AssignmentView> {
    private StudentService apiService;
    private User user;
    private boolean openDialog;

    @Override
    public void attachView(AssignmentView view) {
        super.attachView(view);
        user = preference.getUser().getData();
    }

    public void setOpenDialog(boolean openDialog) {
        this.openDialog = openDialog;
    }

    public void getStudentAssignment(int userId, int bcsMapId, String userType, int academicSessionId, int masterId, int subjectId, int tagId, String dueDate) {
        apiService = serviceBuilder.createService(StudentService.class);
        HashMap<String, String> params = serviceBuilder.getParams(ServiceModel.ASSIGNMENT, ServiceAction.VIEW);
        params.put("userId", String.valueOf(userId));
        params.put("bcsMapId", String.valueOf(bcsMapId));
        params.put("userType", userType);
        params.put("academicSessionId", String.valueOf(academicSessionId));
        params.put("masterId", String.valueOf(masterId));

        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put("page", String.valueOf(getView().getPage()));

        if (subjectId != 0) {
            queryParams.put("assignment__subjectId__eq", String.valueOf(subjectId));
        }

        if (tagId != 0)
            queryParams.put("tagId", String.valueOf(tagId));

        if (!TextUtils.isEmpty(dueDate))
            queryParams.put("assignment__end_date__gte", dueDate);


        disposable = apiService.getStudentAssignment(params, queryParams)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Assignment>() {
                    @Override
                    public void onResponse(Assignment response) {
                        if (response.getData().isEmpty()) {
                            onError(new RetrofitException(getString(R.string.no_assignment)));
                            AssignmentPresenter.this.getView().setAssignmentAdapter(new ArrayList<Assignment>());
                        } else {
                            AssignmentPresenter.this.getView().setPageCount(response.getPageCount());
                            AssignmentPresenter.this.getView().setAssignmentAdapter(response.getData());
                        }
                    }

                    @Override
                    public AssignmentPresenter getPresenter() {
                        return AssignmentPresenter.this;
                    }

                    @Override
                    public boolean openDialog() {
                        return AssignmentPresenter.this.openDialog;
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        AssignmentPresenter.this.getView().onError(e.getMessage());
                    }
                });
    }
}
