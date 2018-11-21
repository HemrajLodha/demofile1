package com.pws.pateast.fragment.assignment.teacher.review.remark;

import com.pws.pateast.base.AppPresenter;

/**
 * Created by intel on 07-Sep-17.
 */

public class RemarkAssignmentPresenter extends AppPresenter<RemarkAssignmentView>
{
    private RemarkAssignmentView view;

    @Override
    public RemarkAssignmentView getView() {
        return view;
    }

    @Override
    public void attachView(RemarkAssignmentView view) {
        this.view = view;

        getView().setTagAdapter(getView().getTags());
        getView().setData(getView().getStudent());
    }


}
