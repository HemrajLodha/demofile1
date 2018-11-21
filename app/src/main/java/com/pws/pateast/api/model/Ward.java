package com.pws.pateast.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * @author : Hemraj
 *         Created by planet on 8/11/2017.
 */

public class Ward extends Response<ArrayList<Ward>> implements Parcelable {
    private int userId, masterId, academicSessionId, bcsMapId, studentId, default_lang, secondary_lang,boardId,classId;
    private String fullname, user_image, institute, section,user_type;
    @SerializedName("class")
    private String class_name;
    private Language primaryLang;
    private ArrayList<Language> languages;
    private User userInfo;

    protected Ward(Parcel in) {
        userId = in.readInt();
        masterId = in.readInt();
        academicSessionId = in.readInt();
        bcsMapId = in.readInt();
        studentId = in.readInt();
        default_lang = in.readInt();
        secondary_lang = in.readInt();
        boardId = in.readInt();
        classId = in.readInt();
        fullname = in.readString();
        user_image = in.readString();
        institute = in.readString();
        section = in.readString();
        class_name = in.readString();
        user_type = in.readString();
        primaryLang = in.readParcelable(Language.class.getClassLoader());
        languages = in.createTypedArrayList(Language.CREATOR);
        userInfo = in.readParcelable(User.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userId);
        dest.writeInt(masterId);
        dest.writeInt(academicSessionId);
        dest.writeInt(bcsMapId);
        dest.writeInt(studentId);
        dest.writeInt(default_lang);
        dest.writeInt(secondary_lang);
        dest.writeInt(boardId);
        dest.writeInt(classId);
        dest.writeString(fullname);
        dest.writeString(user_image);
        dest.writeString(institute);
        dest.writeString(section);
        dest.writeString(class_name);
        dest.writeString(user_type);
        dest.writeParcelable(primaryLang, flags);
        dest.writeTypedList(languages);
        dest.writeParcelable(userInfo, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Ward> CREATOR = new Creator<Ward>() {
        @Override
        public Ward createFromParcel(Parcel in) {
            return new Ward(in);
        }

        @Override
        public Ward[] newArray(int size) {
            return new Ward[size];
        }
    };

    public int getUserId() {
        return userId;
    }

    public int getMasterId() {
        return masterId;
    }

    public int getAcademicSessionId() {
        return academicSessionId;
    }

    public int getBcsMapId() {
        return bcsMapId;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getDefault_lang() {
        return default_lang;
    }

    public int getSecondary_lang() {
        return secondary_lang;
    }

    public int getBoardId() {
        return boardId;
    }

    public int getClassId() {
        return classId;
    }

    public String getFullname() {
        return fullname;
    }

    public String getUser_image() {
        return user_image;
    }

    public String getInstitute() {
        return institute;
    }

    public String getSection() {
        return section;
    }

    public String getClass_name() {
        return class_name;
    }

    public Language getPrimaryLang() {
        return primaryLang;
    }

    public ArrayList<Language> getLanguages() {
        return languages;
    }

    public User getUserInfo() {
        return userInfo;
    }

    public String getUser_type() {
        return user_type;
    }
}
