package com.pws.pateast.fragment.home.student;

import com.pws.pateast.R;
import com.pws.pateast.enums.TaskType;
import com.pws.pateast.fragment.home.DashBoardPresenter;
import com.pws.pateast.model.DashboardItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by intel on 13-May-17.
 */

public class StudentDashBoardPresenter extends DashBoardPresenter<StudentDashBoardView> {

    public void setStudentData() {
        getView().setStudentData(userInfo);
        if (user.getData().getAcademicSessionId() == -1) {
            showSessionDialog(false);
        }
    }

    @Override
    public List<DashboardItem> getDashboardItem() {
        List<DashboardItem> items = new ArrayList<>();
        items.add(new DashboardItem(R.string.menu_my_attendance, R.drawable.attendance, TaskType.STUDENT_ATTENDANCE));
        items.add(new DashboardItem(R.string.menu_my_assignments, R.drawable.assignment, TaskType.STUDENT_ASSIGNMENT));
        items.add(new DashboardItem(R.string.menu_events, R.drawable.event, TaskType.MY_EVENTS));

        items.add(new DashboardItem(R.string.menu_timetable, R.drawable.timetable, TaskType.STUDENT_EXAM_SCHEDULE));
        items.add(new DashboardItem(R.string.menu_messages, R.drawable.message, TaskType.MY_MESSAGE));
        items.add(new DashboardItem(R.string.menu_marks, R.drawable.marks, TaskType.STUDENT_MARKS));

        items.add(new DashboardItem(R.string.menu_my_schedule, R.drawable.timetable, TaskType.STUDENT_SCHEDULE));
        items.add(new DashboardItem(R.string.menu_leave, R.drawable.leave, TaskType.STUDENT_LEAVE));
        items.add(new DashboardItem(R.string.menu_my_profile, R.drawable.profile, TaskType.STUDENT_PROFILE));
        return items;
    }
}
