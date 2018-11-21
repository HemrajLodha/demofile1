package com.pws.pateast.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by intel on 24-Apr-17.
 */

public class Classes implements Parcelable
{
    String createdAt,updatedAt;
    int id,masterId,is_active,classId,languageId;
    String name;

    ArrayList<Classes> classesdetails;


    protected Classes(Parcel in) {
        createdAt = in.readString();
        updatedAt = in.readString();
        id = in.readInt();
        masterId = in.readInt();
        is_active = in.readInt();
        classId = in.readInt();
        languageId = in.readInt();
        name = in.readString();
        classesdetails = in.createTypedArrayList(Classes.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeInt(id);
        dest.writeInt(masterId);
        dest.writeInt(is_active);
        dest.writeInt(classId);
        dest.writeInt(languageId);
        dest.writeString(name);
        dest.writeTypedList(classesdetails);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Classes> CREATOR = new Creator<Classes>() {
        @Override
        public Classes createFromParcel(Parcel in) {
            return new Classes(in);
        }

        @Override
        public Classes[] newArray(int size) {
            return new Classes[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getMasterId() {
        return masterId;
    }

    public void setMasterId(int masterId) {
        this.masterId = masterId;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public ArrayList<Classes> getClassesdetails() {
        return classesdetails;
    }

    public void setClassesdetails(ArrayList<Classes> classesdetails) {
        this.classesdetails = classesdetails;
    }
}
