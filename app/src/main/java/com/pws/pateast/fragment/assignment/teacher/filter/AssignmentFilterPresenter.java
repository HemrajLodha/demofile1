package com.pws.pateast.fragment.assignment.teacher.filter;

import com.pws.pateast.fragment.assignment.teacher.AssignmentListView;
import com.pws.pateast.fragment.presenter.ClassPresenter;
import com.pws.pateast.api.model.TeacherClass;

import java.util.List;

/**
 * Created by intel on 03-May-17.
 */

public class AssignmentFilterPresenter extends ClassPresenter<AssignmentFilterView>
{

    public AssignmentFilterPresenter(boolean openDialog) {
        super(openDialog);
    }


    public void setFilterData(AssignmentListView assignmentView)
    {
        setClasses(assignmentView.getMyClasses());
        getView().setSubject(assignmentView.getSubject());
        getView().setAssignmentTitle(assignmentView.getAssignmentTitle());

    }

    public void setClasses(List<TeacherClass> myClasses)
    {
        if(myClasses == null)
        {
            getMyClasses();
        }
        else
        {
            getView().setClassAdapter(myClasses);
        }
    }
}
