package com.pws.pateast.activity.assignment;

import android.support.v4.app.FragmentManager;

import com.base.ui.adapter.BasePagerAdapter;
import com.pws.pateast.base.Extras;
import com.pws.pateast.fragment.assignment.teacher.AssignmentListFragment;

import java.util.List;

/**
 * Created by intel on 12-Jun-17.
 */

public class AssignmentStatusAdapter extends BasePagerAdapter<AssignmentListFragment>
{


    public AssignmentStatusAdapter(FragmentManager fm, List<AssignmentListFragment> assignmentListFragments) {
        super(fm, assignmentListFragments);
    }

    @Override
    public AssignmentListFragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragments.get(position).getArguments().getString(Extras.TITLE).toUpperCase();
    }
}
