package com.pws.pateast.fragment.assignment.teacher;

import com.pws.pateast.api.model.Assignment;
import com.pws.pateast.api.model.TeacherClass;
import com.pws.pateast.fragment.assignment.AssignmentDownloadView;

import java.util.List;

/**
 * Created by intel on 03-May-17.
 */

public interface AssignmentListView extends AssignmentDownloadView {
    void setMyClasses(List<TeacherClass> myClasses);

    List<TeacherClass> getMyClasses();

    int getClassId();

    void setClassId(int bcsMapId);

    void reviewAssignment(Assignment assignment);

    void addAssignment(Assignment assignment);

    void setAddVisible(boolean visible);

    void setFilter(int bcsMapId, String subject, String assignmentTitle);

    void clearFilter();

    void setAssignmentAdapter(List<Assignment> assignments, String dateFormat);

    void setSubject(String subject);

    String getSubject();

    void setAssignmentTitle(String assignmentTitle);

    String getAssignmentTitle();

    String getStatus();

    void updateAssignmentStatus(int status, Assignment assignment);

    boolean isVisible();
}
