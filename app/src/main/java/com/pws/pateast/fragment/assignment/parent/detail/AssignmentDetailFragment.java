package com.pws.pateast.fragment.assignment.parent.detail;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.pws.pateast.Constants;
import com.pws.pateast.R;
import com.pws.pateast.api.model.Assignment;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.base.Extras;
import com.pws.pateast.download.DownloadItemHelper;
import com.pws.pateast.download.DownloadableResult;
import com.pws.pateast.enums.DownloadingStatus;
import com.pws.pateast.fragment.assignment.AssignmentDownloadService;
import com.pws.pateast.utils.DateUtils;
import com.pws.pateast.utils.FileUtils;
import com.pws.pateast.utils.MediaHelper;
import com.pws.pateast.utils.PermissionUtils;
import com.pws.pateast.utils.Utils;

import java.io.File;
import java.util.Locale;

/**
 * Created by intel on 15-May-17.
 */

public class AssignmentDetailFragment extends AppFragment
        implements AssignmentDetailView, View.OnClickListener {
    private static final String TAG = AssignmentDetailFragment.class.getSimpleName();
    private TextView tvSubject, tvRemark, tvEndDate;
    private LinearLayout layoutRemark;
    private TextView tvDescription;
    private FloatingActionButton imgDownload;
    private Assignment assignment;
    private AssignmentDetailPresenter mPresenter;


    BroadcastReceiver mDownloadCompleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int status = intent.getIntExtra(AssignmentDownloadService.EXTRA_DATA, DownloadManager.STATUS_FAILED);
            switch (status) {
                case DownloadManager.STATUS_SUCCESSFUL:
                    assignment = intent.getParcelableExtra(AssignmentDownloadService.EXTRA_DATA);
                    break;
                case DownloadManager.STATUS_FAILED:
                case DownloadManager.ERROR_FILE_ERROR:
                case DownloadManager.STATUS_PENDING:
                case DownloadManager.STATUS_RUNNING:
                case DownloadManager.STATUS_PAUSED:
                default:

                    break;
            }
            setDownloadButtonData(assignment);
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_download:
                downloadAssignment();
                break;
        }

    }

    private void downloadAssignment() {
        if (PermissionUtils.checkForFragmentPermission(this, PermissionUtils.PERMISSION_READ_STORAGE, PermissionUtils.PERMISSION_READ_STORAGE_REQ)) {
            //Only when the icon is in not downloaded state, then do the following.
            if (assignment.getDownloadingStatus() == DownloadingStatus.NOT_DOWNLOADED) {
                assignment.setDownloadingStatus(DownloadingStatus.WAITING);
                Bundle bundle = new Bundle();
                bundle.putParcelable(AssignmentDownloadService.EXTRA_DATA, assignment);
                getAppListener().startService(AssignmentDownloadService.class, bundle);
            } else if (assignment.getDownloadingStatus() == DownloadingStatus.DOWNLOADED) {
                openAssignmentFile(assignment.getId());
            }
        }
    }

    public void openAssignmentFile(int itemId) {
        Uri uri = DownloadItemHelper.getDownloadedUri(getContext(), itemId);
        try {
            File file = FileUtils.getFile(getContext(), uri);
            Uri fileURI = FileProvider.getUriForFile(getContext(),
                    Constants.FILE_AUTHORITY,
                    file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(fileURI, FileUtils.getMimeType(getContext(), uri));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            getActivity().startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getContext(), R.string.file_open_issue_message, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermissionUtils.PERMISSION_READ_STORAGE_REQ && grantResults.length == permissions.length) {
            Log.i(TAG, "permissions granted");
            downloadAssignment();
        } else {
            Log.i(TAG, "permissions denied");
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void setData(Assignment assignment) {
        tvSubject.setText(assignment.getSubject().getSubjectdetails().get(0).getName());
        if (assignment.getAssignmentremarks() != null && assignment.getAssignmentremarks().size() != 0 &&
                assignment.getAssignmentremarks().get(0).getTag() != null) {
            tvRemark.setText(assignment.getAssignmentremarks().get(0).getTag().getTagdetails().get(0).getTitle());
            layoutRemark.setVisibility(View.VISIBLE);
        } else {
            layoutRemark.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(assignment.getAssignment_file_name())) {
            SpannableString content = new SpannableString(getString(R.string.file_name, assignment.getAssignment_file_name(), Utils.readableFileSize(assignment.getAssignment_size(), true)));
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            imgDownload.setVisibility(View.VISIBLE);
        } else {
            imgDownload.setVisibility(View.GONE);
        }

        tvEndDate.setText(
                DateUtils.toTime(DateUtils.parse(assignment.getStart_date(), DateUtils.DATE_FORMAT_PATTERN), mPresenter.getDateFormat(), Locale.getDefault())
                        + " - " +
                        DateUtils.toTime(DateUtils.parse(assignment.getEnd_date(), DateUtils.DATE_FORMAT_PATTERN), mPresenter.getDateFormat(), Locale.getDefault()));
        tvDescription.setText(Utils.fromHtml(assignment.getAssignmentdetails().get(0).getComment()));
        setDownloadButtonData(assignment);
    }

    /**
     * set download button status
     *
     * @param assignment
     */
    private void setDownloadButtonData(Assignment assignment) {
        DownloadableResult result = DownloadItemHelper.getDownloadedResult(getContext(), assignment.getId());
        DownloadingStatus downloadingStatus = DownloadingStatus.getValue(result.getDownloadStatus());
        if (downloadingStatus == DownloadingStatus.IN_PROGRESS) {
            //setDownloadProgress(true);
            imgDownload.post(new Runnable() {
                @Override
                public void run() {
                    imgDownload.setIndeterminate(true);
                    imgDownload.invalidate();
                }
            });
            //imgDownload.setProgress(result.getPercent(), true);
        } else if (downloadingStatus != DownloadingStatus.NOT_DOWNLOADED &&
                downloadingStatus != DownloadingStatus.WAITING) {
            boolean downloaded = false;
            if (!TextUtils.isEmpty(assignment.getAssignment_file()) &&
                    !assignment.getAssignment_file().equals(result.getFileUrl())) {
                downloadingStatus = DownloadingStatus.NOT_DOWNLOADED;
            } else if (!TextUtils.isEmpty(result.getFileUri())) {
                Uri fileUri = Uri.parse(result.getFileUri());
                File file = FileUtils.getFile(getContext(), fileUri);
                if (file != null && !file.exists()) {
                    downloadingStatus = DownloadingStatus.NOT_DOWNLOADED;
                } else {
                    downloaded = true;
                }
            } else if (!TextUtils.isEmpty(assignment.getAssignment_file_name()) &&
                    !assignment.getAssignment_file_name().equals(result.getFileName())) {
                downloadingStatus = DownloadingStatus.NOT_DOWNLOADED;
            } else {
                downloaded = true;
            }
            if (downloaded) {
                //imgDownload.setProgress(100, true);
                imgDownload.setImageResource(R.drawable.ic_insert_drive_file);
                //imgDownload.setProgress(0, false);
                imgDownload.setIndeterminate(false);
            }
            //setDownloadProgress(false);
        } else {
            //setDownloadProgress(false);
        }
        assignment.setDownloadingStatus(downloadingStatus);
    }

    private void setDownloadProgress(boolean enabled) {
        imgDownload.setShowProgressBackground(enabled);
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_assignment_detail;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        mPresenter = new AssignmentDetailPresenter();
        mPresenter.attachView(this);
        tvSubject = (TextView) findViewById(R.id.tv_subject);
        tvRemark = (TextView) findViewById(R.id.tv_remark);
        tvEndDate = (TextView) findViewById(R.id.tv_end_date);
        imgDownload = (FloatingActionButton) findViewById(R.id.img_download);
        layoutRemark = (LinearLayout) findViewById(R.id.layout_remark);
        imgDownload.setOnClickListener(this);
        tvDescription = (TextView) findViewById(R.id.tv_description);
        imgDownload = (FloatingActionButton) findViewById(R.id.img_download);
        imgDownload.setOnClickListener(this);
        if (getArguments() != null) {
            assignment = getArguments().getParcelable(Extras.EXTRA_DATA);
            if (assignment != null) {
                getAppListener().setTitle(assignment.getAssignmentdetails().get(0).getTitle());
                setData(assignment);
            }
        }
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mDownloadCompleteReceiver,
                new IntentFilter(Constants.DOWNLOAD_BROADCAST_ACTION));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mDownloadCompleteReceiver);
    }

    @Override
    public void viewAssignmentFile(Uri uri, String type) {
        Intent intent = MediaHelper.dispatchOpenFileIntent(getContext(), uri, type);
        if (intent != null) {
            startActivity(intent);
        }
    }
}
