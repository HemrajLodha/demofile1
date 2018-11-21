package com.pws.pateast.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.pws.pateast.R;
import com.pws.pateast.activity.SplashActivity;
import com.pws.pateast.activity.attendance.ParentAttendanceReportActivity;
import com.pws.pateast.activity.attendance.StudentAttendanceReportActivity;
import com.pws.pateast.activity.chat.inbox.InboxActivity;
import com.pws.pateast.activity.driver.home.DriverHomeActivity;
import com.pws.pateast.activity.leave.DriverLeaveStatusActivity;
import com.pws.pateast.activity.leave.ParentLeaveStatusActivity;
import com.pws.pateast.activity.leave.StudentLeaveStatusActivity;
import com.pws.pateast.activity.leave.TeacherLeaveStatusActivity;
import com.pws.pateast.activity.login.LoginActivity;
import com.pws.pateast.activity.parent.home.ParentHomeActivity;
import com.pws.pateast.activity.parent.ward.WardSelectionActivity;
import com.pws.pateast.activity.schedule.activity.ParentScheduleActivity;
import com.pws.pateast.activity.schedule.activity.StudentScheduleActivity;
import com.pws.pateast.activity.schedule.activity.TeacherScheduleActivity;
import com.pws.pateast.activity.student.home.StudentHomeActivity;
import com.pws.pateast.activity.tasks.ParentTaskActivity;
import com.pws.pateast.activity.tasks.StudentTaskActivity;
import com.pws.pateast.activity.tasks.TeacherTaskActivity;
import com.pws.pateast.activity.teacher.home.TeacherHomeActivity;
import com.pws.pateast.base.Extras;
import com.pws.pateast.base.FragmentActivity;
import com.pws.pateast.enums.NotificationType;
import com.pws.pateast.enums.TaskType;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.widget.BadgeDrawable;

/**
 * Created by intel on 08-Aug-17.
 */

public class NotificationHelper {
    private static int notificationId = 101;


