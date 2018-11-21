package com.pws.pateast.activity.driver.track;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.pws.pateast.R;
import com.pws.pateast.api.model.Route;
import com.pws.pateast.api.model.RouteMap;
import com.pws.pateast.api.model.Trip;
import com.pws.pateast.api.model.TripEvent;
import com.pws.pateast.api.model.TripLocation;
import com.pws.pateast.api.model.TripNextStop;
import com.pws.pateast.base.locationmanager.LocationPresenter;
import com.pws.pateast.enums.BoardStatus;
import com.pws.pateast.enums.TripStatus;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.events.LocationEvent;
import com.pws.pateast.events.SocketEvent;
import com.pws.pateast.utils.Utils;

import java.util.List;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

import static com.pws.pateast.enums.TripStatus.DROP_STARTED;
import static com.pws.pateast.enums.TripStatus.DROP_STOPPED;
import static com.pws.pateast.enums.TripStatus.PICKUP_STARTED;
import static com.pws.pateast.enums.TripStatus.PICKUP_STOPPED;
import static com.pws.pateast.events.LocationEvent.EVENT_JOIN_DRIVER;
import static com.pws.pateast.events.LocationEvent.EVENT_LEAVE_DRIVER;
import static com.pws.pateast.events.LocationEvent.EVENT_NEXT_STOP_ARRIVING;
import static com.pws.pateast.events.LocationEvent.EVENT_START_DROP;
import static com.pws.pateast.events.LocationEvent.EVENT_START_PICKUP;
import static com.pws.pateast.events.LocationEvent.EVENT_START_PICKUP_ON_BOARD;
import static com.pws.pateast.events.LocationEvent.EVENT_STOP_DROP;
import static com.pws.pateast.events.LocationEvent.EVENT_STOP_PICKUP;
import static com.pws.pateast.events.LocationEvent.EVENT_TRIP_POSITION;
import static com.pws.pateast.events.LocationEvent.EVENT_TRIP_RECORD_STATUS_CHANGED;
import static com.pws.pateast.events.LocationEvent.EVENT_TRIP_STATUS_CHANGED;
import static com.pws.pateast.events.SocketEvent.EVENT_IS_CONNECTED;

/**
 * Created by intel on 12-Sep-17.
 */

public class DriverTrackPresenter extends LocationPresenter<DriverTrackView> {

    private TripStatus tripStatus = TripStatus.NONE;


    @Override
    public void attachView(DriverTrackView view) {
        super.attachView(view);
        tripStatus = TripStatus.getStatus(getView().getTrip().getStatus());
        eventBus.post(new SocketEvent(EVENT_IS_CONNECTED));
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
        if (connected && (tripStatus == TripStatus.PICKUP_STARTED || tripStatus == TripStatus.DROP_STARTED)) {
            emitLocation(location);
            getDistanceMatrix(current);
        }
        super.onLocationChanged(location);
        addMarker(current);
        //check next point distance
        //checkRouteMap(current);
    }

    @Override
    public void onSocketEvent() {
        if (connected) {
            joinTrip();
            getView().setTripStatus(tripStatus);
        } else {
            leaveTrip();
            getView().setTripStatus(TripStatus.NONE);
        }
    }

    @Override
    public void onLocationEvent(LocationEvent event) {
        TripEvent tripEvent = event.getTripEvent();
        switch (event.getEventType()) {
            case EVENT_TRIP_RECORD_STATUS_CHANGED:
                onTripEvent(tripEvent);
                break;
            case EVENT_TRIP_STATUS_CHANGED:
                if (tripEvent.isStatus()) {
                    tripStatus = TripStatus.getStatus(tripEvent.getTripStatus());
                    getView().setTripStatus(tripStatus);
                } else {
                    showMessage(R.string.unable_to_connect);
                }
                break;
        }
    }

    @Override
    protected List<RouteMap> getRouteMaps() {
        return getView().getTrip().getRouteMap().getRvdhsmapaddresses();
    }

    @Override
    protected void setWayPoints(String... wayPoints) {
        getView().setTripRoute(wayPoints);
    }


