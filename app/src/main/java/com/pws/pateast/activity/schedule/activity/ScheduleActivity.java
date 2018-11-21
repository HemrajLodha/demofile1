package com.pws.pateast.activity.schedule.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;

import com.pws.pateast.R;
import com.pws.pateast.activity.schedule.SchedulePresenter;
import com.pws.pateast.activity.schedule.ScheduleView;
import com.pws.pateast.activity.schedule.adapter.ExamHeadAdapter;
import com.pws.pateast.activity.schedule.adapter.ScheduleAdapter;
import com.pws.pateast.api.model.Schedule;
import com.pws.pateast.api.model.Session;
import com.pws.pateast.base.AppView;
import com.pws.pateast.base.ResponseAppActivity;
import com.pws.pateast.enums.TaskType;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.fragment.schedule.ClassScheduleView;
import com.pws.pateast.widget.RecyclerTabLayout;

import java.util.Calendar;
import java.util.List;

/**
 * Created by intel on 21-Aug-17.
 */

public class ScheduleActivity extends ResponseAppActivity implements ScheduleView, ViewPager.OnPageChangeListener {
    private RecyclerTabLayout tabSchedule;
    private ViewPager pagerSchedule;
    private ScheduleAdapter scheduleAdapter;
    private ExamHeadAdapter examHeadAdapter;
    private AppView mScheduleView;

    protected SchedulePresenter mPresenter;

    @Override
    protected int getResourceLayout() {
        return R.layout.activity_schedule;
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
    public int tabIndicatorColor() {
        return R.color.white;
    }

    @Override
    public int tabSelectedTextColor() {
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
    public UserType getUserType() {
        return UserType.NONE;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.detachView();
    }


    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);

        tabSchedule = findViewById(R.id.tab_schedule);
        tabSchedule.setBackgroundColor(ContextCompat.getColor(getContext(), tabBackground()));
        tabSchedule.setIndicatorColor(ContextCompat.getColor(getContext(), tabIndicatorColor()));
        tabSchedule.setTabSelectedTextColor(true, ContextCompat.getColor(getContext(), tabSelectedTextColor()));
        tabSchedule.setTabTextAppearance(tabTextAppearance());

        pagerSchedule = findViewById(R.id.pager_schedule);
        pagerSchedule.addOnPageChangeListener(this);

        mPresenter = new SchedulePresenter(false);
        mPresenter.attachView(this);
        mPresenter.setTaskType(getIntent());

    }

    @Override
    public void onActionClick() {
        super.onActionClick();
        if (mScheduleView != null)
            mScheduleView.onActionClick();
    }

    @Override
    public void setScheduleAdapter(TaskType taskType, Session session) {
        scheduleAdapter = new ScheduleAdapter(getBaseFragmentManager(), taskType, session);
        pagerSchedule.setAdapter(scheduleAdapter);
        tabSchedule.setUpWithViewPager(pagerSchedule);

        pagerSchedule.post(new Runnable() {
            @Override
            public void run() {
                int position = scheduleAdapter.getPosition(Calendar.getInstance());
                if (position > 0)
                    pagerSchedule.setCurrentItem(position);
                else
                    onPageSelected(0);
            }
        });
    }

    @Override
    public void setExamHeads(List<Schedule> examHeads, boolean isSchedule) {
        examHeadAdapter = new ExamHeadAdapter(getBaseFragmentManager(), isSchedule ? mPresenter.getExamScheduleFragments(examHeads) : mPresenter.getExamMarksFragments(examHeads));
        pagerSchedule.setAdapter(examHeadAdapter);
        tabSchedule.setUpWithViewPager(pagerSchedule);
        pagerSchedule.post(new Runnable() {
            @Override
            public void run() {
                onPageSelected(0);
            }
        });
    }

    @Override
    public void setExamHead(Schedule examHead) {

    }


    @Override
    public Object getScheduleData() {
        return mPresenter.getScheduleData();
    }

    @Override
    public void setScheduleData(Object scheduleData) {
        mPresenter.setScheduleData(scheduleData);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (scheduleAdapter != null) {
            mScheduleView = (AppView) scheduleAdapter.getItem(position);
            if (mScheduleView instanceof ClassScheduleView)
                ((ClassScheduleView) mScheduleView).attachView(this);
            mScheduleView.onActionClick();
        } else if (examHeadAdapter != null) {
            mScheduleView = (AppView) examHeadAdapter.getItem(position);
            mScheduleView.onActionClick();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
