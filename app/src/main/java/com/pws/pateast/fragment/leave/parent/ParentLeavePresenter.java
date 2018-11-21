package com.pws.pateast.fragment.leave.parent;

import com.pws.pateast.fragment.leave.LeavePresenter;

/**
 * Created by intel on 18-Aug-17.
 */

public class ParentLeavePresenter extends LeavePresenter<ParentLeaveView> {

    @Override
    public void attachView(ParentLeaveView view) {
        super.attachView(view);
        ward = preference.getWard();
    }

    public void getWardLeave() {
        getLeave(ward.getAcademicSessionId(), ward.getMasterId(), ward.getUserId());
    }

    public void cancelWardLeave(int leaveId) {
        cancelLeave(leaveId, ward.getMasterId());
    }
}
