package com.pws.pateast.fragment.student.report;

import android.view.View;

import com.pws.calender.domain.Event;
import com.pws.pateast.api.model.Tag;
import com.pws.pateast.base.AppView;

import java.util.ArrayList;

/**
 * Created by intel on 10-Jan-18.
 */

public interface StudentReportView extends AppView,View.OnClickListener {
    String getStudentName();

    ArrayList<Event> getAttendance();

    ArrayList<Tag> getAttendanceTags();

    void showAttendanceReport();
}
