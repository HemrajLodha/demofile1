package com.pws.pateast.api.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.pws.pateast.provider.table.UserChats;

import java.util.ArrayList;

/**
 * Created by intel on 21-Apr-17.
 */

public class UserInfo extends Response<UserInfo> implements Parcelable {
    int id, userId, masterId, countryId, stateId, cityId, teacherId, languageId, parentId, roleId, default_lang, secondary_lang, is_active;
    String fullname, join_date, dob, createdAt, updatedAt, last_qualification, address, mobile,
            alternate_mobile, salutation, email, backup_email, user_name, password, govt_identity_number,
            govtIdentityId, govt_identity_expiry, user_image, govt_identity_image, user_type, institute_name, name, alias;

    ArrayList<UserInfo> teacherdetails, userdetails, institutedetails;
    ArrayList<Subject> teachersubjects;

    Role role;
    UserInfo user, teacher, institute;
    Country country;
    State state;
    City city;
    Student student;

    public UserInfo(Cursor cursor) {
        fullname = cursor.getString(UserChats.INDEX_COLUMN_USER_NAME);
        user_image = cursor.getString(UserChats.INDEX_COLUMN_USER_IMAGE);
        user_type = cursor.getString(UserChats.INDEX_COLUMN_USER_TYPE);
    }

    public UserInfo() {

    }

    protected UserInfo(Parcel in) {
        id = in.readInt();
        userId = in.readInt();
        masterId = in.readInt();
        countryId = in.readInt();
        stateId = in.readInt();
        cityId = in.readInt();
        teacherId = in.readInt();
        languageId = in.readInt();
        parentId = in.readInt();
        roleId = in.readInt();
        default_lang = in.readInt();
        secondary_lang = in.readInt();
        is_active = in.readInt();
        fullname = in.readString();
        join_date = in.readString();
        dob = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        last_qualification = in.readString();
        address = in.readString();
        mobile = in.readString();
        alternate_mobile = in.readString();
        salutation = in.readString();
        email = in.readString();
        backup_email = in.readString();
        user_name = in.readString();
        password = in.readString();
        govt_identity_number = in.readString();
        govtIdentityId = in.readString();
        govt_identity_expiry = in.readString();
        user_image = in.readString();
        govt_identity_image = in.readString();
        user_type = in.readString();
        institute_name = in.readString();
        name = in.readString();
        alias = in.readString();
        teacherdetails = in.createTypedArrayList(UserInfo.CREATOR);
        userdetails = in.createTypedArrayList(UserInfo.CREATOR);
        institutedetails = in.createTypedArrayList(UserInfo.CREATOR);
        teachersubjects = in.createTypedArrayList(Subject.CREATOR);
        user = in.readParcelable(UserInfo.class.getClassLoader());
        teacher = in.readParcelable(UserInfo.class.getClassLoader());
        institute = in.readParcelable(UserInfo.class.getClassLoader());
        student = in.readParcelable(Student.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(userId);
        dest.writeInt(masterId);
        dest.writeInt(countryId);
        dest.writeInt(stateId);
        dest.writeInt(cityId);
        dest.writeInt(teacherId);
        dest.writeInt(languageId);
        dest.writeInt(parentId);
        dest.writeInt(roleId);
        dest.writeInt(default_lang);
        dest.writeInt(secondary_lang);
        dest.writeInt(is_active);
        dest.writeString(fullname);
        dest.writeString(join_date);
        dest.writeString(dob);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeString(last_qualification);
        dest.writeString(address);
        dest.writeString(mobile);
        dest.writeString(alternate_mobile);
        dest.writeString(salutation);
        dest.writeString(email);
        dest.writeString(backup_email);
        dest.writeString(user_name);
        dest.writeString(password);
        dest.writeString(govt_identity_number);
        dest.writeString(govtIdentityId);
        dest.writeString(govt_identity_expiry);
        dest.writeString(user_image);
        dest.writeString(govt_identity_image);
        dest.writeString(user_type);
        dest.writeString(institute_name);
        dest.writeString(name);
        dest.writeString(alias);
        dest.writeTypedList(teacherdetails);
        dest.writeTypedList(userdetails);
        dest.writeTypedList(institutedetails);
        dest.writeTypedList(teachersubjects);
        dest.writeParcelable(user, flags);
        dest.writeParcelable(teacher, flags);
        dest.writeParcelable(institute, flags);
        dest.writeParcelable(student, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
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

    public int getSecondary_lang() {
        return secondary_lang;
    }

    public void setSecondary_lang(int secondary_lang) {
        this.secondary_lang = secondary_lang;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getJoin_date() {
        return join_date;
    }

    public void setJoin_date(String join_date) {
        this.join_date = join_date;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
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

    public String getLast_qualification() {
        return last_qualification;
    }

    public void setLast_qualification(String last_qualification) {
        this.last_qualification = last_qualification;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAlternate_mobile() {
        return alternate_mobile;
    }

    public void setAlternate_mobile(String alternate_mobile) {
        this.alternate_mobile = alternate_mobile;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBackup_email() {
        return backup_email;
    }

    public void setBackup_email(String backup_email) {
        this.backup_email = backup_email;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGovt_identity_number() {
        return govt_identity_number;
    }

    public void setGovt_identity_number(String govt_identity_number) {
        this.govt_identity_number = govt_identity_number;
    }

    public String getGovtIdentityId() {
        return govtIdentityId;
    }

    public void setGovtIdentityId(String govtIdentityId) {
        this.govtIdentityId = govtIdentityId;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getGovt_identity_image() {
        return govt_identity_image;
    }

    public void setGovt_identity_image(String govt_identity_image) {
        this.govt_identity_image = govt_identity_image;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public ArrayList<UserInfo> getTeacherdetails() {
        return teacherdetails;
    }

    public void setTeacherdetails(ArrayList<UserInfo> teacherdetails) {
        this.teacherdetails = teacherdetails;
    }

    public ArrayList<UserInfo> getUserdetails() {
        return userdetails;
    }

    public void setUserdetails(ArrayList<UserInfo> userdetails) {
        this.userdetails = userdetails;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public UserInfo getTeacher() {
        return teacher;
    }

    public void setTeacher(UserInfo teacher) {
        this.teacher = teacher;
    }

    public ArrayList<Subject> getTeachersubjects() {
        return teachersubjects;
    }

    public void setTeachersubjects(ArrayList<Subject> teachersubjects) {
        this.teachersubjects = teachersubjects;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getInstitute_name() {
        return institute_name;
    }

    public void setInstitute_name(String institute_name) {
        this.institute_name = institute_name;
    }

    public String getGovt_identity_expiry() {
        return govt_identity_expiry;
    }

    public void setGovt_identity_expiry(String govt_identity_expiry) {
        this.govt_identity_expiry = govt_identity_expiry;
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

    public ArrayList<UserInfo> getInstitutedetails() {
        return institutedetails;
    }

    public void setInstitutedetails(ArrayList<UserInfo> institutedetails) {
        this.institutedetails = institutedetails;
    }

    public UserInfo getInstitute() {
        return institute;
    }

    public void setInstitute(UserInfo institute) {
        this.institute = institute;
    }
}
