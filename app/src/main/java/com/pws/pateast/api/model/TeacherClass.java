package com.pws.pateast.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by intel on 20-Apr-17.
 */

public class TeacherClass extends Response<ArrayList<TeacherClass>> implements Parcelable, Cloneable {
    int id, bcsMapId, classId, boardId, academicSessionId, sectionId, classteacherId, masterId, is_active, studentrecord;
    String createdAt, updatedAt;

    ArrayList<Schedule> timetableallocations;
    Schedule timetable;
    TeacherClass bcsmap;
    Section section;
    Board board;
    @SerializedName("class")
    Classes classes;
    UserInfo teacher;
    boolean isOpened;

    public TeacherClass() {

    }

    public TeacherClass(int bcsMapId) {
        this.bcsMapId = bcsMapId;
    }

    protected TeacherClass(Parcel in) {
        id = in.readInt();
        bcsMapId = in.readInt();
        classId = in.readInt();
        boardId = in.readInt();
        academicSessionId = in.readInt();
        sectionId = in.readInt();
        classteacherId = in.readInt();
        masterId = in.readInt();
        is_active = in.readInt();
        studentrecord = in.readInt();
        createdAt = in.readString();
        updatedAt = in.readString();
        timetableallocations = in.createTypedArrayList(Schedule.CREATOR);
        timetable = in.readParcelable(Schedule.class.getClassLoader());
        bcsmap = in.readParcelable(TeacherClass.class.getClassLoader());
        section = in.readParcelable(Section.class.getClassLoader());
        board = in.readParcelable(Board.class.getClassLoader());
        classes = in.readParcelable(Classes.class.getClassLoader());
        teacher = in.readParcelable(UserInfo.class.getClassLoader());
        isOpened = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(bcsMapId);
        dest.writeInt(classId);
        dest.writeInt(boardId);
        dest.writeInt(academicSessionId);
        dest.writeInt(sectionId);
        dest.writeInt(classteacherId);
        dest.writeInt(masterId);
        dest.writeInt(is_active);
        dest.writeInt(studentrecord);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeTypedList(timetableallocations);
        dest.writeParcelable(timetable, flags);
        dest.writeParcelable(bcsmap, flags);
        dest.writeParcelable(section, flags);
        dest.writeParcelable(board, flags);
        dest.writeParcelable(classes, flags);
        dest.writeParcelable(teacher, flags);
        dest.writeByte((byte) (isOpened ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TeacherClass> CREATOR = new Creator<TeacherClass>() {
        @Override
        public TeacherClass createFromParcel(Parcel in) {
            return new TeacherClass(in);
        }

        @Override
        public TeacherClass[] newArray(int size) {
            return new TeacherClass[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public int getBoardId() {
        return boardId;
    }

    public void setBoardId(int boardId) {
        this.boardId = boardId;
    }

    public int getAcademicSessionId() {
        return academicSessionId;
    }

    public void setAcademicSessionId(int academicSessionId) {
        this.academicSessionId = academicSessionId;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
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

    public int getStudentrecord() {
        return studentrecord;
    }

    public void setStudentrecord(int studentrecord) {
        this.studentrecord = studentrecord;
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

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Classes getClasses() {
        return classes;
    }

    public void setClasses(Classes classes) {
        this.classes = classes;
    }

    public UserInfo getTeacher() {
        return teacher;
    }

    public void setTeacher(UserInfo teacher) {
        this.teacher = teacher;
    }

    public int getBcsMapId() {
        return bcsMapId;
    }

    public void setBcsMapId(int bcsMapId) {
        this.bcsMapId = bcsMapId;
    }

    public TeacherClass getBcsmap() {
        return bcsmap;
    }

    public void setBcsmap(TeacherClass bcsmap) {
        this.bcsmap = bcsmap;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public void setOpened(boolean opened) {
        isOpened = opened;
    }

    @Override
    public boolean equals(Object obj) {
        return getBcsMapId() == ((TeacherClass) obj).getBcsMapId();
    }

    public Schedule getTimetable() {
        return timetable;
    }

    public void setTimetable(Schedule timetable) {
        this.timetable = timetable;
    }

    public static TeacherClass getTeacherClass(int bcsMapId) {
        if (bcsMapId == 0)
            return null;
        return new TeacherClass(bcsMapId);
    }

    @Override
    public TeacherClass clone() {
        try {
            return (TeacherClass) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
