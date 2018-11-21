package com.pws.pateast.fragment.home.parent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import com.base.ui.view.BaseRecyclerView;
import com.pws.pateast.model.DashboardItem;
import com.pws.pateast.R;
import com.pws.pateast.fragment.home.DashBoardFragment;
import com.pws.pateast.fragment.home.DashboardAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by intel on 18-Apr-17.
 */

public class ParentFragment extends DashBoardFragment
{
    private BaseRecyclerView rvMenu;

    private DashboardAdapter dashboardAdapter;

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_parent;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState)
    {
        getAppListener().setTitle(R.string.title_dashboard);
        rvMenu = (BaseRecyclerView) findViewById(R.id.rv_menu);
        rvMenu.setUpAsGrid(3);
        rvMenu.setPullRefreshEnabled(false);
        setDashboardAdapter(getParentAdapterItem());
    }

    public void setDashboardAdapter(List<DashboardItem> items) {
        if (rvMenu.getAdapter() == null) {
            dashboardAdapter = new DashboardAdapter(getContext());
            rvMenu.setAdapter(dashboardAdapter);
        }
        dashboardAdapter.add(items);
    }

    private List<DashboardItem> getParentAdapterItem() {
        List<DashboardItem> items = new ArrayList<>();
        /*items.add(new DashboardItem(R.string.menu_assignments, R.drawable.assignment));
        items.add(new DashboardItem(R.string.menu_my_profile, R.drawable.profile));
        items.add(new DashboardItem(R.string.menu_messages, R.drawable.message));
        items.add(new DashboardItem(R.string.menu_timetable, R.drawable.timetable));
        items.add(new DashboardItem(R.string.menu_subject_marks, R.drawable.marks));
        items.add(new DashboardItem(R.string.menu_galley, R.drawable.gallery));
        items.add(new DashboardItem(R.string.menu_my_attendance, R.drawable.attendance));
        items.add(new DashboardItem(R.string.menu_events, R.drawable.event));
        items.add(new DashboardItem(R.string.menu_polls, R.drawable.polls));*/

        return items;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
