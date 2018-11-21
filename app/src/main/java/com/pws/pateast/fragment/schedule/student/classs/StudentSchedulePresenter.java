package com.pws.pateast.fragment.schedule.student.classs;

import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.fragment.schedule.ClassSchedulePresenter;

import io.reactivex.observers.DisposableSingleObserver;

/**
 * Created by intel on 06-Jul-17.
 */

public class StudentSchedulePresenter extends ClassSchedulePresenter<StudentScheduleView> {

    @Override
    public void getMySchedule(AppSingleObserver observer) {
        getStudentSchedule(observer);
    }


    @Override
    public void isHoliday() {
        isHoliday(user.getData().getMasterId(), user.getUserdetails().getAcademicSessionId());
    }
}
