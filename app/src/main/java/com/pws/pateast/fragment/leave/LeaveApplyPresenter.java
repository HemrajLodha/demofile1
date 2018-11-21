package com.pws.pateast.fragment.leave;

import com.pws.pateast.R;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.model.Session;
import com.pws.pateast.api.model.Tag;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.model.Ward;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.StudentService;
import com.pws.pateast.api.service.TeacherService;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.utils.DateUtils;
import com.pws.pateast.utils.Preference;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by intel on 23-Aug-17.
 */

public class LeaveApplyPresenter<V extends LeaveApplyView> extends AppPresenter<V> {
    @Inject
    protected Preference preference;

    @Inject
    protected ServiceBuilder serviceBuilder;

    protected StudentService studentService;

    protected TeacherService teacherService;

    protected User user;
    protected Ward ward;

    V view;

    @Override
    public V getView() {
        return view;
    }

    @Override
    public void attachView(V view) {
        this.view = view;
        getComponent().inject((LeaveApplyPresenter<LeaveApplyView>) this);
        user = preference.getUser();
        ward = preference.getWard();
        setDateFormat();
        setDurationAdapter();
        setCalender(getUserType());
    }

    public UserType getUserType() {
        return UserType.getUserType(user.getData().getUser_type());
    }

    public void setCalender(UserType userType) {
        Session session = null;
        switch (userType) {
            case PARENT:
                session = Session.getSelectedSession(ward.getUserInfo().getAcademicSessions(), ward.getAcademicSessionId());
                break;
            default:
                session = Session.getSelectedSession(user.getUserdetails().getAcademicSessions(), user.getUserdetails().getAcademicSessionId());
                break;
        }
        if (session != null) {
            getView().setCalenderDate(DateUtils.parse(session.getStart_date(), DateUtils.DATE_FORMAT_PATTERN), DateUtils.parse(session.getEnd_date(), DateUtils.DATE_FORMAT_PATTERN));
        }
    }

    private void setDateFormat() {
        getView().setDateFormat(preference.getDateFormat());
    }

    private void setDurationAdapter() {
        String[] leaveType = getContext().getResources().getStringArray(R.array.leave_type);
        String[] leaveDuration = getContext().getResources().getStringArray(R.array.leave_duration);

        ArrayList<Tag> leaveTypes = new ArrayList<>();
        for (int i = 0; i < leaveType.length; i++) {
            leaveTypes.add(Tag.getTag(i, leaveType[i]));
        }

        ArrayList<Tag> leaveDurations = new ArrayList<>();
        for (int i = 0; i < leaveDuration.length; i++) {
            leaveDurations.add(Tag.getTag(i, leaveDuration[i]));
        }

        getView().setTypeLeaveAdapter(leaveTypes);
        getView().setLeaveDurationAdapter(leaveDurations);
    }

    public void getLeaveReason(int masterId) {
        getView().showProgressDialog(getString(R.string.loading));

        studentService = serviceBuilder.createService(StudentService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("masterId", String.valueOf(masterId));

        disposable = studentService.getLeaveReason(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Tag>() {
                    @Override
                    public boolean openDialog() {
                        return true;
                    }

                    @Override
                    public void onResponse(Tag response) {
                        if (response.getData() == null)
                            response.setData(new ArrayList<Tag>());

                        getView().setLeaveReasonAdapter(response.getData());
                    }

                    @Override
                    public LeaveApplyPresenter getPresenter() {
                        return LeaveApplyPresenter.this;
                    }
                });
    }


}
