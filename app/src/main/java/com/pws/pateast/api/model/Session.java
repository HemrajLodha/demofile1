package com.pws.pateast.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by intel on 26-Apr-17.
 */

public class Session implements Parcelable {
    private int id;
    private String name, start_date, end_date;
    private ArrayList<Session> academicsessiondetails;

    protected Session(Parcel in) {
        id = in.readInt();
        name = in.readString();
        start_date = in.readString();
        end_date = in.readString();
        academicsessiondetails = in.createTypedArrayList(Session.CREATOR);
    }

    public static final Creator<Session> CREATOR = new Creator<Session>() {
        @Override
        public Session createFromParcel(Parcel in) {
            return new Session(in);
        }

        @Override
        public Session[] newArray(int size) {
            return new Session[size];
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

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public ArrayList<Session> getAcademicsessiondetails() {
        return academicsessiondetails;
    }

    public void setAcademicsessiondetails(ArrayList<Session> academicsessiondetails) {
        this.academicsessiondetails = academicsessiondetails;
    }

    public static String[] getNameArray(List<Session> sessions) {
        if (sessions == null)
            sessions = new ArrayList<>();
        String[] sessionArray = new String[sessions.size()];
        for (Session item : sessions) {
            sessionArray[sessions.indexOf(item)] = item.getAcademicsessiondetails().get(0).getName();
        }
        return sessionArray;
    }

    public static int getSelectedPosition(List<Session> sessions, int Id) {
        if (sessions == null)
            sessions = new ArrayList<>();
        int pos = -1;
        for (Session item : sessions) {
            if (item.getId() == Id) {
                pos = sessions.indexOf(item);
                break;
            }
        }
        return pos;
    }

    public static Session getSelectedSession(List<Session> sessions, int Id) {
        if (sessions == null)
            sessions = new ArrayList<>();
        Session session = null;
        for (Session item : sessions) {
            if (item.getId() == Id) {
                session = item;
                break;
            }
        }
        return session;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(start_date);
        parcel.writeString(end_date);
        parcel.writeTypedList(academicsessiondetails);
    }
}
