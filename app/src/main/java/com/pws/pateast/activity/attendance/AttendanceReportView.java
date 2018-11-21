package com.pws.pateast.activity.attendance;

import com.pws.pateast.api.model.Student;
import com.pws.pateast.fragment.presenter.SubjectView;

/**
 * Created by intel on 06-Sep-17.
 */

public interface AttendanceReportView  extends SubjectView
{
    int tabIndicatorColor();
    int tabSelectedTextColor();
    int tabTextAppearance();
    int tabTextColor();
    int tabBackground();

    int getClassId();
    Student getStudent();
    int getStudentId();
}
