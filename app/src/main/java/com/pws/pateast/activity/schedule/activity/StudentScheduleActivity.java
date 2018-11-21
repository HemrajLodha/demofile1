package com.pws.pateast.activity.schedule.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.pws.pateast.R;
import com.pws.pateast.api.model.User;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.utils.Preference;

/**
 * Created by intel on 22-Aug-17.
 */

public class StudentScheduleActivity extends ScheduleActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.StudentTheme);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);
        if (isToolbarSetupEnabled()) {
            getToolbar().setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryStudent));
        }
    }

    @Override
    public UserType getUserType() {
        return UserType.STUDENT;
    }

    @Override
    public int tabIndicatorColor() {
        return R.color.colorPrimaryStudent;
    }

    @Override
    public int tabSelectedTextColor() {
        return R.color.colorPrimaryStudent;
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
