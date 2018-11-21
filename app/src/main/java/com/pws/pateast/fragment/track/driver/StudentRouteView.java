package com.pws.pateast.fragment.track.driver;

import com.pws.pateast.api.model.Trip;
import com.pws.pateast.base.locationmanager.LocationView;

import java.util.List;

/**
 * Created by intel on 18-Sep-17.
 */

public interface StudentRouteView extends LocationView {
    Trip getTrip();

    void setTrip(Trip trip);

    List<Trip> getTripRecords();

    void setStudentRouteAdapter(List<Trip> tripList);

    void updateStudentRouteAdapter(int tripRecordId, int tripRecordStatus);

}
