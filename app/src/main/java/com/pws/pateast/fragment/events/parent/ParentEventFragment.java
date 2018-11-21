package com.pws.pateast.fragment.events.parent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.pws.pateast.activity.tasks.ParentTaskActivity;
import com.pws.pateast.activity.tasks.TaskActivity;
import com.pws.pateast.api.model.Events;
import com.pws.pateast.base.Extras;
import com.pws.pateast.enums.TaskType;
import com.pws.pateast.fragment.events.EventFragment;
import com.pws.pateast.fragment.events.EventPresenter;

public class ParentEventFragment extends EventFragment {

    private ParentEventPresenter mPresenter;

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);
        mPresenter = new ParentEventPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void onActionClick() {
        super.onActionClick();
        mPresenter.getParentEvent();
    }

    @Override
    public void onItemClick(View view, int position) {
        Events event = mAdapter.getItem(position);
        switch (view.getId()) {
            default:
                Bundle bundle = new Bundle();
                bundle.putSerializable(TaskActivity.EXTRA_TASK_TYPE, TaskType.EVENTS_DETAILS);
                bundle.putInt(Extras.EVENT_ID, event.getId());
                getAppListener().openActivity(ParentTaskActivity.class, bundle);
                break;
        }
    }

    @Override
    public EventPresenter getEventPresenter() {
        return mPresenter;
    }
}
