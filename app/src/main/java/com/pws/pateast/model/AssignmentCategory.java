package com.pws.pateast.model;

import com.pws.pateast.enums.AssignmentType;

/**
 * Created by intel on 06-Jul-17.
 */

public class AssignmentCategory
{
    private int title;
    private AssignmentType assignmentType;

    public AssignmentCategory(int title,AssignmentType assignmentType)
    {
        setTitle(title);
        setAssignmentType(assignmentType);
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public AssignmentType getAssignmentType() {
        return assignmentType;
    }

    public void setAssignmentType(AssignmentType assignmentType) {
        this.assignmentType = assignmentType;
    }
}
