package com.pws.pateast.fragment.schedule.student.exam.syllabus;

import com.pws.pateast.api.model.Schedule;
import com.pws.pateast.base.AppView;

/**
 * Created by intel on 02-Sep-17.
 */

public interface ExamSyllabusView extends AppView{

    Schedule getSchedule();

    void bindData(Schedule schedule);
}
