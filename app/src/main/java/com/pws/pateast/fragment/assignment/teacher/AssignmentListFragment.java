package com.pws.pateast.fragment.assignment.teacher;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.base.ui.view.BaseRecyclerView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pws.pateast.Constants;
import com.pws.pateast.R;
import com.pws.pateast.activity.assignment.AssignmentStatusView;
import com.pws.pateast.activity.tasks.TaskActivity;
import com.pws.pateast.activity.tasks.TeacherTaskActivity;
import com.pws.pateast.api.model.Assignment;
import com.pws.pateast.api.model.TeacherClass;
import com.pws.pateast.base.AppDialogFragment;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.base.Extras;
import com.pws.pateast.base.FragmentActivity;
import com.pws.pateast.enums.AssignmentType;
import com.pws.pateast.enums.DownloadingStatus;
import com.pws.pateast.enums.TaskType;
import com.pws.pateast.fragment.assignment.AssignmentDownloadService;
import com.pws.pateast.fragment.assignment.teacher.add.AddAssignmentFragment;
import com.pws.pateast.fragment.assignment.teacher.filter.AssignmentFilterFragment;
import com.pws.pateast.listener.AdapterListener;
import com.pws.pateast.scheduler.JobUtil;
import com.pws.pateast.utils.PermissionUtils;
import com.pws.pateast.utils.Utils;
import com.pws.pateast.widget.FooterProgressBar;
import com.pws.pateast.widget.SpaceItemDecoration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.pws.pateast.fragment.assignment.teacher.review.ReviewAssignmentFragment.REVIEW_REQUEST;

/**
 * Created by planet on 5/3/2017.
 */

