package com.pws.pateast.activity.teacher.student;

import android.support.v4.app.FragmentManager;

import com.base.ui.adapter.BasePagerAdapter;
import com.pws.pateast.base.Extras;
import com.pws.pateast.fragment.student.MyStudentFragment;

import java.util.List;

/**
 * Created by intel on 09-Jun-17.
 */

public class StudentFilterAdapter extends BasePagerAdapter<MyStudentFragment>
{


    public StudentFilterAdapter(FragmentManager fm, List<MyStudentFragment> myStudentFragments) {
        super(fm, myStudentFragments);
    }

    @Override
    public MyStudentFragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return getItem(position).getArguments().getString(Extras.TITLE);
    }
}
