package com.pws.pateast.di.component;


import com.pws.pateast.activity.SplashActivity;
import com.pws.pateast.activity.chat.add.ChatUserPresenter;
import com.pws.pateast.activity.chat.inbox.InboxPresenter;
import com.pws.pateast.activity.chat.message.MessagePresenter;
import com.pws.pateast.activity.driver.home.DriverHomePresenter;
import com.pws.pateast.activity.feeds.FeedsActivityPresenter;
import com.pws.pateast.activity.home.HomeActivity;
import com.pws.pateast.activity.home.HomePresenter;
import com.pws.pateast.activity.leave.LeaveStatusPresenter;
import com.pws.pateast.activity.lms.LMSPresenter;
import com.pws.pateast.activity.lms.filter.LMSFilterPresenter;
import com.pws.pateast.activity.login.LoginPresenter;
import com.pws.pateast.activity.login.forgot.ForgotPasswordPresenter;
import com.pws.pateast.activity.parent.fees.WardFeesPresenter;
import com.pws.pateast.activity.parent.ward.WardSelectionPresenter;
import com.pws.pateast.activity.schedule.SchedulePresenter;
import com.pws.pateast.activity.tasks.TaskActivity;
import com.pws.pateast.activity.tasks.TaskPresenter;
import com.pws.pateast.activity.teacher.home.TeacherHomePresenter;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.base.locationmanager.LocationPresenter;
import com.pws.pateast.base.locationmanager.LocationView;
import com.pws.pateast.di.ActivityScope;
import com.pws.pateast.di.module.ActivityModule;
import com.pws.pateast.fragment.assignment.AssignmentDownloadPresenter;
import com.pws.pateast.fragment.assignment.AssignmentDownloadView;
import com.pws.pateast.fragment.assignment.parent.detail.AssignmentDetailPresenter;
import com.pws.pateast.fragment.assignment.student.StudentAssignmentPresenter;
import com.pws.pateast.fragment.assignment.teacher.AssignmentListPresenter;
import com.pws.pateast.fragment.assignment.teacher.add.AddAssignmentPresenter;
import com.pws.pateast.fragment.assignment.teacher.review.ReviewAssignmentPresenter;
import com.pws.pateast.fragment.attendance.student.StudentAttendancePresenter;
import com.pws.pateast.fragment.attendance.teacher.AttendancePresenter;
import com.pws.pateast.fragment.attendance.teacher.addupdate.AddUpdateAttendancePresenter;
import com.pws.pateast.fragment.attendance.teacher.report.AttendanceReportPresenter;
import com.pws.pateast.fragment.attendance.teacher.report.calender.CalenderReportPresenter;
import com.pws.pateast.fragment.attendance.teacher.report.calender.CalenderReportView;
import com.pws.pateast.fragment.circular.CircularPresenter;
import com.pws.pateast.fragment.circular.detail.CircularDetailPresenter;
import com.pws.pateast.fragment.classes.TeacherClassPresenter;
import com.pws.pateast.fragment.classes.report.TeacherReportPresenter;
import com.pws.pateast.fragment.complaint.ComplaintPresenter;
import com.pws.pateast.fragment.complaint.detail.ComplaintDetailPresenter;
import com.pws.pateast.fragment.complaint.report.ComplaintReportPresenter;
import com.pws.pateast.fragment.events.EventPresenter;
import com.pws.pateast.fragment.events.detail.EventDetailPresenter;
import com.pws.pateast.fragment.feeds.FeedsPresenter;
import com.pws.pateast.fragment.fees.FeesPaymentPresenter;
import com.pws.pateast.fragment.fees.invoice.FeesInvoicePresenter;
import com.pws.pateast.fragment.home.DashBoardPresenter;
import com.pws.pateast.fragment.home.DashBoardView;
import com.pws.pateast.fragment.leave.LeaveApplyPresenter;
import com.pws.pateast.fragment.leave.LeaveApplyView;
import com.pws.pateast.fragment.leave.LeavePresenter;
import com.pws.pateast.fragment.leave.LeaveView;
import com.pws.pateast.fragment.leave.teacher.approve.ClassLeavePresenter;
import com.pws.pateast.fragment.leave.teacher.info.TeacherLeaveInfoPresenter;
import com.pws.pateast.fragment.marks.student.ExamMarksPresenter;
import com.pws.pateast.fragment.notification.ParentNotificationListPresenter;
import com.pws.pateast.fragment.presenter.ClassPresenter;
import com.pws.pateast.fragment.presenter.ClassView;
import com.pws.pateast.fragment.presenter.ExamHeadPresenter;
import com.pws.pateast.fragment.presenter.ExamHeadView;
import com.pws.pateast.fragment.presenter.TagPresenter;
import com.pws.pateast.fragment.profile.ProfilePresenter;
import com.pws.pateast.fragment.profile.ProfileView;
import com.pws.pateast.fragment.profile.reset.ResetPresenter;
import com.pws.pateast.fragment.schedule.ClassSchedulePresenter;
import com.pws.pateast.fragment.schedule.ClassScheduleView;
import com.pws.pateast.fragment.schedule.student.exam.ExamSchedulePresenter;
import com.pws.pateast.fragment.settings.SettingsPresenter;
import com.pws.pateast.fragment.student.MyStudentsPresenter;
import com.pws.pateast.fragment.student.message.MessageStudentPresenter;
import com.pws.pateast.fragment.suggestion.ParentSuggestionPresenter;
import com.pws.pateast.fragment.suggestion.WebPresenter;
import com.pws.pateast.fragment.track.parent.WardRoutePresenter;

