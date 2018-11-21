package com.pws.pateast.fragment.marks.student;

import com.pws.pateast.api.model.ExamMarks;
import com.pws.pateast.api.model.Schedule;
import com.pws.pateast.api.model.Student;
import com.pws.pateast.api.model.TeacherClass;
import com.pws.pateast.base.AppView;
import com.pws.pateast.enums.TaskType;
import com.pws.pateast.fragment.presenter.ExamHeadView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by intel on 17-May-17.
 */

public interface ExamMarksView extends ExamHeadView
{
    TaskType getTaskType();
    int getExamScheduleID();

    void bindStudent(Student student);
    void setExamMarksAdapter(ArrayList<ExamMarks> marks);

    TeacherClass getTeacherClass();
    Student getStudent();

    void setError(String error);

}
