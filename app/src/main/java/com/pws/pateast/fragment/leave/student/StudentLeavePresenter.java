package com.pws.pateast.fragment.leave.student;

import com.pws.pateast.fragment.leave.LeavePresenter;

/**
 * Created by intel on 28-Jun-17.
 */

public class StudentLeavePresenter extends LeavePresenter<StudentLeaveView>
{

    public void getStudentLeave()
    {
        getLeave(user.getUserdetails().getAcademicSessionId(), user.getData().getMasterId() ,user.getData().getId());
    }


    public void cancelStudentLeave(int leaveId)
    {
        cancelLeave(leaveId,user.getData().getMasterId());
    }
}
