package com.pws.pateast.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.pws.pateast.enums.UserType;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by intel on 14-Sep-17.
 */

public class TripLocation implements Parcelable {
    int tripId;
    double latitude, longitude;
    float bearing;
    String user_type;

    private TripLocation(Builder builder) {
        setTripId(builder.getTripId());
        setLatitude(builder.getLatitude());
        setLongitude(builder.getLongitude());
        setBearing(builder.getBearing());
        setUser_type(builder.getUser_type());
    }

    protected TripLocation(Parcel in) {
        tripId = in.readInt();
        latitude = in.readDouble();
        longitude = in.readDouble();
        bearing = in.readFloat();
        user_type = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(tripId);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeFloat(bearing);
        dest.writeString(user_type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TripLocation> CREATOR = new Creator<TripLocation>() {
        @Override
        public TripLocation createFromParcel(Parcel in) {
            return new TripLocation(in);
        }

        @Override
        public TripLocation[] newArray(int size) {
            return new TripLocation[size];
        }
    };

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public float getBearing() {
        return bearing;
    }

    public void setBearing(float bearing) {
        this.bearing = bearing;
    }

    public UserType getUser_type() {
        return UserType.getUserType(user_type);
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public static class Builder {
        int tripId;
        double latitude, longitude;
        float bearing;
        String user_type;

        public int getTripId() {
            return tripId;
        }

        public Builder setTripId(int tripId) {
            this.tripId = tripId;
            return this;
        }

        public double getLatitude() {
            return latitude;
        }

        public Builder setLatitude(double latitude) {
            this.latitude = latitude;
            return this;
        }

        public double getLongitude() {
            return longitude;
        }

        public Builder setLongitude(double longitude) {
            this.longitude = longitude;
            return this;
        }

        public float getBearing() {
            return bearing;
        }

        public Builder setBearing(float bearing) {
            this.bearing = bearing;
            return this;
        }

        public String getUser_type() {
            return user_type;
        }

        public Builder setUser_type(String user_type) {
            this.user_type = user_type;
            return this;
        }

        public TripLocation build() {
            return new TripLocation(this);
        }
    }

    public JSONObject getLocation() throws JSONException {
        JSONObject location = new JSONObject(new Gson().toJson(this));
        location.remove("user_type");
        return location;
    }
}
