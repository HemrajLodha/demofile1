package com.pws.pateast.fragment.attendance.student;

import com.pws.pateast.fragment.attendance.teacher.report.calender.CalenderReportPresenter;

/**
 * Created by intel on 15-May-17.
 */

public class StudentAttendancePresenter extends CalenderReportPresenter<StudentAttendanceView> {


    public void getAttendanceReport() {
        switch (getView().getUserType()) {
            case STUDENT:
                getAttendanceReportForStudent();
                break;
            case PARENT:
                getAttendanceReportForWard();
                break;
            case TEACHER:
                getAttendanceReportForTeacher();
                break;
        }
    }

    public String getStudentName() {
        switch (getView().getUserType()) {
            case STUDENT:
                return user.getUserdetails().getFullname();
            case PARENT:
                return ward.getFullname();
            case TEACHER:
                return getView().getStudentName();
            default:
                return user.getFullname();
        }
    }

}
