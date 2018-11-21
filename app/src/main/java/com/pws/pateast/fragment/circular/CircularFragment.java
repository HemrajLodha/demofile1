package com.pws.pateast.fragment.circular;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.view.BaseRecyclerView;
import com.pws.pateast.R;
import com.pws.pateast.api.model.Circular;
import com.pws.pateast.api.model.Events;
import com.pws.pateast.base.AppFragment;

import java.util.List;

public abstract class CircularFragment extends AppFragment implements CircularView, BaseRecyclerAdapter.OnItemClickListener {
    private BaseRecyclerView rvCircular;
    private LinearLayoutManager llm;
    protected CircularAdapter mAdapter;

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_circular;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        getAppListener().setTitle(R.string.menu_circular);
        rvCircular = (BaseRecyclerView) findViewById(R.id.rv_circular);
        llm = new LinearLayoutManager(getContext());
        rvCircular.setLayoutManager(llm);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getCircularPresenter() != null) {
            getCircularPresenter().detachView();
        }
    }

    @Override
    public void setCircularAdapter(List<Circular> circulars) {
        if (rvCircular.getAdapter() == null) {
            mAdapter = new CircularAdapter(getContext(), this);
            rvCircular.setAdapter(mAdapter);
        }
        mAdapter.update(circulars);
    }
}
