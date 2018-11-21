package com.pws.pateast.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by intel on 13-Sep-17.
 */

public class Trip implements Parcelable {
    int id, status, rvdhsmapId, tripId, studentId, type;

    Student student;
    RouteMap routeMap;
    ArrayList<RouteMap> routeMaps;
    ArrayList<Trip> triprecords;

    protected Trip(Parcel in) {
        id = in.readInt();
        status = in.readInt();
        rvdhsmapId = in.readInt();
        tripId = in.readInt();
        studentId = in.readInt();
        type = in.readInt();
        student = in.readParcelable(Student.class.getClassLoader());
        routeMap = in.readParcelable(RouteMap.class.getClassLoader());
        routeMaps = in.createTypedArrayList(RouteMap.CREATOR);
        triprecords = in.createTypedArrayList(Trip.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(status);
        dest.writeInt(rvdhsmapId);
        dest.writeInt(tripId);
        dest.writeInt(studentId);
        dest.writeInt(type);
        dest.writeParcelable(student, flags);
        dest.writeParcelable(routeMap, flags);
        dest.writeTypedList(routeMaps);
        dest.writeTypedList(triprecords);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Trip> CREATOR = new Creator<Trip>() {
        @Override
        public Trip createFromParcel(Parcel in) {
            return new Trip(in);
        }

        @Override
        public Trip[] newArray(int size) {
            return new Trip[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getRouteId() {
        return rvdhsmapId;
    }

    public void setRvdhsmapId(int rvdhsmapId) {
        this.rvdhsmapId = rvdhsmapId;
    }

    public ArrayList<Trip> getTriprecords() {
        return triprecords;
    }

    public void setTriprecords(ArrayList<Trip> triprecords) {
        this.triprecords = triprecords;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public RouteMap getRouteMap() {
        return routeMap;
    }

    public void setRouteMap(RouteMap routeMap) {
        this.routeMap = routeMap;
    }

    public ArrayList<RouteMap> getRouteMaps() {
        return routeMaps;
    }

    public void setRouteMaps(ArrayList<RouteMap> routeMaps) {
        this.routeMaps = routeMaps;
    }
}
