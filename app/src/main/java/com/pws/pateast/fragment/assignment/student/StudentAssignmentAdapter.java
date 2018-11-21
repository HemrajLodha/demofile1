package com.pws.pateast.fragment.assignment.student;

import android.content.Context;
import android.net.Uri;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.adapter.viewholder.BaseItemViewHolder;
import com.pws.pateast.api.model.Assignment;
import com.pws.pateast.download.DownloadableResult;
import com.pws.pateast.enums.DownloadingStatus;
import com.pws.pateast.utils.FileUtils;
import com.pws.pateast.utils.Utils;
import com.pws.pateast.R;
import com.pws.pateast.download.DownloadItemHelper;
import com.pws.pateast.utils.DateUtils;
import com.pws.pateast.widget.DownloaderIconView;

import java.io.File;
import java.util.Locale;

/**
 * Created by intel on 15-May-17.
 */

public class StudentAssignmentAdapter extends BaseRecyclerAdapter<Assignment, StudentAssignmentAdapter.AssignmentHolder> {

    public StudentAssignmentAdapter(Context context, OnItemClickListener itemClickListener) {
        super(context, itemClickListener);
    }

    @Override
    protected int getItemResourceLayout(int viewType) {
        return R.layout.item_student_assignment;
    }

    @Override
    public AssignmentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AssignmentHolder(getView(parent, viewType), mItemClickListener);
    }

    class AssignmentHolder extends BaseItemViewHolder<Assignment> {
        private TextView tvAssignmentTitle, tvClass, tvSubject, tvStartDate, tvEndDate, tvFileName;
        private DownloaderIconView imgDownload;

        public AssignmentHolder(View itemView, OnItemClickListener itemClickListener) {
            super(itemView, itemClickListener);
            tvAssignmentTitle = (TextView) findViewById(R.id.tv_assignment_title);
            tvClass = (TextView) findViewById(R.id.tv_class);
            tvSubject = (TextView) findViewById(R.id.tv_subject);
            tvStartDate = (TextView) findViewById(R.id.tv_start_date);
            tvEndDate = (TextView) findViewById(R.id.tv_end_date);
            tvFileName = (TextView) findViewById(R.id.tv_file_name);
            imgDownload = (DownloaderIconView) findViewById(R.id.img_download);
            imgDownload.setOnClickListener(this);
        }

        @Override
        public void bind(Assignment assignment) {
            DownloadableResult result = DownloadItemHelper.getDownloadedResult(getContext(), assignment.getId());
            DownloadingStatus downloadingStatus = DownloadingStatus.getValue(result.getDownloadStatus());
            assignment.setDownloadId(result.getId());

            tvAssignmentTitle.setText(assignment.getAssignmentdetails().get(0).getTitle());
            tvClass.setText(getString(R.string.class_name, assignment.getBcsmap().getBoard().getBoarddetails().get(0).getAlias(), assignment.getBcsmap().getClasses().getClassesdetails().get(0).getName(), assignment.getBcsmap().getSection().getSectiondetails().get(0).getName()));
            tvSubject.setText(assignment.getSubject().getSubjectdetails().get(0).getName());

            if (!TextUtils.isEmpty(assignment.getAssignment_file_name())) {
                SpannableString content = new SpannableString(getString(R.string.file_name, assignment.getAssignment_file_name(), Utils.readableFileSize(assignment.getAssignment_size(), true)));
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                tvFileName.setText(content);
                imgDownload.setVisibility(View.VISIBLE);
            } else {
                tvFileName.setText("N/A");
                imgDownload.setVisibility(View.GONE);
            }

            tvStartDate.setText(DateUtils.toTime(DateUtils.parse(assignment.getStart_date(), DateUtils.DATE_FORMAT_PATTERN), "dd MMM yyyy", Locale.getDefault()));
            tvEndDate.setText(DateUtils.toTime(DateUtils.parse(assignment.getEnd_date(), DateUtils.DATE_FORMAT_PATTERN), "dd MMM yyyy", Locale.getDefault()));
            imgDownload.setItemId(assignment.getId());
            if (downloadingStatus == DownloadingStatus.IN_PROGRESS) {
                imgDownload.updateProgress(getContext(), result.getPercent());
            } else if (downloadingStatus != DownloadingStatus.NOT_DOWNLOADED &&
                    downloadingStatus != DownloadingStatus.WAITING) {
                if (!TextUtils.isEmpty(assignment.getAssignment_file()) &&
                        !assignment.getAssignment_file().equals(result.getFileUrl())) {
                    downloadingStatus = DownloadingStatus.NOT_DOWNLOADED;
                } else if (!TextUtils.isEmpty(result.getFileUri())) {
                    Uri fileUri = Uri.parse(result.getFileUri());
                    File file = FileUtils.getFile(getContext(), fileUri);
                    if (file != null && !file.exists()) {
                        downloadingStatus = DownloadingStatus.NOT_DOWNLOADED;
                    }
                } else if (!TextUtils.isEmpty(assignment.getAssignment_file_name()) &&
                        !assignment.getAssignment_file_name().equals(result.getFileName())) {
                    downloadingStatus = DownloadingStatus.NOT_DOWNLOADED;
                }
            }
            Log.w("bind", downloadingStatus.getDownloadStatus());
            assignment.setDownloadingStatus(downloadingStatus);
            imgDownload.updateDownloadingStatus(assignment.getDownloadingStatus());
        }
    }
}
