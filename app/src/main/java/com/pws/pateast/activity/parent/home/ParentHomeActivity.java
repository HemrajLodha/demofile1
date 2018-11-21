package com.pws.pateast.activity.parent.home;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.pws.pateast.R;
import com.pws.pateast.activity.attendance.ParentAttendanceReportActivity;
import com.pws.pateast.activity.chat.inbox.InboxActivity;
import com.pws.pateast.activity.dashboard.ChildFooterItem;
import com.pws.pateast.activity.dashboard.HomeActivity;
import com.pws.pateast.activity.feeds.FeedsActivity;
import com.pws.pateast.activity.leave.ParentLeaveStatusActivity;
import com.pws.pateast.activity.parent.fees.WardFeesActivity;
import com.pws.pateast.activity.parent.ward.WardSelectionActivity;
import com.pws.pateast.activity.schedule.activity.ParentScheduleActivity;
import com.pws.pateast.activity.tasks.ParentTaskActivity;
import com.pws.pateast.api.model.DashboardEvent;
import com.pws.pateast.api.model.FeesOld;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.model.Ward;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.base.Extras;
import com.pws.pateast.base.FragmentActivity;
import com.pws.pateast.enums.TaskType;
import com.pws.pateast.utils.DateUtils;
import com.pws.pateast.utils.ImageUtils;
import com.pws.pateast.widget.ProfileImageView;

import static com.pws.pateast.utils.DateUtils.CHAT_DATE_TEMPLATE;
import static com.pws.pateast.utils.DateUtils.SERVER_DATE_TEMPLATE;

public class ParentHomeActivity extends HomeActivity {

