package com.pws.pateast.activity.leave;

import android.support.v4.app.FragmentManager;

import com.base.ui.adapter.BasePagerAdapter;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.base.Extras;

import java.util.List;

/**
 * Created by intel on 28-Jun-17.
 */

public class LeaveStatusAdapter extends BasePagerAdapter<AppFragment>
{
    public LeaveStatusAdapter(FragmentManager fm, List<AppFragment> leaveFragments) {
        super(fm, leaveFragments);
    }

    @Override
    public AppFragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragments.get(position).getArguments().getString(Extras.TITLE);
    }
}
