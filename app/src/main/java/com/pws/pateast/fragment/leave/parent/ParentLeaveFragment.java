package com.pws.pateast.fragment.leave.parent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.view.BaseRecyclerView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pws.pateast.R;
import com.pws.pateast.activity.leave.LeaveStatusView;
import com.pws.pateast.activity.tasks.ParentTaskActivity;
import com.pws.pateast.activity.tasks.TaskActivity;
import com.pws.pateast.api.model.Leave;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.base.Extras;
import com.pws.pateast.enums.TaskType;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.fragment.leave.LeaveAdapter;
import com.pws.pateast.fragment.leave.student.StudentLeaveFragment;
import com.pws.pateast.fragment.leave.student.StudentLeavePresenter;
import com.pws.pateast.listener.AdapterListener;
import com.pws.pateast.utils.DialogUtils;
import com.pws.pateast.widget.SpaceItemDecoration;

import java.util.List;

import static com.pws.pateast.fragment.assignment.teacher.add.AddAssignmentFragment.ADD_REQUEST;
import static com.pws.pateast.fragment.assignment.teacher.add.AddAssignmentFragment.ADD_RESPONSE;

/**
 * Created by intel on 18-Aug-17.
 */

public class ParentLeaveFragment extends AppFragment implements ParentLeaveView,BaseRecyclerAdapter.OnItemClickListener,XRecyclerView.LoadingListener
{
    private BaseRecyclerView rvLeave;
    private ParentLeavePresenter leavePresenter;

    private LeaveAdapter leaveAdapter;

    private int page;
    private int pageCount;

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_recycler_view;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        rvLeave = (BaseRecyclerView) findViewById(R.id.rv_leave);
        rvLeave.setUpAsList();
        rvLeave.addItemDecoration(new SpaceItemDecoration(getContext(), R.dimen.size_5, 1, true));
        rvLeave.setPullRefreshEnabled(false);
        rvLeave.setLoadingListener(this);

        leavePresenter = new ParentLeavePresenter();
        leavePresenter.attachView(this);
    }

    @Override
    public void onActionClick() {
        super.onActionClick();
        onRefresh();
    }

    @Override
    public int getPage() {
        return page;
    }

    @Override
    public void addPage() {
        page++;
    }

    @Override
    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    @Override
    public String getLeaveStatus()
    {
        return getArguments().getString(Extras.STATUS);
    }

    @Override
    public void leaveApply()
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TaskActivity.EXTRA_TASK_TYPE, TaskType.PARENT_LEAVE_APPLY);
        getAppListener().openActivityForResult(ParentTaskActivity.class,bundle,ADD_REQUEST);
    }

    @Override
    public void setApplyVisible(boolean visible) {
        ((LeaveStatusView)getActivity()).setApplyVisible(visible);
    }

    @Override
    public void setLeaveAdapter(List<Leave> leaves)
    {
        if(rvLeave.getAdapter() == null)
        {
            leaveAdapter = new LeaveAdapter(getContext(),this);
            leaveAdapter.setUserType(UserType.PARENT);
            rvLeave.setAdapter(leaveAdapter);
        }
        leaveAdapter.addOrUpdate(leaves);
        if(page<pageCount)
            addPage();
        else
            rvLeave.setLoadingMoreEnabled(false);
        rvLeave.loadMoreComplete();
    }


    @Override
    public void onRefresh()
    {
        rvLeave.setLoadingMoreEnabled(true);
        if(leaveAdapter != null)
            leaveAdapter.clear();
        if(leavePresenter != null)
        {
            page = 1;
            leavePresenter.getWardLeave();
        }
    }

    @Override
    public void onLoadMore()
    {
        if(leavePresenter != null)
            leavePresenter.getWardLeave();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ADD_REQUEST:
                if (resultCode == ADD_RESPONSE) {
                    onActionClick();
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(leavePresenter != null)
            leavePresenter.detachView();
    }

    @Override
    public void onItemClick(View view, int position)
    {
        final Leave leave = leaveAdapter.getItem(position);
        switch (view.getId())
        {
            case R.id.tv_options:
                DialogUtils.showDialog(getContext(), R.string.app_name, R.string.cancel_prompt_message, new AdapterListener<String>() {
                    @Override
                    public void onClick(int id, String value) {
                       switch (id)
                       {
                           case 0:
                               leavePresenter.cancelWardLeave(leave.getId());
                               break;
                       }
                    }
                }, R.string.yes, R.string.no);
                break;
            default:
                leave.setOpened(!leave.isOpened());
                leaveAdapter.addOrUpdate(leave);
                break;
        }
    }

}
