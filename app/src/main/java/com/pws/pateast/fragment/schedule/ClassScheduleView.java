package com.pws.pateast.fragment.schedule;

import com.pws.pateast.activity.schedule.ScheduleView;
import com.pws.pateast.base.AppView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by intel on 24-Aug-17.
 */

public interface ClassScheduleView extends AppView
{
    void attachView(ScheduleView scheduleView);

    long getTimeInMillis();

    void setSchedules(HashMap<String,ArrayList> schedules);

    HashMap<String,ArrayList> getSchedules();

    void setScheduleAdapter(List<Object> schedules);
}
