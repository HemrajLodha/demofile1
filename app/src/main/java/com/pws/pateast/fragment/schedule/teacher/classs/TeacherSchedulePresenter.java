package com.pws.pateast.fragment.schedule.teacher.classs;

import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.fragment.schedule.ClassSchedulePresenter;

import io.reactivex.observers.DisposableSingleObserver;

/**
 * Created by intel on 25-Apr-17.
 */

public class TeacherSchedulePresenter extends ClassSchedulePresenter<TeacherScheduleView>
{


    @Override
    public void getMySchedule(AppSingleObserver observer) {
        getTeacherSchedule(observer);
    }

    @Override
    public void isHoliday() {
        isHoliday(user.getData().getMasterId(), user.getUserdetails().getAcademicSessionId());
    }
}
