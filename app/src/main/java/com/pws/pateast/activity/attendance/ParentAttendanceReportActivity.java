package com.pws.pateast.activity.attendance;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.pws.pateast.R;
import com.pws.pateast.enums.UserType;

/**
 * Created by intel on 06-Sep-17.
 */

public class ParentAttendanceReportActivity extends AttendanceReportActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
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


    @Override
    public int tabIndicatorColor() {
        return R.color.colorPrimaryParent;
    }

    @Override
    public int tabSelectedTextColor() {
        return R.color.colorPrimaryParent;
    }

    @Override
    public int tabTextAppearance() {
        return R.style.RecyclerTabLayoutTab;
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
