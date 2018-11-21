package com.pws.pateast.activity.tasks;

import android.os.Bundle;

import com.pws.pateast.api.model.User;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.base.Extras;
import com.pws.pateast.enums.TaskType;
import com.pws.pateast.fragment.assignment.parent.ParentAssignmentFragment;
import com.pws.pateast.fragment.assignment.parent.detail.AssignmentDetailFragment;
import com.pws.pateast.fragment.assignment.teacher.AssignmentListFragment;
import com.pws.pateast.fragment.assignment.teacher.add.AddAssignmentFragment;
import com.pws.pateast.fragment.assignment.teacher.review.ReviewAssignmentFragment;
import com.pws.pateast.fragment.attendance.teacher.AttendanceFragment;
import com.pws.pateast.fragment.attendance.teacher.report.AttendanceReportFragment;
import com.pws.pateast.fragment.circular.detail.CircularDetailFragment;
import com.pws.pateast.fragment.circular.parent.ParentCircularFragment;
import com.pws.pateast.fragment.circular.student.StudentCircularFragment;
import com.pws.pateast.fragment.circular.teacher.TeacherCircularFragment;
import com.pws.pateast.fragment.classes.TeacherClassFragment;
import com.pws.pateast.fragment.complaint.ComplaintFragment;
import com.pws.pateast.fragment.complaint.detail.ComplaintDetailFragment;
import com.pws.pateast.fragment.complaint.report.ComplaintReportFragment;
import com.pws.pateast.fragment.events.detail.EventDetailFragment;
import com.pws.pateast.fragment.events.parent.ParentEventFragment;
import com.pws.pateast.fragment.events.student.StudentEventFragment;
import com.pws.pateast.fragment.events.teacher.TeacherEventFragment;
import com.pws.pateast.fragment.feeds.add.AddFeedsFragment;
import com.pws.pateast.fragment.fees.FeesPaymentFragment;
import com.pws.pateast.fragment.home.DummyFragment;
import com.pws.pateast.fragment.leave.parent.apply.WardLeaveApplyFragment;
import com.pws.pateast.fragment.leave.student.apply.StudentLeaveApplyFragment;
import com.pws.pateast.fragment.leave.teacher.apply.TeacherLeaveApplyFragment;
import com.pws.pateast.fragment.leave.teacher.info.TeacherLeaveInfoFragment;
import com.pws.pateast.fragment.marks.student.ExamMarksFragment;
import com.pws.pateast.fragment.notification.ParentNotificationListFragment;
import com.pws.pateast.fragment.privacy.WebFragment;
import com.pws.pateast.fragment.profile.driver.DriverProfileFragment;
import com.pws.pateast.fragment.profile.parent.ParentProfileFragment;
import com.pws.pateast.fragment.profile.student.StudentProfileFragment;
import com.pws.pateast.fragment.profile.teacher.TeacherProfileFragment;
import com.pws.pateast.fragment.schedule.student.classs.StudentScheduleFragment;
import com.pws.pateast.fragment.schedule.student.exam.ExamScheduleFragment;
import com.pws.pateast.fragment.schedule.teacher.classs.TeacherScheduleFragment;
import com.pws.pateast.fragment.settings.SettingsFragment;
import com.pws.pateast.fragment.suggestion.ParentSuggestionFragment;
import com.pws.pateast.fragment.track.driver.StudentRouteFragment;
import com.pws.pateast.fragment.track.parent.WardRouteFragment;
import com.pws.pateast.listener.AppListener;
import com.pws.pateast.utils.Preference;

import javax.inject.Inject;

/**
 * Created by intel on 20-Apr-17.
 */

public class TaskPresenter extends AppPresenter<TaskView> {
    @Inject
    Preference preference;

    private User user;

    private TaskView mTaskView;

    @Override
    public TaskView getView() {
        return mTaskView;
    }

    @Override
    public void attachView(TaskView mTaskView) {
        this.mTaskView = mTaskView;
        getComponent().inject(this);
        user = preference.getUser().getData();
    }

