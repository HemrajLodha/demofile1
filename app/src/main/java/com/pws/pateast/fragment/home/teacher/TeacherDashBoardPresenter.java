package com.pws.pateast.fragment.home.teacher;

import com.pws.pateast.enums.TaskType;
import com.pws.pateast.events.ProfileUpdateEvent;
import com.pws.pateast.fragment.home.DashBoardPresenter;
import com.pws.pateast.model.DashboardItem;
import com.pws.pateast.R;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by intel on 20-Apr-17.
 */

public class TeacherDashBoardPresenter extends DashBoardPresenter<TeacherDashBoardView> {


    @Override
    public void attachView(TeacherDashBoardView view) {
        super.attachView(view);
        mEventBus.register(this);
    }

    @Override
    public void detachView() {
        super.detachView();
        mEventBus.unregister(this);
    }

    @Subscribe
    public void onEvent(ProfileUpdateEvent event)
    {
        userInfo = preference.getUserInfo().getData();
        getView().setTeacherData(userInfo);
    }

    public void setTeacherData() {
        getView().setTeacherData(userInfo);
        if (user.getData().getAcademicSessionId() == -1) {
            showSessionDialog(false);
        }
    }

    @Override
    public List<DashboardItem> getDashboardItem() {
        List<DashboardItem> items = new ArrayList<>();
        items.add(new DashboardItem(R.string.menu_my_classes, R.drawable.profile, TaskType.TEACHER_CLASSES));
        items.add(new DashboardItem(R.string.menu_my_students, R.drawable.student, TaskType.TEACHER_STUDENTS));
        items.add(new DashboardItem(R.string.menu_attendance, R.drawable.attendance, TaskType.TEACHER_ATTENDANCE));
        items.add(new DashboardItem(R.string.menu_assignments, R.drawable.assignment, TaskType.ASSIGNMENT));
        items.add(new DashboardItem(R.string.menu_messages, R.drawable.message, TaskType.MY_MESSAGE));
        items.add(new DashboardItem(R.string.menu_marks, R.drawable.marks, TaskType.UPLOAD_MARKS));
        items.add(new DashboardItem(R.string.menu_my_schedule, R.drawable.timetable, TaskType.TEACHER_SCHEDULE));
        items.add(new DashboardItem(R.string.menu_class_leave, R.drawable.leave, TaskType.CLASS_LEAVE));
        items.add(new DashboardItem(R.string.menu_my_profile, R.drawable.profile, TaskType.MY_PROFILE));
        return items;
    }
}
