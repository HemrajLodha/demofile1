package com.pws.pateast.fragment.student.report;

import com.pws.pateast.base.AppPresenter;

/**
 * Created by intel on 10-Jan-18.
 */

public class StudentReportPresenter extends AppPresenter<StudentReportView> {
    private StudentReportView mView;

    @Override
    public StudentReportView getView() {
        return mView;
    }

    @Override
    public void attachView(StudentReportView view) {
        mView = view;
        getView().showAttendanceReport();
    }
}
