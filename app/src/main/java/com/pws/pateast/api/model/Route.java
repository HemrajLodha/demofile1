package com.pws.pateast.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by intel on 13-Sep-17.
 */

public class Route implements Parcelable
{
    int id,position;
    String name,address;
    double lat,lang;

    ArrayList<Route> routeaddresses;



    protected Route(Parcel in) {
        id = in.readInt();
        position = in.readInt();
        name = in.readString();
        address = in.readString();
        lat = in.readDouble();
        lang = in.readDouble();
        routeaddresses = in.createTypedArrayList(Route.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(position);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeDouble(lat);
        dest.writeDouble(lang);
        dest.writeTypedList(routeaddresses);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Route> CREATOR = new Creator<Route>() {
        @Override
        public Route createFromParcel(Parcel in) {
            return new Route(in);
        }

        @Override
        public Route[] newArray(int size) {
            return new Route[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLang() {
        return lang;
    }

    public void setLang(double lang) {
        this.lang = lang;
    }

    public ArrayList<Route> getRouteaddresses() {
        return routeaddresses;
    }

    public void setRouteaddresses(ArrayList<Route> routeaddresses) {
        this.routeaddresses = routeaddresses;
    }
}
