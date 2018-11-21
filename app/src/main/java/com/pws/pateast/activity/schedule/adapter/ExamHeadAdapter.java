package com.pws.pateast.activity.schedule.adapter;

import android.support.v4.app.FragmentManager;

import com.base.ui.adapter.BasePagerAdapter;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.base.Extras;
import com.pws.pateast.fragment.schedule.student.exam.ExamScheduleFragment;

import java.util.List;

/**
 * Created by intel on 24-Aug-17.
 */

public class ExamHeadAdapter extends BasePagerAdapter<AppFragment>
{


    public ExamHeadAdapter(FragmentManager fm, List<AppFragment> examScheduleFragments) {
        super(fm, examScheduleFragments);
    }

    @Override
    public AppFragment getItem(int position) {
        return getFragments().get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return getFragments().get(position).getArguments().getString(Extras.TITLE);
    }
}
