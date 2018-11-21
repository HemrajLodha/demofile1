package com.pws.pateast.fragment.notification;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.view.View;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.view.BaseRecyclerView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pws.pateast.R;
import com.pws.pateast.activity.leave.LeaveStatusView;
import com.pws.pateast.activity.tasks.ParentTaskActivity;
import com.pws.pateast.activity.tasks.TaskActivity;
import com.pws.pateast.api.model.Leave;
import com.pws.pateast.api.model.Notification;
import com.pws.pateast.api.model.NotificationData;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.base.Extras;
import com.pws.pateast.enums.TaskType;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.fragment.leave.LeaveAdapter;
import com.pws.pateast.listener.AdapterListener;
import com.pws.pateast.utils.DialogUtils;
import com.pws.pateast.utils.NotificationHelper;
import com.pws.pateast.widget.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

import static com.pws.pateast.fragment.assignment.teacher.add.AddAssignmentFragment.ADD_REQUEST;
import static com.pws.pateast.fragment.assignment.teacher.add.AddAssignmentFragment.ADD_RESPONSE;

/**
 * Created by intel on 18-Aug-17.
 */

public class ParentNotificationListFragment extends AppFragment implements ParentNotificationListView, BaseRecyclerAdapter.OnItemClickListener, XRecyclerView.LoadingListener {
    private BaseRecyclerView rvLeave;
    private ParentNotificationListPresenter mPresenter;

    private NotificationListAdapter notificationListAdapter;

    private int page;
    private int pageCount;

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_recycler_view;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        getAppListener().setTitle(R.string.menu_notifications);
        rvLeave = (BaseRecyclerView) findViewById(R.id.rv_leave);
        rvLeave.setUpAsList();
        rvLeave.addItemDecoration(new SpaceItemDecoration(getContext(), R.dimen.size_8, 1, true));
        rvLeave.setPullRefreshEnabled(false);
        rvLeave.setLoadingListener(this);

        mPresenter = new ParentNotificationListPresenter();
        mPresenter.attachView(this);
        onActionClick();
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
    public void setListAdapter(List<NotificationData> notifications) {
        if (rvLeave.getAdapter() == null) {
            notificationListAdapter = new NotificationListAdapter(getContext(), this);
            rvLeave.setAdapter(notificationListAdapter);
        }
        notificationListAdapter.addOrUpdate(notifications);
        if (page < pageCount)
            addPage();
        else
            rvLeave.setLoadingMoreEnabled(false);
        rvLeave.loadMoreComplete();
    }


    @Override
    public void onRefresh() {
        rvLeave.setLoadingMoreEnabled(true);
        if (notificationListAdapter != null)
            notificationListAdapter.clear();
        if (mPresenter != null) {
            page = 1;
            mPresenter.getNotificationList();
        }
    }

    @Override
    public void onLoadMore() {
        if (mPresenter != null)
            mPresenter.getNotificationList();
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
        if (mPresenter != null)
            mPresenter.detachView();
    }

    @Override
    public void onItemClick(View view, int position) {
        NotificationData item = notificationListAdapter.getItem(position);
        mPresenter.setNotificationStatus(item, position, 1);
    }

    @Override
    public void onStatusChange(int position) {
        NotificationData item = notificationListAdapter.getItem(position);
        item.setStatus(1);
        notificationListAdapter.addOrUpdate(item);
        NotificationHelper.showNotificationIntent(getContext(), item.getNotification().getType(),false);
    }
}
