package com.pws.pateast.fragment.leave;

import android.view.View;

import com.pws.pateast.R;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.exception.RetrofitException;
import com.pws.pateast.api.model.Leave;
import com.pws.pateast.api.model.Response;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.model.Ward;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.StudentService;
import com.pws.pateast.api.service.TeacherService;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.enums.LeaveType;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.utils.DateUtils;
import com.pws.pateast.utils.Preference;
import com.pws.pateast.utils.Utils;

import java.util.Calendar;
import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by intel on 23-Aug-17.
 */

public class LeavePresenter<V extends LeaveView> extends AppPresenter<V> {
    @Inject
    protected Preference preference;

    @Inject
    protected ServiceBuilder serviceBuilder;

    protected User user;

    protected Ward ward;

    protected StudentService studentService;

    protected TeacherService teacherService;

    V view;

    @Override
    public V getView() {
        return view;
    }

    public UserType getUserType() {
        return UserType.getUserType(user.getData().getUser_type());
    }

    @Override
    public void attachView(V view) {
        this.view = view;
        getComponent().inject((LeavePresenter<LeaveView>) this);
        user = preference.getUser();
    }

    protected int getLeaveStatus() {
        switch (LeaveType.getLeaveType(getView().getLeaveStatus())) {
            case PENDING:
                return Leave.PENDING;
            case APPROVED:
                return Leave.APPROVED;
            case CANCELED:
                return Leave.CANCELED;
            case REJECTED:
                return Leave.REJECTED;
            default:
                return -1;
        }
    }

    protected void getLeave(int academicSessionId, int masterId, int userId) {
        studentService = serviceBuilder.createService(StudentService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("academicSessionId", String.valueOf(academicSessionId));
        params.put("masterId", String.valueOf(masterId));
        params.put("userId", String.valueOf(userId));

        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put("studentleave__leavestatus", String.valueOf(getLeaveStatus()));
        queryParams.put("page", String.valueOf(getView().getPage()));

        dispose();
        disposable = studentService.getStudentLeave(params, queryParams)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Leave>() {

                    @Override
                    public void onResponse(Leave response) {
                        if (response.getData().isEmpty()) {
                            switch (LeaveType.getLeaveType(LeavePresenter.this.getView().getLeaveStatus())) {
                                case PENDING:
                                    onError(new RetrofitException(getString(R.string.no_pending_leave)));
                                    break;
                                case APPROVED:
                                    onError(new RetrofitException(getString(R.string.no_approved_leave)));
                                    break;
                                case CANCELED:
                                    onError(new RetrofitException(getString(R.string.no_cancelled_leave)));
                                    break;
                                case REJECTED:
                                    onError(new RetrofitException(getString(R.string.no_rejected_leave)));
                                    break;
                                default:
                                    onError(new RetrofitException(getString(R.string.no_leave_applied)));
                                    break;
                            }

                            getView().showAction(getString(R.string.apply), true, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getView().leaveApply();
                                }
                            });
                            getView().setApplyVisible(false);
                        } else {
                            getView().setPageCount(response.getPageCount());
                            getView().setLeaveAdapter(response.getData());
                            getView().setApplyVisible(true);
                        }
                    }

                    @Override
                    public LeavePresenter getPresenter() {
                        return LeavePresenter.this;
                    }
                });
    }

    protected void cancelLeave(int leaveId, int masterId) {
        getView().showProgressDialog(getString(R.string.wait));
        studentService = serviceBuilder.createService(StudentService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("masterId", String.valueOf(masterId));
        params.put("date", DateUtils.toDate(Calendar.getInstance().getTime(), "yyyy-MM-dd"));


        disposable = studentService.cancelStudentLeave(params, String.valueOf(leaveId))
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
                    public LeavePresenter getPresenter() {
                        return LeavePresenter.this;
                    }
                });
    }

}
