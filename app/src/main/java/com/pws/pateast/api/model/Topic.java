package com.pws.pateast.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.pws.pateast.enums.LMSCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by intel on 24-Jan-18.
 */

public class Topic extends Response<ArrayList<Topic>> implements Parcelable {
    int id, lmstopicId,lmsType;
    String name, content, type;
    List<Topic> lmstopicdetails, lmstopicdocuments;

    public Topic(int lmsType, List<Topic> studyMaterial){
        setLmsType(lmsType);
        setLmstopicdocuments(studyMaterial);
    }

    protected Topic(Parcel in) {
        id = in.readInt();
        lmstopicId = in.readInt();
        lmsType = in.readInt();
        name = in.readString();
        content = in.readString();
        type = in.readString();
        lmstopicdetails = in.createTypedArrayList(Topic.CREATOR);
        lmstopicdocuments = in.createTypedArrayList(Topic.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(lmstopicId);
        dest.writeInt(lmsType);
        dest.writeString(name);
        dest.writeString(content);
        dest.writeString(type);
        dest.writeTypedList(lmstopicdetails);
        dest.writeTypedList(lmstopicdocuments);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Topic> CREATOR = new Creator<Topic>() {
        @Override
        public Topic createFromParcel(Parcel in) {
            return new Topic(in);
        }

        @Override
        public Topic[] newArray(int size) {
            return new Topic[size];
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

    public List<Topic> getLmstopicdetails() {
        return lmstopicdetails;
    }

    public void setLmstopicdetails(List<Topic> lmstopicdetails) {
        this.lmstopicdetails = lmstopicdetails;
    }

    public int getLmstopicId() {
        return lmstopicId;
    }

    public void setLmstopicId(int lmstopicId) {
        this.lmstopicId = lmstopicId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Topic> getLmstopicdocuments() {
        return lmstopicdocuments;
    }

    public void setLmstopicdocuments(List<Topic> lmstopicdocuments) {
        this.lmstopicdocuments = lmstopicdocuments;
    }

    public int getLmsType() {
        return lmsType;
    }

    public void setLmsType(int lmsType) {
        this.lmsType = lmsType;
    }
}