    public void setTaskFragment(TaskType taskType, AppListener appListener, Bundle extras) {
        switch (taskType) {
            case TEACHER_CLASSES:
                extras.putBoolean(Extras.CLASS_ATTENDANCE, false);
                mTaskView.setTaskFragment(AppFragment.newInstance(TeacherClassFragment.class, appListener, extras));
                break;
            case TEACHER_ATTENDANCE:
                extras.putBoolean(Extras.CLASS_ATTENDANCE, true);
                mTaskView.setTaskFragment(AppFragment.newInstance(TeacherClassFragment.class, appListener, extras));
                break;
            case CLASS_ATTENDANCE:
                mTaskView.setTaskFragment(AppFragment.newInstance(AttendanceFragment.class, appListener, extras));
                break;
            case STUDENT_ASSIGNMENT:
                mTaskView.setTaskFragment(AppFragment.newInstance(ParentAssignmentFragment.class, appListener, extras));
                break;
            case STUDENT_MARKS:
                mTaskView.setTaskFragment(AppFragment.newInstance(ExamMarksFragment.class, appListener, extras));
                break;
            case TEACHER_MARKS:
                mTaskView.setTaskFragment(AppFragment.newInstance(ExamMarksFragment.class, appListener, extras));
                break;
            case STUDENT_EXAM_SCHEDULE:
                mTaskView.setTaskFragment(AppFragment.newInstance(ExamScheduleFragment.class, appListener, extras));
                break;
            case TEACHER_EXAM_SCHEDULE:
                mTaskView.setTaskFragment(AppFragment.newInstance(ExamScheduleFragment.class, appListener, extras));
                break;
            case ASSIGNMENT:
                mTaskView.setTaskFragment(AppFragment.newInstance(AssignmentListFragment.class, appListener, extras));
                break;
            case ADD_ASSIGNMENT:
                mTaskView.setTaskFragment(AppFragment.newInstance(AddAssignmentFragment.class, appListener, extras));
                break;
            case REVIEW_ASSIGNMENT:
                mTaskView.setTaskFragment(AppFragment.newInstance(ReviewAssignmentFragment.class, appListener, extras));
                break;
            case ATTENDANCE_REPORT:
                mTaskView.setTaskFragment(AppFragment.newInstance(AttendanceReportFragment.class, appListener, extras));
                break;
            case EMPLOYEE_LEAVE_APPLY:
                mTaskView.setTaskFragment(AppFragment.newInstance(TeacherLeaveApplyFragment.class, appListener, extras));
                break;
            case TEACHER_LEAVE_INFO:
                mTaskView.setTaskFragment(AppFragment.newInstance(TeacherLeaveInfoFragment.class, appListener, extras));
                break;
            case STUDENT_LEAVE_APPLY:
                mTaskView.setTaskFragment(AppFragment.newInstance(StudentLeaveApplyFragment.class, appListener, extras));
                break;
            case PARENT_LEAVE_APPLY:
                mTaskView.setTaskFragment(AppFragment.newInstance(WardLeaveApplyFragment.class, appListener, extras));
                break;
            case TEACHER_SCHEDULE:
                mTaskView.setTaskFragment(AppFragment.newInstance(TeacherScheduleFragment.class, appListener, extras));
                break;
            case STUDENT_SCHEDULE:
                mTaskView.setTaskFragment(AppFragment.newInstance(StudentScheduleFragment.class, appListener, extras));
                break;
            case SETTINGS:
                mTaskView.setTaskFragment(SettingsFragment.newInstance(SettingsFragment.class, appListener, extras));
                break;
            case PARENT_ASSIGNMENT:
                mTaskView.setTaskFragment(AppFragment.newInstance(ParentAssignmentFragment.class, appListener, extras));
                break;
            case ASSIGNMENT_LIST:
                mTaskView.setTaskFragment(AppFragment.newInstance(ParentAssignmentFragment.class, appListener, extras));
                break;
            case ASSIGNMENT_DETAIL:
                mTaskView.setTaskFragment(AppFragment.newInstance(AssignmentDetailFragment.class, appListener, extras));
                break;
            case TEACHER_PROFILE:
                mTaskView.setTaskFragment(AppFragment.newInstance(TeacherProfileFragment.class, appListener, extras));
                break;
            case STUDENT_PROFILE:
                mTaskView.setTaskFragment(AppFragment.newInstance(StudentProfileFragment.class, appListener, extras));
                break;
            case PARENT_PROFILE:
                mTaskView.setTaskFragment(AppFragment.newInstance(ParentProfileFragment.class, appListener, extras));
                break;
            case DRIVER_PROFILE:
                mTaskView.setTaskFragment(AppFragment.newInstance(DriverProfileFragment.class, appListener, extras));
                break;
            case WARD_TRACK_ROUTE:
                mTaskView.setTaskFragment(AppFragment.newInstance(WardRouteFragment.class, appListener, extras));
                break;
            case STUDENT_ON_BOARD:
                mTaskView.setTaskFragment(AppFragment.newInstance(StudentRouteFragment.class, appListener, extras));
                break;
            case PARENT_SUGGESTION:
                mTaskView.setTaskFragment(AppFragment.newInstance(ParentSuggestionFragment.class, appListener, extras));
                break;
            case PRIVACY_TOS:
                mTaskView.setTaskFragment(AppFragment.newInstance(WebFragment.class, appListener, extras));
                break;
            case NOTIFICATION_LIST:
                mTaskView.setTaskFragment(AppFragment.newInstance(ParentNotificationListFragment.class, appListener, extras));
                break;
            case PAYMENT_HISTORY:
                mTaskView.setTaskFragment(AppFragment.newInstance(FeesPaymentFragment.class, appListener, extras));
                break;
            case COMPLAINTS:
                mTaskView.setTaskFragment(AppFragment.newInstance(ComplaintFragment.class, appListener, extras));
                break;
            case COMPLAINT_DETAIL:
                mTaskView.setTaskFragment(AppFragment.newInstance(ComplaintDetailFragment.class, appListener, extras));
                break;
            case COMPLAINT_REPORT:
                mTaskView.setTaskFragment(AppFragment.newInstance(ComplaintReportFragment.class, appListener, extras));
                break;
            case PARENT_EVENTS:
                mTaskView.setTaskFragment(AppFragment.newInstance(ParentEventFragment.class, appListener, extras));
                break;
            case STUDENT_EVENTS:
                mTaskView.setTaskFragment(AppFragment.newInstance(StudentEventFragment.class, appListener, extras));
                break;
            case TEACHER_EVENTS:
                mTaskView.setTaskFragment(AppFragment.newInstance(TeacherEventFragment.class, appListener, extras));
                break;
            case EVENTS_DETAILS:
                mTaskView.setTaskFragment(AppFragment.newInstance(EventDetailFragment.class, appListener, extras));
                break;
            case PARENT_CIRCULAR:
                mTaskView.setTaskFragment(AppFragment.newInstance(ParentCircularFragment.class, appListener, extras));
                break;
            case STUDENT_CIRCULAR:
                mTaskView.setTaskFragment(AppFragment.newInstance(StudentCircularFragment.class, appListener, extras));
                break;
            case TEACHER_CIRCULAR:
                mTaskView.setTaskFragment(AppFragment.newInstance(TeacherCircularFragment.class, appListener, extras));
                break;
            case CIRCULAR_DETAILS:
                mTaskView.setTaskFragment(AppFragment.newInstance(CircularDetailFragment.class, appListener, extras));
                break;
            case ADD_FEED:
                mTaskView.setTaskFragment(AppFragment.newInstance(AddFeedsFragment.class, appListener, extras));
                break;
            default:
                mTaskView.setTaskFragment(AppFragment.newInstance(DummyFragment.class, appListener, extras));
                break;
        }
    }
}
