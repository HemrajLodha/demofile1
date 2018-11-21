package com.pws.pateast.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.pws.pateast.enums.DownloadingStatus;

import java.util.ArrayList;

/**
 * Created by intel on 03-May-17.
 */

public class Assignment extends Response<ArrayList<Assignment>> implements Parcelable {
    int id, userId, masterId, academicSessionId, bcsMapId, subjectId, assignmentId, languageId;

    String start_date, end_date, assignment_file, assignment_type, assignment_file_name, createdAt, updatedAt, title, comment;

    String assignment_status;

    long assignment_size;
    ArrayList<Assignment> assignmentdetails;

    ArrayList<Tag> assignmentremarks;

    TeacherClass bcsmap;

    Subject subject;
    Institute institute;

    private int downloadStatus;
    private DownloadingStatus downloadingStatus;
    private long downloadId;
    private int itemDownloadPercent;
    private long lastEmittedDownloadPercent = -1;

    public Assignment() {
    }

    public Assignment(int id, long downloadId, DownloadingStatus downloadingStatus, int itemDownloadPercent, String assignment_file, String assignment_file_name) {
        this.id = id;
        this.assignment_file = assignment_file;
        this.downloadingStatus = downloadingStatus;
        this.downloadId = downloadId;
        this.itemDownloadPercent = itemDownloadPercent;
        this.assignment_file_name = assignment_file_name;
    }


    protected Assignment(Parcel in) {
        id = in.readInt();
        userId = in.readInt();
        masterId = in.readInt();
        academicSessionId = in.readInt();
        bcsMapId = in.readInt();
        subjectId = in.readInt();
        assignmentId = in.readInt();
        languageId = in.readInt();
        start_date = in.readString();
        end_date = in.readString();
        assignment_file = in.readString();
        assignment_type = in.readString();
        assignment_file_name = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        title = in.readString();
        comment = in.readString();
        assignment_status = in.readString();
        assignment_size = in.readLong();
        assignmentdetails = in.createTypedArrayList(Assignment.CREATOR);
        assignmentremarks = in.createTypedArrayList(Tag.CREATOR);
        bcsmap = in.readParcelable(TeacherClass.class.getClassLoader());
        subject = in.readParcelable(Subject.class.getClassLoader());
        downloadId = in.readLong();
        itemDownloadPercent = in.readInt();
        lastEmittedDownloadPercent = in.readLong();
        downloadStatus = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(userId);
        dest.writeInt(masterId);
        dest.writeInt(academicSessionId);
        dest.writeInt(bcsMapId);
        dest.writeInt(subjectId);
        dest.writeInt(assignmentId);
        dest.writeInt(languageId);
        dest.writeString(start_date);
        dest.writeString(end_date);
        dest.writeString(assignment_file);
        dest.writeString(assignment_type);
        dest.writeString(assignment_file_name);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeString(title);
        dest.writeString(comment);
        dest.writeString(assignment_status);
        dest.writeLong(assignment_size);
        dest.writeTypedList(assignmentdetails);
        dest.writeTypedList(assignmentremarks);
        dest.writeParcelable(bcsmap, flags);
        dest.writeParcelable(subject, flags);
        dest.writeLong(downloadId);
        dest.writeInt(itemDownloadPercent);
        dest.writeLong(lastEmittedDownloadPercent);
        dest.writeInt(downloadStatus);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Assignment> CREATOR = new Creator<Assignment>() {
        @Override
        public Assignment createFromParcel(Parcel in) {
            return new Assignment(in);
        }

        @Override
        public Assignment[] newArray(int size) {
            return new Assignment[size];
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

    public int getAcademicSessionId() {
        return academicSessionId;
    }

    public void setAcademicSessionId(int academicSessionId) {
        this.academicSessionId = academicSessionId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getAssignment_status() {
        return assignment_status;
    }

    public void setAssignment_status(String assignment_status) {
        this.assignment_status = assignment_status;
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getAssignment_file() {
        return assignment_file;
    }

    public void setAssignment_file(String assignment_file) {
        this.assignment_file = assignment_file;
    }

    public String getAssignment_type() {
        return assignment_type;
    }

    public void setAssignment_type(String assignment_type) {
        this.assignment_type = assignment_type;
    }

    public long getAssignment_size() {
        return assignment_size;
    }

    public void setAssignment_size(long assignment_size) {
        this.assignment_size = assignment_size;
    }

    public String getAssignment_file_name() {
        return assignment_file_name;
    }

    public void setAssignment_file_name(String assignment_file_name) {
        this.assignment_file_name = assignment_file_name;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ArrayList<Assignment> getAssignmentdetails() {
        return assignmentdetails;
    }

    public void setAssignmentdetails(ArrayList<Assignment> assignmentdetails) {
        this.assignmentdetails = assignmentdetails;
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

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Institute getInstitute() {
        return institute;
    }

    public void setInstitute(Institute institute) {
        this.institute = institute;
    }

    public DownloadingStatus getDownloadingStatus() {
        return downloadingStatus != null ? downloadingStatus : DownloadingStatus.NOT_DOWNLOADED;
    }

    public void setDownloadingStatus(DownloadingStatus downloadingStatus) {
        this.downloadingStatus = downloadingStatus;
    }

    public long getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(long downloadId) {
        this.downloadId = downloadId;
    }

    public int getItemDownloadPercent() {
        return itemDownloadPercent;
    }

    public void setItemDownloadPercent(int itemDownloadPercent) {
        this.itemDownloadPercent = itemDownloadPercent;
    }

    public long getLastEmittedDownloadPercent() {
        return lastEmittedDownloadPercent;
    }

    public void setLastEmittedDownloadPercent(long lastEmittedDownloadPercent) {
        this.lastEmittedDownloadPercent = lastEmittedDownloadPercent;
    }

    public int getDownloadStatus() {
        return downloadStatus;
    }

    public void setDownloadStatus(int downloadStatus) {
        this.downloadStatus = downloadStatus;
    }

    public ArrayList<Tag> getAssignmentremarks() {
        return assignmentremarks;
    }

    public void setAssignmentremarks(ArrayList<Tag> assignmentremarks) {
        this.assignmentremarks = assignmentremarks;
    }

    @Override
    public boolean equals(Object obj) {
        return getId() == ((Assignment) obj).getId();
    }
}
