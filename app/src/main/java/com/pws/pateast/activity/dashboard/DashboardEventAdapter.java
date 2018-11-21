package com.pws.pateast.activity.dashboard;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.base.ui.adapter.BaseExpandableRecyclerAdapter;
import com.base.ui.adapter.viewholder.ChildViewHolder;
import com.base.ui.adapter.viewholder.ParentViewHolder;
import com.pws.pateast.R;
import com.pws.pateast.api.model.Assignment;
import com.pws.pateast.api.model.DashboardEvent;
import com.pws.pateast.api.model.ExamMarks;
import com.pws.pateast.api.model.Response;
import com.pws.pateast.api.model.Schedule;
import com.pws.pateast.enums.ExamResult;
import com.pws.pateast.enums.ExpandableViewType;
import com.pws.pateast.fragment.attendance.teacher.addupdate.AttendanceTakerView;
import com.pws.pateast.utils.DateUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by intel on 25-May-17.
 */

public class DashboardEventAdapter
        extends BaseExpandableRecyclerAdapter<DashboardEvent<Response>, Response,
        ChildFooterItem, DashboardEventAdapter.HeaderViewHolder,
        ChildViewHolder> {

    public DashboardEventAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getParentResourceLayout(int viewType) {
        return R.layout.item_dashboard_event_header;
    }

    @Override
    protected int getChildResourceLayout(int viewType) {
        if (viewType == TYPE_CHILD_FOOTER) {
            return R.layout.item_dashboard_view_more;
        } else {
            switch (ExpandableViewType.getExpandableViewType(viewType)) {
                case SCHEDULE:
                    return R.layout.item_dashboard_schedule;
                case ATTENDANCE:
                    return R.layout.item_dashboard_attendance;
                case ASSIGNMENT:
                    return R.layout.item_dashboard_assignment;
                case EXAM_SCHEDULE:
                    return R.layout.item_dashboard_exam_schedule;
                case EXAM_MARKS:
                    return R.layout.item_dashboard_exam_marks;
            }
        }
        return R.layout.item_dashboard_schedule;
    }

    @Override
    public int getChildViewType(int parentPosition, int childPosition) {
        return getParentItem(parentPosition) != null ? getParentItem(parentPosition).getExpandableViewType().getValue() :
                super.getChildViewType(parentPosition, childPosition);
    }

    class HeaderViewHolder extends ParentViewHolder<DashboardEvent<Response>, Response, ChildFooterItem> {
        private TextView tvTitle;
        private ImageView imgIcon;
        private TextView expandCollepseIcon;

        public HeaderViewHolder(View view, OnParentClickListener onParentClickListener) {
            super(view, onParentClickListener);
            tvTitle = (TextView) findViewById(R.id.tv_event_name);
            imgIcon = (ImageView) findViewById(R.id.img_icon);
            expandCollepseIcon = (TextView) findViewById(R.id.expand_collepse_icon);
        }

        @Override
        public void bind(DashboardEvent dashboardEvent) {
            if (isExpanded()) {
                itemView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.dashboard_card_top_bg));
            } else {
                itemView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.dashboard_card_top_bottom_bg));
            }
            imgIcon.setImageResource(dashboardEvent.getTitleIcon());
            if (dashboardEvent.getIcon_color() != 0) {
                imgIcon.setColorFilter(ContextCompat.getColor(getContext(), dashboardEvent.getIcon_color()));
            }
            tvTitle.setText(dashboardEvent.getTitle());
            if (isExpanded()) {
                expandCollepseIcon.setText(R.string.mdi_minus);
            } else {
                expandCollepseIcon.setText(R.string.mdi_plus);
            }
        }
    }

    class ScheduleHolder extends ChildViewHolder<Schedule, ChildFooterItem> {
        private TextView iconClock, tvTime, tvSubject, tvTeacherName;
        private DateFormat format1, format2;

        public ScheduleHolder(@NonNull View itemView, OnChildClickListener onChildClickListener) {
            super(itemView, onChildClickListener);
            iconClock = (TextView) findViewById(R.id.icon_clock);
            tvTime = (TextView) findViewById(R.id.tv_time);
            tvSubject = (TextView) findViewById(R.id.tv_subject);
            tvTeacherName = (TextView) findViewById(R.id.tv_teacher_name);
            format1 = new SimpleDateFormat("HH:mm:ss");
            format2 = new SimpleDateFormat("hh:mm a");
        }

        @Override
        public void bind(Schedule schedule) {
            if (schedule.getSubject() != null) {
                tvSubject.setText(schedule.getSubject().getSubjectdetails().get(0).getName());
            } else {
                if (schedule.getIs_break() == 1)
                    tvSubject.setText(getString(R.string.hint_break));
                else
                    tvSubject.setText(R.string.not_available);
            }
            if (schedule.getTeacher() != null) {
                tvTeacherName.setText(schedule.getTeacher().getUser().getUserdetails().get(0).getFullname());
                tvTime.setText(schedule.getTime(format1, format2));
                tvTeacherName.setVisibility(View.VISIBLE);
            } else {
                if (schedule.getIs_break() == 1) {
                    iconClock.setVisibility(View.INVISIBLE);
                    tvTime.setVisibility(View.GONE);
                    tvTeacherName.setVisibility(View.GONE);
                } else {
                    iconClock.setVisibility(View.VISIBLE);
                    tvTime.setVisibility(View.VISIBLE);
                    tvTeacherName.setVisibility(View.VISIBLE);
                    tvTeacherName.setText(R.string.not_available);
                }
            }
        }
    }

    class AttendanceHolder extends ChildViewHolder<Schedule, ChildFooterItem> {
        private TextView tvDate, tvLeaveStatus;

        public AttendanceHolder(@NonNull View itemView, OnChildClickListener onChildClickListener) {
            super(itemView, onChildClickListener);
            tvDate = (TextView) findViewById(R.id.tv_date);
            tvLeaveStatus = (TextView) findViewById(R.id.tv_leave_status);
        }

        @Override
        public void bind(Schedule schedule) {
            tvDate.setText(DateUtils.toDate(DateUtils.parse(schedule.getDate(), DateUtils.DATE_FORMAT_PATTERN), "dd MMM, yyyy"));
            switch (schedule.getAttendancerecords().get(0).getIs_present()) {
                case AttendanceTakerView.SELECTED_PRESENT:
                    tvLeaveStatus.setText(R.string.leave_status_present);
                    tvLeaveStatus.setTextColor(ContextCompat.getColor(getContext(), R.color.md_green_800));
                    break;
                case AttendanceTakerView.SELECTED_ABSENT:
                    tvLeaveStatus.setText(R.string.leave_status_absent);
                    tvLeaveStatus.setTextColor(ContextCompat.getColor(getContext(), R.color.md_red_800));
                    break;
                case AttendanceTakerView.SELECTED_LEAVE:
                    tvLeaveStatus.setText(R.string.leave_status_late);
                    tvLeaveStatus.setTextColor(ContextCompat.getColor(getContext(), R.color.md_yellow_800));
                    break;
            }
        }
    }

    class AssignmentHolder extends ChildViewHolder<Assignment, ChildFooterItem> {
        private TextView tvAssignmentTitle, tvSubject, tvDate;

        public AssignmentHolder(@NonNull View itemView, OnChildClickListener onChildClickListener) {
            super(itemView, onChildClickListener);
            tvAssignmentTitle = (TextView) findViewById(R.id.tv_title);
            tvSubject = (TextView) findViewById(R.id.tv_subject);
            tvDate = (TextView) findViewById(R.id.tv_date);
        }

        @Override
        public void bind(Assignment assignment) {
            tvAssignmentTitle.setText(assignment.getAssignmentdetails().get(0).getTitle());
            tvSubject.setText(assignment.getSubject().getSubjectdetails().get(0).getName());
            tvDate.setText(DateUtils.toDate(DateUtils.parse(assignment.getEnd_date(), DateUtils.DATE_FORMAT_PATTERN), "dd MMM, yyyy"));
        }
    }

    class ExamScheduleHolder extends ChildViewHolder<Schedule, ChildFooterItem> {
        private TextView tvTime, tvSubject, tvDate;
        private DateFormat format1, format2;

        public ExamScheduleHolder(@NonNull View itemView, OnChildClickListener onChildClickListener) {
            super(itemView, onChildClickListener);
            tvTime = (TextView) findViewById(R.id.tv_time);
            tvSubject = (TextView) findViewById(R.id.tv_subject);
            tvDate = (TextView) findViewById(R.id.tv_date);
            format1 = new SimpleDateFormat("HH:mm:ss");
            format2 = new SimpleDateFormat("hh:mm a");
        }

        @Override
        public void bind(Schedule schedule) {
            tvTime.setText(schedule.getTime(format1, format2));
            if (schedule.getSubject() != null) {
                tvSubject.setText(schedule.getSubject().getSubjectdetails().get(0).getName());
            } else {
                tvSubject.setText(R.string.not_available);
            }
            if (!TextUtils.isEmpty(schedule.getDate())) {
                tvDate.setText(DateUtils.toDate(DateUtils.parse(schedule.getDate(), DateUtils.DATE_FORMAT_PATTERN), "dd MMM, yyyy"));
            } else {
                tvDate.setText(R.string.not_available);
            }
        }
    }

    class ExamMarksHolder extends ChildViewHolder<ExamMarks, ChildFooterItem> {
        private TextView tvSubject, tvPercent, tvRank, tvStatus;

        public ExamMarksHolder(@NonNull View itemView, OnChildClickListener onChildClickListener) {
            super(itemView, onChildClickListener);
            tvSubject = (TextView) findViewById(R.id.tv_subject);
            tvPercent = (TextView) findViewById(R.id.tv_percent);
            tvRank = (TextView) findViewById(R.id.tv_rank);
            tvStatus = (TextView) findViewById(R.id.tv_status);
        }

        @Override
        public void bind(ExamMarks examMarks) {
            double subjectPercent = examMarks.getSubject_percent();
            switch (ExamResult.getExamResult(examMarks.getResult_is())) {
                case FAILED:
                    tvStatus.setText(R.string.exam_result_fail);
                    tvStatus.setTextColor(ContextCompat.getColor(getContext(), R.color.md_red_800));
                    break;
                case PASS:
                    tvStatus.setText(R.string.exam_result_pass);
                    tvStatus.setTextColor(ContextCompat.getColor(getContext(), R.color.md_green_800));
                    break;
            }
            tvPercent.setText(subjectPercent == -1 ? getString(R.string.leave_status_absent) : getString(R.string.student_mark_percent, subjectPercent));
            tvRank.setText(getString(R.string.student_mark_rank, examMarks.getSubject_rank()));
            if (examMarks.getSubject() != null) {
                tvSubject.setText(examMarks.getSubject().getSubjectdetails().get(0).getName());
            } else {
                tvSubject.setText(R.string.not_available);
            }
        }
    }

    class ChildFooterHolder extends ChildViewHolder<ChildFooterItem, ChildFooterItem> {
        private Button btnTitle;
        private ProgressBar progressBar;
        private TextView tvNoDataAvailable;

        public ChildFooterHolder(@NonNull View itemView, OnChildItemClickListener onChildClickListener) {
            super(itemView, onChildClickListener);
            btnTitle = (Button) findViewById(R.id.btn_view_more);
            progressBar = (ProgressBar) findViewById(R.id.progress_bar);
            tvNoDataAvailable = (TextView) findViewById(R.id.no_data_available);
            btnTitle.setOnClickListener(this);
        }

        @Override
        public void bind(ChildFooterItem footerItem) {
            if (mParent.getChildList() == null) {
                btnTitle.setVisibility(View.GONE);
                tvNoDataAvailable.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
                if (mParent.getChildList().size() == 0) {
                    btnTitle.setVisibility(View.GONE);
                    tvNoDataAvailable.setVisibility(View.VISIBLE);
                    tvNoDataAvailable.setText(R.string.title_data_not_available);
                } else {
                    btnTitle.setVisibility(View.VISIBLE);
                    tvNoDataAvailable.setVisibility(View.GONE);
                    btnTitle.setText(footerItem.getTitle());
                }
            }
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btn_view_more) {
                ((OnChildItemClickListener) mOnChildClickListener).onViewMoreClick(v, (DashboardEvent) mParent, mChildFooter);
            }
            super.onClick(v);
        }
    }


    @NonNull
    @Override
    public HeaderViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        return new HeaderViewHolder(getParentView(parentViewGroup, viewType), mOnParentClickListener);
    }

    @NonNull
    @Override
    public ChildViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        if (viewType == TYPE_CHILD_FOOTER) {
            return new ChildFooterHolder(getChildView(childViewGroup, viewType), (OnChildItemClickListener) mOnChildClickListener);
        } else {
            switch (ExpandableViewType.getExpandableViewType(viewType)) {
                case SCHEDULE:
                    return new ScheduleHolder(getChildView(childViewGroup, viewType), mOnChildClickListener);
                case ATTENDANCE:
                    return new AttendanceHolder(getChildView(childViewGroup, viewType), mOnChildClickListener);
                case ASSIGNMENT:
                    return new AssignmentHolder(getChildView(childViewGroup, viewType), mOnChildClickListener);
                case EXAM_SCHEDULE:
                    return new ExamScheduleHolder(getChildView(childViewGroup, viewType), mOnChildClickListener);
                case EXAM_MARKS:
                    return new ExamMarksHolder(getChildView(childViewGroup, viewType), mOnChildClickListener);
            }
        }
        return new ScheduleHolder(getChildView(childViewGroup, viewType), mOnChildClickListener);
    }

    public interface OnChildItemClickListener extends OnChildClickListener<DashboardEvent<Response>, Response> {
        void onViewMoreClick(View view, DashboardEvent parent, ChildFooterItem childFooter);
    }

}
