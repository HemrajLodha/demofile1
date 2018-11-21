package com.pws.pateast.activity.parent.track;

import com.google.android.gms.maps.model.LatLng;
import com.pws.pateast.api.model.RouteMap;
import com.pws.pateast.api.model.TripEvent;
import com.pws.pateast.api.model.TripLocation;
import com.pws.pateast.base.locationmanager.LocationPresenter;
import com.pws.pateast.events.LocationEvent;

import java.util.List;

import static com.pws.pateast.events.LocationEvent.EVENT_JOIN_PARENT;
import static com.pws.pateast.events.LocationEvent.EVENT_LEAVE_PARENT;
import static com.pws.pateast.events.LocationEvent.EVENT_TRACK_POSITION;

/**
 * Created by intel on 14-Sep-17.
 */

public class ParentTrackPresenter extends LocationPresenter<ParentTrackView> {


    @Override
    public void onSocketEvent() {

    }

    @Override
    public void attachView(ParentTrackView view) {
        super.attachView(view);
        eventBus.post(new LocationEvent(EVENT_JOIN_PARENT,
                new TripEvent.Builder()
                        .setRouteId(getView().getTrip().getRouteId())
                        .setTripId(getView().getTrip().getId())
                        .build()));
    }

    @Override
    public void onLocationEvent(LocationEvent event) {
        final TripLocation location = event.getLocation();
        switch (event.getEventType()) {
            case EVENT_TRACK_POSITION:
                if (location != null) {
                    addMarker(new LatLng(location.getLatitude(), location.getLongitude()));
                }
                break;
        }
    }

    @Override
    protected List<RouteMap> getRouteMaps() {
        return getView().getTrip().getRouteMaps();
    }

    @Override
    public void detachView() {
        eventBus.post(new LocationEvent(EVENT_LEAVE_PARENT,
                new TripEvent.Builder()
                        .setRouteId(getView().getTrip().getRouteId())
                        .setTripId(getView().getTrip().getId())
                        .build()));
        super.detachView();
    }
}
