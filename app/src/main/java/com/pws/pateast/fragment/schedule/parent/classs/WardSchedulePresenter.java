package com.pws.pateast.fragment.schedule.parent.classs;

import com.pws.pateast.api.model.User;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.fragment.schedule.ClassSchedulePresenter;
import com.pws.pateast.utils.Preference;

import javax.inject.Inject;

import io.reactivex.observers.DisposableSingleObserver;

/**
 * Created by intel on 24-Aug-17.
 */

public class WardSchedulePresenter extends ClassSchedulePresenter<WardScheduleView> {
    @Override
    public void getMySchedule(AppSingleObserver observer) {
        getWardSchedule(observer);
    }


    @Override
    public void isHoliday() {
        isHoliday(ward.getMasterId(), ward.getUserInfo().getAcademicSessionId());
    }
}
