package com.pws.pateast.activity.lms;

import android.support.v4.app.FragmentManager;

import com.base.ui.adapter.BasePagerAdapter;
import com.pws.pateast.base.Extras;
import com.pws.pateast.enums.LMSCategory;
import com.pws.pateast.fragment.lms.StudentLMSFragment;

import java.util.List;

/**
 * Created by intel on 24-Jan-18.
 */

public class LMSAdapter extends BasePagerAdapter<StudentLMSFragment> {

    public LMSAdapter(FragmentManager fm, List<StudentLMSFragment> studentLMSFragments) {
        super(fm, studentLMSFragments);
    }

    @Override
    public StudentLMSFragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return ((LMSCategory) getItem(position).getArguments().getParcelable(Extras.LMS_CATEGORY)).getTitle();
    }
}
