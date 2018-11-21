package com.pws.pateast.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by intel on 06-Jul-17.
 */

public class ClassSchedule extends Response<ClassSchedule> implements Parcelable
{
    int id,bcsMapId,academicSessionId,classteacherId,masterId,is_active,period_no,period_duration;
    String start_time,break_duration_1,after_period_1,break_duration_2,after_period_2,weekday,createdAt,updatedAt;

    ArrayList<Schedule> timetableallocations;
    TeacherClass bcsmap;
    UserInfo teacher;

    protected ClassSchedule(Parcel in) {
        id = in.readInt();
        bcsMapId = in.readInt();
        academicSessionId = in.readInt();
        classteacherId = in.readInt();
        masterId = in.readInt();
        is_active = in.readInt();
        period_no = in.readInt();
        period_duration = in.readInt();
        start_time = in.readString();
        break_duration_1 = in.readString();
        after_period_1 = in.readString();
        break_duration_2 = in.readString();
        after_period_2 = in.readString();
        weekday = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        timetableallocations = in.createTypedArrayList(Schedule.CREATOR);
        bcsmap = in.readParcelable(TeacherClass.class.getClassLoader());
        teacher = in.readParcelable(UserInfo.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(bcsMapId);
        dest.writeInt(academicSessionId);
        dest.writeInt(classteacherId);
        dest.writeInt(masterId);
        dest.writeInt(is_active);
        dest.writeInt(period_no);
        dest.writeInt(period_duration);
        dest.writeString(start_time);
        dest.writeString(break_duration_1);
        dest.writeString(after_period_1);
        dest.writeString(break_duration_2);
        dest.writeString(after_period_2);
        dest.writeString(weekday);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeTypedList(timetableallocations);
        dest.writeParcelable(bcsmap, flags);
        dest.writeParcelable(teacher, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ClassSchedule> CREATOR = new Creator<ClassSchedule>() {
        @Override
        public ClassSchedule createFromParcel(Parcel in) {
            return new ClassSchedule(in);
        }

        @Override
        public ClassSchedule[] newArray(int size) {
            return new ClassSchedule[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBcsMapId() {
        return bcsMapId;
    }

    public void setBcsMapId(int bcsMapId) {
        this.bcsMapId = bcsMapId;
    }

    public int getAcademicSessionId() {
        return academicSessionId;
    }

    public void setAcademicSessionId(int academicSessionId) {
        this.academicSessionId = academicSessionId;
    }

    public int getClassteacherId() {
        return classteacherId;
    }

    public void setClassteacherId(int classteacherId) {
        this.classteacherId = classteacherId;
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

    public int getPeriod_no() {
        return period_no;
    }

    public void setPeriod_no(int period_no) {
        this.period_no = period_no;
    }

    public int getPeriod_duration() {
        return period_duration;
    }

    public void setPeriod_duration(int period_duration) {
        this.period_duration = period_duration;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getBreak_duration_1() {
        return break_duration_1;
    }

    public void setBreak_duration_1(String break_duration_1) {
        this.break_duration_1 = break_duration_1;
    }

    public String getAfter_period_1() {
        return after_period_1;
    }

    public void setAfter_period_1(String after_period_1) {
        this.after_period_1 = after_period_1;
    }

    public String getBreak_duration_2() {
        return break_duration_2;
    }

    public void setBreak_duration_2(String break_duration_2) {
        this.break_duration_2 = break_duration_2;
    }

    public String getAfter_period_2() {
        return after_period_2;
    }

    public void setAfter_period_2(String after_period_2) {
        this.after_period_2 = after_period_2;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
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

    public ArrayList<Schedule> getTimetableallocations() {
        return timetableallocations;
    }

    public void setTimetableallocations(ArrayList<Schedule> timetableallocations) {
        this.timetableallocations = timetableallocations;
    }

    public TeacherClass getBcsmap() {
        return bcsmap;
    }

    public void setBcsmap(TeacherClass bcsmap) {
        this.bcsmap = bcsmap;
    }

    public UserInfo getTeacher() {
        return teacher;
    }

    public void setTeacher(UserInfo teacher) {
        this.teacher = teacher;
    }
}
