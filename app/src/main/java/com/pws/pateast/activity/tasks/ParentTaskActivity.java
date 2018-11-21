package com.pws.pateast.activity.tasks;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.pws.pateast.R;
import com.pws.pateast.activity.tasks.TaskView;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.base.FragmentActivity;
import com.pws.pateast.base.ParentFragmentActivity;
import com.pws.pateast.enums.TaskType;

/**
 * Created by planet on 4/20/2017.
 */

public class ParentTaskActivity extends TaskActivity {

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
