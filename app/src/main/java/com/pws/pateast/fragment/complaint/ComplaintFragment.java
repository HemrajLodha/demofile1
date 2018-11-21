package com.pws.pateast.fragment.complaint;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.pws.pateast.R;
import com.pws.pateast.activity.tasks.ParentTaskActivity;
import com.pws.pateast.activity.tasks.StudentTaskActivity;
import com.pws.pateast.activity.tasks.TaskActivity;
import com.pws.pateast.api.model.Complaint;
import com.pws.pateast.api.model.Tag;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.base.Extras;
import com.pws.pateast.base.FragmentActivity;
import com.pws.pateast.enums.TaskType;
import com.pws.pateast.enums.UserType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by intel on 10-Feb-18.
 */

public class ComplaintFragment extends AppFragment implements ComplaintView {
    private RecyclerView rvComplaints;
    private LinearLayoutManager llm;
    private DividerItemDecoration itemDecoration;

    private ComplaintPresenter mPresenter;
    private ComplaintAdapter mAdapter;

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_complaint;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        getAppListener().setTitle(R.string.menu_complaint);
        rvComplaints = (RecyclerView) findViewById(R.id.rv_complaints);
        llm = new LinearLayoutManager(getContext());
        itemDecoration = new DividerItemDecoration(getContext(), llm.getOrientation());
        itemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider_recycle_view));
        rvComplaints.setLayoutManager(llm);
        rvComplaints.addItemDecoration(itemDecoration);

        mPresenter = new ComplaintPresenter();
        mPresenter.attachView(this);
        onActionClick();
    }

    @Override
    public void onActionClick() {
        super.onActionClick();
        if (mPresenter != null)
            mPresenter.getStudentComplaints();
    }


    @Override
    public void setComplaintAdapter(List<Complaint> complaints) {
        if (rvComplaints.getAdapter() == null) {
            mAdapter = new ComplaintAdapter(getContext(), this);
            rvComplaints.setAdapter(mAdapter);
        }
        mAdapter.update(complaints);
    }

    @Override
    public void setTags(ArrayList<Tag> tags) {
        getArguments().putParcelableArrayList(Extras.COMPLAINT_TAGS, tags);
    }

    @Override
    public ArrayList<Tag> getTags() {
        return getArguments().getParcelableArrayList(Extras.COMPLAINT_TAGS);
    }


    @Override
    public void onItemClick(View view, int position) {
        UserType userType = mPresenter.getUserType();
        Complaint complaint = mAdapter.getItem(position);
        switch (view.getId()) {
            default:
                Class aClass = TaskActivity.class;
                Bundle bundle = new Bundle();
                bundle.putParcelable(Extras.COMPLAINT, complaint);
                bundle.putParcelableArrayList(Extras.COMPLAINT_TAGS, getTags());
                bundle.putInt(FragmentActivity.EXTRA_TASK_TYPE, TaskType.COMPLAINT_DETAIL.getValue());
                switch (userType) {
                    case STUDENT:
                        aClass = StudentTaskActivity.class;
                        break;
                    case PARENT:
                        aClass = ParentTaskActivity.class;
                        break;
                }
                getAppListener().openActivity(aClass, bundle);
                break;
        }
    }
}
