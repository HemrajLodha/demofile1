package com.pws.pateast.fragment.attendance.student;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pws.calender.CompactCalendarView;
import com.pws.calender.domain.Event;
import com.pws.pateast.R;
import com.pws.pateast.api.model.Student;
import com.pws.pateast.api.model.Tag;
import com.pws.pateast.base.AppDialogFragment;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.base.Extras;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.fragment.student.report.StudentReportFragment;
import com.pws.pateast.utils.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by intel on 15-May-17.
 */

public class StudentAttendanceFragment extends AppFragment implements StudentAttendanceView,
        View.OnClickListener,
        CompactCalendarView.CompactCalendarViewListener {

    private LinearLayout layoutCalendarView;
    private CompactCalendarView cvAttendanceReport;
    private TextView tvTotalDays, tvTotalAbsent, tvTotalPresent, tvTotalLeave;
    private TextView tvCurrentMonth;
    private TextView btnNextMonth, btnPreviousMonth;

    private StudentAttendancePresenter mPresenter;

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_student_attendance;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        layoutCalendarView = (LinearLayout) findViewById(R.id.layout_calendar_view);
        cvAttendanceReport = (CompactCalendarView) findViewById(R.id.cv_attendance_report);

        tvTotalDays = (TextView) findViewById(R.id.tv_total_days);
        tvTotalAbsent = (TextView) findViewById(R.id.tv_total_absent);
        tvTotalPresent = (TextView) findViewById(R.id.tv_total_present);
        tvTotalLeave = (TextView) findViewById(R.id.tv_total_leave);
        tvCurrentMonth = (TextView) findViewById(R.id.tv_current_month);
        btnNextMonth = (TextView) findViewById(R.id.btn_next_month);
        btnPreviousMonth = (TextView) findViewById(R.id.btn_previous_month);

        cvAttendanceReport.setUseThreeLetterAbbreviation(true);
        cvAttendanceReport.setFirstDayOfWeek(Calendar.MONDAY);
        cvAttendanceReport.shouldDrawIndicatorsBelowSelectedDays(true);
        cvAttendanceReport.shouldDrawIndicatorsCurrentDay(false);
        cvAttendanceReport.shouldDrawIndicatorsSelectedDay(false);
        cvAttendanceReport.shouldDrawIndicatorsBelowSelectedDays(true);
        cvAttendanceReport.setListener(this);
        btnNextMonth.setOnClickListener(this);
        btnPreviousMonth.setOnClickListener(this);

        mPresenter = new StudentAttendancePresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void onActionClick() {
        super.onActionClick();
        mPresenter.getAttendanceReport();
    }

    @Override
    public UserType getUserType() {
        return UserType.getUserType(getArguments().getString(Extras.USER_TYPE));
    }

    @Override
    public int getClassId() {
        return getArguments().getInt(Extras.CLASS_ID);
    }

    @Override
    public int getSubjectId() {
        return getArguments().getInt(Extras.SUBJECT_ID);
    }

    @Override
    public int getStudentId() {
        return getStudent().getStudentId();
    }

    @Override
    public String getStudentName() {
        return getStudent().getStudent().getUser().getUserdetails().get(0).getFullname();
    }

    @Override
    public Student getStudent() {
        return getArguments().getParcelable(Extras.STUDENT);
    }

    @Override
    public void setAttendanceTags(List<Tag> tags) {
        getArguments().putParcelableArrayList(Extras.ATTENDANCE_TAGS, (ArrayList<? extends Parcelable>) tags);
    }

    @Override
    public ArrayList<Tag> getAttendanceTags() {
        return getArguments().getParcelableArrayList(Extras.ATTENDANCE_TAGS);
    }

    @Override
    public void setReportCalender(List<Student> students) {
        setCurrentMoth(cvAttendanceReport.getFirstDayOfCurrentMonth());
        layoutCalendarView.setVisibility(View.VISIBLE);
        cvAttendanceReport.removeAllEvents();
        cvAttendanceReport.addEvents(mPresenter.getEvents(students));
        tvTotalDays.setText(mPresenter.getTotalDays());
        tvTotalAbsent.setText(mPresenter.getTotalAbsent());
        tvTotalPresent.setText(mPresenter.getTotalPresent());
        tvTotalLeave.setText(mPresenter.getTotalLeave());
        layoutCalendarView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setCurrentMoth(Date month) {
        tvCurrentMonth.setText(DateUtils.toTime(month, DateUtils.MONTH_FORMAT_PATTERN));
    }

    @Override
    public void onDayClick(Date dateClicked) {
        ArrayList<Event> events = (ArrayList<Event>) cvAttendanceReport.getEvents(dateClicked);
        if (events != null && !events.isEmpty()) {
            Bundle bundle = new Bundle();
            bundle.putString(Extras.STUDENT_NAME, mPresenter.getStudentName());
            bundle.putParcelableArrayList(Extras.CLASS_ATTENDANCE, events);
            bundle.putParcelableArrayList(Extras.ATTENDANCE_TAGS, getAttendanceTags());
            StudentReportFragment filterFragment = AppDialogFragment.newInstance(StudentReportFragment.class, getAppListener(), bundle);
            filterFragment.show(getFragmentManager(), StudentReportFragment.class.getSimpleName());
        }
    }

    @Override
    public void onMonthScroll(Date firstDayOfNewMonth) {
        setCurrentMoth(firstDayOfNewMonth);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_previous_month:
                if (cvAttendanceReport != null)
                    cvAttendanceReport.showPreviousMonth();
                break;
            case R.id.btn_next_month:
                if (cvAttendanceReport != null)
                    cvAttendanceReport.showNextMonth();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.detachView();
    }


}
