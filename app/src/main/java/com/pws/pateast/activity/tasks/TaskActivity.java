package com.pws.pateast.activity.tasks;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.pws.pateast.base.AppFragment;
import com.pws.pateast.base.Extras;
import com.pws.pateast.base.FragmentActivity;
import com.pws.pateast.enums.TaskType;

/**
 * Created by planet on 4/20/2017.
 */

public class TaskActivity extends FragmentActivity implements TaskView {

    protected TaskPresenter mTaskPresenter;
    protected TaskType mTaskType;
    protected Fragment appFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int appTheme = getAppTheme();
        if (appTheme != 0)
            setTheme(appTheme);
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getAppTheme() {
        return getIntent().getIntExtra(Extras.APP_THEME, 0);
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);
        getBaseActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent() != null) {
            try {
                int type = getIntent().getIntExtra(EXTRA_TASK_TYPE, TaskType.DEFAULT.getValue());
                mTaskType = TaskType.getTaskType(type);
                if (mTaskType == TaskType.DEFAULT) {
                    mTaskType = (TaskType) getIntent().getSerializableExtra(EXTRA_TASK_TYPE);
                }
                if (mTaskType == null) {
                    mTaskType = TaskType.DEFAULT;
                }
            } catch (ClassCastException e) {
                //
            }

            mTaskPresenter = new TaskPresenter();
            mTaskPresenter.attachView(this);

            mTaskPresenter.setTaskFragment(mTaskType, this, getIntent().getExtras());
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void setTaskFragment(Fragment taskFragment) {
        appFragment = taskFragment;
        changeFragment(taskFragment, taskFragment.getClass().getSimpleName());
    }

    @Override
    public void onActionClick() {
        super.onActionClick();
        if (appFragment != null)
            ((AppFragment) appFragment).onActionClick();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTaskPresenter.detachView();
    }
}
