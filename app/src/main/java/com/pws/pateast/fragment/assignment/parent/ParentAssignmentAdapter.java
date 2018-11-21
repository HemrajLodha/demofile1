package com.pws.pateast.fragment.assignment.parent;

import android.content.Context;
import android.net.Uri;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.adapter.viewholder.BaseItemViewHolder;
import com.github.clans.fab.FloatingActionButton;
import com.pws.pateast.R;
import com.pws.pateast.api.model.Assignment;
import com.pws.pateast.download.DownloadItemHelper;
import com.pws.pateast.download.DownloadableResult;
import com.pws.pateast.enums.DownloadingStatus;
import com.pws.pateast.utils.DateUtils;
import com.pws.pateast.utils.FileUtils;
import com.pws.pateast.utils.Utils;

import java.io.File;
import java.util.Locale;

/**
 * Created by intel on 15-May-17.
 */

public class ParentAssignmentAdapter extends BaseRecyclerAdapter<Assignment, ParentAssignmentAdapter.AssignmentHolder> {

    private static final String TAG = ParentAssignmentAdapter.class.getSimpleName();

    public ParentAssignmentAdapter(Context context, OnItemClickListener itemClickListener) {
        super(context, itemClickListener);
    }

    @Override
    protected int getItemResourceLayout(int viewType) {
        return R.layout.item_parent_assignment;
    }

    @Override
    public AssignmentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AssignmentHolder(getView(parent, viewType), mItemClickListener);
    }

    class AssignmentHolder extends BaseItemViewHolder<Assignment> {
        private TextView tvAssignmentTitle, tvSubject, tvRemark, tvEndDate;
        private LinearLayout layoutRemark;
        private FloatingActionButton imgDownload;

        public AssignmentHolder(View itemView, OnItemClickListener itemClickListener) {
            super(itemView, itemClickListener);
            tvAssignmentTitle = (TextView) findViewById(R.id.tv_assignment_title);
            tvSubject = (TextView) findViewById(R.id.tv_subject);
            tvRemark = (TextView) findViewById(R.id.tv_remark);
            tvEndDate = (TextView) findViewById(R.id.tv_end_date);
            imgDownload = (FloatingActionButton) findViewById(R.id.img_download);
            layoutRemark = (LinearLayout) findViewById(R.id.layout_remark);
            imgDownload.setOnClickListener(this);
        }

        @Override
        public void bind(Assignment assignment) {
            tvAssignmentTitle.setText(assignment.getAssignmentdetails().get(0).getTitle());
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
                    DateUtils.toTime(DateUtils.parse(assignment.getStart_date(), DateUtils.DATE_FORMAT_PATTERN), "dd MMM, yyyy", Locale.getDefault())
                            + " - " +
                            DateUtils.toTime(DateUtils.parse(assignment.getEnd_date(), DateUtils.DATE_FORMAT_PATTERN), "dd MMM, yyyy", Locale.getDefault()));

            setDownloadStatus(assignment);
        }

        private void setDownloadStatus(Assignment assignment) {
            DownloadableResult result = DownloadItemHelper.getDownloadedResult(getContext(), assignment.getId());
            DownloadingStatus downloadingStatus = DownloadingStatus.getValue(result.getDownloadStatus());
            assignment.setDownloadId(result.getId());

            if (downloadingStatus == DownloadingStatus.IN_PROGRESS) {
                setDownloadProgress(true);
                imgDownload.setProgress(result.getPercent(), true);
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
                    imgDownload.post(new Runnable() {
                        @Override
                        public void run() {
                            imgDownload.setProgress(100, false);
                            imgDownload.setImageResource(R.drawable.ic_insert_drive_file);
                            imgDownload.setProgress(0, false);
                        }
                    });
                }
                setDownloadProgress(false);
            } else {
                setDownloadProgress(false);
            }
            assignment.setDownloadingStatus(downloadingStatus);
        }

        private void setDownloadProgress(boolean enabled) {
            imgDownload.setShowProgressBackground(enabled);
            imgDownload.setIndeterminate(enabled);
        }
    }
}
