package com.pws.pateast.fragment.schedule.student.classs;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.base.ui.view.BaseRecyclerView;
import com.pws.pateast.R;
import com.pws.pateast.activity.schedule.ScheduleView;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.base.Extras;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.fragment.schedule.ClassScheduleAdapter;
import com.pws.pateast.widget.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by intel on 06-Jul-17.
 */

public class StudentScheduleFragment extends AppFragment implements StudentScheduleView {
    private BaseRecyclerView rvSchedule;
    private StudentSchedulePresenter schedulePresenter;
    private ClassScheduleAdapter scheduleAdapter;
    private ScheduleView scheduleView;

    //private AuthorView authorView;
    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_class_schedule;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        rvSchedule = (BaseRecyclerView) findViewById(R.id.rv_schedule);
        rvSchedule.addItemDecoration(new SpaceItemDecoration(getContext(), R.dimen.size_8, 1, true));
        rvSchedule.setUpAsList();
        rvSchedule.setPullRefreshEnabled(false);
        rvSchedule.setLoadingMoreEnabled(true);

        schedulePresenter = new StudentSchedulePresenter();
        schedulePresenter.attachView(this);
    }

    @Override
    public void attachView(ScheduleView scheduleView) {
        this.scheduleView = scheduleView;
    }

    @Override
    public void onActionClick() {
        super.onActionClick();
        schedulePresenter.setCalendar();
        schedulePresenter.isHoliday();
    }

    @Override
    public long getTimeInMillis() {
        return getArguments().getLong(Extras.TIME_IN_MILLIS);
    }

    @Override
    public void setSchedules(HashMap<String, ArrayList> schedules) {
        if (scheduleView != null)
            scheduleView.setScheduleData(schedules);
    }

    @Override
    public HashMap<String, ArrayList> getSchedules() {
        return scheduleView != null ? (HashMap<String, ArrayList>) scheduleView.getScheduleData() : null;
    }

    @Override
    public void setScheduleAdapter(List<Object> schedules) {
        if (rvSchedule.getAdapter() == null) {
            scheduleAdapter = new ClassScheduleAdapter(getContext());
            scheduleAdapter.setUserType(UserType.STUDENT);
            rvSchedule.setAdapter(scheduleAdapter);
        }
        scheduleAdapter.update(schedules);
    }

    @Override
    public void onDestroy() {
        schedulePresenter.detachView();
        super.onDestroy();
    }

}
