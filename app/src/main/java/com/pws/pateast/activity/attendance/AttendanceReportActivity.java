package com.pws.pateast.activity.attendance;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;

import com.pws.pateast.R;
import com.pws.pateast.api.model.Student;
import com.pws.pateast.api.model.Subject;
import com.pws.pateast.api.model.TeacherClass;
import com.pws.pateast.base.AppView;
import com.pws.pateast.base.Extras;
import com.pws.pateast.base.ResponseAppActivity;
import com.pws.pateast.widget.FontTabLayout;

import java.util.List;

/**
 * Created by intel on 06-Sep-17.
 */

public class AttendanceReportActivity extends ResponseAppActivity implements AttendanceReportView, ViewPager.OnPageChangeListener {

    private FontTabLayout tabSubjects;
    private ViewPager pagerAttendance;
    private AttendanceReportAdapter reportAdapter;

    private AttendanceReportPresenter mPresenter;
    private AppView mAppView;

    @Override
    public boolean isToolbarSetupEnabled() {
        return true;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.activity_attendance_report;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);
        setTitle(R.string.menu_attendance_report);

        tabSubjects = findViewById(R.id.tab_subjects);
        tabSubjects.setBackgroundColor(ContextCompat.getColor(getContext(), tabBackground()));
        tabSubjects.setSelectedTabIndicatorColor(ContextCompat.getColor(getContext(), tabIndicatorColor()));
        tabSubjects.setTabTextColors(ContextCompat.getColor(getContext(), tabTextColor()), ContextCompat.getColor(getContext(), tabSelectedTextColor()));
        pagerAttendance = findViewById(R.id.pager_attendance);

        mPresenter = new AttendanceReportPresenter(false);
        mPresenter.attachView(this);
        mPresenter.setUserType();
    }

    @Override
    public void onActionClick() {
        super.onActionClick();
        if (mAppView == null)
            mPresenter.getSubject();
        else
            mAppView.onActionClick();
    }

    @Override
    public int tabIndicatorColor() {
        return R.color.white;
    }

    @Override
    public int tabSelectedTextColor() {
        return R.color.white;
    }

    @Override
    public int tabTextColor() {
        return R.color.white;
    }

    @Override
    public int tabTextAppearance() {
        return R.style.rtl_RecyclerTabLayoutTab;
    }

    @Override
    public int tabBackground() {
        return R.color.colorPrimary;
    }

    @Override
    public int getClassId() {
        return getIntent().getIntExtra(Extras.CLASS_ID, 0);
    }

    @Override
    public Student getStudent() {
        return getIntent().getParcelableExtra(Extras.STUDENT);
    }

    @Override
    public int getStudentId() {
        return getStudent().getId();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void setStudent(Student student) {

    }

    @Override
    public void setStudentAdapter(List<Student> students) {

    }

    @Override
    public void setSubject(Subject subject) {

    }

    @Override
    public void setSubjectAdapter(List<Subject> subjects) {
        reportAdapter = new AttendanceReportAdapter(getSupportFragmentManager(), mPresenter.getAttendanceReportFragments(subjects));
        pagerAttendance.setAdapter(reportAdapter);
        tabSubjects.setupWithViewPager(pagerAttendance);
        pagerAttendance.post(new Runnable() {
            @Override
            public void run() {
                onPageSelected(0);
                pagerAttendance.addOnPageChangeListener(AttendanceReportActivity.this);
            }
        });
    }

    @Override
    public void setClass(TeacherClass classes) {

    }

    @Override
    public void setClassAdapter(List<TeacherClass> classes) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (reportAdapter != null) {
            mAppView = (AppView) reportAdapter.getItem(position);
            //((StudentAttendanceView) mAppView).attachView(this);
            mAppView.onActionClick();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
