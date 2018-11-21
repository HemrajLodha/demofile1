package com.pws.pateast.events;

import com.pws.pateast.api.model.TripEvent;
import com.pws.pateast.api.model.TripLocation;
import com.pws.pateast.api.model.TripNextStop;

/**
 * Created by intel on 14-Sep-17.
 */

public class LocationEvent {

    public static final String EVENT_TRIP_POSITION = "trip-position";
    public static final String EVENT_TRACK_POSITION = "track-position";

    public static final String EVENT_JOIN_DRIVER = "join-driver";
    public static final String EVENT_LEAVE_DRIVER = "leave-driver";

    public static final String EVENT_JOIN_PARENT = "join-parent";
    public static final String EVENT_LEAVE_PARENT = "leave-parent";

    public static final String EVENT_JOIN_TRIP = "join-trip-room";
    public static final String EVENT_LEAVE_TRIP = "leave-trip-room";

    public static final String EVENT_JOIN_ROUTE_MAP = "join-rvdhsmap-room";
    public static final String EVENT_LEAVE_ROUTE_MAP = "leave-rvdhsmap-room";

    public static final String EVENT_JOIN_TRIP_ADMIN = "join-trip-admin-room";
    public static final String EVENT_LEAVE_TRIP_ADMIN = "leave-trip-admin-room";

    public static final String EVENT_JOIN_TRIP_RECORD = "join-triprecord-room";
    public static final String EVENT_LEAVE_TRIP_RECORD = "leave-triprecord-room";

    public static final String EVENT_START_PICKUP = "start-pick-up";
    public static final String EVENT_STOP_PICKUP = "stop-pick-up";
    public static final String EVENT_START_DROP = "start-drop";
    public static final String EVENT_STOP_DROP = "stop-drop";
    public static final String EVENT_TRIP_STATUS_CHANGED = "trip_status_changed";

    public static final String EVENT_NEXT_STOP_ARRIVING = "vehicle-arrival";

    public static final String EVENT_START_PICKUP_ON_BOARD = "start-pick-up-on-board";

    public static final String EVENT_CONFIRM_PICKUP_ON_BOARD = "confirm-pick-up-on-board";
    public static final String EVENT_CANCEL_PICKUP_ON_BOARD = "cancel-pick-up-on-board";
    public static final String EVENT_STOP_PICKUP_OFF_BOARD = "pick-up-off-board";
    public static final String EVENT_START_DROP_ON_BOARD = "drop-on-board";
    public static final String EVENT_CANCEL_DROP_ON_BOARD = "leave-drop";
    public static final String EVENT_STOP_DROP_OFF_BOARD = "drop-off-board";

    public static final String EVENT_TRIP_RECORD_STATUS_CHANGED = "trip_record_status_changed";


    private String eventType;
    private TripLocation location;
    private TripEvent tripEvent;
    private TripNextStop tripNextStop;

    public LocationEvent(String eventType) {
        this.eventType = eventType;
    }

    public LocationEvent(String eventType, TripLocation location) {
        this.eventType = eventType;
        this.location = location;
    }

    public LocationEvent(String eventType, TripEvent tripEvent) {
        this.eventType = eventType;
        this.tripEvent = tripEvent;
    }

    public LocationEvent(String eventType, TripNextStop tripNextStop) {
        this.eventType = eventType;
        this.tripNextStop = tripNextStop;
    }

    public String getEventType() {
        return eventType;
    }

    public TripLocation getLocation() {
        return location;
    }

    public TripEvent getTripEvent() {
        return tripEvent;
    }

    public TripNextStop getNextStopArrivalEvent() {
        return tripNextStop;
    }


}
