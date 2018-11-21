package com.pws.pateast.fragment.leave.teacher.approve;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.view.BaseRecyclerView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pws.pateast.R;
import com.pws.pateast.api.model.Leave;
import com.pws.pateast.base.AppDialogFragment;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.base.Extras;
import com.pws.pateast.fragment.leave.teacher.filter.LeaveFilterFragment;
import com.pws.pateast.listener.AdapterListener;
import com.pws.pateast.utils.DialogUtils;
import com.pws.pateast.widget.SpaceItemDecoration;

import java.util.List;

/**
 * Created by intel on 29-Jun-17.
 */

public class ClassLeaveFragment extends AppFragment implements ClassLeaveView, BaseRecyclerAdapter.OnItemClickListener, XRecyclerView.LoadingListener {
    private BaseRecyclerView rvLeave;
    private ClassLeavePresenter leavePresenter;

    private ClassLeaveAdapter leaveAdapter;
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

        leavePresenter = new ClassLeavePresenter();
        leavePresenter.attachView(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.filter_class_leave, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_filter:
                LeaveFilterFragment filterFragment = AppDialogFragment.newInstance(LeaveFilterFragment.class, getAppListener());
                filterFragment.attachView(this);
                filterFragment.show(getFragmentManager(), LeaveFilterFragment.class.getSimpleName());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected boolean hasOptionMenu() {
        return false;
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
    public void setFilter(String studentName, String startDate, String endDate) {
        getArguments().putString(Extras.STUDENT_NAME, studentName);
        getArguments().putString(Extras.START_DATE, startDate);
        getArguments().putString(Extras.END_DATE, endDate);
        onActionClick();
    }

    @Override
    public void clearFilter() {
        getArguments().putString(Extras.STUDENT_NAME, null);
        getArguments().putString(Extras.START_DATE, null);
        getArguments().putString(Extras.END_DATE, null);
        onActionClick();
    }

    @Override
    public String getStudentName() {
        return getArguments().getString(Extras.STUDENT_NAME);
    }

    @Override
    public String getStartDate() {
        return getArguments().getString(Extras.START_DATE);
    }

    @Override
    public String getEndDate() {
        return getArguments().getString(Extras.END_DATE);
    }

    @Override
    public String getLeaveStatus() {
        return getArguments().getString(Extras.STATUS);
    }

    @Override
    public void leaveApply() {

    }

    @Override
    public void setApplyVisible(boolean visible) {

    }


    @Override
    public void setLeaveAdapter(List<Leave> leaves) {
        if (rvLeave.getAdapter() == null) {
            leaveAdapter = new ClassLeaveAdapter(getContext(), this);
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
            leavePresenter.getClassLeave();
        }
    }

    @Override
    public void onLoadMore() {
        if (leavePresenter != null) {
            leavePresenter.getClassLeave();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        final Leave leave = leaveAdapter.getItem(position);
        switch (view.getId()) {
            case R.id.tv_options_approve:
                DialogUtils.showDialog(getContext(), R.string.app_name, R.string.approve_prompt_message, new AdapterListener<String>() {
                    @Override
                    public void onClick(int id, String value) {
                        switch (id) {
                            case 0:
                                leavePresenter.changeStudentLeaveStatus(leave.getId(), Leave.APPROVED);
                                break;
                        }
                    }
                }, R.string.yes, R.string.no);
                break;
            case R.id.tv_options_reject:
                DialogUtils.showDialog(getContext(), R.string.app_name, R.string.reject_prompt_message, new AdapterListener<String>() {
                    @Override
                    public void onClick(int id, String value) {
                        switch (id) {
                            case 0:
                                leavePresenter.changeStudentLeaveStatus(leave.getId(), Leave.REJECTED);
                                break;
                        }
                    }
                }, R.string.yes, R.string.no);
                break;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (leavePresenter != null)
            leavePresenter.detachView();
    }
}
