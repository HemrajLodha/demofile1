package com.pws.pateast.activity.schedule;

import com.pws.pateast.api.model.Schedule;
import com.pws.pateast.api.model.Session;
import com.pws.pateast.base.AppView;
import com.pws.pateast.enums.TaskType;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.fragment.presenter.ExamHeadView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by intel on 21-Aug-17.
 */

public interface ScheduleView extends ExamHeadView
{
    int tabIndicatorColor();
    int tabSelectedTextColor();
    int tabTextAppearance();
    int tabBackground();
    UserType getUserType();

    void setTitle(int title);
    void setTitle(String title);
    void setScheduleAdapter(TaskType taskType, Session session);

    Object getScheduleData();
    void setScheduleData(Object scheduleData);
}
