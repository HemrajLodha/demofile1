package com.pws.pateast.fragment.leave.teacher.apply;

import com.pws.pateast.api.model.LeaveType;
import com.pws.pateast.api.model.Tag;
import com.pws.pateast.base.AppView;
import com.pws.pateast.fragment.leave.LeaveApplyView;

import java.util.ArrayList;

/**
 * Created by intel on 23-Aug-17.
 */

public interface TeacherLeaveApplyView extends LeaveApplyView
{
    void setLeaveTypeAdapter(ArrayList<LeaveType> leaveType);
}
