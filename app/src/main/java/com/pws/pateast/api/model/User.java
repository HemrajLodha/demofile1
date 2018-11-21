package com.pws.pateast.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by intel on 19-Apr-17.
 */

public class User extends Response<User> implements Parcelable {
    int id, masterId, secondary_lang, roleId, default_lang, userId, bcsMapId, boardId, classId, academicSessionId, is_notification;
    String email, password, user_type, servicePath, user_image, user_name, fullname, mobile, institute_name, salutation, date_format;
    User userdetails, dashboard;
    TeacherClass bcs;
    Language primaryLang;
    ArrayList<Ward> ward_list;
    ArrayList<Language> languages;
    ArrayList<Session> academicSessions;

    protected User(Parcel in) {
        id = in.readInt();
        masterId = in.readInt();
        secondary_lang = in.readInt();
        roleId = in.readInt();
        default_lang = in.readInt();
        userId = in.readInt();
        bcsMapId = in.readInt();
        boardId = in.readInt();
        classId = in.readInt();
        academicSessionId = in.readInt();
        is_notification = in.readInt();
        email = in.readString();
        password = in.readString();
        user_type = in.readString();
        servicePath = in.readString();
        user_image = in.readString();
        user_name = in.readString();
        fullname = in.readString();
        mobile = in.readString();
        institute_name = in.readString();
        salutation = in.readString();
        date_format = in.readString();
        userdetails = in.readParcelable(User.class.getClassLoader());
        dashboard = in.readParcelable(User.class.getClassLoader());
        bcs = in.readParcelable(TeacherClass.class.getClassLoader());
        primaryLang = in.readParcelable(Language.class.getClassLoader());
        ward_list = in.createTypedArrayList(Ward.CREATOR);
        languages = in.createTypedArrayList(Language.CREATOR);
        academicSessions = in.createTypedArrayList(Session.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(masterId);
        dest.writeInt(secondary_lang);
        dest.writeInt(roleId);
        dest.writeInt(default_lang);
        dest.writeInt(userId);
        dest.writeInt(bcsMapId);
        dest.writeInt(boardId);
        dest.writeInt(classId);
        dest.writeInt(academicSessionId);
        dest.writeInt(is_notification);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(user_type);
        dest.writeString(servicePath);
        dest.writeString(user_image);
        dest.writeString(user_name);
        dest.writeString(fullname);
        dest.writeString(mobile);
        dest.writeString(institute_name);
        dest.writeString(salutation);
        dest.writeString(date_format);
        dest.writeParcelable(userdetails, flags);
        dest.writeParcelable(dashboard, flags);
        dest.writeParcelable(bcs, flags);
        dest.writeParcelable(primaryLang, flags);
        dest.writeTypedList(ward_list);
        dest.writeTypedList(languages);
        dest.writeTypedList(academicSessions);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
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

    public int getSecondary_lang() {
        return secondary_lang;
    }

    public void setSecondary_lang(int secondary_lang) {
        this.secondary_lang = secondary_lang;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getDefault_lang() {
        return default_lang;
    }

    public void setDefault_lang(int default_lang) {
        this.default_lang = default_lang;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getServicePath() {
        return servicePath;
    }

    public void setServicePath(String servicePath) {
        this.servicePath = servicePath;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public User getUserdetails() {
        return userdetails;
    }

    public void setUserdetails(User userdetails) {
        this.userdetails = userdetails;
    }

    public ArrayList<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(ArrayList<Language> languages) {
        this.languages = languages;
    }

    public User getDashboard() {
        return dashboard;
    }

    public void setDashboard(User dashboard) {
        this.dashboard = dashboard;
    }

    public ArrayList<Session> getAcademicSessions() {
        return academicSessions;
    }

    public void setAcademicSessions(ArrayList<Session> academicSessions) {
        this.academicSessions = academicSessions;
    }

    public int getBcsMapId() {
        return bcsMapId;
    }

    public void setBcsMapId(int bcsMapId) {
        this.bcsMapId = bcsMapId;
    }

    public int getBoardId() {
        return boardId;
    }

    public void setBoardId(int boardId) {
        this.boardId = boardId;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public Language getPrimaryLang() {
        return primaryLang;
    }

    public void setPrimaryLang(Language primaryLang) {
        this.primaryLang = primaryLang;
    }

    public ArrayList<Ward> getWard_list() {
        return ward_list;
    }

    public void setWard_list(ArrayList<Ward> ward_list) {
        this.ward_list = ward_list;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getAcademicSessionId() {
        return academicSessionId;
    }

    public void setAcademicSessionId(int academicSessionId) {
        this.academicSessionId = academicSessionId;
    }

    public TeacherClass getBcs() {
        return bcs;
    }

    public void setBcs(TeacherClass bcs) {
        this.bcs = bcs;
    }

    public String getInstitute_name() {
        return institute_name;
    }

    public void setInstitute_name(String institute_name) {
        this.institute_name = institute_name;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public int getIs_notification() {
        return is_notification;
    }

    public void setIs_notification(int is_notification) {
        this.is_notification = is_notification;
    }

    public String getDate_format() {
        return date_format;
    }

    public void setDate_format(String date_format) {
        this.date_format = date_format;
    }
}
