package com.pws.pateast.activity.feeds;

import android.support.v4.app.FragmentManager;

import com.base.ui.adapter.BasePagerAdapter;
import com.pws.pateast.base.Extras;
import com.pws.pateast.fragment.feeds.FeedsFragment;

import java.util.List;

public class FeedsActivityAdapter extends BasePagerAdapter<FeedsFragment> {

    public FeedsActivityAdapter(FragmentManager fm, List<FeedsFragment> feedsFragments) {
        super(fm, feedsFragments);
    }

    @Override
    public FeedsFragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return getItem(position).getArguments().getString(Extras.TITLE);
    }
}
