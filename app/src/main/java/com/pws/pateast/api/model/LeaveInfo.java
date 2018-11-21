package com.pws.pateast.api.model;

import java.util.ArrayList;

/**
 * Created by intel on 28-Jun-17.
 */

public class LeaveInfo extends Response<LeaveInfo>
{
    int id,masterId,userId,tagId,academicSessionId,empLeaveTypeId,leavestatus,status_updatedby;
    double duration;
    String start_date,end_date,halfday,comment,reject_reason,status_updatedbytype,dfhdfhdfh;
    Tag tag;
    UserInfo user;
    LeaveType empleavetype;

    public int getId() {
        return id;
    }

    public int getMasterId() {
        return masterId;
    }

    public int getUserId() {
        return userId;
    }

    public int getTagId() {
        return tagId;
    }

    public int getAcademicSessionId() {
        return academicSessionId;
    }

    public int getEmpLeaveTypeId() {
        return empLeaveTypeId;
    }

    public int getLeavestatus() {
        return leavestatus;
    }

    public int getStatus_updatedby() {
        return status_updatedby;
    }

    public double getDuration() {
        return duration;
    }

    public String getStart_date() {
        return start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public String getHalfday() {
        return halfday;
    }

    public String getComment() {
        return comment;
    }

    public String getReject_reason() {
        return reject_reason;
    }

    public String getStatus_updatedbytype() {
        return status_updatedbytype;
    }

    public String getDfhdfhdfh() {
        return dfhdfhdfh;
    }

    public Tag getTag() {
        return tag;
    }

    public UserInfo getUser() {
        return user;
    }

    public LeaveType getEmpleavetype() {
        return empleavetype;
    }
}
