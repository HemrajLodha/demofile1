package com.pws.pateast.activity.driver.home;

import com.pws.pateast.api.model.RouteMap;
import com.pws.pateast.api.model.Trip;
import com.pws.pateast.api.model.User;
import com.pws.pateast.base.AppView;
import com.pws.pateast.listener.AppListener;

import java.util.ArrayList;

/**
 * Created by planet on 9/8/2017.
 */

public interface DriverHomeView extends AppView {
    void inflateMenu(int resId);

    void setData(User user);

    void setRouteMapsAdapter(ArrayList<RouteMap> routeMaps);

    void startTrip(Trip trip);

    AppListener getAppListener();
}
