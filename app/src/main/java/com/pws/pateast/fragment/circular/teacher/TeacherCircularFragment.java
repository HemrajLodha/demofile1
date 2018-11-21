package com.pws.pateast.fragment.circular.teacher;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.pws.pateast.activity.tasks.TaskActivity;
import com.pws.pateast.activity.tasks.TeacherTaskActivity;
import com.pws.pateast.api.model.Circular;
import com.pws.pateast.api.model.Events;
import com.pws.pateast.base.Extras;
import com.pws.pateast.enums.TaskType;
import com.pws.pateast.fragment.circular.CircularFragment;
import com.pws.pateast.fragment.circular.CircularPresenter;

public class TeacherCircularFragment extends CircularFragment {

    private TeacherCircularPresenter mPresenter;

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);
        mPresenter = new TeacherCircularPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void onActionClick() {
        super.onActionClick();
        mPresenter.getTeacherCircular();
    }

    @Override
    public void onItemClick(View view, int position) {
        Circular circular = mAdapter.getItem(position);
        switch (view.getId()) {
            default:
                Bundle bundle = new Bundle();
                bundle.putSerializable(TaskActivity.EXTRA_TASK_TYPE, TaskType.CIRCULAR_DETAILS);
                bundle.putInt(Extras.CIRCULAR_ID, circular.getId());
                getAppListener().openActivity(TeacherTaskActivity.class, bundle);
                break;
        }
    }

    @Override
    public CircularPresenter getCircularPresenter() {
        return mPresenter;
    }
}
