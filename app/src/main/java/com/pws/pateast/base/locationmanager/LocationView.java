package com.pws.pateast.base.locationmanager;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.pws.pateast.base.AppView;
import com.pws.pateast.listener.AppListener;

/**
 * Created by intel on 12-Sep-17.
 */

public interface LocationView extends AppView {
    int LOCATION_INTERVAL = 1000;
    int LOCATION_FASTEST_INTERVAL = 1000;
    int LOCATION_DISPLACEMENT = 0;
    int LOCATION_PRIORITY = LocationRequest.PRIORITY_HIGH_ACCURACY;

    GoogleMap getGoogleMap();

    void getLocation();

    AppListener getAppListener();

    void setError(String error);

}