import dagger.Component;

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class})
public interface ActivityComponent {

    void inject(SplashActivity appActivity);

    void inject(HomeActivity appActivity);

    void inject(TaskActivity appActivity);

    void inject(AppFragment appFragment);

    void inject(LoginPresenter basePresenter);

    void inject(ForgotPasswordPresenter basePresenter);

    void inject(HomePresenter basePresenter);

    void inject(DashBoardPresenter<DashBoardView> basePresenter);

    void inject(TaskPresenter basePresenter);

    void inject(TeacherClassPresenter basePresenter);

    void inject(ClassPresenter<ClassView> basePresenter);

    void inject(ClassSchedulePresenter<ClassScheduleView> basePresenter);

    void inject(ExamHeadPresenter<ExamHeadView> basePresenter);

    void inject(MyStudentsPresenter basePresenter);

    void inject(SettingsPresenter basePresenter);

    void inject(ProfilePresenter<ProfileView> basePresenter);

    void inject(AttendancePresenter basePresenter);

    void inject(AddUpdateAttendancePresenter basePresenter);

    void inject(AssignmentListPresenter basePresenter);

    void inject(AttendanceReportPresenter basePresenter);

    void inject(AddAssignmentPresenter basePresenter);


    void inject(StudentAttendancePresenter basePresenter);

    void inject(StudentAssignmentPresenter basePresenter);

    void inject(AssignmentDownloadPresenter<AssignmentDownloadView> basePresenter);

    void inject(ExamSchedulePresenter basePresenter);

    void inject(ExamMarksPresenter basePresenter);

    void inject(ResetPresenter basePresenter);

    void inject(LeaveApplyPresenter<LeaveApplyView> basePresenter);

    void inject(LeavePresenter<LeaveView> basePresenter);

    void inject(LeaveStatusPresenter basePresenter);


    void inject(ClassLeavePresenter basePresenter);

    void inject(TeacherReportPresenter basePresenter);


    void inject(MessageStudentPresenter basePresenter);

    void inject(AssignmentDetailPresenter basePresenter);

    void inject(InboxPresenter basePresenter);

    void inject(MessagePresenter basePresenter);

    void inject(TeacherLeaveInfoPresenter basePresenter);

    void inject(ChatUserPresenter basePresenter);

    void inject(WardSelectionPresenter wardSelectionPresenter);


    void inject(SchedulePresenter parentHomePresenter);

    void inject(com.pws.pateast.activity.dashboard.HomePresenter parentHomePresenter);


    void inject(TeacherHomePresenter parentHomePresenter);

    void inject(TagPresenter tagPresenter);

    void inject(CalenderReportPresenter<CalenderReportView> tagPresenter);

    void inject(ReviewAssignmentPresenter tagPresenter);

    void inject(DriverHomePresenter presenter);

    void inject(WardRoutePresenter presenter);

    void inject(LocationPresenter<LocationView> presenter);

    void inject(ParentSuggestionPresenter presenter);

    void inject(WebPresenter presenter);

    void inject(ParentNotificationListPresenter presenter);

    void inject(LMSFilterPresenter presenter);

    void inject(LMSPresenter presenter);

    void inject(FeesPaymentPresenter presenter);

    void inject(WardFeesPresenter presenter);

    void inject(FeesInvoicePresenter presenter);

    void inject(ComplaintPresenter presenter);

    void inject(ComplaintDetailPresenter presenter);

    void inject(ComplaintReportPresenter presenter);

    void inject(EventPresenter presenter);

    void inject(CircularPresenter presenter);

    void inject(EventDetailPresenter presenter);

    void inject(CircularDetailPresenter presenter);

    void inject(FeedsPresenter presenter);

    void inject(FeedsActivityPresenter presenter);

}