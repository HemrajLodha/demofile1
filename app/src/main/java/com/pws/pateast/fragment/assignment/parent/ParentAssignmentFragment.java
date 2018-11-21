package com.pws.pateast.fragment.assignment.parent;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.view.BaseRecyclerView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pws.pateast.BuildConfig;
import com.pws.pateast.Constants;
import com.pws.pateast.R;
import com.pws.pateast.activity.tasks.ParentTaskActivity;
import com.pws.pateast.activity.tasks.StudentTaskActivity;
import com.pws.pateast.api.model.Assignment;
import com.pws.pateast.api.model.Student;
import com.pws.pateast.api.model.Subject;
import com.pws.pateast.api.model.Tag;
import com.pws.pateast.api.model.TeacherClass;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.base.Extras;
import com.pws.pateast.base.FragmentActivity;
import com.pws.pateast.enums.DownloadingStatus;
import com.pws.pateast.enums.TaskType;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.fragment.assignment.AssignmentDownloadService;
import com.pws.pateast.fragment.presenter.SubjectPresenter;
import com.pws.pateast.fragment.presenter.SubjectView;
import com.pws.pateast.fragment.presenter.TagPresenter;
import com.pws.pateast.fragment.presenter.TagView;
import com.pws.pateast.utils.PermissionUtils;
import com.pws.pateast.widget.FooterLoadingView;
import com.pws.pateast.widget.FooterProgressBar;
import com.pws.pateast.widget.SpaceItemDecoration;
import com.pws.pateast.widget.assignmentfilterview.AssignmentFilterView;
import com.pws.pateast.widget.assignmentfilterview.DropdownListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by intel on 15-May-17.
 */

