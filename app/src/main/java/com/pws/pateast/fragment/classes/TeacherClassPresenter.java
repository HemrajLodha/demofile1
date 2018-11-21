package com.pws.pateast.fragment.classes;

import com.pws.pateast.R;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.exception.RetrofitException;
import com.pws.pateast.api.model.Schedule;
import com.pws.pateast.api.model.Session;
import com.pws.pateast.api.model.TeacherClass;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.TeacherService;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.utils.DateUtils;
import com.pws.pateast.utils.Preference;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by intel on 12-May-17.
 */

public class TeacherClassPresenter extends AppPresenter<TeacherClassView> {
    @Inject
    public Preference preference;

    @Inject
    public ServiceBuilder serviceBuilder;

    protected User user;

    protected TeacherService apiService;

    private TeacherClassView classesView;

    @Override
    public TeacherClassView getView() {
        return classesView;
    }

    @Override
    public void attachView(TeacherClassView view) {
        classesView = view;
        getComponent().inject(this);
        user = preference.getUser();
    }

    public void setCalender() {
        Session session = Session.getSelectedSession(user.getUserdetails().getAcademicSessions(), user.getUserdetails().getAcademicSessionId());
        if (session != null) {
            getView().setCalenderDate(DateUtils.parse(session.getStart_date(), DateUtils.DATE_FORMAT_PATTERN), DateUtils.parse(session.getEnd_date(), DateUtils.DATE_FORMAT_PATTERN));
        }
    }

    public void getTeacherClasses() {
        apiService = serviceBuilder.createService(TeacherService.class);
        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("userId", String.valueOf(user.getUserdetails().getUserId()));
        params.put("academicSessionId", String.valueOf(user.getUserdetails().getAcademicSessionId()));
        params.put("masterId", String.valueOf(user.getData().getMasterId()));
        params.put("weekday", getView().getWeekday());
        params.put("date", getView().getDate());

        disposable = apiService.getTeacherClasses(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<TeacherClass>() {

                    @Override
                    public void onResponse(TeacherClass response) {
                        if (response.getData().isEmpty()) {
                            onError(new RetrofitException(getString(R.string.no_schedule), RetrofitException.ERROR_TYPE_MESSAGE));
                        } else {
                            arrangeClasses(response.getData());
                        }
                    }

                    @Override
                    public TeacherClassPresenter getPresenter() {
                        return TeacherClassPresenter.this;
                    }
                });
    }

    private void arrangeClasses(ArrayList<TeacherClass> classes) {
        ArrayList<TeacherClass> teacherClasses = new ArrayList<>();
        for (TeacherClass teacherClass : classes) {
            for (Schedule schedule : teacherClass.getTimetableallocations()) {
                teacherClass.setTimetable(schedule);
                teacherClasses.add(teacherClass.clone());
            }
        }
        getView().setClassAdapter(teacherClasses);
    }
}
