package com.pws.pateast.fragment.assignment.student;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.view.BaseRecyclerView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pws.pateast.api.model.Assignment;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.enums.DownloadingStatus;
import com.pws.pateast.fragment.assignment.AssignmentDownloadService;
import com.pws.pateast.utils.PermissionUtils;
import com.pws.pateast.Constants;
import com.pws.pateast.R;
import com.pws.pateast.base.AppDialogFragment;
import com.pws.pateast.fragment.assignment.student.filter.StudentAssignmentFilter;

import java.io.File;
import java.util.List;

/**
 * Created by intel on 15-May-17.
 */

public class StudentAssignmentFragment extends AppFragment
        implements StudentAssignmentView,
        BaseRecyclerAdapter.OnItemClickListener, XRecyclerView.LoadingListener {
    private BaseRecyclerView rvAssignment;
    private StudentAssignmentAdapter assignmentAdapter;

    private FloatingActionButton fabAdd;

    private StudentAssignmentPresenter assignmentPresenter;

    private int page;
    private int pageCount;

    BroadcastReceiver mDownloadCompleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int status = intent.getIntExtra(AssignmentDownloadService.EXTRA_DOWNLOAD_STATUS, DownloadManager.STATUS_FAILED);
            switch (status) {
                case DownloadManager.STATUS_SUCCESSFUL:
                    Assignment assignment = intent.getParcelableExtra(AssignmentDownloadService.EXTRA_DATA);
                    if (assignment != null && assignmentAdapter != null) {
                        assignmentAdapter.addOrUpdate(assignment);
                    }
                    break;
                case DownloadManager.STATUS_FAILED:
                case DownloadManager.ERROR_FILE_ERROR:
                case DownloadManager.STATUS_PENDING:
                case DownloadManager.STATUS_RUNNING:
                case DownloadManager.STATUS_PAUSED:
                default:
                    if (assignmentAdapter != null) {
                        assignmentAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_student_assignment;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        getAppListener().setTitle(R.string.menu_my_assignments);
        rvAssignment = (BaseRecyclerView) findViewById(R.id.rv_assignment);
        fabAdd = (FloatingActionButton) findViewById(R.id.fab_add_assignment);
        rvAssignment.setUpAsList();
        rvAssignment.setPullRefreshEnabled(false);
        rvAssignment.setLoadingListener(this);

        assignmentPresenter = new StudentAssignmentPresenter();
        assignmentPresenter.attachView(this);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mDownloadCompleteReceiver,
                new IntentFilter(Constants.DOWNLOAD_BROADCAST_ACTION));
        onActionClick();

       /* getContext().getContentResolver().delete(DatabaseContract.TableDownloads.CONTENT_URI, null, null);
        if (Utils.isMyServiceRunning(getContext(), AssignmentDownloadService.class)) {
            getActivity().stopService(new Intent(getContext(), AssignmentDownloadService.class));
        }*/

    }

    @Override
    protected boolean hasOptionMenu() {
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.filter_assignment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_filter:
                StudentAssignmentFilter filterFragment = AppDialogFragment.newInstance(StudentAssignmentFilter.class, getAppListener());
                filterFragment.attach(this);
                filterFragment.show(getFragmentManager(), StudentAssignmentFilter.class.getSimpleName());
                return true;
        }
        return super.onOptionsItemSelected(item);
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
    public void setFilter(String subjectTitle, String assignmentTitle) {
        setSubjectTitle(subjectTitle);
        setAssignmentTitle(assignmentTitle);
        onActionClick();
    }

    @Override
    public void clearFilter() {
        setSubjectTitle(null);
        setAssignmentTitle(null);
        onActionClick();
    }

    @Override
    public void setAssignmentAdapter(List<Assignment> assignments) {
        if (rvAssignment.getAdapter() == null) {
            assignmentAdapter = new StudentAssignmentAdapter(getContext(), this);
            rvAssignment.setAdapter(assignmentAdapter);
        }
        assignmentAdapter.addOrUpdate(assignments);
        if(page<pageCount)
            addPage();
        else
            rvAssignment.setLoadingMoreEnabled(false);
        rvAssignment.loadMoreComplete();
    }

    @Override
    public void onRefresh() {
        rvAssignment.setLoadingMoreEnabled(true);
        if(assignmentAdapter != null)
            assignmentAdapter.clear();
        if(assignmentPresenter != null)
        {
            page = 1;
            assignmentPresenter.getStudentAssignment(getSubjectTitle(), getAssignmentTitle());
        }

    }

    @Override
    public void onLoadMore() {
        if(assignmentPresenter != null)
            assignmentPresenter.getStudentAssignment(getSubjectTitle(), getAssignmentTitle());
    }

    @Override
    public void setSubjectTitle(String subjectTitle) {
        getArguments().putString("subjectTitle", subjectTitle);
    }

    @Override
    public String getSubjectTitle() {
        return getArguments().getString("subjectTitle");
    }

    @Override
    public void setAssignmentTitle(String assignmentTitle) {
        getArguments().putString("assignmentTitle", assignmentTitle);
    }

    @Override
    public String getAssignmentTitle() {
        return getArguments().getString("assignmentTitle");
    }

    @Override
    public void onItemClick(View view, int position) {
        Assignment assignment = assignmentAdapter.getItem(position);
        switch (view.getId()) {
            case R.id.img_download:
                if (PermissionUtils.checkForFragmentPermission(this, PermissionUtils.PERMISSION_READ_STORAGE, PermissionUtils.PERMISSION_READ_STORAGE_REQ)) {
                    //Only when the icon is in not downloaded state, then do the following.
                    if (assignment.getDownloadingStatus() == DownloadingStatus.NOT_DOWNLOADED) {
                        assignment.setDownloadingStatus(DownloadingStatus.WAITING);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(AssignmentDownloadService.EXTRA_DATA, assignment);
                        getAppListener().startService(AssignmentDownloadService.class, bundle);
                    } else if (assignment.getDownloadingStatus() == DownloadingStatus.DOWNLOADED) {
                        assignmentPresenter.openAssignmentFile(assignment.getId());
                    }
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        assignmentPresenter.detachView();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mDownloadCompleteReceiver);
    }


    @Override
    public void viewAssignmentFile(File file, String type) {
        /*try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, type);
            getActivity().startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getContext(), R.string.file_open_issue_message, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }*/
    }


}
