package com.pws.pateast.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by intel on 24-Apr-17.
 */

public class Section implements Parcelable
{
    String createdAt,updatedAt;
    int id,userId,masterId,is_active,languageId,sectionId;
    String name;

    ArrayList<Section> sectiondetails;


    protected Section(Parcel in) {
        createdAt = in.readString();
        updatedAt = in.readString();
        id = in.readInt();
        userId = in.readInt();
        masterId = in.readInt();
        is_active = in.readInt();
        languageId = in.readInt();
        sectionId = in.readInt();
        name = in.readString();
        sectiondetails = in.createTypedArrayList(Section.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeInt(id);
        dest.writeInt(userId);
        dest.writeInt(masterId);
        dest.writeInt(is_active);
        dest.writeInt(languageId);
        dest.writeInt(sectionId);
        dest.writeString(name);
        dest.writeTypedList(sectiondetails);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Section> CREATOR = new Creator<Section>() {
        @Override
        public Section createFromParcel(Parcel in) {
            return new Section(in);
        }

        @Override
        public Section[] newArray(int size) {
            return new Section[size];
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public ArrayList<Section> getSectiondetails() {
        return sectiondetails;
    }

    public void setSectiondetails(ArrayList<Section> sectiondetails) {
        this.sectiondetails = sectiondetails;
    }
}
