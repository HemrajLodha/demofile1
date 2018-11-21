package com.pws.pateast.activity.tasks;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.pws.pateast.R;

/**
 * Created by intel on 23-Aug-17.
 */

public class StudentTaskActivity extends TaskActivity {

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

}
