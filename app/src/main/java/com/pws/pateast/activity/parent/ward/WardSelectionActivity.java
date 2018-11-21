package com.pws.pateast.activity.parent.ward;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.view.BaseRecyclerView;
import com.pws.pateast.R;
import com.pws.pateast.activity.parent.home.ParentHomeActivity;
import com.pws.pateast.api.model.Ward;
import com.pws.pateast.base.ParentFragmentActivity;
import com.pws.pateast.widget.SpaceItemDecoration;

import java.util.ArrayList;

/**
 * Created by planet on 8/10/2017.
 */

public class WardSelectionActivity extends ParentFragmentActivity
        implements WardSelectionView,
        BaseRecyclerAdapter.OnItemClickListener {
    public static final String EXTRA_WARD_LIST = "extra_ward_list";
    private BaseRecyclerView rvWard;
    private WardSelectionPresenter selectionPresenter;
    private WardAdapter wardAdapter;
    private View footerView;


    @Override
    protected int getResourceLayout() {
        return R.layout.activity_select_ward;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);
        setTitle(R.string.title_ward_selection);
        rvWard = findViewById(R.id.rv_ward);
        rvWard.setUpAsList();
        rvWard.setPullRefreshEnabled(false);
        rvWard.setLoadingMoreEnabled(true);
        rvWard.addItemDecoration(new SpaceItemDecoration(this, R.dimen.size_5, 1, true));
        selectionPresenter = new WardSelectionPresenter();
        selectionPresenter.attachView(this);
        if (getIntent() != null && getIntent().getParcelableArrayListExtra(EXTRA_WARD_LIST) != null) {
            getBaseActionBar().setDisplayHomeAsUpEnabled(false);
            ArrayList<Ward> wardList = getIntent().getParcelableArrayListExtra(EXTRA_WARD_LIST);
            setWardAdapter(wardList);
        } else {
            getBaseActionBar().setDisplayHomeAsUpEnabled(true);
            onActionClick();
        }
        footerView = LayoutInflater.from(this).inflate(R.layout.author_quote_item, null, false);
        rvWard.setFootView(footerView);
    }

    @Override
    public void onActionClick() {
        super.onActionClick();
        selectionPresenter.getWardList();
    }

    @Override
    public void setWardAdapter(ArrayList<Ward> wardList) {
        if (rvWard.getAdapter() == null) {
            wardAdapter = new WardAdapter(this, this);
            rvWard.setAdapter(wardAdapter);
        }
        wardAdapter.addOrUpdate(wardList);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onItemClick(View view, int position) {
        Ward ward = wardAdapter.getItem(position);
        selectionPresenter.setWardPreference(ward);
        openActivityOnTop(ParentHomeActivity.class);
    }

}
