package com.pws.pateast.activity.attendance;

import android.support.v4.app.FragmentManager;

import com.base.ui.adapter.BasePagerAdapter;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.base.Extras;

import java.util.List;

/**
 * Created by intel on 06-Sep-17.
 */

public class AttendanceReportAdapter extends BasePagerAdapter<AppFragment>
{


    public AttendanceReportAdapter(FragmentManager fm, List<AppFragment> attendanceReportFragments) {
        super(fm, attendanceReportFragments);
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
