package com.pws.pateast.activity.parent.track;

import com.pws.pateast.api.model.Trip;
import com.pws.pateast.base.locationmanager.LocationView;

/**
 * Created by intel on 14-Sep-17.
 */

public interface ParentTrackView extends LocationView
{
    Trip getTrip();

}
