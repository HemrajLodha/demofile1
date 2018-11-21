package com.pws.pateast.fragment.assignment.student;

import com.pws.pateast.api.model.Assignment;
import com.pws.pateast.fragment.assignment.AssignmentDownloadView;

import java.util.List;

/**
 * Created by intel on 15-May-17.
 */

public interface StudentAssignmentView extends AssignmentDownloadView
{
    void setFilter(String subjectTitle, String assignmentTitle);
    void clearFilter();

    void setAssignmentAdapter(List<Assignment> assignments);

    void setSubjectTitle(String subjectTitle);
    String getSubjectTitle();

    void setAssignmentTitle(String assignmentTitle);
    String getAssignmentTitle();

}
