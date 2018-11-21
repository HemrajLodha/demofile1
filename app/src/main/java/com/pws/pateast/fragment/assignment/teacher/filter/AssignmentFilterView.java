package com.pws.pateast.fragment.assignment.teacher.filter;

import com.pws.pateast.fragment.presenter.ClassView;

/**
 * Created by intel on 03-May-17.
 */

public interface AssignmentFilterView extends ClassView
{

    void setSubject(String subject);

    void setAssignmentTitle(String assignmentTitle);

}