    @Override
    protected void calculateDistanceMatrix(DistanceMatrix matrix) {

        List<RouteMap> routeMaps = getRouteMaps();

        long distanceInMeters = matrix.rows[0].elements[0].distance.inMeters;
        int nearestIndex = 0;

        for (int i = 0; i < matrix.rows[0].elements.length; i++) {
            DistanceMatrixElement item = matrix.rows[0].elements[i];
            if (item.distance.inMeters < distanceInMeters) {
                distanceInMeters = item.distance.inMeters;
                nearestIndex = i;
            }
        }
        int routeId = routeMaps.get(nearestIndex).getRouteaddress().getId();
        long distance = distanceInMeters / 1000;
        //Utils.showToast(getContext(), "Next stop is " + String.valueOf(distance) + " KM Away" + stopId);
        if (distance < 1) {
            int id = preference.getDriverNextStopId();
            if (id != routeId) {
                emitNextStopArriving(routeId);
                preference.setDriverNextStopId(routeId);
                Utils.showToast(getContext(), "Next stop is " + String.valueOf(distance) + " KM Away");
            }
        }

    }

    @Override
    protected void checkRouteMap(LatLng currentPosition) {
        List<RouteMap> routeMaps = getView().getTrip().getRouteMap().getRvdhsmapaddresses();
        double distance = (StreamSupport
                .stream(routeMaps)
                .filter(routeMap -> routeMap.getRouteaddress().getId() != 2)
                .map(routeMap -> {
                    Route route = routeMap.getRouteaddress();
                    LatLng nextPosition = new LatLng(route.getLat(), route.getLang());
                    return SphericalUtil.computeDistanceBetween(currentPosition, nextPosition);
                })
                .sorted((o1, o2) -> o1.compareTo(o2))
                .findFirst()
                .get()) / 1000;
        if (distance < 1)
            Utils.showToast(getContext(), "Next stop is " + String.valueOf(distance) + " KM Away");
        else if (distance <= 0)
            Utils.showToast(getContext(), "Reached");
    }

    public void onTripEvent(TripEvent tripEvent) {
        switch (tripEvent.getTripEvent()) {
            case EVENT_START_PICKUP_ON_BOARD:
                changeTripRecordStatus(tripEvent.getTripRecordIds(), BoardStatus.PARENT_ON_BOARD_DROP);
                break;
        }
    }

    public TripStatus getTripStatus() {
        return tripStatus;
    }

    public String getTripRecordCount() {
        int tripRecordCount = 0;
        for (Trip tripRecord : getView().getTripRecords()) {
            if (tripStatus == PICKUP_STARTED && BoardStatus.getStatus(tripRecord.getStatus()) == BoardStatus.PARENT_ON_BOARD_DROP)
                tripRecordCount += 1;
            else if (tripStatus == PICKUP_STOPPED)
                tripRecordCount += 1;
        }
        return String.valueOf(tripRecordCount);
    }

    public void changeTripRecordStatus(List<Integer> tripRecordIds, BoardStatus boardStatus) {
        for (Trip tripRecord : getView().getTripRecords()) {
            for (int tripRecordId : tripRecordIds) {
                if (tripRecord.getId() == tripRecordId)
                    tripRecord.setStatus(boardStatus.getStatus());
            }
        }
        getView().invalidateOptionsMenu();
    }


    private void emitLocation(Location location) {
        eventBus.post(new LocationEvent(EVENT_TRIP_POSITION,
                new TripLocation.Builder()
                        .setTripId(getView().getTrip().getId())
                        .setLatitude(location.getLatitude())
                        .setLongitude(location.getLongitude())
                        .setBearing(location.getBearing())
                        .setUser_type(UserType.DRIVER.getValue())
                        .build()));
    }

    private void emitNextStopArriving(int routeId) {
        eventBus.post(new LocationEvent(EVENT_NEXT_STOP_ARRIVING,
                new TripNextStop.Builder()
                        .setTripId(getView().getTrip().getId())
                        .setRouteId(routeId)
                        .build()));
    }

    private void joinTrip() {
        eventBus.post(new LocationEvent(EVENT_JOIN_DRIVER,
                new TripEvent.Builder()
                        .setRouteId(getView().getTrip().getRouteId())
                        .setTripId(getView().getTrip().getId())
                        .build()));
    }

    private void leaveTrip() {
        eventBus.post(new LocationEvent(EVENT_LEAVE_DRIVER,
                new TripEvent.Builder()
                        .setRouteId(getView().getTrip().getRouteId())
                        .setTripId(getView().getTrip().getId())
                        .build()));
    }

