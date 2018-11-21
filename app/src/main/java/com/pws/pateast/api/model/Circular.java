package com.pws.pateast.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Circular extends Response<List<Circular>> implements Parcelable {
    int id;
    String number, date, file, details, title;
    List<Circular> circulardetails;

    protected Circular(Parcel in) {
        id = in.readInt();
        number = in.readString();
        date = in.readString();
        file = in.readString();
        details = in.readString();
        title = in.readString();
        circulardetails = in.createTypedArrayList(Circular.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(number);
        dest.writeString(date);
        dest.writeString(file);
        dest.writeString(details);
        dest.writeString(title);
        dest.writeTypedList(circulardetails);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Circular> CREATOR = new Creator<Circular>() {
        @Override
        public Circular createFromParcel(Parcel in) {
            return new Circular(in);
        }

        @Override
        public Circular[] newArray(int size) {
            return new Circular[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Circular> getCirculardetails() {
        return circulardetails;
    }

    public void setCirculardetails(List<Circular> circulardetails) {
        this.circulardetails = circulardetails;
    }
}
