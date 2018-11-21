package com.pws.pateast.model;

import com.pws.pateast.enums.LeaveType;

/**
 * Created by intel on 06-Jul-17.
 */

public class LeaveCategory
{
    private int title;
    private int icon;
    private LeaveType leaveType;

    public LeaveCategory(int title,int icon,LeaveType leaveType)
    {
        setTitle(title);
        setIcon(icon);
        setLeaveType(leaveType);
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public LeaveType getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
