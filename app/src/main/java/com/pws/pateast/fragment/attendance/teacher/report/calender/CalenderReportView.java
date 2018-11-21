package com.pws.pateast.fragment.attendance.teacher.report.calender;

import com.pws.pateast.api.model.Student;
import com.pws.pateast.api.model.Tag;
import com.pws.pateast.base.AppView;

import java.util.Date;
import java.util.List;

/**
 * Created by intel on 31-Aug-17.
 */

public interface CalenderReportView extends AppView
{
    int getClassId();
    int getSubjectId();
    int getStudentId();
    String getStudentName();
    Student getStudent();
    void setAttendanceTags(List<Tag> tags);
    List<Tag> getAttendanceTags();

    void setReportCalender(List<Student> students);
    void setCurrentMoth(Date month);
}
