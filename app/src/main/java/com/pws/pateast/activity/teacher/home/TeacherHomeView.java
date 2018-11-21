package com.pws.pateast.activity.teacher.home;

import com.pws.pateast.activity.dashboard.HomeView;
import com.pws.pateast.api.model.TeacherDashboard;
import com.pws.pateast.api.model.User;
import com.pws.pateast.base.AppView;
import com.pws.pateast.fragment.profile.ProfileView;
import com.pws.pateast.listener.AppListener;

/**
 * Created by planet on 8/25/2017.
 */

public interface TeacherHomeView extends AppView {

    void setData(User user);

    void setNotificationCount(TeacherDashboard teacherDashboard);

    AppListener getAppListener();
}
