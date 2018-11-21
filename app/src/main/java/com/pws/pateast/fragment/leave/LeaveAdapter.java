package com.pws.pateast.fragment.leave;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.adapter.viewholder.BaseItemViewHolder;
import com.pws.pateast.R;
import com.pws.pateast.api.model.Leave;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.utils.DateUtils;

import java.util.Locale;

/**
 * Created by intel on 28-Jun-17.
 */

public class LeaveAdapter extends BaseRecyclerAdapter<Leave, LeaveAdapter.LeaveHolder> {
    private final int TYPE_STUDENT = 1;
    private final int TYPE_TEACHER = 2;
    private final int TYPE_PARENT = 3;

    private UserType userType;

    public LeaveAdapter(Context context, OnItemClickListener onItemClickListener) {
        super(context, onItemClickListener);
    }

    @Override
    protected int getItemResourceLayout(int viewType) {
        switch (viewType) {
            case TYPE_STUDENT:
                return R.layout.item_student_leave;
            case TYPE_TEACHER:
                return R.layout.item_teacher_leave;
            case TYPE_PARENT:
                return R.layout.item_ward_leave;
        }
        return R.layout.item_student_leave;
    }

    @Override
    public LeaveHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_STUDENT:
                return new StudentLeaveHolder(getView(parent, viewType), mItemClickListener);
            case TYPE_TEACHER:
                return new TeacherLeaveHolder(getView(parent, viewType), mItemClickListener);
            case TYPE_PARENT:
                return new ParentLeaveHolder(getView(parent, viewType), mItemClickListener);
        }
        return null;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    @Override
    public int getItemViewType(int position) {
        switch (userType) {
            case STUDENT:
                return TYPE_STUDENT;
            case TEACHER:
                return TYPE_TEACHER;
            case PARENT:
                return TYPE_PARENT;
        }
        return super.getItemViewType(position);
    }

    class ParentLeaveHolder extends LeaveHolder {


        public ParentLeaveHolder(View itemView, OnItemClickListener itemClickListener) {
            super(itemView, itemClickListener);

        }

        @Override
        public void bind(Leave leave) {
            super.bind(leave);
            tvRejectedReason.setVisibility(View.GONE);
            if (leave.getLeavestatus() != Leave.REJECTED) {
                layoutRejection.setVisibility(View.GONE);
            } else {
                layoutRejection.setVisibility(View.VISIBLE);
                layoutRejection.setVisibility(View.VISIBLE);
                tvRejectedBy.setText(UserType.getUserType(leave.getStatus_updatedbytype()).getName());
            }
            tvOptions.setVisibility(leave.getLeavestatus() == Leave.CANCELED || leave.getLeavestatus() == Leave.REJECTED ? View.GONE : View.VISIBLE);
        }
    }

    class StudentLeaveHolder extends LeaveHolder {

        public StudentLeaveHolder(View itemView, OnItemClickListener itemClickListener) {
            super(itemView, itemClickListener);
        }

        @Override
        public void bind(Leave leave) {
            super.bind(leave);
            tvRejectedReason.setVisibility(View.GONE);
            if (leave.getLeavestatus() != Leave.REJECTED) {
                layoutRejection.setVisibility(View.GONE);
            } else {
                layoutRejection.setVisibility(View.VISIBLE);
                layoutRejection.setVisibility(View.VISIBLE);
                tvRejectedBy.setText(UserType.getUserType(leave.getStatus_updatedbytype()).getName());
            }
            tvOptions.setVisibility(leave.getLeavestatus() == Leave.CANCELED || leave.getLeavestatus() == Leave.REJECTED ? View.GONE : View.VISIBLE);
        }
    }

    class TeacherLeaveHolder extends LeaveHolder {


        public TeacherLeaveHolder(View itemView, OnItemClickListener itemClickListener) {
            super(itemView, itemClickListener);
        }

        @Override
        public void bind(Leave leave) {
            super.bind(leave);
            if (leave.getLeavestatus() != Leave.REJECTED) {
                layoutRejection.setVisibility(View.GONE);
            } else {
                layoutRejection.setVisibility(View.VISIBLE);
                tvRejectedBy.setText(UserType.getUserType(leave.getStatus_updatedbytype()).getName());
                tvRejectedReason.setText(leave.getReject_reason().trim());
            }

            tvOptions.setVisibility(leave.getLeavestatus() == Leave.CANCELED || leave.getLeavestatus() == Leave.REJECTED ? View.GONE : View.VISIBLE);
        }
    }


    class LeaveHolder extends BaseItemViewHolder<Leave> {
        protected TextView tvLeaveReason, tvStartDate, tvEndDate, tvDuration, tvAppliedOn, tvOptions;
        protected ImageView imgMood;
        protected TextView tvRejectedBy, tvRejectedReason;
        protected LinearLayout layoutRejection;

        public LeaveHolder(View itemView, OnItemClickListener itemClickListener) {
            super(itemView, itemClickListener);
            tvLeaveReason = (TextView) findViewById(R.id.tv_leave_reason);
            tvStartDate = (TextView) findViewById(R.id.tv_start_date);
            tvEndDate = (TextView) findViewById(R.id.tv_end_date);
            tvDuration = (TextView) findViewById(R.id.tv_duration);
            tvAppliedOn = (TextView) findViewById(R.id.tv_applied_date);
            tvOptions = (TextView) findViewById(R.id.tv_options);
            imgMood = (ImageView) findViewById(R.id.mood_icon);
            layoutRejection = (LinearLayout) findViewById(R.id.layout_rejection);
            tvRejectedBy = (TextView) findViewById(R.id.tv_rejected_by);
            tvRejectedReason = (TextView) findViewById(R.id.tv_reject_reason);
            tvOptions.setOnClickListener(this);
        }

        @Override
        public void bind(Leave leave) {

            switch (leave.getLeavestatus()) {
                case Leave.APPROVED:
                    imgMood.setImageResource(R.drawable.ic_approved_256);
                    break;
                case Leave.CANCELED:
                    imgMood.setImageResource(R.drawable.ic_cancelled_256);
                    break;
                case Leave.PENDING:
                    imgMood.setImageResource(R.drawable.ic_pending_256);
                    break;
                case Leave.REJECTED:
                    imgMood.setImageResource(R.drawable.ic_rejected_256);
                    break;
            }

            tvOptions.setVisibility(leave.isOpened() ? View.VISIBLE : View.GONE);
            if (leave.getTag() != null && leave.getTag().getTagdetails().size() != 0) {
                if (!TextUtils.isEmpty(leave.getTag().getTagdetails().get(0).getDescription())) {
                    tvLeaveReason.setText(leave.getTag().getTagdetails().get(0).getDescription());
                } else if (!TextUtils.isEmpty(leave.getTag().getTagdetails().get(0).getTitle())) {
                    tvLeaveReason.setText(leave.getTag().getTagdetails().get(0).getTitle());
                } else {
                    tvLeaveReason.setText(getString(R.string.not_available));
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
                        String.valueOf((int) leave.getDuration())));
            }

        }
    }
}
