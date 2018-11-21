package com.pws.pateast.activity.lms;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.pws.pateast.base.AppView;
import com.pws.pateast.fragment.lms.StudentLMSFragment;

import java.util.List;

/**
 * Created by intel on 24-Jan-18.
 */

public interface LMSView extends AppView, ViewPager.OnPageChangeListener {

    void showFilterFragment();

    void setFilterData(Bundle filterData);

    void doFilter();

    Bundle getFilterData();

    void setLMSAdapter(List<StudentLMSFragment> lmsFragments);
}
