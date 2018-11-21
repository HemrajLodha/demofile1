package com.pws.pateast.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by planet on 8/2/2017.
 */

public class LeaveType implements Parcelable {
    int id,masterId,total_leaves,is_active,empLeaveTypeId,languageId;
    double balance;
    String name;
    ArrayList<LeaveType> empleavetypedetails;


    protected LeaveType(Parcel in) {
        id = in.readInt();
        masterId = in.readInt();
        total_leaves = in.readInt();
        is_active = in.readInt();
        empLeaveTypeId = in.readInt();
        languageId = in.readInt();
        balance = in.readDouble();
        name = in.readString();
        empleavetypedetails = in.createTypedArrayList(LeaveType.CREATOR);
    }

    public static final Creator<LeaveType> CREATOR = new Creator<LeaveType>() {
        @Override
        public LeaveType createFromParcel(Parcel in) {
            return new LeaveType(in);
        }

        @Override
        public LeaveType[] newArray(int size) {
            return new LeaveType[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMasterId() {
        return masterId;
    }

    public void setMasterId(int masterId) {
        this.masterId = masterId;
    }

    public int getTotal_leaves() {
        return total_leaves;
    }

    public void setTotal_leaves(int total_leaves) {
        this.total_leaves = total_leaves;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }

    public int getEmpLeaveTypeId() {
        return empLeaveTypeId;
    }

    public void setEmpLeaveTypeId(int empLeaveTypeId) {
        this.empLeaveTypeId = empLeaveTypeId;
    }

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<LeaveType> getEmpleavetypedetails() {
        return empleavetypedetails;
    }

    public void setEmpleavetypedetails(ArrayList<LeaveType> empleavetypedetails) {
        this.empleavetypedetails = empleavetypedetails;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(masterId);
        parcel.writeInt(total_leaves);
        parcel.writeInt(is_active);
        parcel.writeInt(empLeaveTypeId);
        parcel.writeInt(languageId);
        parcel.writeDouble(balance);
        parcel.writeString(name);
        parcel.writeTypedList(empleavetypedetails);
    }
}