    private ProfileImageView imgProfileView;
    private TextView tvHeaderParentName;
    private TextView tvHeaderStudentName;
    private TextView tvHeaderPhoneNo;
    private TextView tvHeaderEmailId;
    private FeesOld feesOld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.ParentTheme);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        navigationView = findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0);
        imgProfileView = headerView.findViewById(R.id.tv_profile_image);
        tvHeaderParentName = headerView.findViewById(R.id.tv_profile_name);
        tvHeaderStudentName = headerView.findViewById(R.id.tv_student_name);
        tvHeaderPhoneNo = headerView.findViewById(R.id.tv_profile_contact_no);
        tvHeaderEmailId = headerView.findViewById(R.id.tv_profile_email);
        super.onViewReady(savedInstanceState);
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.activity_parent_home;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void setData(User user) {
        if (isDestroyed())
            return;
        User userLogin = homePresenter.getUser();
        Ward ward = homePresenter.getWard();

        //Student
        ImageUtils.setImageUrl(getContext(), studentImage, user.getUser_image(), R.drawable.avatar1);
        tvStudentName.setText(user.getFullname());
        tvHeaderStudentName.setText(getString(R.string.parent_of_student_name, user.getFullname()));
        String className = getString(R.string.assignment_bcs_name,
                getString(R.string.title_class),
                user.getBcs().getClasses().getClassesdetails().get(0).getName(),
                user.getBcs().getSection().getSectiondetails().get(0).getName());
        tvStudentClass.setText(className);

        //parent
        ImageUtils.setImageUrl(getContext(), imgProfileView, userLogin.getUserdetails().getUser_image(), R.drawable.avatar1);
        tvHeaderParentName.setText(userLogin.getUserdetails().getFullname());
        if (!TextUtils.isEmpty(userLogin.getData().getMobile())) {
            tvHeaderPhoneNo.setText(userLogin.getData().getMobile());
        } else {
            tvHeaderPhoneNo.setText(R.string.not_available);
        }

        if (!TextUtils.isEmpty(userLogin.getData().getEmail())) {
            tvHeaderEmailId.setText(userLogin.getData().getEmail());
        } else {
            tvHeaderEmailId.setText(R.string.not_available);
        }

    }

    @Override
    public void setDashboardEventAdapter(final DashboardEvent dashboardEvent) {
        super.setDashboardEventAdapter(dashboardEvent);
        rvUpcomingEvent.post(new Runnable() {
            @Override
            public void run() {
                layoutFees.setVisibility(View.GONE);
                if (layoutFees != null && dashboardEvent != null) {
                    feesOld = dashboardEvent.getFee();
                    if (feesOld != null && feesOld.getTotal() > 0 && feesOld.getDate() != null && !TextUtils.isEmpty(feesOld.getDate())) {
                        tvDueDate.setText(DateUtils.toDate(DateUtils.parse(feesOld.getDate(), SERVER_DATE_TEMPLATE), CHAT_DATE_TEMPLATE));
                        tvDueFee.setText(String.format("%s %d", Html.fromHtml("&#8377"), (int) feesOld.getTotal()));
                        layoutFees.setOnClickListener(ParentHomeActivity.this);
                        layoutFees.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

    }

    @Override
    public void onViewMoreClick(View view, DashboardEvent parent, ChildFooterItem childFooter) {
        switch (parent.getExpandableViewType()) {
            case SCHEDULE:
                openActivity(ParentScheduleActivity.class, TaskType.WARD_SCHEDULE);
                break;
            case ATTENDANCE:
                openActivity(ParentAttendanceReportActivity.class);
                break;
            case ASSIGNMENT:
                openActivity(ParentTaskActivity.class, TaskType.PARENT_ASSIGNMENT);
                break;
            case EXAM_SCHEDULE:
                openActivity(ParentScheduleActivity.class, TaskType.WARD_EXAM_SCHEDULE);
                break;
            case EXAM_MARKS:
                openActivity(ParentScheduleActivity.class, TaskType.WARD_MARKS);
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawer.closeDrawers();
        switch (item.getItemId()) {
            case R.id.nav_switch_ward:
                openActivity(WardSelectionActivity.class);
                return false;
            case R.id.nav_my_profile:
                openActivity(ParentTaskActivity.class, TaskType.PARENT_PROFILE);
                return false;
            case R.id.nav_messages:
                Bundle bundle = new Bundle();
                bundle.putInt(Extras.APP_THEME, R.style.ParentTheme);
                openActivity(InboxActivity.class, bundle);
                return false;
            case R.id.nav_school_holidays:
                openActivity(ParentScheduleActivity.class, TaskType.HOLIDAY);
                return false;
            case R.id.nav_school_events:
                openActivity(ParentTaskActivity.class, TaskType.PARENT_EVENTS);
                return false;
            case R.id.nav_circular:
                openActivity(ParentTaskActivity.class, TaskType.PARENT_CIRCULAR);
                return false;
            case R.id.nav_complaint:
                openActivity(ParentTaskActivity.class, TaskType.COMPLAINTS);
                return false;
            case R.id.nav_payment_history:
                openActivity(ParentTaskActivity.class, TaskType.PAYMENT_HISTORY);
                return false;
            case R.id.nav_track_bus:
                openActivity(ParentTaskActivity.class, TaskType.WARD_TRACK_ROUTE);
                return false;
            case R.id.nav_class_schedule:
                openActivity(ParentScheduleActivity.class, TaskType.WARD_SCHEDULE);
                return false;
            case R.id.nav_attendance:
                openActivity(ParentAttendanceReportActivity.class);
                return false;
            case R.id.nav_child_leaves:
                openActivity(ParentLeaveStatusActivity.class, TaskType.PARENT_LEAVE_APPLY);
                return false;
            case R.id.nav_exam_schedules:
                openActivity(ParentScheduleActivity.class, TaskType.WARD_EXAM_SCHEDULE);
                return false;
            case R.id.nav_exam_marks:
                openActivity(ParentScheduleActivity.class, TaskType.WARD_MARKS);
                return false;
            case R.id.nav_assignments:
                openActivity(ParentTaskActivity.class, TaskType.PARENT_ASSIGNMENT);
                return false;
            case R.id.nav_suggestion:
                openActivity(ParentTaskActivity.class, TaskType.PARENT_SUGGESTION);
                return false;
            case R.id.nav_settings:
                openActivity(ParentTaskActivity.class, TaskType.SETTINGS);
                return false;
            case R.id.nav_notifications:
                openActivity(ParentTaskActivity.class, TaskType.NOTIFICATION_LIST);
                return false;
            case R.id.nav_feeds:
                bundle = new Bundle();
                bundle.putInt(Extras.APP_THEME, R.style.ParentTheme);
                openActivity(FeedsActivity.class, bundle);
                return false;
        }
        return super.onNavigationItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.profile_view:
                Bundle bundle = new Bundle();
                bundle.putBoolean(AppFragment.EXTRA_DATA, true);
                bundle.putInt(FragmentActivity.EXTRA_TASK_TYPE, TaskType.STUDENT_PROFILE.getValue());
                openActivity(ParentTaskActivity.class, bundle);
                break;
            case R.id.assignment_view:
                openActivity(ParentTaskActivity.class, TaskType.PARENT_ASSIGNMENT);
                break;
            case R.id.bus_track_view:
                openActivity(ParentTaskActivity.class, TaskType.WARD_TRACK_ROUTE);
                break;
            case R.id.message_view:
                bundle = new Bundle();
                bundle.putInt(Extras.APP_THEME, R.style.ParentTheme);
                openActivity(InboxActivity.class, bundle);
                break;
            case R.id.feeds_view:
                bundle = new Bundle();
                bundle.putInt(Extras.APP_THEME, R.style.ParentTheme);
                openActivity(FeedsActivity.class, bundle);
                break;
            case R.id.fees_view:
                bundle = new Bundle();
                bundle.putParcelable(Extras.FEES, feesOld);
                openActivity(WardFeesActivity.class, bundle);
                break;
        }
    }

    @Override
    public void onError(String message) {

    }

}
