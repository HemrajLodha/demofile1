package com.pws.pateast.activity.driver.track;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.pws.pateast.R;
import com.pws.pateast.activity.tasks.DriverTaskActivity;
import com.pws.pateast.activity.tasks.TaskActivity;
import com.pws.pateast.api.model.Trip;
import com.pws.pateast.base.Extras;
import com.pws.pateast.base.locationmanager.base.LocationActivity;
import com.pws.pateast.base.locationmanager.configuration.Configurations;
import com.pws.pateast.base.locationmanager.configuration.LocationConfiguration;
import com.pws.pateast.base.locationmanager.constants.FailType;
import com.pws.pateast.base.locationmanager.constants.ProcessType;
import com.pws.pateast.enums.TaskType;
import com.pws.pateast.enums.TripStatus;
import com.pws.pateast.listener.AdapterListener;
import com.pws.pateast.listener.AppListener;
import com.pws.pateast.utils.MapHelper;
import com.pws.pateast.utils.NotificationHelper;
import com.pws.pateast.utils.Utils;

import java.util.List;

public class DriverTrackActivity extends LocationActivity implements DriverTrackView, OnMapReadyCallback, View.OnClickListener {

    private final static int ON_BOARD_STUDENT = 100;

    private Button btnTripStatus;
    private ImageButton btnTripRoute;

    private GoogleMap mMap;

    private Trip mTrip;

    private DriverTrackPresenter mPresenter;

    @Override
    protected int getResourceLayout() {
        return R.layout.activity_driver_track;
    }

    @Override
    public boolean isToolbarSetupEnabled() {
        return true;
    }

    @Override
    public LocationConfiguration getLocationConfiguration() {
        return Configurations.getConfiguration(true, getString(R.string.gimme_permission), getString(R.string.turn_on_gps));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);

        if (savedInstanceState.getParcelable(Extras.TRIP) != null) {
            mTrip = savedInstanceState.getParcelable(Extras.TRIP);
        } else {
            mTrip = getIntent().getParcelableExtra(Extras.TRIP);
        }

        btnTripStatus = findViewById(R.id.btn_trip_status);
        btnTripRoute = findViewById(R.id.btn_trip_route);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ward_track, menu);
        MenuItem itemCart = menu.findItem(R.id.action_student_on_board);
        LayerDrawable icon = (LayerDrawable) itemCart.getIcon();
        if (mPresenter != null) {
            NotificationHelper.setBadgeCount(this, icon, mPresenter.getTripRecordCount());
            return getTripRecords() != null && !getTripRecords().isEmpty();
        } else {
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_student_on_board:
                Bundle bundle = new Bundle();
                bundle.putSerializable(TaskActivity.EXTRA_TASK_TYPE, TaskType.STUDENT_ON_BOARD);
                bundle.putParcelable(Extras.TRIP, getTrip());
                openActivityForResult(DriverTaskActivity.class, bundle, ON_BOARD_STUDENT);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mPresenter = new DriverTrackPresenter();
        mPresenter.attachView(this);
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
    public Trip getTrip() {
        return mTrip;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(Extras.TRIP, getTrip());
        super.onSaveInstanceState(outState);
    }

    @Override
    public List<Trip> getTripRecords() {
        return getTrip().getTriprecords();
    }

    @Override
    public void setTripStatus(TripStatus tripStatus) {
        getTrip().setStatus(tripStatus.getStatus());
        btnTripStatus.setVisibility(View.VISIBLE);
        btnTripStatus.setOnClickListener(DriverTrackActivity.this);
        switch (tripStatus) {
            case NOT_STARTED:
                btnTripStatus.setText(R.string.label_start_pickup);
                break;
            case PICKUP_STARTED:
                btnTripStatus.setText(R.string.label_stop_pickup);
                break;
            case PICKUP_STOPPED:
                btnTripStatus.setText(R.string.label_start_drop);
                break;
            case DROP_STARTED:
                btnTripStatus.setText(R.string.label_stop_drop);
                break;
            case DROP_STOPPED:
                btnTripStatus.setText(R.string.label_trip_complete);
                break;
            default:
                btnTripStatus.setVisibility(View.GONE);
                btnTripStatus.setOnClickListener(null);
                break;
        }

        invalidateOptionsMenu();
    }

    @Override
    public void setTripRoute(String... waypoint) {
        btnTripRoute.setTag(waypoint);
        btnTripRoute.setVisibility(View.VISIBLE);
        btnTripRoute.setOnClickListener(DriverTrackActivity.this);
    }

    @Override
    public void openGoogleMap(String... waypoints) {
        Intent intent = MapHelper.dispatchShowDirections(getContext(), waypoints);
        if (intent != null)
            startActivity(intent);
        else
            Utils.showToast(getContext(), R.string.no_app_exists);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void getLocation() {
        super.getLocation();
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
    public void onProcessTypeChanged(@ProcessType int processType) {
        mPresenter.onProcessTypeChanged(processType);
    }

    @Override
    public boolean canTrackWhenLightOff() {
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_trip_status:
                mPresenter.changeTripStatus();
                break;
            case R.id.btn_trip_route:
                mPresenter.openRouteDirections((String[]) view.getTag());
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null)
            mPresenter.registerEvent(true);
        if (getLocationManager().isWaitingForLocation()
                && !getLocationManager().isAnyDialogShowing()) {
            showProgressDialog("Getting location...");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPresenter != null)
            mPresenter.registerEvent(false);
        hideProgressDialog();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.detachView();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ON_BOARD_STUDENT:
                if (resultCode == RESULT_OK) {
                    setIntent(data);
                    invalidateOptionsMenu();
                }
                break;
        }
    }
}
