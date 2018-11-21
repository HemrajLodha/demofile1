package com.pws.pateast.activity.assignment;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.pws.pateast.Constants;
import com.pws.pateast.R;
import com.pws.pateast.activity.tasks.TaskActivity;
import com.pws.pateast.activity.tasks.TeacherTaskActivity;
import com.pws.pateast.api.model.Assignment;
import com.pws.pateast.base.ResponseAppActivity;
import com.pws.pateast.enums.TaskType;
import com.pws.pateast.fragment.assignment.AssignmentDownloadService;
import com.pws.pateast.fragment.assignment.teacher.AssignmentListView;
import com.pws.pateast.model.AssignmentCategory;
import com.pws.pateast.utils.Utils;
import com.pws.pateast.widget.FontTabLayout;

import java.util.List;

import static com.pws.pateast.fragment.assignment.teacher.add.AddAssignmentFragment.ADD_REQUEST;
import static com.pws.pateast.fragment.assignment.teacher.add.AddAssignmentFragment.ADD_RESPONSE;
import static com.pws.pateast.fragment.assignment.teacher.review.ReviewAssignmentFragment.REVIEW_REQUEST;
import static com.pws.pateast.fragment.assignment.teacher.review.ReviewAssignmentFragment.REVIEW_RESPONSE;

/**
 * Created by intel on 12-Jun-17.
 */

public class AssignmentStatusActivity extends ResponseAppActivity implements AssignmentStatusView, ViewPager.OnPageChangeListener, View.OnClickListener {
    private FontTabLayout tabAssignment;
    private ViewPager pagerAssignment;
    private FloatingActionButton fabAdd;

    private AssignmentStatusAdapter assignmentStatusAdapter;

    private AssignmentStatusPresenter statusPresenter;
    private AssignmentListView mAssignmentView;

    BroadcastReceiver mDownloadCompleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int status = intent.getIntExtra(AssignmentDownloadService.EXTRA_DATA, DownloadManager.STATUS_FAILED);
            Assignment assignment = intent.getParcelableExtra(AssignmentDownloadService.EXTRA_DATA);
            if (mAssignmentView != null) {
                mAssignmentView.updateAssignmentStatus(status, assignment);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.TeacherTheme);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.activity_assignment_filter;
    }


    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);
        if (isToolbarSetupEnabled()) {
            getToolbar().setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryTeacher));
        }
        setTitle(R.string.menu_assignments);

        tabAssignment = findViewById(R.id.tab_assignment);
        pagerAssignment = findViewById(R.id.pager_assignment);
        fabAdd = findViewById(R.id.fab_add_assignment);

        fabAdd.setOnClickListener(this);

        statusPresenter = new AssignmentStatusPresenter();
        statusPresenter.attachView(this);
        statusPresenter.setStatusAdapter();

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mDownloadCompleteReceiver,
                new IntentFilter(Constants.DOWNLOAD_BROADCAST_ACTION));
    }

    @Override
    public void onActionClick() {
        super.onActionClick();
        if (mAssignmentView != null)
            mAssignmentView.onActionClick();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        statusPresenter.detachView();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mDownloadCompleteReceiver);
    }

    @Override
    public boolean isToolbarSetupEnabled() {
        return true;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void addAssignment() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TaskActivity.EXTRA_TASK_TYPE, TaskType.ADD_ASSIGNMENT);
        openActivityForResult(TeacherTaskActivity.class, bundle, ADD_REQUEST);
    }

    @Override
    public void setAddVisible(boolean visible) {
        if (fabAdd != null) {
            fabAdd.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void setStatusAdapter(List<AssignmentCategory> categories) {
        assignmentStatusAdapter = new AssignmentStatusAdapter(getBaseFragmentManager(), statusPresenter.getAssignmentFragments(categories, getIntent().getExtras()));
        pagerAssignment.setAdapter(assignmentStatusAdapter);
        tabAssignment.setupWithViewPager(pagerAssignment, true);
        pagerAssignment.post(new Runnable() {
            @Override
            public void run() {
                onPageSelected(pagerAssignment.getCurrentItem());
                pagerAssignment.addOnPageChangeListener(AssignmentStatusActivity.this);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_add_assignment:
                addAssignment();
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Utils.keyboard(getContext(), pagerAssignment, false);
        if (assignmentStatusAdapter != null) {
            mAssignmentView = assignmentStatusAdapter.getItem(position);
            if (mAssignmentView != null && mAssignmentView.isVisible())
                mAssignmentView.onActionClick();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ADD_REQUEST:
                if (resultCode == ADD_RESPONSE && mAssignmentView != null) {
                    mAssignmentView.onActionClick();
                }
                break;
            case REVIEW_REQUEST:
                if (resultCode == REVIEW_RESPONSE && mAssignmentView != null) {
                    mAssignmentView.onActionClick();
                }
                break;
        }
    }
}
