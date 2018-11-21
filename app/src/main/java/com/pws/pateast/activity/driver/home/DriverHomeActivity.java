package com.pws.pateast.activity.driver.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.view.BaseRecyclerView;
import com.pws.pateast.R;
import com.pws.pateast.activity.driver.track.DriverTrackActivity;
import com.pws.pateast.activity.leave.DriverLeaveStatusActivity;
import com.pws.pateast.activity.schedule.activity.DriverScheduleActivity;
import com.pws.pateast.activity.tasks.DriverTaskActivity;
import com.pws.pateast.api.model.RouteMap;
import com.pws.pateast.api.model.Trip;
import com.pws.pateast.api.model.User;
import com.pws.pateast.base.Extras;
import com.pws.pateast.base.FragmentActivity;
import com.pws.pateast.enums.TaskType;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.listener.AppListener;
import com.pws.pateast.utils.ImageUtils;
import com.pws.pateast.widget.SpaceItemDecoration;

import java.util.ArrayList;

public class DriverHomeActivity extends FragmentActivity implements DriverHomeView, NavigationView.OnNavigationItemSelectedListener, BaseRecyclerAdapter.OnItemClickListener {
    private ActionBarDrawerToggle mDrawerToggle;
    protected DrawerLayout drawer;
    protected NavigationView navigationView;
    protected ImageView imgProfileView;
    protected TextView tvHeaderName;
    protected TextView tvHeaderEmpType;
    protected TextView tvHeaderEmail;

    private BaseRecyclerView rvRoutes;

    private RouteMapAdapter routeMapAdapter;
    private DriverHomePresenter mPresenter;

    @Override
    protected int getResourceLayout() {
        return R.layout.activity_driver_home;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.DriverTheme);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);
        setTitle(R.string.title_dashboard);
        setDrawer();
        rvRoutes = findViewById(R.id.rv_routes);
        rvRoutes.setUpAsList();
        rvRoutes.setPullRefreshEnabled(false);
        rvRoutes.setLoadingMoreEnabled(false);
        rvRoutes.addItemDecoration(new SpaceItemDecoration(getContext(), R.dimen.size_2, 1, true));

        mPresenter = new DriverHomePresenter();
        mPresenter.attachView(this);
        mPresenter.getUserDetails();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null)
            mPresenter.getUserDetails();
    }

    @Override
    public void onActionClick() {
        super.onActionClick();
        mPresenter.getDashboardDetails();
    }

    private void setDrawer() {
        drawer = findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(mDrawerToggle);
        drawer.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        imgProfileView = headerView.findViewById(R.id.tv_profile_image);
        tvHeaderName = headerView.findViewById(R.id.tv_profile_name);
        tvHeaderEmpType = headerView.findViewById(R.id.tv_employee_type);
        tvHeaderEmail = headerView.findViewById(R.id.tv_profile_email);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawer.closeDrawers();
        switch (item.getItemId()) {
            case R.id.nav_my_profile:
                openActivity(DriverTaskActivity.class, TaskType.DRIVER_PROFILE);
                return false;
            case R.id.nav_school_holidays:
                openActivity(DriverScheduleActivity.class, TaskType.HOLIDAY);
                return false;
            case R.id.nav_leaves:
                openActivity(DriverLeaveStatusActivity.class, TaskType.EMPLOYEE_LEAVE_APPLY);
                return false;
            case R.id.nav_notifications:
                openActivity(DriverTaskActivity.class, TaskType.NOTIFICATION_LIST);
                return false;
            case R.id.nav_settings:
                openActivity(DriverTaskActivity.class, TaskType.SETTINGS);
                return false;
        }
        return true;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void inflateMenu(int resId) {
        navigationView.inflateMenu(resId);
    }

    @Override
    public void setData(User user) {
        User userLogin = mPresenter.getUser();
        tvHeaderEmpType.setText(UserType.getUserType(userLogin.getData().getUser_type()).getName());
        ImageUtils.setImageUrl(this, imgProfileView, user.getUser_image(), R.drawable.avatar1);
        if (!TextUtils.isEmpty(user.getFullname())) {
            tvHeaderName.setText(user.getFullname());
        } else {
            tvHeaderName.setText(getString(R.string.not_available));
        }

        if (!TextUtils.isEmpty(userLogin.getData().getEmail())) {
            tvHeaderEmail.setText(userLogin.getData().getEmail());
        } else {
            tvHeaderEmail.setText(R.string.not_available);
        }
    }

    @Override
    public void setRouteMapsAdapter(ArrayList<RouteMap> routeMaps) {
        if (rvRoutes.getAdapter() == null) {
            routeMapAdapter = new RouteMapAdapter(getContext(), this);
            rvRoutes.setAdapter(routeMapAdapter);
        }
        routeMapAdapter.update(routeMaps);
    }

    @Override
    public void startTrip(Trip trip) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Extras.TRIP, trip);
        openActivity(DriverTrackActivity.class, bundle);
    }

    @Override
    public AppListener getAppListener() {
        return this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.detachView();
    }

    @Override
    public void onItemClick(View view, int position) {
        RouteMap routeMap = routeMapAdapter.getItem(position);
        switch (view.getId()) {
            default:
                if (mPresenter != null)
                    mPresenter.getTrip(routeMap);
                break;
        }
    }
}
