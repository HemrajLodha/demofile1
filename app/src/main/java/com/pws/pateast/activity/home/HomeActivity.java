package com.pws.pateast.activity.home;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;

import com.pws.pateast.R;
import com.pws.pateast.base.FragmentActivity;
import com.pws.pateast.base.ParentFragmentActivity;
import com.pws.pateast.fragment.home.DashBoardFragment;
import com.pws.pateast.listener.AppListener;
import com.pws.pateast.service.socket.SocketService;

public class HomeActivity extends FragmentActivity implements HomeView {

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    private HomePresenter homePresenter;

    @Override
    protected int getResourceLayout() {
        return R.layout.activity_home;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);
        getComponent().inject(this);
        setDrawer();
        homePresenter = new HomePresenter();
        homePresenter.attachView(this);
        homePresenter.getUserDetails();
    }

    private void setDrawer() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(mDrawerToggle);
        drawer.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
    }


    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void setHomeFragment(DashBoardFragment homeFragment) {
        navigationView.setNavigationItemSelectedListener(homeFragment);
        changeFragment(homeFragment, homeFragment.getClass().getSimpleName());
    }

    @Override
    public void inflateMenu(int resId) {
        navigationView.inflateMenu(resId);
    }



    @Override
    public AppListener getAppListener() {
        return this;
    }

    @Override
    protected void onDestroy() {
        homePresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void onActionClick() {
        super.onActionClick();
        homePresenter.getUserDetails();
    }

    @Override
    public void closeDrawer() {
        drawer.closeDrawers();
    }
}