    public static void showNotificationIntent(Context context, String notificationType, boolean fromNotification) {

        Preference preference = Preference.get(context);
        UserType userType = UserType.getUserType(preference.getUser().getData().getUser_type());
        Intent intent = null;

        switch (NotificationType.getNotificationType(notificationType)) {
            case EXAM_SYLLABUS:
            case EXAM_SCHEDULE:
                switch (userType) {
                    case STUDENT:
                        intent = new Intent(context, StudentScheduleActivity.class);
                        intent.putExtra(FragmentActivity.EXTRA_TASK_TYPE, TaskType.STUDENT_EXAM_SCHEDULE.getValue());
                        break;
                    case PARENT:
                        intent = new Intent(context, ParentScheduleActivity.class);
                        intent.putExtra(FragmentActivity.EXTRA_TASK_TYPE, TaskType.STUDENT_EXAM_SCHEDULE.getValue());
                        break;
                    case TEACHER:
                        intent = new Intent(context, TeacherTaskActivity.class);
                        intent.putExtra(FragmentActivity.EXTRA_TASK_TYPE, TaskType.TEACHER_EXAM_SCHEDULE.getValue());
                        break;
                }
                break;
            case TRANSPORT:
            case START_PICK_UP:
            case CONFIRM_PICKUP_ON_BOARD:
                switch (userType) {
                    case STUDENT:
                        // TODO later
                        break;
                    case PARENT:
                        intent = new Intent(context, ParentTaskActivity.class);
                        intent.putExtra(FragmentActivity.EXTRA_TASK_TYPE, TaskType.WARD_TRACK_ROUTE.getValue());
                        break;
                }
                break;
            case ATTENDANCE:
                switch (userType) {
                    case STUDENT:
                        intent = new Intent(context, StudentAttendanceReportActivity.class);
                        break;
                    case PARENT:
                        intent = new Intent(context, ParentAttendanceReportActivity.class);
                        break;
                }
                break;
            case LEAVE_REJECTED:
            case LEAVE_APPROVED:
                switch (userType) {
                    case TEACHER:
                        intent = new Intent(context, TeacherLeaveStatusActivity.class);
                        intent.putExtra(FragmentActivity.EXTRA_TASK_TYPE, TaskType.EMPLOYEE_LEAVE_APPLY.getValue());
                        intent.putExtra(FragmentActivity.EXTRA_NOTIFICATION_TYPE, notificationType);
                        break;
                    case DRIVER:
                        intent = new Intent(context, DriverLeaveStatusActivity.class);
                        intent.putExtra(FragmentActivity.EXTRA_TASK_TYPE, TaskType.EMPLOYEE_LEAVE_APPLY.getValue());
                        intent.putExtra(FragmentActivity.EXTRA_NOTIFICATION_TYPE, notificationType);
                        break;
                }
                break;
            case TIMETABLE:
                intent = new Intent(context, TeacherScheduleActivity.class);
                intent.putExtra(FragmentActivity.EXTRA_TASK_TYPE, TaskType.TEACHER_SCHEDULE.getValue());
                break;
            case ASSIGNMENT:
            case ASSIGNMENT_REMARK:
                switch (userType) {
                    case STUDENT:
                        intent = new Intent(context, StudentTaskActivity.class);
                        intent.putExtra(FragmentActivity.EXTRA_TASK_TYPE, TaskType.STUDENT_ASSIGNMENT.getValue());
                        break;
                    case PARENT:
                        intent = new Intent(context, ParentTaskActivity.class);
                        intent.putExtra(FragmentActivity.EXTRA_TASK_TYPE, TaskType.PARENT_ASSIGNMENT.getValue());
                        break;
                }
                break;
            case STUDENT_LEAVE:
                switch (userType) {
                    case STUDENT:
                        intent = new Intent(context, StudentLeaveStatusActivity.class);
                        intent.putExtra(FragmentActivity.EXTRA_TASK_TYPE, TaskType.STUDENT_LEAVE_APPLY.getValue());
                        intent.putExtra(FragmentActivity.EXTRA_NOTIFICATION_TYPE, notificationType);
                        break;
                    case PARENT:
                        intent = new Intent(context, ParentLeaveStatusActivity.class);
                        intent.putExtra(FragmentActivity.EXTRA_TASK_TYPE, TaskType.PARENT_LEAVE_APPLY.getValue());
                        intent.putExtra(FragmentActivity.EXTRA_NOTIFICATION_TYPE, notificationType);
                        break;
                }
                break;
            case MESSAGE:
                intent = new Intent(context, InboxActivity.class);
                switch (userType) {
                    case STUDENT:
                        intent.putExtra(Extras.APP_THEME, R.style.StudentTheme);
                        break;
                    case PARENT:
                        intent.putExtra(Extras.APP_THEME, R.style.ParentTheme);
                        break;
                    case TEACHER:
                        intent.putExtra(Extras.APP_THEME, R.style.TeacherTheme);
                        break;
                }
                break;
            case EXAM_MARKS:
                switch (userType) {
                    case STUDENT:
                        intent = new Intent(context, StudentScheduleActivity.class);
                        intent.putExtra(FragmentActivity.EXTRA_TASK_TYPE, TaskType.STUDENT_MARKS.getValue());
                        break;
                    case PARENT:
                        intent = new Intent(context, ParentScheduleActivity.class);
                        intent.putExtra(FragmentActivity.EXTRA_TASK_TYPE, TaskType.STUDENT_MARKS.getValue());
                        break;
                }
                break;
            case COMPLAINT:
                switch (userType) {
                    case STUDENT:
                        intent = new Intent(context, StudentTaskActivity.class);
                        intent.putExtra(FragmentActivity.EXTRA_TASK_TYPE, TaskType.COMPLAINTS.getValue());
                        break;
                    case PARENT:
                        intent = new Intent(context, ParentTaskActivity.class);
                        intent.putExtra(FragmentActivity.EXTRA_TASK_TYPE, TaskType.COMPLAINTS.getValue());
                        break;
                }
                break;
            case CIRCULAR:
                switch (userType) {
                    case STUDENT:
                        intent = new Intent(context, StudentTaskActivity.class);
                        intent.putExtra(FragmentActivity.EXTRA_TASK_TYPE, TaskType.STUDENT_CIRCULAR.getValue());
                        break;
                    case PARENT:
                        intent = new Intent(context, ParentTaskActivity.class);
                        intent.putExtra(FragmentActivity.EXTRA_TASK_TYPE, TaskType.PARENT_CIRCULAR.getValue());
                        break;
                    case TEACHER:
                        intent = new Intent(context, TeacherTaskActivity.class);
                        intent.putExtra(FragmentActivity.EXTRA_TASK_TYPE, TaskType.TEACHER_CIRCULAR.getValue());
                        break;
                }
                break;
            case EVENT:
                switch (userType) {
                    case STUDENT:
                        intent = new Intent(context, StudentTaskActivity.class);
                        intent.putExtra(FragmentActivity.EXTRA_TASK_TYPE, TaskType.STUDENT_EVENTS.getValue());
                        break;
                    case PARENT:
                        intent = new Intent(context, ParentTaskActivity.class);
                        intent.putExtra(FragmentActivity.EXTRA_TASK_TYPE, TaskType.PARENT_EVENTS.getValue());
                        break;
                    case TEACHER:
                        intent = new Intent(context, TeacherTaskActivity.class);
                        intent.putExtra(FragmentActivity.EXTRA_TASK_TYPE, TaskType.TEACHER_EVENTS.getValue());
                        break;
                }
                break;
            case NONE:
            default:
                if (fromNotification) {
                    switch (userType) {
                        case STUDENT:
                            intent = new Intent(context, StudentHomeActivity.class);
                            break;
                        case PARENT:
                            intent = new Intent(context, preference.getWard() == null ? WardSelectionActivity.class : ParentHomeActivity.class);
                            break;
                        case TEACHER:
                            intent = new Intent(context, TeacherHomeActivity.class);
                            break;
                        case DRIVER:
                            intent = new Intent(context, DriverHomeActivity.class);
                            break;
                    }
                }
                break;
        }
        if (intent != null) {
            context.startActivity(intent);
        }
    }


