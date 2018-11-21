package com.pws.pateast.fragment.leave.teacher.filter;

import com.pws.pateast.base.AppView;

/**
 * Created by intel on 03-Jul-17.
 */

public interface LeaveFilterView extends AppView
{
    void setDate(int id, String date);
    void setStudentName(String studentName);
}
