package com.pws.pateast.activity.parent.track;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.pws.pateast.R;
import com.pws.pateast.api.model.Trip;
import com.pws.pateast.base.Extras;
import com.pws.pateast.base.locationmanager.base.LocationActivity;
import com.pws.pateast.base.locationmanager.configuration.Configurations;
import com.pws.pateast.base.locationmanager.configuration.LocationConfiguration;
import com.pws.pateast.base.locationmanager.constants.FailType;
import com.pws.pateast.listener.AdapterListener;
import com.pws.pateast.listener.AppListener;

/**
 * Created by intel on 14-Sep-17.
 */

public class ParentTrackActivity extends LocationActivity implements ParentTrackView, OnMapReadyCallback {

    private GoogleMap mMap;

    private ParentTrackPresenter mPresenter;


    @Override
    protected int getResourceLayout() {
        return R.layout.activity_parent_track;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mPresenter = new ParentTrackPresenter();
        mPresenter.attachView(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (getLocationManager().isWaitingForLocation()
                && !getLocationManager().isAnyDialogShowing()) {
            showProgressDialog("Getting location...");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideProgressDialog();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public AppListener getAppListener() {
        return this;
    }

    @Override
    public GoogleMap getGoogleMap() {
        return mMap;
    }

    @Override
    public void getLocation() {
        super.getLocation();
    }

    @Override
    public LocationConfiguration getLocationConfiguration() {
        return Configurations.defaultConfiguration(false, getString(R.string.gimme_permission), getString(R.string.turn_on_gps));
    }


    @Override
    public void setError(String error) {
        showDialog(getString(R.string.app_name), error, new AdapterListener<String>() {
            @Override
            public void onClick(int id, String value) {
                onBackPressed();
            }
        }, R.string.ok);
    }

    @Override
    public void onLocationChanged(Location location) {
        mPresenter.onLocationChanged(location);
    }

    @Override
    public void onLocationFailed(@FailType int type) {
        mPresenter.onLocationFailed(type);
    }

    @Override
    public boolean isToolbarSetupEnabled() {
        return true;
    }


    @Override
    public Trip getTrip() {
        return getIntent().getParcelableExtra(Extras.TRIP);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.detachView();
    }
}