    public static void showNotificationWithIntent(Context context, String title, String message, String notificationType) {

        Preference preference = Preference.get(context);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = null;
        String channelId = context.getString(R.string.notification_channel_id);
        String channelName = "Pateast";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        // start login activity if user logged out from app
        if (preference.getUser() == null) {
            Intent intent = new Intent(context, LoginActivity.class);
            pendingIntent = getStackPendingIntent(context, notificationId, intent);
            notificationManager.notify(notificationId, getNotificationBuilder(context, channelId, title, message, pendingIntent).build());
            return;
        }

        UserType userType = UserType.getUserType(preference.getUser().getData().getUser_type());
        Intent intent = null;
        switch (NotificationType.getNotificationType(notificationType)) {
            case EXAM_SYLLABUS:
            case EXAM_SCHEDULE:
                switch (userType) {
                    case STUDENT:
                        intent = new Intent(context, StudentScheduleActivity.class);
                        intent.putExtra(FragmentActivity.EXTRA_TASK_TYPE, TaskType.STUDENT_EXAM_SCHEDULE.getValue());
                        break;
                    case PARENT:
                        intent = new Intent(context, ParentScheduleActivity.class);
                        intent.putExtra(FragmentActivity.EXTRA_TASK_TYPE, TaskType.WARD_EXAM_SCHEDULE.getValue());
                        break;
                    case TEACHER:
                        intent = new Intent(context, TeacherTaskActivity.class);
                        intent.putExtra(FragmentActivity.EXTRA_TASK_TYPE, TaskType.TEACHER_EXAM_SCHEDULE.getValue());
                        break;
                }
                break;
            case TRANSPORT:
            case START_PICK_UP:
            case CONFIRM_PICKUP_ON_BOARD:
                switch (userType) {
                    case STUDENT:
                        // TODO later
                        break;
                    case PARENT:
                        intent = new Intent(context, ParentTaskActivity.class);
                        intent.putExtra(FragmentActivity.EXTRA_TASK_TYPE, TaskType.WARD_TRACK_ROUTE.getValue());
                        break;
                }
                break;
            case ATTENDANCE:
                switch (userType) {
                    case STUDENT:
                        intent = new Intent(context, StudentAttendanceReportActivity.class);
                        break;
                    case PARENT:
                        intent = new Intent(context, ParentAttendanceReportActivity.class);
                        break;
                }
                break;
            case LEAVE_REJECTED:
            case LEAVE_APPROVED:
                switch (userType) {
                    case TEACHER:
                        intent = new Intent(context, TeacherLeaveStatusActivity.class);
                        intent.putExtra(FragmentActivity.EXTRA_TASK_TYPE, TaskType.EMPLOYEE_LEAVE_APPLY.getValue());
                        intent.putExtra(FragmentActivity.EXTRA_NOTIFICATION_TYPE, notificationType);
                        break;
                    case DRIVER:
                        intent = new Intent(context, DriverLeaveStatusActivity.class);
                        intent.putExtra(FragmentActivity.EXTRA_TASK_TYPE, TaskType.EMPLOYEE_LEAVE_APPLY.getValue());
                        intent.putExtra(FragmentActivity.EXTRA_NOTIFICATION_TYPE, notificationType);
                        break;
                }
                break;
            case TIMETABLE:
                intent = new Intent(context, TeacherScheduleActivity.class);
                intent.putExtra(FragmentActivity.EXTRA_TASK_TYPE, TaskType.TEACHER_SCHEDULE.getValue());
                break;
            case ASSIGNMENT:
            case ASSIGNMENT_REMARK:
                switch (userType) {
                    case STUDENT:
                        intent = new Intent(context, StudentTaskActivity.class);
                        intent.putExtra(FragmentActivity.EXTRA_TASK_TYPE, TaskType.STUDENT_ASSIGNMENT.getValue());
                        break;
                    case PARENT:
                        intent = new Intent(context, ParentTaskActivity.class);
                        intent.putExtra(FragmentActivity.EXTRA_TASK_TYPE, TaskType.PARENT_ASSIGNMENT.getValue());
                        break;
                }
                break;
            case STUDENT_LEAVE:
                switch (userType) {
                    case STUDENT:
                        intent = new Intent(context, StudentLeaveStatusActivity.class);
                        intent.putExtra(FragmentActivity.EXTRA_TASK_TYPE, TaskType.STUDENT_LEAVE_APPLY.getValue());
                        intent.putExtra(FragmentActivity.EXTRA_NOTIFICATION_TYPE, notificationType);
                        break;
                    case PARENT:
                        intent = new Intent(context, ParentLeaveStatusActivity.class);
                        intent.putExtra(FragmentActivity.EXTRA_TASK_TYPE, TaskType.PARENT_LEAVE_APPLY.getValue());
                        intent.putExtra(FragmentActivity.EXTRA_NOTIFICATION_TYPE, notificationType);
                        break;
                }
                break;
            case MESSAGE:
                intent = new Intent(context, InboxActivity.class);
                switch (userType) {
                    case STUDENT:
                        intent.putExtra(Extras.APP_THEME, R.style.StudentTheme);
                        break;
                    case PARENT:
                        intent.putExtra(Extras.APP_THEME, R.style.ParentTheme);
                        break;
                    case TEACHER:
                        intent.putExtra(Extras.APP_THEME, R.style.TeacherTheme);
                        break;
                }
                break;
            case EXAM_MARKS:
                switch (userType) {
                    case STUDENT:
                        intent = new Intent(context, StudentScheduleActivity.class);
                        intent.putExtra(FragmentActivity.EXTRA_TASK_TYPE, TaskType.STUDENT_MARKS.getValue());
                        break;
                    case PARENT:
                        intent = new Intent(context, ParentScheduleActivity.class);
                        intent.putExtra(FragmentActivity.EXTRA_TASK_TYPE, TaskType.WARD_MARKS.getValue());
                        break;
                }
                break;
            case CIRCULAR:
                switch (userType) {
                    case STUDENT:
                        intent = new Intent(context, StudentTaskActivity.class);
                        intent.putExtra(FragmentActivity.EXTRA_TASK_TYPE, TaskType.STUDENT_CIRCULAR.getValue());
                        break;
                    case PARENT:
                        intent = new Intent(context, ParentTaskActivity.class);
                        intent.putExtra(FragmentActivity.EXTRA_TASK_TYPE, TaskType.PARENT_CIRCULAR.getValue());
                        break;
                    case TEACHER:
                        intent = new Intent(context, TeacherTaskActivity.class);
                        intent.putExtra(FragmentActivity.EXTRA_TASK_TYPE, TaskType.TEACHER_CIRCULAR.getValue());
                        break;
                }
                break;
            case EVENT:
                switch (userType) {
                    case STUDENT:
                        intent = new Intent(context, StudentTaskActivity.class);
                        intent.putExtra(FragmentActivity.EXTRA_TASK_TYPE, TaskType.STUDENT_EVENTS.getValue());
                        break;
                    case PARENT:
                        intent = new Intent(context, ParentTaskActivity.class);
                        intent.putExtra(FragmentActivity.EXTRA_TASK_TYPE, TaskType.PARENT_EVENTS.getValue());
                        break;
                    case TEACHER:
                        intent = new Intent(context, TeacherTaskActivity.class);
                        intent.putExtra(FragmentActivity.EXTRA_TASK_TYPE, TaskType.TEACHER_EVENTS.getValue());
                        break;
                }
                break;
            case NONE:
            default:
                switch (userType) {
                    case STUDENT:
                        intent = new Intent(context, StudentTaskActivity.class);
                        intent.putExtra(FragmentActivity.EXTRA_TASK_TYPE, TaskType.NOTIFICATION_LIST.getValue());
                        break;
                    case PARENT:
                        intent = new Intent(context, ParentTaskActivity.class);
                        intent.putExtra(FragmentActivity.EXTRA_TASK_TYPE, TaskType.NOTIFICATION_LIST.getValue());
                        break;
                    case TEACHER:
                        intent = new Intent(context, TeacherTaskActivity.class);
                        intent.putExtra(FragmentActivity.EXTRA_TASK_TYPE, TaskType.NOTIFICATION_LIST.getValue());
                        break;
                    default:
                        intent = new Intent(context, SplashActivity.class);
                        break;
                }
        }
        if (intent != null) {
            pendingIntent = getPendingIntent(context, notificationId, intent);

            notificationManager.notify(notificationId, getNotificationBuilder(context, channelId, title, message, pendingIntent).build());
        }
    }

    private static PendingIntent getStackPendingIntent(Context context, int requestCode, Intent intent) {
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(intent.resolveActivity(context.getPackageManager()));
        stackBuilder.addNextIntent(intent);
        return PendingIntent.getActivities(context, requestCode,
                new Intent[]{new Intent(context, SplashActivity.class), intent}, PendingIntent.FLAG_ONE_SHOT);
        /*return stackBuilder.getPendingIntent(requestCode, PendingIntent.FLAG_UPDATE_CURRENT);*/
    }


    private static PendingIntent getPendingIntent(Context context, int requestCode, Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return PendingIntent.getActivity(context,
                requestCode,
                intent,
                PendingIntent.FLAG_ONE_SHOT);
    }


    private static NotificationCompat.Builder getNotificationBuilder(Context context, String channelId, String title, String message, PendingIntent pendingIntent) {

        final Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        return new NotificationCompat.Builder(context, channelId)
                //.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_notification))
                .setSmallIcon(R.drawable.ic_notification)
                .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setSound(soundUri);
    }


    public static void setBadgeCount(Context context, LayerDrawable icon, String count) {
        BadgeDrawable badge;
        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
    }

}
