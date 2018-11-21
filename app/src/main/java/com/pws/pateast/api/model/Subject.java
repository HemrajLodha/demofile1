package com.pws.pateast.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by intel on 21-Apr-17.
 */

public class Subject extends Response<ArrayList<Subject>> implements Parcelable
{
    int id,subjectId,languageId,masterId,userId,is_active;
    String name,alias,createdAt,updatedAt;

    Subject subject;
    ArrayList<Subject> subjectdetails;
    Institute institute;

    public Subject(int subjectId) {
        this.subjectId = subjectId;
    }

    protected Subject(Parcel in) {
        id = in.readInt();
        subjectId = in.readInt();
        languageId = in.readInt();
        masterId = in.readInt();
        userId = in.readInt();
        is_active = in.readInt();
        name = in.readString();
        alias = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        subject = in.readParcelable(Subject.class.getClassLoader());
        subjectdetails = in.createTypedArrayList(Subject.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(subjectId);
        dest.writeInt(languageId);
        dest.writeInt(masterId);
        dest.writeInt(userId);
        dest.writeInt(is_active);
        dest.writeString(name);
        dest.writeString(alias);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeParcelable(subject, flags);
        dest.writeTypedList(subjectdetails);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Subject> CREATOR = new Creator<Subject>() {
        @Override
        public Subject createFromParcel(Parcel in) {
            return new Subject(in);
        }

        @Override
        public Subject[] newArray(int size) {
            return new Subject[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public int getMasterId() {
        return masterId;
    }

    public void setMasterId(int masterId) {
        this.masterId = masterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
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

    public ArrayList<Subject> getSubjectdetails() {
        return subjectdetails;
    }

    public void setSubjectdetails(ArrayList<Subject> subjectdetails) {
        this.subjectdetails = subjectdetails;
    }

    public Institute getInstitute() {
        return institute;
    }

    public void setInstitute(Institute institute) {
        this.institute = institute;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    @Override
    public boolean equals(Object obj)
    {
        return getSubjectId() == ((Subject)obj).getSubjectId();
    }

    public static Subject getSubject(int subjectId)
    {
        if(subjectId == 0)
            return null;

        return new Subject(subjectId);
    }
}
