package com.pws.pateast.fragment.assignment.teacher.review;

import com.pws.pateast.api.model.Assignment;
import com.pws.pateast.api.model.Student;
import com.pws.pateast.api.model.Tag;
import com.pws.pateast.base.AppView;

import java.util.List;

/**
 * Created by intel on 07-Sep-17.
 */

public interface ReviewAssignmentView extends AppView {

    int getClassId();

    int getAssignmentId();

    Assignment getAssignment();

    boolean showSearch();


    void assignmentReviewed();

    void setAssignmentAdapter(List<Student> students);

    void setAssignmentAdapter(Student student);

    void updateAssignmentAdapter(List<Student> students);

    void setAssignmentTags(List<Tag> tags);

    List<Tag> getAssignmentTags();

    List<Student> getStudents();
}
