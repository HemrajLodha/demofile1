package com.pws.pateast.fragment.leave.teacher.info;

import com.pws.pateast.api.model.Leave;
import com.pws.pateast.api.model.LeaveInfo;
import com.pws.pateast.base.AppView;

import java.util.List;

public interface TeacherLeaveInfoView extends AppView
{
    void setLeaveInfoAdapter(LeaveInfo leaveInfo);
}
