package com.pws.pateast.fragment.presenter;

import com.pws.pateast.api.model.Student;

import java.util.List;

/**
 * Created by intel on 10-May-17.
 */

public interface StudentView extends ClassView
{
    void setStudent(Student student);
    void setStudentAdapter(List<Student> students);
}
