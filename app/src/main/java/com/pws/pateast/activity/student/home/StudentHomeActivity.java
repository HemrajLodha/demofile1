package com.pws.pateast.activity.student.home;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.pws.pateast.R;
import com.pws.pateast.activity.attendance.StudentAttendanceReportActivity;
import com.pws.pateast.activity.chat.inbox.InboxActivity;
import com.pws.pateast.activity.dashboard.ChildFooterItem;
import com.pws.pateast.activity.dashboard.HomeActivity;
import com.pws.pateast.activity.feeds.FeedsActivity;
import com.pws.pateast.activity.leave.StudentLeaveStatusActivity;
import com.pws.pateast.activity.lms.LMSActivity;
import com.pws.pateast.activity.schedule.activity.StudentScheduleActivity;
import com.pws.pateast.activity.tasks.StudentTaskActivity;
import com.pws.pateast.api.model.DashboardEvent;
import com.pws.pateast.api.model.User;
import com.pws.pateast.base.Extras;
import com.pws.pateast.enums.TaskType;
import com.pws.pateast.utils.ImageUtils;
import com.pws.pateast.widget.ProfileImageView;

public class StudentHomeActivity extends HomeActivity {
    private ProfileImageView imgProfileView;
    private TextView tvHeaderStudentName;
    private TextView tvHeaderClass;
    private TextView tvHeaderUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.StudentTheme);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        navigationView = findViewById(R.id.navigation_view);

        View headerView = navigationView.getHeaderView(0);
        imgProfileView = headerView.findViewById(R.id.tv_profile_image);
        tvHeaderStudentName = headerView.findViewById(R.id.tv_profile_name);
        tvHeaderClass = headerView.findViewById(R.id.tv_student_class);
        tvHeaderUserName = headerView.findViewById(R.id.tv_student_user_name);

        super.onViewReady(savedInstanceState);
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.activity_student_home;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void setData(User user) {
        if (isDestroyed())
            return;
        User userLogin = homePresenter.getUser();
        ImageUtils.setImageUrl(getContext(), studentImage, user.getUser_image(), R.drawable.avatar1);
        ImageUtils.setImageUrl(getContext(), imgProfileView, user.getUser_image(), R.drawable.avatar1);

        tvStudentName.setText(user.getFullname());
        tvHeaderStudentName.setText(user.getFullname());

        String className = getString(R.string.assignment_bcs_name,
                getString(R.string.title_class),
                user.getBcs().getClasses().getClassesdetails().get(0).getName(),
                user.getBcs().getSection().getSectiondetails().get(0).getName());
        tvStudentClass.setText(className);
        tvHeaderClass.setText(className);

        if (!TextUtils.isEmpty(userLogin.getData().getUser_name())) {
            tvHeaderUserName.setText(userLogin.getData().getUser_name());
        } else {
            tvHeaderUserName.setText(R.string.not_available);
        }
    }


    @Override
    public void onViewMoreClick(View view, DashboardEvent parent, ChildFooterItem childFooter) {
        switch (parent.getExpandableViewType()) {
            case SCHEDULE:
                openActivity(StudentScheduleActivity.class, TaskType.STUDENT_SCHEDULE);
                break;
            case ATTENDANCE:
                openActivity(StudentAttendanceReportActivity.class);
                break;
            case ASSIGNMENT:
                openActivity(StudentTaskActivity.class, TaskType.STUDENT_ASSIGNMENT);
                break;
            case EXAM_SCHEDULE:
                openActivity(StudentScheduleActivity.class, TaskType.STUDENT_EXAM_SCHEDULE);
                break;
            case EXAM_MARKS:
                openActivity(StudentScheduleActivity.class, TaskType.STUDENT_MARKS);
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawer.closeDrawers();
        switch (item.getItemId()) {
            case R.id.nav_my_profile:
                openActivity(StudentTaskActivity.class, TaskType.STUDENT_PROFILE);
                return false;
            case R.id.nav_messages:
                Bundle bundle = new Bundle();
                bundle.putInt(Extras.APP_THEME, R.style.StudentTheme);
                openActivity(InboxActivity.class, bundle);
                return false;
            case R.id.nav_school_holidays:
                openActivity(StudentScheduleActivity.class, TaskType.HOLIDAY);
                return false;
            case R.id.nav_school_events:
                openActivity(StudentTaskActivity.class, TaskType.STUDENT_EVENTS);
                return false;
            case R.id.nav_circular:
                openActivity(StudentTaskActivity.class, TaskType.STUDENT_CIRCULAR);
                return false;
            case R.id.nav_complaint:
                openActivity(StudentTaskActivity.class, TaskType.COMPLAINTS);
                return false;
            case R.id.nav_attendance:
                openActivity(StudentAttendanceReportActivity.class);
                return false;
            case R.id.nav_assignments:
                openActivity(StudentTaskActivity.class, TaskType.STUDENT_ASSIGNMENT);
                return false;
            case R.id.nav_leaves:
                openActivity(StudentLeaveStatusActivity.class, TaskType.STUDENT_LEAVE_APPLY);
                return false;
            case R.id.nav_class_schedule:
                openActivity(StudentScheduleActivity.class, TaskType.STUDENT_SCHEDULE);
                return false;
            case R.id.nav_exam_schedules:
                openActivity(StudentScheduleActivity.class, TaskType.STUDENT_EXAM_SCHEDULE);
                return false;
            case R.id.nav_exam_marks:
                openActivity(StudentScheduleActivity.class, TaskType.STUDENT_MARKS);
                return false;
            case R.id.nav_settings:
                openActivity(StudentTaskActivity.class, TaskType.SETTINGS);
                return false;
            case R.id.nav_notifications:
                openActivity(StudentTaskActivity.class, TaskType.NOTIFICATION_LIST);
                return false;
            case R.id.nav_lms:
                openActivity(LMSActivity.class);
                return false;
            case R.id.nav_feeds:
                bundle = new Bundle();
                bundle.putInt(Extras.APP_THEME, R.style.StudentTheme);
                openActivity(FeedsActivity.class, bundle);
                return false;
        }
        return super.onNavigationItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.profile_view:
                openActivity(StudentTaskActivity.class, TaskType.STUDENT_PROFILE);
                break;
            case R.id.assignment_view:
                openActivity(StudentTaskActivity.class, TaskType.STUDENT_ASSIGNMENT);
                break;
            case R.id.bus_track_view:
                openActivity(StudentScheduleActivity.class, TaskType.HOLIDAY);
                break;
            case R.id.message_view:
                Bundle bundle = new Bundle();
                bundle.putInt(Extras.APP_THEME, R.style.StudentTheme);
                openActivity(InboxActivity.class, bundle);
                break;
            case R.id.feeds_view:
                bundle = new Bundle();
                bundle.putInt(Extras.APP_THEME, R.style.StudentTheme);
                openActivity(FeedsActivity.class, bundle);
                break;
        }
    }

    @Override
    public void onError(String message) {

    }


}
