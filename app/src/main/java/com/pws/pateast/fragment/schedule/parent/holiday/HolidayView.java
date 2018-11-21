package com.pws.pateast.fragment.schedule.parent.holiday;

import com.pws.pateast.base.AppView;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.fragment.schedule.ClassScheduleView;

/**
 * Created by intel on 25-Aug-17.
 */

public interface HolidayView extends ClassScheduleView
{
    UserType getUserType();
}
