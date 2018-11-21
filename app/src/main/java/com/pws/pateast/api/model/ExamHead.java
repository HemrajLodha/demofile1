package com.pws.pateast.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by intel on 16-May-17.
 */

public class ExamHead extends Response<ArrayList<ExamHead>> implements Parcelable
{
    int id,masterId,is_active,examheadId,languageId;
    String createdAt,updatedAt,name;

    ArrayList<ExamHead> examheaddetails;

    protected ExamHead(Parcel in) {
        id = in.readInt();
        masterId = in.readInt();
        is_active = in.readInt();
        examheadId = in.readInt();
        languageId = in.readInt();
        createdAt = in.readString();
        updatedAt = in.readString();
        name = in.readString();
        examheaddetails = in.createTypedArrayList(ExamHead.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(masterId);
        dest.writeInt(is_active);
        dest.writeInt(examheadId);
        dest.writeInt(languageId);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeString(name);
        dest.writeTypedList(examheaddetails);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ExamHead> CREATOR = new Creator<ExamHead>() {
        @Override
        public ExamHead createFromParcel(Parcel in) {
            return new ExamHead(in);
        }

        @Override
        public ExamHead[] newArray(int size) {
            return new ExamHead[size];
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

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }

    public int getExamheadId() {
        return examheadId;
    }

    public void setExamheadId(int examheadId) {
        this.examheadId = examheadId;
    }

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ExamHead> getExamheaddetails() {
        return examheaddetails;
    }

    public void setExamheaddetails(ArrayList<ExamHead> examheaddetails) {
        this.examheaddetails = examheaddetails;
    }

    public static String[] getNameArray(List<Schedule> examHeads) {
        if (examHeads == null)
            examHeads = new ArrayList<>();
        String[] examHeadArray = new String[examHeads.size()];
        for (Schedule item : examHeads) {
            examHeadArray[examHeads.indexOf(item)] = item.getExamhead().getExamheaddetails().get(0).getName();
        }
        return examHeadArray;
    }

    public static int getSelectedPosition(List<Schedule> examHeads, int Id) {
        if (examHeads == null)
            examHeads = new ArrayList<>();
        int pos = -1;
        for (Schedule item : examHeads)
        {
            if (item.getExamheadId() == Id) {
                pos = examHeads.indexOf(item);
                break;
            }
        }
        return pos;
    }
}
