package com.pws.pateast.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Events extends Response<List<Events>> implements Parcelable {
    int id, masterId, academicSessionId, users, eventId, languageId, bcsMapId;
    String start, end, createdAt, updatedAt, venue, title, details, instructions, milestone, dresscode, file;
    List<Events> eventdetails, eventrecords;


    protected Events(Parcel in) {
        id = in.readInt();
        masterId = in.readInt();
        academicSessionId = in.readInt();
        users = in.readInt();
        eventId = in.readInt();
        languageId = in.readInt();
        bcsMapId = in.readInt();
        start = in.readString();
        end = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        venue = in.readString();
        title = in.readString();
        details = in.readString();
        instructions = in.readString();
        milestone = in.readString();
        dresscode = in.readString();
        file = in.readString();
        eventdetails = in.createTypedArrayList(Events.CREATOR);
        eventrecords = in.createTypedArrayList(Events.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(masterId);
        dest.writeInt(academicSessionId);
        dest.writeInt(users);
        dest.writeInt(eventId);
        dest.writeInt(languageId);
        dest.writeInt(bcsMapId);
        dest.writeString(start);
        dest.writeString(end);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeString(venue);
        dest.writeString(title);
        dest.writeString(details);
        dest.writeString(instructions);
        dest.writeString(milestone);
        dest.writeString(dresscode);
        dest.writeString(file);
        dest.writeTypedList(eventdetails);
        dest.writeTypedList(eventrecords);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Events> CREATOR = new Creator<Events>() {
        @Override
        public Events createFromParcel(Parcel in) {
            return new Events(in);
        }

        @Override
        public Events[] newArray(int size) {
            return new Events[size];
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

    public int getAcademicSessionId() {
        return academicSessionId;
    }

    public void setAcademicSessionId(int academicSessionId) {
        this.academicSessionId = academicSessionId;
    }

    public int getUsers() {
        return users;
    }

    public void setUsers(int users) {
        this.users = users;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
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

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Events> getEventdetails() {
        return eventdetails;
    }

    public void setEventdetails(List<Events> eventdetails) {
        this.eventdetails = eventdetails;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public int getBcsMapId() {
        return bcsMapId;
    }

    public void setBcsMapId(int bcsMapId) {
        this.bcsMapId = bcsMapId;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getMilestone() {
        return milestone;
    }

    public void setMilestone(String milestone) {
        this.milestone = milestone;
    }

    public String getDresscode() {
        return dresscode;
    }

    public void setDresscode(String dresscode) {
        this.dresscode = dresscode;
    }

    public List<Events> getEventrecords() {
        return eventrecords;
    }

    public void setEventrecords(List<Events> eventrecords) {
        this.eventrecords = eventrecords;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
