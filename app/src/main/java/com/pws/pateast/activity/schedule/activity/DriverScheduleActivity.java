package com.pws.pateast.activity.schedule.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.pws.pateast.R;
import com.pws.pateast.enums.UserType;

/**
 * Created by intel on 24-Aug-17.
 */

public class DriverScheduleActivity extends ScheduleActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
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
    public UserType getUserType() {
        return UserType.DRIVER;
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
    public int tabTextAppearance() {
        return R.style.RecyclerTabLayoutTab;
    }

    @Override
    public int tabBackground() {
        return R.color.white;
    }
}
