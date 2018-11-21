package com.pws.pateast.activity.leave;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.pws.pateast.R;
import com.pws.pateast.activity.tasks.DriverTaskActivity;
import com.pws.pateast.activity.tasks.ParentTaskActivity;
import com.pws.pateast.activity.tasks.StudentTaskActivity;
import com.pws.pateast.activity.tasks.TaskActivity;
import com.pws.pateast.activity.tasks.TeacherTaskActivity;
import com.pws.pateast.base.AppView;
import com.pws.pateast.base.FragmentActivity;
import com.pws.pateast.base.ResponseAppActivity;
import com.pws.pateast.enums.NotificationType;
import com.pws.pateast.enums.TaskType;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.model.LeaveCategory;
import com.pws.pateast.widget.FontTabLayout;

import java.util.List;

import static com.pws.pateast.fragment.assignment.teacher.add.AddAssignmentFragment.ADD_REQUEST;
import static com.pws.pateast.fragment.assignment.teacher.add.AddAssignmentFragment.ADD_RESPONSE;

/**
 * Created by intel on 28-Jun-17.
 */

public class LeaveStatusActivity extends ResponseAppActivity implements LeaveStatusView, View.OnClickListener, ViewPager.OnPageChangeListener {
    protected FontTabLayout tabLeave;
    private ViewPager pagerLeave;
    private FloatingActionButton fabApplyLeave;

    private LeaveStatusAdapter leaveStatusAdapter;

    private LeaveStatusPresenter leavePresenter;
    private AppView mLeaveView;
    private TaskType taskType;
    private NotificationType notificationType;

    @Override
    protected int getResourceLayout() {
        return R.layout.activity_leave;
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
    protected void onViewReady(Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);
        tabLeave = (FontTabLayout) findViewById(R.id.tab_leave);
        tabLeave.setBackgroundColor(ContextCompat.getColor(getContext(), tabBackground()));
        tabLeave.setSelectedTabIndicatorColor(ContextCompat.getColor(getContext(), tabIndicatorColor()));
        tabLeave.setTabTextColors(ContextCompat.getColor(getContext(), tabTextColor()), ContextCompat.getColor(getContext(), tabSelectedTextColor()));
        pagerLeave = (ViewPager) findViewById(R.id.pager_leave);

        fabApplyLeave = (FloatingActionButton) findViewById(R.id.fab_apply_leave);

        fabApplyLeave.setOnClickListener(this);

        leavePresenter = new LeaveStatusPresenter();
        leavePresenter.attachView(this);
        if (getIntent() != null) {
            taskType = TaskType.getTaskType(getIntent().getIntExtra(FragmentActivity.EXTRA_TASK_TYPE, TaskType.STUDENT_LEAVE_APPLY.getValue()));
            notificationType = NotificationType.getNotificationType(getIntent().getStringExtra(FragmentActivity.EXTRA_NOTIFICATION_TYPE));
            leavePresenter.setTaskType(taskType);
        } else {
            taskType = TaskType.STUDENT_LEAVE_APPLY;
            leavePresenter.setTaskType(TaskType.STUDENT_LEAVE_APPLY);
        }
        leavePresenter.setTitle();
        leavePresenter.setStatusAdapter();
    }

    @Override
    public void onActionClick() {
        super.onActionClick();
        if (mLeaveView != null)
            mLeaveView.onActionClick();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_apply_leave:
                leaveApply();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        leavePresenter.detachView();
    }

    @Override
    public int tabIndicatorColor() {
        return R.color.white;
    }

    @Override
    public int tabSelectedTextColor() {
        return R.color.white;
    }

    @Override
    public int tabTextColor() {
        return R.color.white;
    }

    @Override
    public int tabBackground() {
        return R.color.colorPrimary;
    }

    @Override
    public void leaveApply() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TaskActivity.EXTRA_TASK_TYPE, taskType);
        if (taskType == TaskType.PARENT_LEAVE_APPLY)
            openActivityForResult(ParentTaskActivity.class, bundle, ADD_REQUEST);
        else if (taskType == TaskType.STUDENT_LEAVE_APPLY)
            openActivityForResult(StudentTaskActivity.class, bundle, ADD_REQUEST);
        else if (taskType == TaskType.EMPLOYEE_LEAVE_APPLY) {
            if (leavePresenter.getUserType() == UserType.TEACHER)
                openActivityForResult(TeacherTaskActivity.class, bundle, ADD_REQUEST);
            else
                openActivityForResult(DriverTaskActivity.class, bundle, ADD_REQUEST);
        } else
            openActivityForResult(TaskActivity.class, bundle, ADD_REQUEST);
    }

    @Override
    public void setApplyVisible(boolean visible) {
        fabApplyLeave.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setStatusAdapter(final List<LeaveCategory> categories) {
        leaveStatusAdapter = new LeaveStatusAdapter(getBaseFragmentManager(), leavePresenter.getLeaveFragments(categories, getIntent().getExtras()));
        pagerLeave.setAdapter(leaveStatusAdapter);
        tabLeave.setupWithViewPager(pagerLeave, true);
        pagerLeave.post(new Runnable() {
            @Override
            public void run() {
                onPageSelected(pagerLeave.getCurrentItem());
                pagerLeave.addOnPageChangeListener(LeaveStatusActivity.this);
                if (notificationType != null) {
                    setFragmentSelectionByNotification(categories);
                }
            }
        });
    }

    private void setFragmentSelectionByNotification(final List<LeaveCategory> categories) {
        com.pws.pateast.enums.LeaveType leaveType = com.pws.pateast.enums.LeaveType.NONE;
        switch (notificationType) {
            case LEAVE_REJECTED:
                leaveType = com.pws.pateast.enums.LeaveType.REJECTED;
                break;
            case LEAVE_APPROVED:
                leaveType = com.pws.pateast.enums.LeaveType.APPROVED;
                break;
            case NONE:
            default:
                break;
        }
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getLeaveType() == leaveType) {
                pagerLeave.setCurrentItem(i);
                break;
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (leaveStatusAdapter != null) {
            mLeaveView = (AppView) leaveStatusAdapter.getItem(position);
            mLeaveView.onActionClick();
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
                if (resultCode == ADD_RESPONSE && mLeaveView != null) {
                    mLeaveView.onActionClick();
                }
                break;
        }
    }
}
