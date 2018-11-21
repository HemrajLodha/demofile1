package com.pws.pateast.fragment.attendance.teacher.report;

import com.pws.pateast.api.model.Student;
import com.pws.pateast.api.model.Subject;
import com.pws.pateast.base.AppView;
import com.pws.pateast.api.model.TeacherClass;

import java.util.Date;
import java.util.List;

/**
 * Created by intel on 10-May-17.
 */

public interface AttendanceReportView extends AppView
{
    int getClassId();
    int getSubjectId();
    int getStudentId();
    String getStartDate();
    String getEndDate();

    boolean isSearch();

    void updateReportAdapter(List<Student> students);
    void setReportAdapter(List<Student> students);

}