public class AssignmentListFragment extends AppFragment
        implements AssignmentListView,
        AssignmentListAdapter.OnItemClickListener, XRecyclerView.LoadingListener {
    private BaseRecyclerView rvAssignment;
    private AssignmentListAdapter assignmentAdapter;

    private AssignmentListPresenter presenter;

    private int page;
    private int pageCount;


    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_assignment_list;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        getAppListener().setTitle(R.string.menu_assignments);

        rvAssignment = (BaseRecyclerView) findViewById(R.id.rv_assignment);
        rvAssignment.addItemDecoration(new SpaceItemDecoration(getContext(), R.dimen.size_5, 1, true));
        rvAssignment.setUpAsList();
        rvAssignment.setPullRefreshEnabled(false);
        rvAssignment.setLoadingListener(this);

        FooterProgressBar footerLoadingView = new FooterProgressBar(getContext());
        rvAssignment.setFootView(footerLoadingView);

        presenter = new AssignmentListPresenter();
        presenter.attachView(this);
    }

    @Override
    public void updateAssignmentStatus(int status, Assignment assignment) {
        switch (status) {
            case DownloadManager.STATUS_SUCCESSFUL:
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


    @Override
    protected boolean hasOptionMenu() {
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.filter_assignment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_filter:
                AssignmentFilterFragment filterFragment = AppDialogFragment.newInstance(AssignmentFilterFragment.class, getAppListener());
                filterFragment.attach(this);
                filterFragment.show(getFragmentManager(), AssignmentFilterFragment.class.getSimpleName());
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
    public void setMyClasses(List<TeacherClass> myClasses) {
        getArguments().putParcelableArrayList(Extras.CLASSES, (ArrayList<? extends Parcelable>) myClasses);
    }

    @Override
    public List<TeacherClass> getMyClasses() {
        return getArguments().getParcelableArrayList(Extras.CLASSES);
    }

    @Override
    public int getClassId() {
        return getArguments().getInt(Extras.CLASS_ID);
    }

    @Override
    public void setClassId(int bcsMapId) {
        getArguments().putInt(Extras.CLASS_ID, bcsMapId);
    }

    @Override
    public void reviewAssignment(Assignment assignment) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TaskActivity.EXTRA_TASK_TYPE, TaskType.REVIEW_ASSIGNMENT);
        bundle.putParcelable(AppFragment.EXTRA_DATA, assignment);
        getAppListener().openActivityForResult(TeacherTaskActivity.class, bundle, REVIEW_REQUEST);
    }

    @Override
    public void addAssignment(Assignment assignment) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TaskActivity.EXTRA_TASK_TYPE, TaskType.ADD_ASSIGNMENT);
        bundle.putParcelable(AppFragment.EXTRA_DATA, assignment);
        getAppListener().openActivityForResult(TeacherTaskActivity.class, bundle, AddAssignmentFragment.ADD_REQUEST);
    }

    @Override
    public void setAddVisible(boolean visible) {
        ((AssignmentStatusView) getActivity()).setAddVisible(visible);
    }

    @Override
    public void setFilter(int bcsMapId, String subject, String assignmentTitle) {
        setClassId(bcsMapId);
        setSubject(subject);
        setAssignmentTitle(assignmentTitle);
        onActionClick();
    }

    @Override
    public void clearFilter() {
        setClassId(0);
        setSubject(null);
        setAssignmentTitle(null);
        onActionClick();
    }

    @Override
    public void setAssignmentAdapter(List<Assignment> assignments, String dateFormat) {
        if (rvAssignment.getAdapter() == null) {
            assignmentAdapter = new AssignmentListAdapter(getContext());
            assignmentAdapter.setDateFormat(dateFormat);
            assignmentAdapter.setAssignmentType(AssignmentType.getAssignmentType(getStatus()));
            assignmentAdapter.setOnItemClickListener(this);
            rvAssignment.setAdapter(assignmentAdapter);
        }
        assignmentAdapter.addOrUpdate(assignments);
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
        if (presenter != null) {
            page = 1;
            presenter.getAssignment(getClassId(), getSubject(), getAssignmentTitle(), getStatus());
        }

    }

    @Override
    public void onLoadMore() {
        if (presenter != null)
            presenter.getAssignment(getClassId(), getSubject(), getAssignmentTitle(), getStatus());
    }

    @Override
    public void setSubject(String subject) {
        getArguments().putString(Extras.SUBJECT, subject);
    }

    @Override
    public String getSubject() {
        return getArguments().getString(Extras.SUBJECT);
    }

    @Override
    public void setAssignmentTitle(String assignmentTitle) {
        getArguments().putString(Extras.ASSIGNMENT_TITLE, assignmentTitle);
    }

    @Override
    public String getAssignmentTitle() {
        return getArguments().getString(Extras.ASSIGNMENT_TITLE);
    }

    @Override
    public String getStatus() {
        return getArguments().getString(Extras.STATUS, "");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void onItemClick(View view, int position) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            JobUtil.scheduleDownloadJob(getContext());
        }

        Assignment assignment = assignmentAdapter.getItem(position);
        switch (view.getId()) {
            case R.id.tv_options:
                PopupMenu popup = new PopupMenu(getContext(), view);
                presenter.setPopupMenu(popup, assignment.getAssignment_status());
                popup.setOnMenuItemClickListener(new OnMenuItemClickListener(assignment));
                popup.show();
                break;
            case R.id.img_download:
                if (PermissionUtils.checkForFragmentPermission(this, PermissionUtils.PERMISSION_READ_STORAGE, PermissionUtils.PERMISSION_READ_STORAGE_REQ)) {
                    //Only when the icon is in not downloaded state, then do the following.
                    if (assignment.getDownloadingStatus() == DownloadingStatus.NOT_DOWNLOADED) {
                        assignment.setDownloadingStatus(DownloadingStatus.WAITING);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(AssignmentDownloadService.EXTRA_DATA, assignment);
                        getAppListener().startService(AssignmentDownloadService.class, bundle);

                    } else if (assignment.getDownloadingStatus() == DownloadingStatus.DOWNLOADED) {
                        presenter.openAssignmentFile(assignment.getId());
                    }
                }
                break;
            default:
                Bundle b = new Bundle();
                b.putParcelable(Extras.EXTRA_DATA, assignment);
                b.putInt(FragmentActivity.EXTRA_TASK_TYPE, TaskType.ASSIGNMENT_DETAIL.getValue());
                getAppListener().openActivity(TeacherTaskActivity.class, b);
                break;
        }
    }

    @Override
    public void viewAssignmentFile(File file, String type) {
        if (PermissionUtils.checkForFragmentPermission(this, PermissionUtils.PERMISSION_READ_STORAGE, PermissionUtils.PERMISSION_READ_STORAGE_REQ)) {
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
    }

    class OnMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        private Assignment assignment;

        public OnMenuItemClickListener(Assignment assignment) {
            this.assignment = assignment;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {

            switch (item.getItemId()) {

                case R.id.menu_edit:
                    addAssignment(assignment);
                    break;
                case R.id.menu_delete:
                    showDialog(getString(R.string.menu_delete), getString(R.string.delete_prompt_message), new AdapterListener<String>() {
                                @Override
                                public void onClick(int id, String value) {
                                    if (id == 0) {
                                        presenter.deleteAssignment(assignment.getId());
                                    }
                                }
                            }, R.string.menu_delete, R.string.cancel
                    );
                    break;
                case R.id.menu_publish:
                    presenter.changeAssignmentStatus(assignment.getId(), AssignmentType.PUBLISHED.getValue());
                    break;
                case R.id.menu_cancel:
                    presenter.changeAssignmentStatus(assignment.getId(), AssignmentType.CANCELED.getValue());
                    break;
                case R.id.menu_review:
                    reviewAssignment(assignment);
                    break;
            }
            return true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case AddAssignmentFragment.ADD_REQUEST:
                if (resultCode == AddAssignmentFragment.ADD_RESPONSE) {
                    onActionClick();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermissionUtils.PERMISSION_READ_STORAGE_REQ) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Utils.showToast(getContext(), "Permission granted.");
            } else {
                Utils.showToast(getContext(), "Permission denied.");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
