package com.pws.pateast.fragment.track.parent;

import android.location.Location;

import com.pws.pateast.R;
import com.pws.pateast.api.exception.RetrofitException;
import com.pws.pateast.api.model.RouteMap;
import com.pws.pateast.api.model.TripEvent;
import com.pws.pateast.api.model.TripLocation;
import com.pws.pateast.api.model.Ward;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.ParentService;
import com.pws.pateast.base.locationmanager.LocationPresenter;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.events.LocationEvent;
import com.pws.pateast.events.SocketEvent;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.pws.pateast.events.LocationEvent.EVENT_JOIN_PARENT;
import static com.pws.pateast.events.LocationEvent.EVENT_LEAVE_PARENT;
import static com.pws.pateast.events.LocationEvent.EVENT_START_PICKUP_ON_BOARD;
import static com.pws.pateast.events.LocationEvent.EVENT_TRACK_POSITION;
import static com.pws.pateast.events.LocationEvent.EVENT_TRIP_RECORD_STATUS_CHANGED;
import static com.pws.pateast.events.LocationEvent.EVENT_TRIP_STATUS_CHANGED;
import static com.pws.pateast.events.SocketEvent.EVENT_IS_CONNECTED;

/**
 * Created by intel on 13-Sep-17.
 */

public class WardRoutePresenter extends LocationPresenter<WardRouteView> {
    protected ParentService parentService;

    @Override
    public void attachView(WardRouteView view) {
        super.attachView(view);
        eventBus.post(new SocketEvent(EVENT_IS_CONNECTED));
    }

    @Override
    public void onSocketEvent() {
        if (connected) {
            joinTripRoom();
        } else {
            leaveTripRoom();
        }
    }

    @Override
    public void onLocationEvent(LocationEvent event) {
        final TripLocation location = event.getLocation();
        final TripEvent tripEvent = event.getTripEvent();
        switch (event.getEventType()) {
            case EVENT_TRACK_POSITION:
                break;
            case EVENT_TRIP_STATUS_CHANGED:
            case EVENT_TRIP_RECORD_STATUS_CHANGED:
                if (tripEvent != null) {
                    getView().updateTripStatus();
                }
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        super.onLocationChanged(location);
    }

    public void getWardList() {
        parentService = serviceBuilder.createService(ParentService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("mobile", user.getData().getMobile());
        params.put("langId", "1");
        params.put("lang", "en");

        disposable = parentService.getWardList(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Ward>() {

                    @Override
                    public void onResponse(Ward response) {
                        if (response.getData() != null && !response.getData().isEmpty()) {
                            getView().setWards(response.getData());
                            getView().updateTripStatus();
                        } else {
                            onError(new RetrofitException(getString(R.string.no_result_found)));
                        }
                    }

                    @Override
                    public WardRoutePresenter getPresenter() {
                        return WardRoutePresenter.this;
                    }
                });
    }

    public void getRouteMap() {
        leaveTripRoom();
        parentService = serviceBuilder.createService(ParentService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        getRouteParams(params);
        disposable = parentService.getRouteMap(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<RouteMap>() {

                    @Override
                    public void onResponse(RouteMap response) {
                        if (response.getData() != null && !response.getData().isEmpty()) {
                            getView().setRouteMapAdapter(response.getData());
                            joinTripRoom();
                        } else {
                            onError(new RetrofitException(getString(R.string.no_routes)));
                        }
                    }

                    @Override
                    public WardRoutePresenter getPresenter() {
                        return WardRoutePresenter.this;
                    }
                });
    }

    private void getRouteParams(HashMap<String, String> params) {
        StringBuilder studentIds = new StringBuilder();
        StringBuilder academicSessionIds = new StringBuilder();

        for (Ward ward : getView().getWards()) {
            if (studentIds.length() > 0)
                studentIds.append(",");
            if (academicSessionIds.length() > 0)
                academicSessionIds.append(",");

            studentIds.append(ward.getStudentId());
            academicSessionIds.append(ward.getAcademicSessionId());
        }
        params.put("studentIds", studentIds.toString());
        params.put("academicSessionIds", academicSessionIds.toString());
    }

    public void onBoardYourWard(int tripRecordId) {
        if (!connected) {
            showMessage(R.string.unable_to_connect);
            return;
        }
        getView().hideDialog();
        eventBus.post(new LocationEvent(EVENT_START_PICKUP_ON_BOARD,
                new TripEvent.Builder()
                        .setUser_type(UserType.PARENT.getValue())
                        .setTripRecordId(tripRecordId)
                        .build()));
    }


    public void joinTripRoom() {
        TripEvent.Builder builder = new TripEvent.Builder();
        for (RouteMap routeMap : getView().getRouteMaps()) {
            builder.setRouteId(routeMap.getId());
            if (routeMap.getTrip() != null)
                builder.setTripId(routeMap.getTrip().getId());
            for (RouteMap record : routeMap.getRvdhsmaprecords()) {
                if (record.getStudent().getTriprecord() != null)
                    builder.setTripRecordId(record.getStudent().getTriprecord().getId());
            }
        }
        eventBus.post(new LocationEvent(EVENT_JOIN_PARENT, builder.build()));
    }

    public void leaveTripRoom() {
        TripEvent.Builder builder = new TripEvent.Builder();
        for (RouteMap routeMap : getView().getRouteMaps()) {
            builder.setRouteId(routeMap.getId());
            if (routeMap.getTrip() != null)
                builder.setTripId(routeMap.getTrip().getId());
            for (RouteMap record : routeMap.getRvdhsmaprecords()) {
                if (record.getStudent().getTriprecord() != null)
                    builder.setTripRecordId(record.getStudent().getTriprecord().getId());
            }
        }
        eventBus.post(new LocationEvent(EVENT_LEAVE_PARENT, builder.build()));
    }
}
