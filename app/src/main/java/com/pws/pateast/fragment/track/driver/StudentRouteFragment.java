package com.pws.pateast.fragment.track.driver;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.google.android.gms.maps.GoogleMap;
import com.pws.pateast.R;
import com.pws.pateast.api.model.Trip;
import com.pws.pateast.base.Extras;
import com.pws.pateast.base.locationmanager.base.LocationFragment;
import com.pws.pateast.base.locationmanager.configuration.Configurations;
import com.pws.pateast.base.locationmanager.configuration.LocationConfiguration;
import com.pws.pateast.base.locationmanager.constants.FailType;
import com.pws.pateast.listener.AdapterListener;
import com.pws.pateast.widget.SpaceItemDecoration;

import java.util.List;

/**
 * Created by intel on 18-Sep-17.
 */

public class StudentRouteFragment extends LocationFragment implements StudentRouteView, BaseRecyclerAdapter.OnItemClickListener {
    private RecyclerView rvStudentRoute;
    private StudentRouteAdapter routeAdapter;

    private StudentRoutePresenter mPresenter;


    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_student_route;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        getAppListener().setTitle(R.string.mapped_student);
        rvStudentRoute = (RecyclerView) findViewById(R.id.rv_student_route);
        rvStudentRoute.setLayoutManager(new LinearLayoutManager(getContext()));
        rvStudentRoute.addItemDecoration(new SpaceItemDecoration(getContext(), R.dimen.size_2, 1, true));

        mPresenter = new StudentRoutePresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void onActionClick() {
        super.onActionClick();
        mPresenter.getTrip();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null)
            mPresenter.registerEvent(true);
        if (getLocationManager().isWaitingForLocation()
                && !getLocationManager().isAnyDialogShowing()) {
            showProgressDialog("Getting location...");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPresenter != null)
            mPresenter.registerEvent(false);
        hideProgressDialog();
    }

    @Override
    public GoogleMap getGoogleMap() {
        return null;
    }

    @Override
    public Trip getTrip() {
        return getArguments().getParcelable(Extras.TRIP);
    }

    @Override
    public void setTrip(Trip trip) {
        getArguments().putParcelable(Extras.TRIP, trip);
    }

    @Override
    public List<Trip> getTripRecords() {
        return getTrip().getTriprecords();
    }

    @Override
    public void setStudentRouteAdapter(List<Trip> tripList) {
        if (rvStudentRoute.getAdapter() == null) {
            routeAdapter = new StudentRouteAdapter(getContext(), this);
            routeAdapter.setTrip(getTrip());
            rvStudentRoute.setAdapter(routeAdapter);
        }
        routeAdapter.update(tripList);
    }

    @Override
    public void updateStudentRouteAdapter(int tripRecordId, int tripRecordStatus) {

        if (routeAdapter != null)
            routeAdapter.updateTripRecord(tripRecordId, tripRecordStatus);
    }

    @Override
    public LocationConfiguration getLocationConfiguration() {
        return Configurations.defaultConfiguration(false, getString(R.string.gimme_permission), getString(R.string.turn_on_gps));
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
                getActivity().onBackPressed();
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
    public void onItemClick(View view, int position) {
        Trip tripRecord = routeAdapter.getItem(position);
        switch (view.getId()) {
            case R.id.btn_board:
                mPresenter.changeTripRecordStatus(tripRecord.getId(), tripRecord.getStatus(), false);
                break;
            case R.id.btn_cancel:
                mPresenter.changeTripRecordStatus(tripRecord.getId(), tripRecord.getStatus(), true);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.detachView();
    }

}
