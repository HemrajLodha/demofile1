package com.pws.pateast.fragment.circular.parent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.pws.pateast.activity.tasks.ParentTaskActivity;
import com.pws.pateast.activity.tasks.TaskActivity;
import com.pws.pateast.api.model.Circular;
import com.pws.pateast.api.model.Events;
import com.pws.pateast.base.Extras;
import com.pws.pateast.enums.TaskType;
import com.pws.pateast.fragment.circular.CircularFragment;
import com.pws.pateast.fragment.circular.CircularPresenter;

public class ParentCircularFragment extends CircularFragment {

    private ParentCircularPresenter mPresenter;

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);
        mPresenter = new ParentCircularPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void onActionClick() {
        super.onActionClick();
        mPresenter.getParentCircular();
    }

    @Override
    public void onItemClick(View view, int position) {
        Circular circular = mAdapter.getItem(position);
        switch (view.getId()) {
            default:
                Bundle bundle = new Bundle();
                bundle.putSerializable(TaskActivity.EXTRA_TASK_TYPE, TaskType.CIRCULAR_DETAILS);
                bundle.putInt(Extras.CIRCULAR_ID, circular.getId());
                getAppListener().openActivity(ParentTaskActivity.class, bundle);
                break;
        }
    }

    @Override
    public CircularPresenter getCircularPresenter() {
        return mPresenter;
    }
}
