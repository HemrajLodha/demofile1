package com.pws.pateast.fragment.leave.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.view.BaseRecyclerView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pws.pateast.R;
import com.pws.pateast.activity.leave.LeaveStatusView;
import com.pws.pateast.activity.tasks.DriverTaskActivity;
import com.pws.pateast.activity.tasks.TaskActivity;
import com.pws.pateast.activity.tasks.TeacherTaskActivity;
import com.pws.pateast.api.model.Leave;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.base.Extras;
import com.pws.pateast.enums.TaskType;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.fragment.leave.LeaveAdapter;
import com.pws.pateast.widget.SpaceItemDecoration;

import java.util.List;

import static com.pws.pateast.fragment.assignment.teacher.add.AddAssignmentFragment.ADD_REQUEST;
import static com.pws.pateast.fragment.assignment.teacher.add.AddAssignmentFragment.ADD_RESPONSE;

/**
 * Created by intel on 28-Jun-17.
 */

public class TeacherLeaveFragment extends AppFragment implements TeacherLeaveView, BaseRecyclerAdapter.OnItemClickListener, XRecyclerView.LoadingListener {
    private BaseRecyclerView rvLeave;
    private TeacherLeavePresenter leavePresenter;

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
        rvLeave.addItemDecoration(new SpaceItemDecoration(getContext(), R.dimen.size_8, 1, true));
        rvLeave.setPullRefreshEnabled(false);
        rvLeave.setLoadingListener(this);

        leavePresenter = new TeacherLeavePresenter();
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
    public String getLeaveStatus() {
        return getArguments().getString(Extras.STATUS);
    }

    @Override
    public void leaveApply() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TaskActivity.EXTRA_TASK_TYPE, TaskType.EMPLOYEE_LEAVE_APPLY);
        if (leavePresenter.getUserType() == UserType.TEACHER) {
            getAppListener().openActivityForResult(TeacherTaskActivity.class, bundle, ADD_REQUEST);
        } else {
            getAppListener().openActivityForResult(DriverTaskActivity.class, bundle, ADD_REQUEST);
        }
    }

    @Override
    public void setApplyVisible(boolean visible) {
        ((LeaveStatusView) getActivity()).setApplyVisible(visible);
    }

    @Override
    public void setLeaveAdapter(List<Leave> leaves) {
        if (rvLeave.getAdapter() == null) {
            leaveAdapter = new LeaveAdapter(getContext(), this);
            leaveAdapter.setUserType(UserType.TEACHER);
            rvLeave.setAdapter(leaveAdapter);
        }
        leaveAdapter.addOrUpdate(leaves);
        if (page < pageCount)
            addPage();
        else
            rvLeave.setLoadingMoreEnabled(false);
        rvLeave.loadMoreComplete();
    }


    @Override
    public void onRefresh() {
        rvLeave.setLoadingMoreEnabled(true);
        if (leaveAdapter != null)
            leaveAdapter.clear();
        if (leavePresenter != null) {
            page = 1;
            leavePresenter.getTeacherLeave();
        }
    }

    @Override
    public void onLoadMore() {
        if (leavePresenter != null)
            leavePresenter.getTeacherLeave();
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
        if (leavePresenter != null)
            leavePresenter.detachView();
    }

    @Override
    public void onItemClick(View view, int position) {
        Leave mLeave = leaveAdapter.getItem(position);
        switch (view.getId()) {
            case R.id.tv_options:
                leavePresenter.cancelTeacherLeave(mLeave.getId());
                break;
            default:
                mLeave.setOpened(!mLeave.isOpened());
                leaveAdapter.addOrUpdate(mLeave);
                break;
            /*default:
                Bundle bundle = new Bundle();
                bundle.putInt(TeacherLeaveInfoFragment.ARGS_DATA_ID, mLeave.getId());
                bundle.putInt(FragmentActivity.EXTRA_TASK_TYPE, TaskType.TEACHER_LEAVE_INFO.getValue());
                getAppListener().openActivity(TeacherTaskActivity.class, bundle);
                break;*/
        }
    }

}
