package com.pws.pateast.fragment.track.parent;

import com.pws.pateast.api.model.RouteMap;
import com.pws.pateast.api.model.Trip;
import com.pws.pateast.api.model.Ward;
import com.pws.pateast.base.AppView;
import com.pws.pateast.base.locationmanager.LocationView;

import java.util.List;

/**
 * Created by intel on 13-Sep-17.
 */

public interface WardRouteView extends LocationView
{
    List<Ward> getWards();
    void setWards(List<Ward> wards);

    List<RouteMap> getRouteMaps();

    void setRouteMapAdapter(List<RouteMap> routeMaps);

    void trackWard(Trip trip);

    void updateTripStatus();
}
