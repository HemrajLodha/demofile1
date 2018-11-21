package com.pws.pateast.activity.attendance;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.pws.pateast.R;

/**
 * Created by intel on 04-Oct-17.
 */

public class TeacherAttendanceReportActivity extends AttendanceReportActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.TeacherTheme);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);
        if (isToolbarSetupEnabled()) {
            getToolbar().setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryTeacher));
        }

    }

    @Override
    public int tabIndicatorColor() {
        return R.color.colorPrimaryTeacher;
    }

    @Override
    public int tabSelectedTextColor() {
        return R.color.colorPrimaryTeacher;
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
