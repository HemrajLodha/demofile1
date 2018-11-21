package com.pws.pateast.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.pws.pateast.R;

/**
 * Created by planet on 4/19/2017.
 */

public abstract class FragmentActivity extends ResponseAppActivity {
    public static final String EXTRA_TASK_TYPE = "task_type";
    public static final String EXTRA_NOTIFICATION_TYPE = "notification_type";
    public static final String EXTRA_DATA = "extra_data";

    private FragmentManager mManager;
    private FragmentTransaction mTransaction;

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);
        mManager = getSupportFragmentManager();
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.activity_fragment;
    }

    @Override
    public void changeFragment(Fragment fragment, String TAG) {
        mTransaction = mManager.beginTransaction();
        mTransaction.replace(R.id.content_home, fragment, TAG);
        mTransaction.commit();
    }

    @Override
    public void changeFragmentBack(Fragment fragment, String TAG) {
        mTransaction = mManager.beginTransaction();
        mTransaction.replace(R.id.content_home, fragment, TAG);
        mTransaction.addToBackStack(null);
        mTransaction.commit();
    }

    @Override
    public boolean isToolbarSetupEnabled() {
        return true;
    }
}
