package com.pws.pateast.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by intel on 09-Feb-18.
 */

public class Complaint extends Response<List<Complaint>> implements Parcelable {
    int id, complaintId, studentId, academicsessionId, masterId, bcsmapId, is_penalty, fine_amount, userId, penalty_status;
    String complaint_detail, tagIds, image, createdAt, updatedAt;

    Complaint complaint;
    UserInfo user;
    List<Tag> tagsData;

    protected Complaint(Parcel in) {
        id = in.readInt();
        complaintId = in.readInt();
        studentId = in.readInt();
        academicsessionId = in.readInt();
        masterId = in.readInt();
        bcsmapId = in.readInt();
        is_penalty = in.readInt();
        fine_amount = in.readInt();
        userId = in.readInt();
        penalty_status = in.readInt();
        complaint_detail = in.readString();
        tagIds = in.readString();
        image = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        complaint = in.readParcelable(Complaint.class.getClassLoader());
        user = in.readParcelable(UserInfo.class.getClassLoader());
        tagsData = in.createTypedArrayList(Tag.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(complaintId);
        dest.writeInt(studentId);
        dest.writeInt(academicsessionId);
        dest.writeInt(masterId);
        dest.writeInt(bcsmapId);
        dest.writeInt(is_penalty);
        dest.writeInt(fine_amount);
        dest.writeInt(userId);
        dest.writeInt(penalty_status);
        dest.writeString(complaint_detail);
        dest.writeString(tagIds);
        dest.writeString(image);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeParcelable(complaint, flags);
        dest.writeParcelable(user, flags);
        dest.writeTypedList(tagsData);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Complaint> CREATOR = new Creator<Complaint>() {
        @Override
        public Complaint createFromParcel(Parcel in) {
            return new Complaint(in);
        }

        @Override
        public Complaint[] newArray(int size) {
            return new Complaint[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(int complaintId) {
        this.complaintId = complaintId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getAcademicsessionId() {
        return academicsessionId;
    }

    public void setAcademicsessionId(int academicsessionId) {
        this.academicsessionId = academicsessionId;
    }

    public int getMasterId() {
        return masterId;
    }

    public void setMasterId(int masterId) {
        this.masterId = masterId;
    }

    public int getBcsmapId() {
        return bcsmapId;
    }

    public void setBcsmapId(int bcsmapId) {
        this.bcsmapId = bcsmapId;
    }

    public int getIs_penalty() {
        return getFine_amount() == 0 ? 0 : is_penalty;
    }

    public void setIs_penalty(int is_penalty) {
        this.is_penalty = is_penalty;
    }

    public int getFine_amount() {
        return fine_amount;
    }

    public void setFine_amount(int fine_amount) {
        this.fine_amount = fine_amount;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getComplaint_detail() {
        return complaint_detail;
    }

    public void setComplaint_detail(String complaint_detail) {
        this.complaint_detail = complaint_detail;
    }

    public String getTagIds() {
        return tagIds;
    }

    public void setTagIds(String tagIds) {
        this.tagIds = tagIds;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public Complaint getComplaint() {
        return complaint;
    }

    public void setComplaint(Complaint complaint) {
        this.complaint = complaint;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public List<Tag> getTagsData() {
        return tagsData;
    }

    public void setTagsData(List<Tag> tagsData) {
        this.tagsData = tagsData;
    }

    public int getPenalty_status() {
        return penalty_status;
    }

    public void setPenalty_status(int penalty_status) {
        this.penalty_status = penalty_status;
    }
}
