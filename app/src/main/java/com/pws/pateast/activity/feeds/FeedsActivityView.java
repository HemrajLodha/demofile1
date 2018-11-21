package com.pws.pateast.activity.feeds;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.pws.pateast.base.AppView;

public interface FeedsActivityView extends AppView, View.OnClickListener, ViewPager.OnPageChangeListener {
    int ADD_FEED_REQUEST = 100;
    int ADD_FEED_RESPONSE = 101;

    int getAppTheme();

    void setFeedsCategoryAdapter();

}
