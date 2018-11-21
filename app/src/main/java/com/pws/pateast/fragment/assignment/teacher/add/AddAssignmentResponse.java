package com.pws.pateast.fragment.assignment.teacher.add;

import android.os.Parcel;
import android.os.Parcelable;

import com.pws.pateast.api.model.Response;
import com.pws.pateast.enums.DownloadingStatus;

/**
 * Created by planet on 5/12/2017.
 */

public class AddAssignmentResponse extends Response<AddAssignmentResponse> implements Parcelable {

    int id,userId,masterId,academicSessionId,classId,boardId,sectionId,subjectId,is_active,assignmentId,languageId;

    String start_date,end_date,assignment_file,assignment_type,assignment_file_name,createdAt,updatedAt,title,comment;
    Long assignment_size;
    AddAssignmentResponse assignmentdetails;

    private DownloadingStatus downloadingStatus;
    private long downloadId;
    private int itemDownloadPercent;
    private long lastEmittedDownloadPercent = -1;

    public AddAssignmentResponse(int id, String assignment_file, DownloadingStatus downloadingStatus, long downloadId, int itemDownloadPercent) {
        this.id = id;
        this.assignment_file = assignment_file;
        this.downloadingStatus = downloadingStatus;
        this.downloadId = downloadId;
        this.itemDownloadPercent = itemDownloadPercent;
    }

    protected AddAssignmentResponse(Parcel in) {
        id = in.readInt();
        userId = in.readInt();
        masterId = in.readInt();
        academicSessionId = in.readInt();
        classId = in.readInt();
        boardId = in.readInt();
        sectionId = in.readInt();
        subjectId = in.readInt();
        is_active = in.readInt();
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
        assignmentdetails = in.readParcelable(AddAssignmentResponse.class.getClassLoader());
        downloadId = in.readLong();
        itemDownloadPercent = in.readInt();
        lastEmittedDownloadPercent = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(userId);
        dest.writeInt(masterId);
        dest.writeInt(academicSessionId);
        dest.writeInt(classId);
        dest.writeInt(boardId);
        dest.writeInt(sectionId);
        dest.writeInt(subjectId);
        dest.writeInt(is_active);
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
        dest.writeParcelable(assignmentdetails, flags);
        dest.writeLong(downloadId);
        dest.writeInt(itemDownloadPercent);
        dest.writeLong(lastEmittedDownloadPercent);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AddAssignmentResponse> CREATOR = new Creator<AddAssignmentResponse>() {
        @Override
        public AddAssignmentResponse createFromParcel(Parcel in) {
            return new AddAssignmentResponse(in);
        }

        @Override
        public AddAssignmentResponse[] newArray(int size) {
            return new AddAssignmentResponse[size];
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

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
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

    public Long getAssignment_size() {
        return assignment_size;
    }

    public void setAssignment_size(Long assignment_size) {
        this.assignment_size = assignment_size;
    }

    public AddAssignmentResponse getAssignmentdetails() {
        return assignmentdetails;
    }

    public void setAssignmentdetails(AddAssignmentResponse assignmentdetails) {
        this.assignmentdetails = assignmentdetails;
    }

    public DownloadingStatus getDownloadingStatus() {
        return downloadingStatus;
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
}
