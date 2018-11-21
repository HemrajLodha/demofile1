package com.pws.pateast.fragment.assignment.student.filter;

import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.fragment.assignment.student.StudentAssignmentView;

/**
 * Created by intel on 15-May-17.
 */

public class StudentAssignmentFilterPresenter extends AppPresenter<StudentAssignmentFilterView>
{
    private StudentAssignmentFilterView filterView;
    @Override
    public StudentAssignmentFilterView getView() {
        return filterView;
    }

    @Override
    public void attachView(StudentAssignmentFilterView view) {
        filterView = view;
    }

    public void setFilterData(StudentAssignmentView assignmentView)
    {
        filterView.setSubjectTitle(assignmentView.getSubjectTitle());
        filterView.setAssignmentTitle(assignmentView.getAssignmentTitle());
    }
}
