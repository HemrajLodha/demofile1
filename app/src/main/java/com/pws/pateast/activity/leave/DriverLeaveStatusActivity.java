package com.pws.pateast.activity.leave;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.pws.pateast.R;

/**
 * Created by intel on 23-Aug-17.
 */

public class DriverLeaveStatusActivity extends LeaveStatusActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.DriverTheme);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);
        if (isToolbarSetupEnabled()) {
            getToolbar().setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDriver));
        }

    }

    @Override
    public int tabIndicatorColor() {
        return R.color.colorPrimaryDriver;
    }

    @Override
    public int tabSelectedTextColor() {
        return R.color.colorPrimaryDriver;
    }

    @Override
    public int tabTextColor() {
        return R.color.textColor;
    }

    @Override
    public int tabBackground() {
        return R.color.white;
    }
}