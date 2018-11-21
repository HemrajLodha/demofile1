package com.pws.pateast.fragment.leave.teacher.filter;

import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.R;
import com.pws.pateast.fragment.leave.teacher.approve.ClassLeaveView;

/**
 * Created by intel on 03-Jul-17.
 */

public class LeaveFilterPresenter extends AppPresenter<LeaveFilterView>
{
    private LeaveFilterView mFilterView;

    @Override
    public LeaveFilterView getView() {
        return mFilterView;
    }

    @Override
    public void attachView(LeaveFilterView view) {
        mFilterView = view;
    }

    public void setFilterData(ClassLeaveView leaveView)
    {
        getView().setStudentName(leaveView.getStudentName());
        getView().setDate(R.id.et_start_date,leaveView.getStartDate());
        getView().setDate(R.id.et_end_date,leaveView.getEndDate());
    }
}
