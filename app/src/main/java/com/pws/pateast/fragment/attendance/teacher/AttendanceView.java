package com.pws.pateast.fragment.attendance.teacher;

import com.pws.pateast.api.model.Student;
import com.pws.pateast.api.model.Tag;
import com.pws.pateast.base.AppView;

import java.util.List;

/**
 * Created by intel on 27-Apr-17.
 */

public interface AttendanceView extends AppView
{
    int getClassId();
    int getSubjectId();
    int getSubjectOrder();
    String getDate();
    String getEnrollNumber();
    String getStudentName();
    void setAttendanceTags(List<Tag> tags);
    List<Tag> getAttendanceTags();
    String getClassName();
    void setAttendanceAdapter(List<Student> students);
    void updateAttendanceAdapter(List<Student> students);
    void setAttendanceAdapter(Student student);
    void setSubmitVisible(boolean visible);
    void setAttendanceId(int attendanceId);
    int getAttendanceId();

    boolean isUpdate();
}