    private void changeTripStatus(String eventType, TripStatus tripStatus) {
        eventBus.post(new LocationEvent(eventType,
                new TripEvent.Builder()
                        .setUser_type(UserType.DRIVER.getValue())
                        .setTripId(getView().getTrip().getId())
                        .setTripStatus(tripStatus.getStatus())
                        .build()));
    }

    public void changeTripStatus() {
        switch (tripStatus) {
            case NOT_STARTED:
                changeTripStatus(EVENT_START_PICKUP, PICKUP_STARTED);
                break;
            case PICKUP_STARTED:
                preference.setDriverNextStopId(-1);
                if (canStopPickUp()) {
                    changeTripStatus(EVENT_STOP_PICKUP, PICKUP_STOPPED);
                    break;
                }
                return;
            case PICKUP_STOPPED:
                if (canStartDrop()) {
                    changeTripStatus(EVENT_START_DROP, DROP_STARTED);
                    break;
                }
                showMessage(R.string.message_driver_cannot_start_drop);
                return;
            case DROP_STARTED:
                preference.setDriverNextStopId(-1);
                if (canStopDrop()) {
                    changeTripStatus(EVENT_STOP_DROP, DROP_STOPPED);
                    break;
                }
                showMessage(R.string.message_driver_cannot_stop_drop);
                return;
            case DROP_STOPPED:
                break;
        }
    }


    private boolean canStopPickUp() {
        boolean canStopPickUpPickedAll = true;
        boolean canStopPickUpDroppedAll = true;
        for (Trip tripRecord : getStudentRouteTripRecord(getView().getTripRecords())) {
            if (canStopPickUpPickedAll)
                canStopPickUpPickedAll = tripRecord.getStatus() != BoardStatus.PARENT_ON_BOARD_DROP.getStatus();

            if (canStopPickUpDroppedAll)
                canStopPickUpDroppedAll = tripRecord.getStatus() != BoardStatus.DRIVER_ON_BOARD_PICKUP.getStatus();

            if (!canStopPickUpPickedAll || !canStopPickUpDroppedAll)
                continue;
        }
        if (!canStopPickUpPickedAll)
            showMessage(R.string.message_driver_cannot_stop_pickup_pick_all);
        else if (!canStopPickUpDroppedAll)
            showMessage(R.string.message_driver_cannot_stop_pickup_drop_all);

        return canStopPickUpPickedAll && canStopPickUpDroppedAll;
    }

    private boolean canStartDrop() {
        boolean canStartDrop = false;
        for (Trip tripRecord : getStudentRouteTripRecord(getView().getTripRecords())) {
            canStartDrop = tripRecord.getStatus() != BoardStatus.DRIVER_ON_BOARD_PICKUP.getStatus();
            canStartDrop = canStartDrop && tripRecord.getStatus() != BoardStatus.NONE.getStatus();

            if (!canStartDrop)
                break;
        }
        return canStartDrop;
    }

    private boolean canStopDrop() {
        boolean canStopDrop = false;
        for (Trip tripRecord : getStudentRouteTripRecord(getView().getTripRecords())) {
            canStopDrop = tripRecord.getStatus() != BoardStatus.DRIVER_ON_BOARD_DROP.getStatus();
            if (!canStopDrop)
                break;
        }
        return canStopDrop;
    }

    private List<Trip> getStudentRouteTripRecord(List<Trip> tripList) {
        return StreamSupport
                .stream(tripList)
                .filter(trip -> getTripRecord(trip))
                .collect(Collectors.toList());
    }

    private boolean getTripRecord(Trip tripRecord) {
        if (tripRecord.getType() == 3) {
            return true;
        }
        Trip trip = getView().getTrip();
        switch (TripStatus.getStatus(trip.getStatus())) {
            case NOT_STARTED:
            case PICKUP_STARTED:
                return tripRecord.getType() == 1;
            case PICKUP_STOPPED:
            case DROP_STARTED:
                return tripRecord.getType() == 2;
            case DROP_STOPPED:
            default:
                return false;
        }
    }


    public void openRouteDirections(String... waypoints) {
        getView().openGoogleMap(waypoints);
    }

    @Override
    public void detachView() {
        leaveTrip();
        super.detachView();
    }
}
