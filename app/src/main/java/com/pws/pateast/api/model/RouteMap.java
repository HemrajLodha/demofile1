package com.pws.pateast.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.base.ui.adapter.model.Parent;
import com.pws.pateast.activity.dashboard.ChildFooterItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by intel on 12-Sep-17.
 */

public class RouteMap extends Response<ArrayList<RouteMap>> implements Parcelable, Parent<RouteMap, ChildFooterItem> {
    int id, position;
    String pick_up_time, drop_time, address;
    double lat, lang;

    Route route, routeaddress;
    Vehicle vehicle;
    UserInfo driver, helper;
    Trip trip;

    ArrayList<RouteMap> rvdhsmaps, routeaddresses, vehicledetails, rvdhsmaprecords, rvdhsmapaddresses;

    Student student;

    protected RouteMap(Parcel in) {
        id = in.readInt();
        position = in.readInt();
        pick_up_time = in.readString();
        drop_time = in.readString();
        address = in.readString();
        lat = in.readDouble();
        lang = in.readDouble();
        route = in.readParcelable(Route.class.getClassLoader());
        routeaddress = in.readParcelable(Route.class.getClassLoader());
        vehicle = in.readParcelable(Vehicle.class.getClassLoader());
        driver = in.readParcelable(UserInfo.class.getClassLoader());
        helper = in.readParcelable(UserInfo.class.getClassLoader());
        trip = in.readParcelable(Trip.class.getClassLoader());
        rvdhsmaps = in.createTypedArrayList(RouteMap.CREATOR);
        routeaddresses = in.createTypedArrayList(RouteMap.CREATOR);
        vehicledetails = in.createTypedArrayList(RouteMap.CREATOR);
        rvdhsmaprecords = in.createTypedArrayList(RouteMap.CREATOR);
        rvdhsmapaddresses = in.createTypedArrayList(RouteMap.CREATOR);
        student = in.readParcelable(Student.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(position);
        dest.writeString(pick_up_time);
        dest.writeString(drop_time);
        dest.writeString(address);
        dest.writeDouble(lat);
        dest.writeDouble(lang);
        dest.writeParcelable(route, flags);
        dest.writeParcelable(routeaddress, flags);
        dest.writeParcelable(vehicle, flags);
        dest.writeParcelable(driver, flags);
        dest.writeParcelable(helper, flags);
        dest.writeParcelable(trip, flags);
        dest.writeTypedList(rvdhsmaps);
        dest.writeTypedList(routeaddresses);
        dest.writeTypedList(vehicledetails);
        dest.writeTypedList(rvdhsmaprecords);
        dest.writeTypedList(rvdhsmapaddresses);
        dest.writeParcelable(student, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RouteMap> CREATOR = new Creator<RouteMap>() {
        @Override
        public RouteMap createFromParcel(Parcel in) {
            return new RouteMap(in);
        }

        @Override
        public RouteMap[] newArray(int size) {
            return new RouteMap[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public UserInfo getDriver() {
        return driver;
    }

    public void setDriver(UserInfo driver) {
        this.driver = driver;
    }

    public UserInfo getHelper() {
        return helper;
    }

    public void setHelper(UserInfo helper) {
        this.helper = helper;
    }

    public ArrayList<RouteMap> getRouteMaps() {
        return rvdhsmaps;
    }

    public void setRvdhsmaps(ArrayList<RouteMap> rvdhsmaps) {
        this.rvdhsmaps = rvdhsmaps;
    }

    public ArrayList<RouteMap> getRouteaddresses() {
        return routeaddresses;
    }

    public void setRouteaddresses(ArrayList<RouteMap> routeaddresses) {
        this.routeaddresses = routeaddresses;
    }

    public ArrayList<RouteMap> getVehicledetails() {
        return vehicledetails;
    }

    public void setVehicledetails(ArrayList<RouteMap> vehicledetails) {
        this.vehicledetails = vehicledetails;
    }

    public ArrayList<RouteMap> getRvdhsmaprecords() {
        return rvdhsmaprecords;
    }

    public void setRvdhsmaprecords(ArrayList<RouteMap> rvdhsmaprecords) {
        this.rvdhsmaprecords = rvdhsmaprecords;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getPick_up_time() {
        return pick_up_time;
    }

    public void setPick_up_time(String pick_up_time) {
        this.pick_up_time = pick_up_time;
    }

    public String getDrop_time() {
        return drop_time;
    }

    public void setDrop_time(String drop_time) {
        this.drop_time = drop_time;
    }

    public Route getRouteaddress() {
        return routeaddress;
    }

    public void setRouteaddress(Route routeaddress) {
        this.routeaddress = routeaddress;
    }

    public ArrayList<RouteMap> getRvdhsmapaddresses() {
        return rvdhsmapaddresses;
    }

    public void setRvdhsmapaddresses(ArrayList<RouteMap> rvdhsmapaddresses) {
        this.rvdhsmapaddresses = rvdhsmapaddresses;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public ArrayList<RouteMap> getRvdhsmaps() {
        return rvdhsmaps;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
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

    @Override
    public List<RouteMap> getChildList() {
        return getRvdhsmaprecords();
    }

    @Override
    public boolean isInitiallyExpanded() {
        return true;
    }

    @Override
    public ChildFooterItem getChildListFooter() {
        return null;
    }

    @Override
    public boolean expandableViewFooterEnable() {
        return false;
    }

    @Override
    public int getMaxChildCount() {
        return 0;
    }


}
