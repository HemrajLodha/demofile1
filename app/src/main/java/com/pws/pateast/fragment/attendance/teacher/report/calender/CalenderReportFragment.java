package com.pws.pateast.fragment.attendance.teacher.report.calender;

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
import com.pws.pateast.base.Extras;
import com.pws.pateast.fragment.student.report.StudentReportFragment;
import com.pws.pateast.utils.DateUtils;
import com.pws.pateast.utils.FontManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.view.View.GONE;

/**
 * Created by intel on 31-Aug-17.
 */

public class CalenderReportFragment extends AppDialogFragment implements CalenderReportView, View.OnClickListener, CompactCalendarView.CompactCalendarViewListener {
    private LinearLayout layoutCalendarView;
    private CompactCalendarView cvAttendanceReport;
    private TextView tvStudentName, tvTotalDays, tvTotalAbsent, tvTotalPresent, tvTotalLeave;
    private TextView tvCurrentMonth;
    private TextView btnNextMonth, btnPreviousMonth;

    private CalenderReportPresenter mPresenter;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_calender_report;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);
        setToolbarVisibility(GONE);

        layoutCalendarView = (LinearLayout) findViewById(R.id.layout_calendar_view);
        cvAttendanceReport = (CompactCalendarView) findViewById(R.id.cv_attendance_report);
        tvStudentName = (TextView) findViewById(R.id.tv_student_name);
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
        cvAttendanceReport.setTypeFace(FontManager.getAppFont(getContext()));

        cvAttendanceReport.setListener(this);
        btnNextMonth.setOnClickListener(this);
        btnPreviousMonth.setOnClickListener(this);

        mPresenter = new CalenderReportPresenter();
        mPresenter.attachView(this);
        onActionClick();
    }

    @Override
    public void onActionClick() {
        super.onActionClick();
        mPresenter.getAttendanceReportForTeacher();
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
        tvStudentName.setText(getStudentName());

        setCurrentMoth(cvAttendanceReport.getFirstDayOfCurrentMonth());
        layoutCalendarView.setVisibility(View.VISIBLE);
        cvAttendanceReport.removeAllEvents();
        cvAttendanceReport.addEvents(mPresenter.getEvents(students));
        tvTotalDays.setText(mPresenter.getTotalDays());
        tvTotalAbsent.setText(mPresenter.getTotalAbsent());
        tvTotalPresent.setText(mPresenter.getTotalPresent());
        try {
            tvTotalLeave.setText(mPresenter.getTotalLeave());
            /*if (Integer.parseInt(mPresenter.getTotalLeave()) < 1) {
                tvTotalLeave.setText("0");
            } else if (Integer.parseInt(mPresenter.getTotalLeave()) < 10) {
                tvTotalLeave.setText("0" + mPresenter.getTotalLeave());
            } else {
                tvTotalLeave.setText(mPresenter.getTotalLeave());
            }*/
        } catch (Exception e) {
            e.printStackTrace();
            tvTotalLeave.setText(mPresenter.getTotalLeave());
        }
    }

    @Override
    public void setCurrentMoth(Date month) {
        tvCurrentMonth.setText(DateUtils.toTime(month, DateUtils.MONTH_FORMAT_PATTERN_2).toUpperCase());
    }

    @Override
    public void onDayClick(Date dateClicked) {
        ArrayList<Event> events = (ArrayList<Event>) cvAttendanceReport.getEvents(dateClicked);
        if (events != null && !events.isEmpty()) {
            Bundle bundle = new Bundle();
            bundle.putString(Extras.STUDENT_NAME, getStudentName());
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
