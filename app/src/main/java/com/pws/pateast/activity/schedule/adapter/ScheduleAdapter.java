package com.pws.pateast.activity.schedule.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.pws.pateast.activity.schedule.ScheduleUtils;
import com.pws.pateast.api.model.Session;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.base.Extras;
import com.pws.pateast.enums.TaskType;
import com.pws.pateast.fragment.schedule.parent.classs.WardScheduleFragment;
import com.pws.pateast.fragment.schedule.parent.holiday.HolidayFragment;
import com.pws.pateast.fragment.schedule.student.classs.StudentScheduleFragment;
import com.pws.pateast.fragment.schedule.teacher.classs.TeacherScheduleFragment;
import com.pws.pateast.widget.CachedFragmentStatePagerAdapter;

import java.util.Calendar;

/**
 * Created by intel on 21-Aug-17.
 */

public class ScheduleAdapter extends CachedFragmentStatePagerAdapter {
    private String CLASS_SCHEDULE_DATE_FORMAT = "dd MMM";
    private String HOLIDAY_DATE_FORMAT = "MMM, yyyy";
    private TaskType mTaskType;
    private ScheduleUtils scheduleUtils;

    public ScheduleAdapter(FragmentManager fragmentManager, TaskType taskType, Session session) {
        super(fragmentManager);
        mTaskType = taskType;
        scheduleUtils = new ScheduleUtils();
        scheduleUtils.setSession(session);
    }

    @Override
    public int getCount() {
        switch (mTaskType)
        {
            case HOLIDAY:
                return scheduleUtils.getMonth();
            default:
                return scheduleUtils.getDays();
        }
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (mTaskType) {
            case STUDENT_SCHEDULE:
                fragment = instantiateItem(AppFragment.newInstance(StudentScheduleFragment.class), position);
                break;
            case TEACHER_SCHEDULE:
                fragment = instantiateItem(AppFragment.newInstance(TeacherScheduleFragment.class), position);
                break;
            case WARD_SCHEDULE:
                fragment = instantiateItem(AppFragment.newInstance(WardScheduleFragment.class), position);
                break;
            case HOLIDAY:
                fragment = instantiateItem(AppFragment.newInstance(HolidayFragment.class), position);
                break;
        }
        if (fragment != null) {
            long timeForPosition = getTimeForPosition(position);
            Bundle bundle = fragment.getArguments() != null ? fragment.getArguments() : new Bundle();
            bundle.putLong(Extras.TIME_IN_MILLIS, timeForPosition);
        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Calendar cal;
        switch (mTaskType)
        {
            case HOLIDAY:
                cal = scheduleUtils.getMonthForPosition(position);
                return ScheduleUtils.getFormattedDate(cal.getTime(), HOLIDAY_DATE_FORMAT);
            default:
                cal = scheduleUtils.getDayForPosition(position);
                return ScheduleUtils.getFormattedDate(cal.getTime(), CLASS_SCHEDULE_DATE_FORMAT);
        }
    }

    public long  getTimeForPosition(int position)
    {
        switch (mTaskType)
        {
            case HOLIDAY:
                return scheduleUtils.getMonthForPosition(position).getTimeInMillis();
            default:
                return scheduleUtils.getDayForPosition(position).getTimeInMillis();
        }
    }


    public int getPosition(Calendar day)
    {
        switch (mTaskType)
        {
            case HOLIDAY:
                return scheduleUtils.getPositionForMonth(day);
            default:
                return scheduleUtils.getPositionForDay(day);
        }
    }
}
