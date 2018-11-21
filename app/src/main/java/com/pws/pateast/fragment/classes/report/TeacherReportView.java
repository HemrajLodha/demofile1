package com.pws.pateast.fragment.classes.report;

import com.pws.pateast.api.model.Schedule;
import com.pws.pateast.api.model.TeacherClass;
import com.pws.pateast.api.model.TeacherReport;
import com.pws.pateast.base.AppView;

/**
 * Created by intel on 04-Jul-17.
 */

public interface TeacherReportView extends AppView
{
    TeacherClass getTeacherClass();
    Schedule getSchedule();
    String getDate();
    String getTime();
    void setReportId(int reportId);
    int getReportId();
    int getOrder();
    void setError(int id, String error);
    String getContent();

    void bind(TeacherClass classes,Schedule schedule);
    void setTeacherReport(TeacherReport report);

    void dismiss();
}
