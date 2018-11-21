package com.pws.pateast.api.model;

import com.pws.pateast.enums.UserType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by intel on 15-Sep-17.
 */

public class TripEvent {

    List<Integer> routeIds;
    List<Integer> tripIds;
    List<Integer> tripRecordIds;
    String user_type;
    String tripEvent;
    boolean status;
    int tripId;
    int tripRecordId;
    int tripStatus;
    int tripRecordStatus;


    private TripEvent(Builder builder) {
        setRouteIds(builder.getRouteIds());
        setTripIds(builder.getTripIds());
        setTripRecordIds(builder.getTripRecordIds());
        setUser_type(builder.getUser_type());
        setTripEvent(builder.getTripEvent());
        setTripStatus(builder.getTripStatus());
        setTripRecordStatus(builder.getTripRecordStatus());
    }

    public void setTripRecordStatus(int tripRecordStatus) {
        this.tripRecordStatus = tripRecordStatus;
    }

    public int getTripRecordStatus() {
        return tripRecordStatus;
    }

    public void setTripStatus(int tripStatus) {
        this.tripStatus = tripStatus;
    }


    public int getTripStatus() {
        return tripStatus;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripRecordId(int tripRecordId) {
        this.tripRecordId = tripRecordId;
    }

    public int getTripRecordId() {
        return tripRecordId;
    }

    public boolean isStatus() {
        return status;
    }

    public List<Integer> getRouteIds() {
        return routeIds;
    }

    public void setRouteIds(List<Integer> routeIds) {
        this.routeIds = routeIds;
    }

    public List<Integer> getTripIds() {
        return tripIds;
    }

    public void setTripIds(List<Integer> tripIds) {
        this.tripIds = tripIds;
    }

    public List<Integer> getTripRecordIds() {
        return tripRecordIds;
    }

    public void setTripRecordIds(List<Integer> tripRecordIds) {
        this.tripRecordIds = tripRecordIds;
    }


    public UserType getUser_type() {
        return UserType.getUserType(user_type);
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getTripEvent() {
        return tripEvent;
    }

    public void setTripEvent(String tripEvent) {
        this.tripEvent = tripEvent;
    }

    public static class Builder {
        List<Integer> routeIds;
        List<Integer> tripIds;
        List<Integer> tripRecordIds;
        String user_type;
        String tripEvent;
        int tripStatus;
        int tripRecordStatus;

        public Builder() {
            routeIds = new ArrayList<>();
            tripIds = new ArrayList<>();
            tripRecordIds = new ArrayList<>();
        }


        public Builder setRouteId(int routeId) {
            routeIds.add(routeId);
            return this;
        }


        public Builder setTripId(int tripId) {
            tripIds.add(tripId);
            return this;
        }

        public Builder setTripRecordId(int tripRecordId) {
            tripRecordIds.add(tripRecordId);
            return this;
        }

        public List<Integer> getRouteIds() {
            return routeIds;
        }

        public List<Integer> getTripIds() {
            return tripIds;
        }

        public List<Integer> getTripRecordIds() {
            return tripRecordIds;
        }

        public Builder setRouteIds(List<Integer> routeIds) {
            this.routeIds = routeIds;
            return this;
        }

        public Builder setTripIds(List<Integer> tripIds) {
            this.tripIds = tripIds;
            return this;
        }

        public Builder setTripRecordIds(List<Integer> tripRecordIds) {
            this.tripRecordIds = tripRecordIds;
            return this;
        }

        public String getUser_type() {
            return user_type;
        }

        public Builder setUser_type(String user_type) {
            this.user_type = user_type;
            return this;
        }

        public String getTripEvent() {
            return tripEvent;
        }

        public Builder setTripEvent(String tripEvent) {
            this.tripEvent = tripEvent;
            return this;
        }

        public Builder setTripStatus(int tripStatus) {
            this.tripStatus = tripStatus;
            return this;
        }

        public int getTripStatus() {
            return tripStatus;
        }

        public Builder setTripRecordStatus(int tripRecordStatus) {
            this.tripRecordStatus = tripRecordStatus;
            return this;
        }

        public int getTripRecordStatus() {
            return tripRecordStatus;
        }

        public TripEvent build() {
            return new TripEvent(this);
        }
    }
}
