package com.pws.pateast.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;

import com.pws.pateast.R;

/**
 * Created by planet on 4/19/2017.
 */

public abstract class ParentFragmentActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.ParentTheme);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);
        if (isToolbarSetupEnabled()) {
            getToolbar().setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryParent));
        }
    }
}
