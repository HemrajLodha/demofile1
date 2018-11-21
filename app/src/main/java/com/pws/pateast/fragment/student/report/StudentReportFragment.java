package com.pws.pateast.fragment.student.report;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.pws.calender.domain.Event;
import com.pws.pateast.R;
import com.pws.pateast.api.model.Tag;
import com.pws.pateast.base.AppDialogFragment;
import com.pws.pateast.base.Extras;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by intel on 10-Jan-18.
 */

public class StudentReportFragment extends AppDialogFragment implements StudentReportView {
    private TextView tvStudentName;
    private ViewPager pagerAttendanceReport;
    private TextView btnNextMonth, btnPreviousMonth;

    private StudentReportAdapter mAdapter;
    private StudentReportPresenter mPresenter;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_student_report;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);
        setToolbarVisibility(GONE);
        btnNextMonth = (TextView) findViewById(R.id.btn_next_month);
        btnPreviousMonth = (TextView) findViewById(R.id.btn_previous_month);
        pagerAttendanceReport = (ViewPager) findViewById(R.id.pager_attendance_report);
        tvStudentName = (TextView) findViewById(R.id.tv_student_name);

        btnNextMonth.setOnClickListener(this);
        btnPreviousMonth.setOnClickListener(this);

        mPresenter = new StudentReportPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public String getStudentName() {
        return getArguments().getString(Extras.STUDENT_NAME);
    }

    @Override
    public ArrayList<Event> getAttendance() {
        return (ArrayList<Event>) getArguments().getSerializable(Extras.CLASS_ATTENDANCE);
    }

    @Override
    public ArrayList<Tag> getAttendanceTags() {
        return getArguments().getParcelableArrayList(Extras.ATTENDANCE_TAGS);
    }

    @Override
    public void showAttendanceReport() {
        tvStudentName.setText(getStudentName());
        mAdapter = new StudentReportAdapter(getContext());
        mAdapter.setAttendance(getAttendance());
        mAdapter.setTags(getAttendanceTags());
        pagerAttendanceReport.setAdapter(mAdapter);
        btnNextMonth.setVisibility(getAttendance().size() > 1 ? VISIBLE : GONE);
        btnPreviousMonth.setVisibility(getAttendance().size() > 1 ? VISIBLE : GONE);
    }

    @Override
    public void onClick(View v) {
        if (pagerAttendanceReport == null)
            return;
        int position = pagerAttendanceReport.getCurrentItem();
        switch (v.getId()) {
            case R.id.btn_previous_month:
                pagerAttendanceReport.setCurrentItem(position - 1);
                break;
            case R.id.btn_next_month:
                pagerAttendanceReport.setCurrentItem(position + 1);
                break;
        }
    }
}
