package com.pws.pateast.fragment.leave.teacher.approve;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.adapter.viewholder.BaseItemViewHolder;
import com.pws.pateast.R;
import com.pws.pateast.api.model.Leave;
import com.pws.pateast.utils.DateUtils;

import java.util.Locale;

/**
 * Created by intel on 29-Jun-17.
 */

public class ClassLeaveAdapter extends BaseRecyclerAdapter<Leave, ClassLeaveAdapter.LeaveHolder> {
    public ClassLeaveAdapter(Context context, OnItemClickListener onItemClickListener) {
        super(context, onItemClickListener);
    }

    @Override
    protected int getItemResourceLayout(int viewType) {
        return R.layout.item_class_leave;
    }

    @Override
    public LeaveHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LeaveHolder(getView(parent, viewType), mItemClickListener);
    }

    class LeaveHolder extends BaseItemViewHolder<Leave> {
        private TextView tvStudentName, tvLeaveReason, tvStartDate, tvEndDate, tvDuration, tvAppliedOn, tvOptionsApprove, tvOptionsReject;
        private LinearLayout layoutOptions;

        public LeaveHolder(View itemView, OnItemClickListener itemClickListener) {
            super(itemView, itemClickListener);
            tvStudentName = (TextView) findViewById(R.id.tv_student_name);
            tvLeaveReason = (TextView) findViewById(R.id.tv_leave_reason);
            tvStartDate = (TextView) findViewById(R.id.tv_start_date);
            tvEndDate = (TextView) findViewById(R.id.tv_end_date);
            tvDuration = (TextView) findViewById(R.id.tv_duration);
            tvAppliedOn = (TextView) findViewById(R.id.tv_applied_date);
            tvOptionsApprove = (TextView) findViewById(R.id.tv_options_approve);
            tvOptionsReject = (TextView) findViewById(R.id.tv_options_reject);
            layoutOptions = (LinearLayout) findViewById(R.id.layout_options);

            tvOptionsApprove.setOnClickListener(this);
            tvOptionsReject.setOnClickListener(this);
        }

        @Override
        public void bind(Leave leave) {
            tvStudentName.setText(leave.getUser().getUserdetails().get(0).getFullname());
            if (leave.getTag() != null) {
                if (!TextUtils.isEmpty(leave.getTag().getTagdetails().get(0).getTitle())) {
                    tvLeaveReason.setText(leave.getTag().getTagdetails().get(0).getTitle());
                } else {
                    tvLeaveReason.setText(leave.getTag().getTagdetails().get(0).getDescription());
                }
            } else {
                tvLeaveReason.setText(leave.getComment());
            }
            tvStartDate.setText(DateUtils.toTime(DateUtils.parse(leave.getStart_date(), DateUtils.DATE_FORMAT_PATTERN), "dd MMM, yyyy", Locale.getDefault()));
            tvEndDate.setText(DateUtils.toTime(DateUtils.parse(leave.getEnd_date(), DateUtils.DATE_FORMAT_PATTERN), "dd MMM, yyyy", Locale.getDefault()));
            tvAppliedOn.setText(DateUtils.toTime(DateUtils.parse(leave.getCreatedAt(), DateUtils.DATE_FORMAT_PATTERN), "dd MMM, yyyy", Locale.getDefault()));

            if (leave.getDuration() == 1) {
                tvDuration.setText(getString(R.string.leave_full_day_for_day, String.valueOf(1)));
            } else if (leave.getDuration() == 0.5f) {
                if (leave.getHalfday() == 1) {
                    tvDuration.setText(getString(R.string.leave_duration_first_half));
                } else {
                    tvDuration.setText(getString(R.string.leave_duration_second_half));
                }
            } else {
                tvDuration.setText(getString(R.string.leave_date_duration_for_days, getString(R.string.leave_full_day),
                        String.valueOf(leave.getDuration())));
            }
            setOptionsVisibility(leave.getLeavestatus());
        }

        private void setOptionsVisibility(int leaveStatus) {
            tvOptionsApprove.setVisibility(View.GONE);
            tvOptionsReject.setVisibility(View.GONE);
            switch (leaveStatus) {
                case Leave.PENDING:
                    tvOptionsApprove.setVisibility(View.VISIBLE);
                    tvOptionsReject.setVisibility(View.VISIBLE);
                    break;
                case Leave.APPROVED:
                    tvOptionsReject.setVisibility(View.VISIBLE);
                    break;
                case Leave.REJECTED:
                    tvOptionsApprove.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }
}