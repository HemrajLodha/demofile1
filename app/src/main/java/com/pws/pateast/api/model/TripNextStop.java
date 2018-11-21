package com.pws.pateast.api.model;

import com.pws.pateast.enums.UserType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by intel on 15-Sep-17.
 */

public class TripNextStop {
   private int tripId;
    private int routeId;


    private TripNextStop(Builder builder) {
        setTripId(builder.tripId);
        setRouteId(builder.routeId);
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public int getTripId() {
        return tripId;
    }

    public int getRouteId() {
        return routeId;
    }

    public static class Builder {
        int tripId;
        int routeId;

        public Builder() {

        }

        public Builder setTripId(int tripId) {
            this.tripId = tripId;
            return this;
        }

        public Builder setRouteId(int routeId) {
            this.routeId = routeId;
            return this;
        }

        public TripNextStop build() {
            return new TripNextStop(this);
        }
    }
}
