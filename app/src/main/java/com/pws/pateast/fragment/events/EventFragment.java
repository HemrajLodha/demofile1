package com.pws.pateast.fragment.events;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.view.BaseRecyclerView;
import com.pws.pateast.R;
import com.pws.pateast.activity.tasks.ParentTaskActivity;
import com.pws.pateast.activity.tasks.TaskActivity;
import com.pws.pateast.api.model.Events;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.base.Extras;
import com.pws.pateast.enums.TaskType;

import java.util.List;

public abstract class EventFragment extends AppFragment implements EventView, BaseRecyclerAdapter.OnItemClickListener {
    private BaseRecyclerView rvEvents;
    private LinearLayoutManager llm;
    protected EventAdapter mAdapter;

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_events;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        getAppListener().setTitle(R.string.menu_school_events);
        rvEvents = (BaseRecyclerView) findViewById(R.id.rv_events);
        llm = new LinearLayoutManager(getContext());
        rvEvents.setLayoutManager(llm);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getEventPresenter() != null) {
            getEventPresenter().detachView();
        }
    }

    @Override
    public void setEventsAdapter(List<Events> events) {
        if (rvEvents.getAdapter() == null) {
            mAdapter = new EventAdapter(getContext(), this);
            rvEvents.setAdapter(mAdapter);
        }
        mAdapter.update(events);
    }
}
