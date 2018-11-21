package com.pws.pateast.activity.tasks;

import android.support.v4.app.Fragment;

import com.pws.pateast.base.AppView;

/**
 * Created by intel on 20-Apr-17.
 */

public interface TaskView extends AppView
{

    int getAppTheme();

    void setTaskFragment(Fragment homeFragment);

}
