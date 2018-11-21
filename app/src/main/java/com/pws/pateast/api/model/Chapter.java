package com.pws.pateast.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by intel on 24-Jan-18.
 */

public class Chapter extends Response<ArrayList<Chapter>> implements Parcelable {
    int id;
    String name;
    ArrayList<Chapter> lmschapterdetails;

    protected Chapter(Parcel in) {
        id = in.readInt();
        name = in.readString();
        lmschapterdetails = in.createTypedArrayList(Chapter.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeTypedList(lmschapterdetails);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Chapter> CREATOR = new Creator<Chapter>() {
        @Override
        public Chapter createFromParcel(Parcel in) {
            return new Chapter(in);
        }

        @Override
        public Chapter[] newArray(int size) {
            return new Chapter[size];
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

    public ArrayList<Chapter> getLmschapterdetails() {
        return lmschapterdetails;
    }

    public void setLmschapterdetails(ArrayList<Chapter> lmschapterdetails) {
        this.lmschapterdetails = lmschapterdetails;
    }
}
