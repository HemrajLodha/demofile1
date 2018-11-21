package com.pws.pateast.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by intel on 13-Sep-17.
 */

public class Vehicle implements Parcelable
{
    int id,total_seats;
    String name,number,vehicle_type,vehicle_image;

    ArrayList<Vehicle> vehicledetails;

    protected Vehicle(Parcel in) {
        id = in.readInt();
        total_seats = in.readInt();
        name = in.readString();
        number = in.readString();
        vehicle_type = in.readString();
        vehicle_image = in.readString();
        vehicledetails = in.createTypedArrayList(Vehicle.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(total_seats);
        dest.writeString(name);
        dest.writeString(number);
        dest.writeString(vehicle_type);
        dest.writeString(vehicle_image);
        dest.writeTypedList(vehicledetails);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Vehicle> CREATOR = new Creator<Vehicle>() {
        @Override
        public Vehicle createFromParcel(Parcel in) {
            return new Vehicle(in);
        }

        @Override
        public Vehicle[] newArray(int size) {
            return new Vehicle[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotal_seats() {
        return total_seats;
    }

    public void setTotal_seats(int total_seats) {
        this.total_seats = total_seats;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public String getVehicle_image() {
        return vehicle_image;
    }

    public void setVehicle_image(String vehicle_image) {
        this.vehicle_image = vehicle_image;
    }

    public ArrayList<Vehicle> getVehicledetails() {
        return vehicledetails;
    }

    public void setVehicledetails(ArrayList<Vehicle> vehicledetails) {
        this.vehicledetails = vehicledetails;
    }
}
