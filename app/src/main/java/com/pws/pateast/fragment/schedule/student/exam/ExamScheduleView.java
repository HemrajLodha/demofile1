package com.pws.pateast.fragment.schedule.student.exam;

import com.pws.pateast.api.model.Schedule;
import com.pws.pateast.api.model.TeacherClass;
import com.pws.pateast.base.AppView;
import com.pws.pateast.enums.TaskType;
import com.pws.pateast.fragment.presenter.ExamHeadView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by intel on 16-May-17.
 */

public interface ExamScheduleView extends ExamHeadView
{
    int getExamHeadID();

    TaskType getTaskType();

    void setExamScheduleAdapter(ArrayList<Schedule> examHeads);

    TeacherClass getTeacherClass();

    void onError(String message);
}
