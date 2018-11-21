package com.pws.pateast.fragment.schedule.parent.holiday;

import com.pws.pateast.api.model.Response;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.fragment.schedule.ClassSchedulePresenter;

import io.reactivex.observers.DisposableSingleObserver;

/**
 * Created by intel on 25-Aug-17.
 */

public class HolidayPresenter extends ClassSchedulePresenter<HolidayView> {

    @Override
    public void getMySchedule(AppSingleObserver observer) {

    }

    @Override
    public void isHoliday() {

    }

    public void getHolidays() {
        switch (getView().getUserType()) {
            case STUDENT:
            case TEACHER:
            case DRIVER:
                getHolidays(user.getData().getMasterId(), user.getUserdetails().getAcademicSessionId());
                break;
            case PARENT:
                getHolidays(ward.getMasterId(), ward.getAcademicSessionId());
                break;
        }
    }
}
