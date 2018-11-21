package com.pws.pateast.fragment.leave.teacher.approve;

import com.pws.pateast.api.model.Leave;
import com.pws.pateast.base.AppView;
import com.pws.pateast.fragment.leave.LeaveView;

import java.util.List;

/**
 * Created by intel on 29-Jun-17.
 */

public interface ClassLeaveView extends LeaveView
{
    void setFilter(String studentName, String startDate, String endDate);
    void clearFilter();

    String getStudentName();
    String getStartDate();
    String getEndDate();
}
