package com.pws.pateast.fragment.student;

import com.pws.pateast.api.model.Student;
import com.pws.pateast.api.model.TeacherClass;
import com.pws.pateast.base.AppView;

import java.util.List;

/**
 * Created by intel on 25-Apr-17.
 */

public interface MyStudentView extends AppView
{
    void setFilter(int bcsMapId, String enrollNumber, String studentName);
    void clearFilter();
    void setMyClasses(List<TeacherClass> myClasses);

    boolean isAddUser();
    List<TeacherClass> getMyClasses();
    int getClassId();
    String getEnrollNumber();
    String getStudentName();
    void setMyStudentAdapter(List<Student> students);
    void updateStudentAdapter(List<Student> students);
}
