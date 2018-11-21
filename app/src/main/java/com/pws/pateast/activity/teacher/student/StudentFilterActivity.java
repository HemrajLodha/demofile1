package com.pws.pateast.activity.teacher.student;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.pws.pateast.R;
import com.pws.pateast.api.model.TeacherClass;
import com.pws.pateast.base.Extras;
import com.pws.pateast.base.ResponseAppActivity;
import com.pws.pateast.fragment.student.MyStudentView;
import com.pws.pateast.utils.Utils;
import com.pws.pateast.widget.FontTabLayout;

import java.util.List;

/**
 * Created by intel on 25-Apr-17.
 */

public class StudentFilterActivity extends ResponseAppActivity implements StudentFilterView, ViewPager.OnPageChangeListener, BaseRecyclerAdapter.OnItemClickListener {
    private FontTabLayout tabStudent;
    private ViewPager pagerStudent;

    private StudentFilterAdapter studentFilterAdapter;
   // private StudentFilterTabAdapter classTabRecyclerAdapter;

    private StudentFilterPresenter filterPresenter;
    private MyStudentView mStudentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.TeacherTheme);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.activity_student_filter;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);
        getToolbar().setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryTeacher));
        setTitle(R.string.dashboard_menu_my_students);
        tabStudent = findViewById(R.id.tab_student);
        pagerStudent = findViewById(R.id.pager_student);

        filterPresenter = new StudentFilterPresenter(false);
        filterPresenter.attachView(this);
        onActionClick();
    }

    @Override
    public int tabIndicatorColor() {
        return R.color.white;
    }

    @Override
    public int tabSelectedTextColor() {
        return R.color.colorPrimaryTeacher;
    }

    @Override
    public int tabTextAppearance() {
        return R.style.RecyclerTabLayoutTab;
    }

    @Override
    public int tabBackground() {
        return R.color.white;
    }

    @Override
    public void onActionClick() {
        super.onActionClick();
        if (studentFilterAdapter == null || studentFilterAdapter.getCount() == 0)
            filterPresenter.getMyClasses();
        else if (mStudentView != null)
            mStudentView.onActionClick();
    }

    @Override
    public void setClass(TeacherClass classes) {

    }


    @Override
    public void setClassAdapter(List<TeacherClass> myClasses) {

        studentFilterAdapter = new StudentFilterAdapter(getBaseFragmentManager(), filterPresenter.getStudentFragments(myClasses));
        pagerStudent.setAdapter(studentFilterAdapter);

        //classTabRecyclerAdapter = new StudentFilterTabAdapter(pagerStudent);
        tabStudent.setupWithViewPager(pagerStudent);
        pagerStudent.post(new Runnable() {
            @Override
            public void run() {
                onPageSelected(pagerStudent.getCurrentItem());
                pagerStudent.addOnPageChangeListener(StudentFilterActivity.this);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        filterPresenter.detachView();
    }

    @Override
    public boolean isToolbarSetupEnabled() {
        return true;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Utils.keyboard(getContext(), pagerStudent, false);
        if (studentFilterAdapter != null) {
            mStudentView = studentFilterAdapter.getItem(position);
            mStudentView.onActionClick();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean isAddUser() {
        return getIntent().getBooleanExtra(Extras.ADD_USER, false);
    }

    @Override
    public void onItemClick(View view, int position) {

    }
}
