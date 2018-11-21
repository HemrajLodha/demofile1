package com.pws.pateast.fragment.track.parent;

import android.graphics.Rect;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.base.ui.adapter.BaseExpandableRecyclerAdapter;
import com.google.android.gms.maps.GoogleMap;
import com.pws.pateast.R;
import com.pws.pateast.activity.parent.track.ParentTrackActivity;
import com.pws.pateast.api.model.RouteMap;
import com.pws.pateast.api.model.Trip;
import com.pws.pateast.api.model.Ward;
import com.pws.pateast.base.Extras;
import com.pws.pateast.base.locationmanager.base.LocationFragment;
import com.pws.pateast.base.locationmanager.configuration.Configurations;
import com.pws.pateast.base.locationmanager.configuration.LocationConfiguration;
import com.pws.pateast.base.locationmanager.constants.FailType;
import com.pws.pateast.listener.AdapterListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by intel on 13-Sep-17.
 */

public class WardRouteFragment extends LocationFragment implements WardRouteView, BaseExpandableRecyclerAdapter.OnParentClickListener<RouteMap>, BaseExpandableRecyclerAdapter.OnChildClickListener<RouteMap, RouteMap> {
    private RecyclerView rvWardRoute;
    private WardRouteAdapter wardRouteAdapter;

    private WardRoutePresenter mPresenter;

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_ward_route;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        getAppListener().setTitle(R.string.menu_track_bus);

        rvWardRoute = (RecyclerView) findViewById(R.id.rv_ward_route);
        rvWardRoute.setLayoutManager(new LinearLayoutManager(getContext()));
        rvWardRoute.addItemDecoration(new RouteDecoration());

        mPresenter = new WardRoutePresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null)
            mPresenter.joinTripRoom();
        if (getLocationManager().isWaitingForLocation()
                && !getLocationManager().isAnyDialogShowing()) {
            showProgressDialog("Getting location...");
        }
        if (mPresenter != null && getWards() == null) {
            onActionClick();
            mPresenter.getWardList();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPresenter != null)
            mPresenter.leaveTripRoom();
        hideProgressDialog();
    }

    @Override
    public GoogleMap getGoogleMap() {
        return null;
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
    public List<Ward> getWards() {
        return getArguments().getParcelableArrayList(Extras.STUDENTS);
    }

    @Override
    public void setWards(List<Ward> wards) {
        getArguments().putParcelableArrayList(Extras.STUDENTS, (ArrayList<? extends Parcelable>) wards);
    }

    @Override
    public List<RouteMap> getRouteMaps() {
        return wardRouteAdapter != null ? wardRouteAdapter.getParentList() : new ArrayList<RouteMap>();
    }

    @Override
    public void setRouteMapAdapter(List<RouteMap> routeMaps) {
        if (rvWardRoute.getAdapter() == null) {
            wardRouteAdapter = new WardRouteAdapter(getContext(), this, this);
            rvWardRoute.setAdapter(wardRouteAdapter);
        }
        wardRouteAdapter.setParentList(routeMaps, false);
    }

    @Override
    public void trackWard(Trip trip) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Extras.TRIP, trip);
        getAppListener().openActivity(ParentTrackActivity.class, bundle);
    }

    @Override
    public void updateTripStatus() {
        if (mPresenter != null) {
            onActionClick();
            mPresenter.getRouteMap();
        }
    }


    @Override
    public void onParentClick(View view, RouteMap parent, int position) {
        switch (view.getId()) {
            case R.id.layout_track_bus:
                Trip trip = parent.getTrip();
                trip.setRouteMaps(parent.getRvdhsmapaddresses());
                trackWard(trip);
                break;
        }
    }

    @Override
    public void onChildClick(View view, RouteMap parent, RouteMap child) {
        switch (view.getId()) {
            case R.id.btn_on_board:
                mPresenter.onBoardYourWard(child.getStudent().getTriprecord().getId());
                break;
        }
    }


    class RouteDecoration extends RecyclerView.ItemDecoration {
        private int spacing;

        public RouteDecoration() {
            spacing = getContext().getResources().getDimensionPixelSize(R.dimen.size_5);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);

            outRect.left = spacing;
            outRect.right = spacing;
            if (parent.getAdapter().getItemViewType(position) == BaseExpandableRecyclerAdapter.TYPE_PARENT)
                outRect.top = spacing;
            if (position == parent.getAdapter().getItemCount() - 1) {
                view.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.dashboard_card_bottom_bg));
                outRect.bottom = spacing;
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.detachView();
    }


}
