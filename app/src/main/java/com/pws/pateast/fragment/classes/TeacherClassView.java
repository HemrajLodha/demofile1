package com.pws.pateast.fragment.classes;

import com.pws.pateast.api.model.TeacherClass;
import com.pws.pateast.base.AppView;

import java.util.Date;
import java.util.List;

/**
 * Created by intel on 12-May-17.
 */

public interface TeacherClassView extends AppView
{
    void setCalenderDate(Date minDate,Date maxDate);
    void setDate(Date date);
    void setClassAdapter(List<TeacherClass> classes);


    boolean isClassAttendance();
    boolean isFutureDay();
    String getWeekday();
    String getDate();

}