public class ParentAssignmentFragment extends AppFragment
        implements ParentAssignmentView, SubjectView, TagView,
        BaseRecyclerAdapter.OnItemClickListener,
        XRecyclerView.LoadingListener,
        View.OnClickListener,
        DropdownListener {
    private BaseRecyclerView rvAssignment;
    private ParentAssignmentAdapter assignmentAdapter;
    private FloatingActionButton fabAdd;
    private ParentAssignmentPresenter assignmentPresenter;
    private SubjectPresenter subjectPresenter;
    private TagPresenter tagPresenter;
    private AssignmentFilterView assignmentFilterView;
    private List<Subject> subjects;
    private List<Tag> tags;

    private int page;
    private int pageCount;
    private String mDueDate;
    private int mSubjectId;
    private int mTagId;
    private boolean mResetFilter;

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
        return R.layout.fragment_parent_assignment;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        getAppListener().setTitle(R.string.menu_my_assignments);
        fabAdd = (FloatingActionButton) findViewById(R.id.fab_add_assignment);
        assignmentFilterView = (AssignmentFilterView) findViewById(R.id.filter_view);

        rvAssignment = (BaseRecyclerView) findViewById(R.id.rv_assignment);
        rvAssignment.addItemDecoration(new SpaceItemDecoration(getContext(), R.dimen.size_5, 1, true));
        rvAssignment.setUpAsList();
        rvAssignment.setLoadingMoreEnabled(false);
        rvAssignment.setPullRefreshEnabled(false);
        rvAssignment.setLoadingListener(this);

        FooterProgressBar footerLoadingView = new FooterProgressBar(getContext());
        rvAssignment.setFootView(footerLoadingView);

        assignmentPresenter = new ParentAssignmentPresenter();
        assignmentPresenter.attachView(this);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mDownloadCompleteReceiver,
                new IntentFilter(Constants.DOWNLOAD_BROADCAST_ACTION));
        onActionClick();

        subjectPresenter = new SubjectPresenter(true);
        subjectPresenter.attachView(this);

        tagPresenter = new TagPresenter(true);
        tagPresenter.attachView(this);

        assignmentFilterView.setDropdownListener(this);
        assignmentFilterView.setVisibility(View.GONE); // hide filter view for first time
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
                if (mResetFilter) {
                    mSubjectId = 0;
                    mTagId = 0;
                    mDueDate = null;
                    if (assignmentPresenter != null)
                        assignmentPresenter.getStudentAssignment(mSubjectId, mTagId, null);
                }
                assignmentFilterView.setVisibility(assignmentFilterView.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
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
    public void setAssignmentAdapter(List<Assignment> assignments) {
        if (rvAssignment.getAdapter() == null) {
            assignmentAdapter = new ParentAssignmentAdapter(getContext(), this);
            rvAssignment.setAdapter(assignmentAdapter);
        }
        assignmentAdapter.clear();
        assignmentAdapter.add(assignments);
        if (page < pageCount)
            addPage();
        else
            rvAssignment.setLoadingMoreEnabled(false);
        rvAssignment.loadMoreComplete();
    }

    @Override
    public void onRefresh() {
        rvAssignment.setLoadingMoreEnabled(true);
        if (assignmentAdapter != null)
            assignmentAdapter.clear();
        if (assignmentPresenter != null)
            assignmentPresenter.getStudentAssignment(mSubjectId, mTagId, mDueDate);

    }

    @Override
    public void onLoadMore() {
        if (assignmentPresenter != null)
            assignmentPresenter.getStudentAssignment(mSubjectId, mTagId, mDueDate);
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
            default:
                Bundle b = new Bundle();
                b.putParcelable(Extras.EXTRA_DATA, assignment);
                b.putInt(FragmentActivity.EXTRA_TASK_TYPE, TaskType.ASSIGNMENT_DETAIL.getValue());
                switch (UserType.getUserType(assignmentPresenter.getUser().getData().getUser_type())) {
                    case STUDENT:
                        getAppListener().openActivity(StudentTaskActivity.class, b);
                        break;
                    case PARENT:
                        getAppListener().openActivity(ParentTaskActivity.class, b);
                        break;
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
        try {

            Uri fileURI = FileProvider.getUriForFile(getContext(),
                    Constants.FILE_AUTHORITY,
                    file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(fileURI, type);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            getActivity().startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getContext(), R.string.file_open_issue_message, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_subject:
                if (this.subjects == null) {
                    assignmentPresenter.getSubjectsByStudent();
                } else {
                    assignmentFilterView.setSubjectsAdapter(this.subjects);
                }
                break;
            case R.id.tv_remark:
                if (this.tags == null) {
                    assignmentPresenter.getTags();
                } else {
                    assignmentFilterView.setTagsAdapter(this.tags);
                }
                break;
        }
    }

    @Override
    public void getSubjects(int bcsMapId, int userId, int masterId, int sessionId) {
        subjectPresenter.getSubjectsByStudent(bcsMapId, userId, masterId, sessionId);
    }

    @Override
    public void getTags(int masterId, int type) {
        tagPresenter.getTags(masterId, type);
    }

    @Override
    public void onError(String error) {
        if (assignmentAdapter != null)
            assignmentAdapter.clear();
    }

    @Override
    public void onSubjectItemClick(View view, int position) {
        Subject subject = subjects.get(position);
        mSubjectId = subject.getSubject().getId();
        mResetFilter = true;
        if (assignmentPresenter != null)
            assignmentPresenter.getStudentAssignment(mSubjectId, mTagId, mDueDate);
    }

    @Override
    public void onRemarkItemClick(View view, int position) {
        Tag tag = tags.get(position);
        mTagId = tag.getId();
        mResetFilter = true;
        if (assignmentPresenter != null)
            assignmentPresenter.getStudentAssignment(mSubjectId, mTagId, mDueDate);
    }

    @Override
    public void onDateChange(View view, Calendar time, String timeString) {
        mDueDate = timeString;
        mResetFilter = true;
        if (assignmentPresenter != null)
            assignmentPresenter.getStudentAssignment(mSubjectId, mTagId, mDueDate);
    }

    @Override
    public void setSubject(Subject subject) {

    }

    @Override
    public void setSubjectAdapter(List<Subject> subjects) {
        assignmentFilterView.setSubjectsAdapter(this.subjects = subjects);
    }

    @Override
    public void setTagData(ArrayList<Tag> data) {
        assignmentFilterView.setTagsAdapter(this.tags = data);
    }

    @Override
    public void setStudent(Student student) {
        // Have Fun
    }

    @Override
    public void setStudentAdapter(List<Student> students) {
        // Have Fun
    }

    @Override
    public void setClass(TeacherClass classes) {
        // Have Fun
    }

    @Override
    public void setClassAdapter(List<TeacherClass> classes) {
        // Have Fun
    }


}
