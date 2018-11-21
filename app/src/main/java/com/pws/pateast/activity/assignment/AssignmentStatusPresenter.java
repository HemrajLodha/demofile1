package com.pws.pateast.activity.assignment;

import android.os.Bundle;

import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.R;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.base.Extras;
import com.pws.pateast.enums.AssignmentType;
import com.pws.pateast.fragment.assignment.teacher.AssignmentListFragment;
import com.pws.pateast.model.AssignmentCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by intel on 12-Jun-17.
 */

public class AssignmentStatusPresenter extends AppPresenter<AssignmentStatusView>
{
    private AssignmentStatusView filterView;

    @Override
    public AssignmentStatusView getView() {
        return filterView;
    }

    @Override
    public void attachView(AssignmentStatusView view) {
        filterView = view;
    }

    public List<AssignmentListFragment> getAssignmentFragments(List<AssignmentCategory> categories,Bundle extras)
    {
        List<AssignmentListFragment> assignmentFragments = new ArrayList<>();

        for (AssignmentCategory category: categories)
        {
            Bundle bundle = new Bundle();
            bundle.putString(Extras.STATUS, category.getAssignmentType().getValue());
            bundle.putString(Extras.TITLE, getString(category.getTitle()));
            if(extras != null)
            {
                bundle.putAll(extras);
            }
            assignmentFragments.add(AppFragment.newInstance(AssignmentListFragment.class,null,bundle));
        }

        return assignmentFragments;
    }

    public void setStatusAdapter()
    {
        getView().setStatusAdapter(getAssignmentCategory());
    }

    public List<AssignmentCategory> getAssignmentCategory()
    {
        ArrayList<AssignmentCategory> categories = new ArrayList<>();

        categories.add(new AssignmentCategory(R.string.assignment_draft, AssignmentType.DRAFT));
        categories.add(new AssignmentCategory(R.string.assignment_published, AssignmentType.PUBLISHED));
        categories.add(new AssignmentCategory(R.string.assignment_canceled, AssignmentType.CANCELED));
        categories.add(new AssignmentCategory(R.string.assignment_completed, AssignmentType.COMPLETED));

        return categories;
    }
}
