package com.pws.pateast.api.model;

import java.util.ArrayList;

/**
 * Created by intel on 12-May-17.
 */

public class SubjectStudent extends Response
{
    ArrayList<Subject> subjects;
    ArrayList<Student> students;

    public ArrayList<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(ArrayList<Subject> subjects) {
        this.subjects = subjects;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<Student> students) {
        this.students = students;
    }
}
