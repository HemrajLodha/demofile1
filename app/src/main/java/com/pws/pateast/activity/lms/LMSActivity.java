package com.pws.pateast.activity.lms;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.pws.pateast.R;
import com.pws.pateast.activity.lms.filter.LMSFilterFragment;
import com.pws.pateast.api.model.Topic;
import com.pws.pateast.base.AppDialogFragment;
import com.pws.pateast.base.Extras;
import com.pws.pateast.base.ResponseAppActivity;
import com.pws.pateast.fragment.lms.StudentLMSFragment;
import com.pws.pateast.fragment.lms.StudentLMSView;
import com.pws.pateast.widget.FontTabLayout;

import java.util.List;

/**
 * Created by intel on 24-Jan-18.
 */

public class LMSActivity extends ResponseAppActivity implements LMSView {
    private FontTabLayout tabLMS;
    private ViewPager pagerLMS;

    private LMSPresenter mPresenter;
    private LMSAdapter mAdapter;
    private StudentLMSView mStudentLMSView;
    private Intent mIntent;

    @Override
    public boolean isToolbarSetupEnabled() {
        return true;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.activity_lms;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);
        setTitle(R.string.menu_lms);
        mIntent = getIntent();
        tabLMS = findViewById(R.id.tab_lms);
        pagerLMS = findViewById(R.id.pager_lms);
        tabLMS.setVisibility(View.GONE);
        pagerLMS.setVisibility(View.GONE);

        mPresenter = new LMSPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter_assignment, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_filter:
                showFilterFragment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActionClick() {
        super.onActionClick();
        doFilter();
    }

    @Override
    public void showFilterFragment() {
        Bundle bundle = getFilterData();
        LMSFilterFragment mFilterFragment = AppDialogFragment.newInstance(LMSFilterFragment.class);
        if (bundle != null)
            mFilterFragment.setArguments(bundle);
        mFilterFragment.attachView(this);
        mFilterFragment.show(getBaseFragmentManager(), LMSFilterFragment.class.getSimpleName());
    }

    @Override
    public void setFilterData(Bundle filterData) {
        mIntent.putExtra(Extras.FILTER_DATA, filterData);
        Bundle bundle = getFilterData();
        if (bundle != null) {
            Topic topic = bundle.getParcelable(Extras.TOPIC);
            if (topic == null) {
                onBackPressed();
            }
        }
    }

    @Override
    public void doFilter() {
        Bundle bundle = getFilterData();
        if (bundle != null) {
            Topic topic = bundle.getParcelable(Extras.TOPIC);
            if (topic != null) {
                tabLMS.setVisibility(View.GONE);
                pagerLMS.setVisibility(View.GONE);
                mPresenter.loadStudyMaterial(topic.getId());
                return;
            }
        }
        onBackPressed();
    }

    @Override
    public Bundle getFilterData() {
        return mIntent.getBundleExtra(Extras.FILTER_DATA);
    }

    @Override
    public void setLMSAdapter(List<StudentLMSFragment> lmsFragments) {
        mAdapter = new LMSAdapter(getBaseFragmentManager(), lmsFragments);
        pagerLMS.setAdapter(mAdapter);
        tabLMS.setupWithViewPager(pagerLMS, true);
        pagerLMS.post(new Runnable() {
            @Override
            public void run() {
                onPageSelected(pagerLMS.getCurrentItem());
                pagerLMS.addOnPageChangeListener(LMSActivity.this);
                tabLMS.setVisibility(View.VISIBLE);
                pagerLMS.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (mAdapter != null) {
            mStudentLMSView = mAdapter.getItem(position);
            if (mStudentLMSView != null && mStudentLMSView.isVisible())
                mStudentLMSView.onActionClick();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.detachView();
    }
}
