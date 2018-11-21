package com.base.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by intel on 05-Apr-17.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected Context mContext = this;
    protected Toolbar mToolbar;

    protected ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResourceLayout() != 0)
            setContentView(getResourceLayout());
    }

    public FragmentManager getBaseFragmentManager() {
        return super.getSupportFragmentManager();
    }

    public void setupToolbar(Toolbar toolbar) {
        setupToolbar(toolbar, null);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void setupToolbar(Toolbar toolbar, final View.OnClickListener onClickListener) {
        mToolbar = toolbar;
        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();
        if (mActionBar != null)
            mActionBar.setHomeButtonEnabled(true);

        if (onClickListener != null)
            toolbar.setNavigationOnClickListener(onClickListener);
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setTitle(int title) {
        super.setTitle(title);
        if (mActionBar != null)
            mActionBar.setTitle(getString(title));
    }

    public ActionBar getBaseActionBar() {
        assert mActionBar != null;
        return mActionBar;
    }

    @Override
    public void onBackPressed() {
        if (getBaseFragmentManager().getBackStackEntryCount() > 0) {
            getBaseFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }


    protected abstract int getResourceLayout();

    protected abstract void onViewReady(Bundle savedInstanceState);


}
