package com.pws.pateast.activity.teacher.home;

import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pws.pateast.R;
import com.pws.pateast.activity.assignment.AssignmentStatusActivity;
import com.pws.pateast.activity.chat.inbox.InboxActivity;
import com.pws.pateast.activity.feeds.FeedsActivity;
import com.pws.pateast.activity.leave.TeacherLeaveStatusActivity;
import com.pws.pateast.activity.schedule.activity.TeacherScheduleActivity;
import com.pws.pateast.activity.tasks.TeacherTaskActivity;
import com.pws.pateast.activity.teacher.student.StudentFilterActivity;
import com.pws.pateast.api.model.TeacherDashboard;
import com.pws.pateast.api.model.User;
import com.pws.pateast.base.Extras;
import com.pws.pateast.base.ResponseAppActivity;
import com.pws.pateast.enums.MessageStatusType;
import com.pws.pateast.enums.TaskType;
import com.pws.pateast.listener.AppListener;
import com.pws.pateast.provider.table.Chat;
import com.pws.pateast.utils.DateUtils;
import com.pws.pateast.utils.ImageUtils;
import com.pws.pateast.widget.BadgeView;

public class TeacherHomeActivity extends ResponseAppActivity
        implements TeacherHomeView,
        View.OnClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {
    private int COUNT_LOADER_ID = 101;
    private TeacherHomePresenter teacherHomePresenter;


    private ImageView imgProfile;
    private TextView tvTeacherName;
    private TextView tvSchoolName;
    private TextView tvMessageCount;
    private TextView tvExamMarkDay;
    private TextView tvExamMarkDate;
    private TextView tvNotificationCount;
    private BadgeView badgeMessage;
    private BadgeView badgeNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.TeacherTheme);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);
        imgProfile = findViewById(R.id.img_profile);
        tvTeacherName = findViewById(R.id.tvName);
        tvSchoolName = findViewById(R.id.school_name);
        tvExamMarkDay = findViewById(R.id.exam_day);
        tvExamMarkDate = findViewById(R.id.exam_date);
        tvMessageCount = findViewById(R.id.message_count);
        badgeMessage = new BadgeView(getContext(), tvMessageCount);
        badgeMessage.setBadgeBackgroundColor(ContextCompat.getColor(getContext(), R.color.md_yellow_800));
        tvNotificationCount = findViewById(R.id.notification_count);
        badgeNotification = new BadgeView(getContext(), tvNotificationCount);
        badgeNotification.setBadgeBackgroundColor(ContextCompat.getColor(getContext(), R.color.md_yellow_800));
        findViewById(R.id.profile_view).setOnClickListener(this);
        findViewById(R.id.message_view).setOnClickListener(this);
        findViewById(R.id.notification_view).setOnClickListener(this);
        findViewById(R.id.class_view).setOnClickListener(this);
        findViewById(R.id.assignment_view).setOnClickListener(this);
        findViewById(R.id.schedule_view).setOnClickListener(this);
        findViewById(R.id.exam_mark_view).setOnClickListener(this);
        findViewById(R.id.student_view).setOnClickListener(this);
        findViewById(R.id.exam_syllabus_view).setOnClickListener(this);
        findViewById(R.id.attendance_view).setOnClickListener(this);
        findViewById(R.id.my_leave_view).setOnClickListener(this);
        findViewById(R.id.student_leave_view).setOnClickListener(this);
        findViewById(R.id.holiday_view).setOnClickListener(this);
        findViewById(R.id.event_view).setOnClickListener(this);
        findViewById(R.id.circular_view).setOnClickListener(this);
        findViewById(R.id.feeds_view).setOnClickListener(this);
        findViewById(R.id.settings_view).setOnClickListener(this);

        teacherHomePresenter = new TeacherHomePresenter();
        teacherHomePresenter.attachView(this);
        getLoaderManager().initLoader(COUNT_LOADER_ID, null, this);
        teacherHomePresenter.getDashboardData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (teacherHomePresenter != null) {
            teacherHomePresenter.getDashboardData();
        }
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.activity_teacher_home;
    }


    @Override
    public boolean isToolbarSetupEnabled() {
        return false;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void setData(User user) {
        if (isDestroyed())
            return;
        String salutation = !TextUtils.isEmpty(user.getSalutation()) ?
                (user.getSalutation() + ". ") : "";
        tvTeacherName.setText(salutation + user.getFullname());
        tvSchoolName.setText(user.getInstitute_name());
        tvExamMarkDay.setText(DateUtils.getWeekDayNameByDate(this));
        tvExamMarkDate.setText(String.valueOf(DateUtils.getCurrentDayDate()));

        ImageUtils.setImageUrl(getContext(), imgProfile, user.getUser_image(), R.drawable.avatar1);
    }

    @Override
    public void setNotificationCount(TeacherDashboard teacherDashboard) {
        int notificationCount = teacherDashboard.getNotification();
        if (notificationCount > 0) {
            badgeNotification.setText(String.valueOf(notificationCount < 100 ? notificationCount : "99+"));
            badgeNotification.show();
        } else {
            badgeNotification.hide();
        }
    }

    @Override
    public AppListener getAppListener() {
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_view:
                openActivity(TeacherTaskActivity.class, TaskType.TEACHER_PROFILE);
                break;
            case R.id.schedule_view:
                openActivity(TeacherScheduleActivity.class, TaskType.TEACHER_SCHEDULE);
                break;
            case R.id.class_view:
                openActivity(TeacherTaskActivity.class, TaskType.TEACHER_CLASSES);
                break;
            case R.id.assignment_view:
                openActivity(AssignmentStatusActivity.class);
                break;
            case R.id.exam_mark_view:
                openActivity(TeacherTaskActivity.class, TaskType.TEACHER_MARKS);
                break;
            case R.id.student_view:
                openActivity(StudentFilterActivity.class);
                break;
            case R.id.exam_syllabus_view:
                openActivity(TeacherTaskActivity.class, TaskType.TEACHER_EXAM_SCHEDULE);
                break;
            case R.id.attendance_view:
                openActivity(TeacherTaskActivity.class, TaskType.TEACHER_ATTENDANCE);
                break;
            case R.id.my_leave_view:
                openActivity(TeacherLeaveStatusActivity.class, TaskType.EMPLOYEE_LEAVE_APPLY);
                break;
            case R.id.student_leave_view:
                openActivity(TeacherLeaveStatusActivity.class, TaskType.STUDENT_LEAVE_APPLY);
                break;
            case R.id.holiday_view:
                openActivity(TeacherScheduleActivity.class, TaskType.HOLIDAY);
                break;
            case R.id.event_view:
                openActivity(TeacherTaskActivity.class, TaskType.TEACHER_EVENTS);
                break;
            case R.id.circular_view:
                openActivity(TeacherTaskActivity.class, TaskType.TEACHER_CIRCULAR);
                break;
            case R.id.feeds_view:
                Bundle bundle = new Bundle();
                bundle.putInt(Extras.APP_THEME, R.style.TeacherTheme);
                openActivity(FeedsActivity.class, bundle);
                break;
            case R.id.settings_view:
                openActivity(TeacherTaskActivity.class, TaskType.SETTINGS);
                break;
            case R.id.message_view:
                bundle = new Bundle();
                bundle.putInt(Extras.APP_THEME, R.style.TeacherTheme);
                openActivity(InboxActivity.class, bundle);
                break;
            case R.id.notification_view:
                openActivity(TeacherTaskActivity.class, TaskType.NOTIFICATION_LIST);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getLoaderManager().destroyLoader(COUNT_LOADER_ID);
        if (teacherHomePresenter != null)
            teacherHomePresenter.detachView();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this, Chat.CONTENT_URI, Chat.PROJECTION_COUNT,
                Chat.COLUMN_SENDER_ID + "!=? AND (" +
                        Chat.COLUMN_MESSAGE_STATUS + "=? OR " + Chat.COLUMN_MESSAGE_STATUS + "=?)",
                new String[]{String.valueOf(teacherHomePresenter.getReceiverId()), String.valueOf(MessageStatusType.STATUS_RECEIVED.getValue()), String.valueOf(MessageStatusType.STATUS_SENT.getValue())},
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        int unreadCount = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                unreadCount = cursor.getInt(0);
            }
        }

        if (unreadCount > 0) {
            badgeMessage.setText(unreadCount < 100 ? ((unreadCount < 10 ? "0" : "") + String.valueOf(unreadCount)) : "99+");
            badgeMessage.show();
        } else {
            badgeMessage.hide();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
