package com.pws.pateast.activity.driver.track;

import com.google.android.gms.maps.GoogleMap;
import com.google.maps.model.DirectionsResult;
import com.pws.pateast.api.model.Trip;
import com.pws.pateast.base.locationmanager.LocationView;
import com.pws.pateast.enums.TripStatus;
import com.pws.pateast.listener.AppListener;

import java.util.List;

/**
 * Created by intel on 12-Sep-17.
 */

public interface DriverTrackView extends LocationView {

    Trip getTrip();

    List<Trip> getTripRecords();

    void invalidateOptionsMenu();

    void setTripStatus(TripStatus tripStatus);

    void setTripRoute(String... waypoints);

    void openGoogleMap(String... waypoints);
}
