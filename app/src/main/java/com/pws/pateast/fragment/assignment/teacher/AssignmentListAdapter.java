package com.pws.pateast.fragment.assignment.teacher;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.adapter.viewholder.BaseItemViewHolder;
import com.github.clans.fab.FloatingActionButton;
import com.pws.pateast.R;
import com.pws.pateast.api.model.Assignment;
import com.pws.pateast.download.DownloadItemHelper;
import com.pws.pateast.download.DownloadableResult;
import com.pws.pateast.enums.AssignmentType;
import com.pws.pateast.enums.DownloadingStatus;
import com.pws.pateast.utils.DateUtils;
import com.pws.pateast.utils.FileUtils;

import java.io.File;
import java.util.Locale;

/**
 * Created by intel on 03-May-17.
 */

public class AssignmentListAdapter extends BaseRecyclerAdapter<Assignment, AssignmentListAdapter.AssignmentHolder> {

    private AssignmentType assignmentType;
    private String dateFormat;

    public AssignmentListAdapter(Context context) {
        super(context);
    }

    public void setAssignmentType(AssignmentType assignmentType) {
        this.assignmentType = assignmentType;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    @Override
    protected int getItemResourceLayout(int viewType) {
        return R.layout.item_assignment_list;
    }

    @Override
    public AssignmentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AssignmentHolder(getView(parent, viewType), mItemClickListener);
    }

    class AssignmentHolder extends BaseItemViewHolder<Assignment> {
        private TextView tvAssignmentTitle, tvClass, tvSubject, tvEndDate, tvFileName, tvOptions;
        //private FloatingActionButton imgDownload;

        public AssignmentHolder(View itemView, OnItemClickListener itemClickListener) {
            super(itemView, itemClickListener);
            tvAssignmentTitle = (TextView) findViewById(R.id.tv_assignment_title);
            tvClass = (TextView) findViewById(R.id.tv_class);
            tvSubject = (TextView) findViewById(R.id.tv_subject);
            tvEndDate = (TextView) findViewById(R.id.tv_end_date);
            tvFileName = (TextView) findViewById(R.id.tv_file_name);
            //imgDownload = (FloatingActionButton) findViewById(R.id.img_download);

            tvOptions = (TextView) findViewById(R.id.tv_options);
            tvOptions.setOnClickListener(this);
            //imgDownload.setOnClickListener(this);
            if (assignmentType == AssignmentType.CANCELED || assignmentType == AssignmentType.COMPLETED) {
                tvOptions.setVisibility(View.GONE);
            } else {
                tvOptions.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void bind(Assignment assignment) {
            tvAssignmentTitle.setText(assignment.getAssignmentdetails().get(0).getTitle().trim());
            tvClass.setText(getString(R.string.class_name, assignment.getBcsmap().getBoard().getBoarddetails().get(0).getAlias(), assignment.getBcsmap().getClasses().getClassesdetails().get(0).getName(), assignment.getBcsmap().getSection().getSectiondetails().get(0).getName()));
            tvSubject.setText(assignment.getSubject().getSubjectdetails().get(0).getName());

            //imgDownload.setVisibility(!TextUtils.isEmpty(assignment.getAssignment_file_name()) ? View.VISIBLE : View.GONE);

            tvEndDate.setText(DateUtils.toTime(DateUtils.parse(assignment.getStart_date(), DateUtils.DATE_FORMAT_PATTERN), dateFormat, Locale.getDefault())
                    + " - " +
                    DateUtils.toTime(DateUtils.parse(assignment.getEnd_date(), DateUtils.DATE_FORMAT_PATTERN), dateFormat, Locale.getDefault()));

            tvFileName.setText(assignment.getAssignment_file_name());

            //setDownloadStatus(assignment);

        }

        /*private void setDownloadStatus(Assignment assignment) {
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
                } else {
                    imgDownload.setImageResource(R.drawable.ic_attachment);
                }
                setDownloadProgress(false);
            } else {
                imgDownload.setImageResource(R.drawable.ic_attachment);
                setDownloadProgress(false);
            }
            assignment.setDownloadingStatus(downloadingStatus);
        }*/

        /*private void setDownloadProgress(boolean enabled) {
            imgDownload.setShowProgressBackground(enabled);
            imgDownload.setIndeterminate(enabled);
        }*/
    }
}
