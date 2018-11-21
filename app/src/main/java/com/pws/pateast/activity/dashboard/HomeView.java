package com.pws.pateast.activity.dashboard;

import com.pws.pateast.api.model.DashboardEvent;
import com.pws.pateast.api.model.ExamMarks;
import com.pws.pateast.api.model.Schedule;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.model.UserInfo;
import com.pws.pateast.api.model.Ward;
import com.pws.pateast.base.AppView;
import com.pws.pateast.fragment.profile.ProfileView;
import com.pws.pateast.listener.AppListener;

import java.util.ArrayList;

/**
 * Created by intel on 20-Apr-17.
 */

public interface HomeView extends AppView {

    void inflateMenu(int resId);

    void setData(User user);

    AppListener getAppListener();

    void setDashboardEventAdapter(DashboardEvent dashboardEvent);

    void setDashboardAttendance(ArrayList<Schedule> attendace);

    void setExamSchedule(ArrayList<Schedule> schedule);

    void setExamMarks(ArrayList<ExamMarks> marks);
}
