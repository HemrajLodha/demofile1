package com.pws.pateast.fragment.assignment.teacher;

import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.view.View;

import com.pws.pateast.R;
import com.pws.pateast.api.ServiceAction;
import com.pws.pateast.api.ServiceModel;
import com.pws.pateast.api.exception.RetrofitException;
import com.pws.pateast.api.model.Assignment;
import com.pws.pateast.api.model.Response;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.TeacherService;
import com.pws.pateast.download.DownloadItemHelper;
import com.pws.pateast.enums.AssignmentType;
import com.pws.pateast.fragment.assignment.AssignmentDownloadPresenter;
import com.pws.pateast.utils.Utils;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by intel on 03-May-17.
 */

public class AssignmentListPresenter extends AssignmentDownloadPresenter<AssignmentListView> {
    private static final String TAG = AssignmentListPresenter.class.getSimpleName();

    private TeacherService apiService;


    public void getAssignment(int bcsMapId, String subject, String assignmentTitle, String status) {
        apiService = serviceBuilder.createService(TeacherService.class);

        HashMap<String, String> params = serviceBuilder.getParams(ServiceModel.ASSIGNMENT, ServiceAction.VIEW);
        params.put("userId", String.valueOf(user.getData().getId()));
        params.put("userType", user.getData().getUser_type());
        params.put("academicSessionId", String.valueOf(user.getUserdetails().getAcademicSessionId()));
        params.put("masterId", String.valueOf(user.getData().getMasterId()));


        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put("page", String.valueOf(getView().getPage()));
        if (bcsMapId != 0)
            queryParams.put("assignment__bcsMapId", String.valueOf(bcsMapId));
        if (!TextUtils.isEmpty(subject))
            queryParams.put("subjectdetail__name", subject);

        if (!TextUtils.isEmpty(assignmentTitle))
            queryParams.put("assignmentdetail__title", assignmentTitle);

        if (!TextUtils.isEmpty(status))
            queryParams.put("assignment__assignment_status", status);
        dispose();
        disposable = apiService.getAssignment(params, queryParams)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Assignment>() {
                    @Override
                    public void onResponse(Assignment response) {
                        if (response.getData().isEmpty()) {
                            onError(new RetrofitException(getString(R.string.no_assignment)));
                            AssignmentListPresenter.this.getView().showAction(getString(R.string.add), true, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AssignmentListPresenter.this.getView().addAssignment(null);
                                }
                            });
                            AssignmentListPresenter.this.getView().setAddVisible(false);
                        } else {
                            AssignmentListPresenter.this.getView().setPageCount(response.getPageCount());
                            AssignmentListPresenter.this.getView().setAssignmentAdapter(response.getData(), preference.getDateFormat());
                            AssignmentListPresenter.this.getView().setAddVisible(true);
                        }
                    }

                    @Override
                    public AssignmentListPresenter getPresenter() {
                        return AssignmentListPresenter.this;
                    }
                });
    }

    public void changeAssignmentStatus(int assignmentId, String activeStatus) {
        AssignmentListPresenter.this.getView().showProgressDialog(getString(R.string.wait));
        apiService = serviceBuilder.createService(TeacherService.class);

        HashMap<String, String> params = serviceBuilder.getParams(ServiceModel.ASSIGNMENT, ServiceAction.STATUS);
        params.put("masterId", String.valueOf(user.getData().getMasterId()));

        disposable = apiService.changeAssignmentStatus(params, String.valueOf(assignmentId), activeStatus)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Response>() {
                    @Override
                    public boolean openDialog() {
                        return true;
                    }

                    @Override
                    public void onResponse(Response response) {
                        AssignmentListPresenter.this.getView().onActionClick();
                        Utils.showToast(getContext(), response.getMessage());
                    }

                    @Override
                    public AssignmentListPresenter getPresenter() {
                        return AssignmentListPresenter.this;
                    }
                });
    }

    public void deleteAssignment(final int assignmentId) {
        AssignmentListPresenter.this.getView().showProgressDialog(getString(R.string.wait));
        apiService = serviceBuilder.createService(TeacherService.class);

        HashMap<String, String> params = serviceBuilder.getParams(ServiceModel.ASSIGNMENT, ServiceAction.DELETE);
        params.put("masterId", String.valueOf(user.getData().getMasterId()));

        disposable = apiService.deleteAssignment(params, String.valueOf(assignmentId))
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Response>() {
                    @Override
                    public boolean openDialog() {
                        return true;
                    }

                    @Override
                    public void onResponse(Response response) {
                        AssignmentListPresenter.this.getView().onActionClick();
                        DownloadItemHelper.deleteDownloadedFile(getContext(), assignmentId);
                        Utils.showToast(getContext(), response.getMessage());
                    }

                    @Override
                    public AssignmentListPresenter getPresenter() {
                        return AssignmentListPresenter.this;
                    }
                });
    }

    public void setPopupMenu(PopupMenu popupMenu, String status) {
        switch (AssignmentType.getAssignmentType(status)) {
            case DRAFT:
                popupMenu.inflate(R.menu.menu_assignment);
                popupMenu.getMenu().findItem(R.id.menu_delete).setVisible(true);
                popupMenu.getMenu().findItem(R.id.menu_edit).setVisible(true);
                popupMenu.getMenu().findItem(R.id.menu_publish).setVisible(true);
                break;
            case PUBLISHED:
                popupMenu.inflate(R.menu.menu_assignment);
                popupMenu.getMenu().findItem(R.id.menu_review).setVisible(true);
                popupMenu.getMenu().findItem(R.id.menu_cancel).setVisible(true);
                break;
        }
    }

}
