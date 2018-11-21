package com.pws.pateast.api.model;

import java.util.ArrayList;

/**
 * Created by intel on 28-Jun-17.
 */

public class Leave extends Response<ArrayList<Leave>> {
    public static final int PENDING = 0;
    public static final int APPROVED = 1;
    public static final int CANCELED = 2;
    public static final int REJECTED = 3;

    int id, masterId, userId, tagId, academicSessionId, bcsMapId, leavestatus, halfday, status_updatedby;
    double duration;
    String start_date, end_date, comment, name, createdAt, reject_reason, status_updatedbytype;
    Tag tag;
    UserInfo user;
    boolean isOpened;

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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public int getAcademicSessionId() {
        return academicSessionId;
    }

    public void setAcademicSessionId(int academicSessionId) {
        this.academicSessionId = academicSessionId;
    }

    public int getBcsMapId() {
        return bcsMapId;
    }

    public void setBcsMapId(int bcsMapId) {
        this.bcsMapId = bcsMapId;
    }

    public int getLeavestatus() {
        return leavestatus;
    }

    public void setLeavestatus(int leavestatus) {
        this.leavestatus = leavestatus;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getHalfday() {
        return halfday;
    }

    public void setHalfday(int halfday) {
        this.halfday = halfday;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public void setOpened(boolean opened) {
        isOpened = opened;
    }

    public int getStatus_updatedby() {
        return status_updatedby;
    }

    public void setStatus_updatedby(int status_updatedby) {
        this.status_updatedby = status_updatedby;
    }

    public String getReject_reason() {
        return reject_reason;
    }

    public void setReject_reason(String reject_reason) {
        this.reject_reason = reject_reason;
    }

    public String getStatus_updatedbytype() {
        return status_updatedbytype;
    }

    public void setStatus_updatedbytype(String status_updatedbytype) {
        this.status_updatedbytype = status_updatedbytype;
    }
}
