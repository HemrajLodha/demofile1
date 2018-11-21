package com.pws.pateast.activity.assignment;

import com.pws.pateast.base.AppView;
import com.pws.pateast.model.AssignmentCategory;

import java.util.List;

/**
 * Created by intel on 12-Jun-17.
 */

public interface AssignmentStatusView extends AppView
{
    void addAssignment();
    void setAddVisible(boolean visible);

    void setStatusAdapter(List<AssignmentCategory> categories);


}
