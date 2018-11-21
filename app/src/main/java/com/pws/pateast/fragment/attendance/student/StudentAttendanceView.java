package com.pws.pateast.fragment.attendance.student;

import com.pws.pateast.api.model.Student;
import com.pws.pateast.api.model.Subject;
import com.pws.pateast.base.AppView;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.fragment.attendance.teacher.report.calender.CalenderReportView;

import java.util.Date;
import java.util.List;

/**
 * Created by intel on 15-May-17.
 */

public interface StudentAttendanceView extends CalenderReportView
{
    UserType getUserType();
}
