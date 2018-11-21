package com.pws.pateast.activity.feeds;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.pws.pateast.R;
import com.pws.pateast.activity.tasks.TaskActivity;
import com.pws.pateast.base.Extras;
import com.pws.pateast.base.FragmentActivity;
import com.pws.pateast.base.ResponseAppActivity;
import com.pws.pateast.enums.TaskType;
import com.pws.pateast.fragment.feeds.FeedsView;
import com.pws.pateast.utils.Utils;
import com.pws.pateast.widget.FontTabLayout;

public class FeedsActivity extends ResponseAppActivity implements FeedsActivityView {

    private FloatingActionButton fabAddFeed;
    private FontTabLayout tabFeeds;
    private ViewPager pagerFeeds;

    private FeedsActivityPresenter mPresenter;
    private FeedsActivityAdapter mAdapter;
    private FeedsView mFeedsView;

    @Override
    public boolean isToolbarSetupEnabled() {
        return true;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.activity_feeds;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(getAppTheme());
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getAppTheme() {
        int theme = getIntent().getIntExtra(Extras.APP_THEME, 0);
        if (theme == 0)
            return R.style.AppTheme;
        return theme;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);
        setTitle(R.string.menu_feeds);

        tabFeeds = findViewById(R.id.tab_feeds);
        pagerFeeds = findViewById(R.id.pager_feeds);
        fabAddFeed = findViewById(R.id.fab_add_feed);

        mPresenter = new FeedsActivityPresenter();
        mPresenter.attachView(this);

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void setFeedsCategoryAdapter() {
        mAdapter = new FeedsActivityAdapter(getBaseFragmentManager(), mPresenter.getFeedsFragments());
        pagerFeeds.setAdapter(mAdapter);
        tabFeeds.setupWithViewPager(pagerFeeds);
        pagerFeeds.post(new Runnable() {
            @Override
            public void run() {
                onPageSelected(pagerFeeds.getCurrentItem());
                fabAddFeed.setVisibility(View.VISIBLE);
                pagerFeeds.addOnPageChangeListener(FeedsActivity.this);
                fabAddFeed.setOnClickListener(FeedsActivity.this);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_add_feed:
                Bundle bundle = new Bundle();
                bundle.putInt(FragmentActivity.EXTRA_TASK_TYPE, TaskType.ADD_FEED.getValue());
                bundle.putInt(Extras.APP_THEME, getAppTheme());
                openActivityForResult(TaskActivity.class, bundle, ADD_FEED_REQUEST);
                break;
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Utils.keyboard(getContext(), pagerFeeds, false);
        if (mAdapter != null) {
            mFeedsView = mAdapter.getItem(position);
            if (mFeedsView != null && mFeedsView.isVisible())
                mFeedsView.onActionClick();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ADD_FEED_REQUEST:
                if (resultCode == ADD_FEED_RESPONSE) {
                    onPageSelected(pagerFeeds.getCurrentItem());
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }
}
