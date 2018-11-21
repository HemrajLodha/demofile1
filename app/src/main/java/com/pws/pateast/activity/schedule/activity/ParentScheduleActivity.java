package com.pws.pateast.activity.schedule.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.pws.pateast.R;
import com.pws.pateast.api.model.User;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.utils.Preference;

/**
 * Created by intel on 24-Aug-17.
 */

public class ParentScheduleActivity extends ScheduleActivity
{
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setTheme(R.style.ParentTheme);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        user = Preference.get(this).getUser();
        super.onViewReady(savedInstanceState);
        if (isToolbarSetupEnabled()) {
            getToolbar().setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryParent));
        }

    }

    @Override
    public UserType getUserType() {
        return UserType.PARENT;
    }

    @Override
    public int tabIndicatorColor() {
        switch (UserType.getUserType(user.getData().getUser_type())){
            case STUDENT:
                return R.color.colorPrimaryStudent;
            case PARENT:
                return R.color.colorPrimaryParent;
        }
        return R.color.colorPrimary;
    }

    @Override
    public int tabSelectedTextColor() {
        switch (UserType.getUserType(user.getData().getUser_type())){
            case STUDENT:
                return R.color.colorPrimaryStudent;
            case PARENT:
                return R.color.colorPrimaryParent;
        }
        return R.color.colorPrimary;
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
