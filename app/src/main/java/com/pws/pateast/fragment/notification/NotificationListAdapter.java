package com.pws.pateast.fragment.notification;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.adapter.viewholder.BaseItemViewHolder;
import com.pws.pateast.R;
import com.pws.pateast.api.model.NotificationData;
import com.pws.pateast.enums.NotificationType;

/**
 * Created by intel on 28-Jun-17.
 */

public class NotificationListAdapter extends BaseRecyclerAdapter<NotificationData, NotificationListAdapter.ViewHolder> {

    public NotificationListAdapter(Context context, OnItemClickListener onItemClickListener) {
        super(context, onItemClickListener);
    }

    @Override
    protected int getItemResourceLayout(int viewType) {
        return R.layout.item_notification;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getView(parent, viewType), mItemClickListener);
    }

    class ViewHolder extends BaseItemViewHolder<NotificationData> {
        protected TextView tvTitle, tvMessage;
        protected ImageView imgIcon;

        public ViewHolder(View itemView, OnItemClickListener itemClickListener) {
            super(itemView, itemClickListener);
            tvTitle = (TextView) findViewById(R.id.tv_title);
            tvMessage = (TextView) findViewById(R.id.tv_message);
            imgIcon = (ImageView) findViewById(R.id.img_icon);
        }

        @Override
        public void bind(NotificationData notification) {
            switch (NotificationType.getNotificationType(notification.getNotification().getType())) {
                case EXAM_SYLLABUS:
                    imgIcon.setImageResource(R.drawable.dashboard_exam_syllabus);
                    break;
                case EXAM_SCHEDULE:
                    imgIcon.setImageResource(R.drawable.dashboard_my_assignments_250);
                    break;
                case TRANSPORT:
                    imgIcon.setImageResource(R.drawable.bus_256);
                    break;
                case ATTENDANCE:
                    imgIcon.setImageResource(R.drawable.dashboard_attendance);
                    break;
                case LEAVE_REJECTED:
                case LEAVE_APPROVED:
                case STUDENT_LEAVE:
                    imgIcon.setImageResource(R.drawable.dashboard_student_leave);
                    break;
                case TIMETABLE:
                case EXAM_MARKS:
                    imgIcon.setImageResource(R.drawable.dashboard_exam_syllabus);
                    break;
                case ASSIGNMENT:
                case ASSIGNMENT_REMARK:
                    imgIcon.setImageResource(R.drawable.dashboard_assignments);
                    break;
                case START_PICK_UP:
                case CONFIRM_PICKUP_ON_BOARD:
                    imgIcon.setImageResource(R.drawable.bus_256);
                    break;
            }

            if (notification.getStatus() == 0) {
                itemView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.md_green_50));
            } else {
                itemView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
            }
            if (notification.getNotification().getUser() != null) {
                tvTitle.setText(String.format("%s (%s)", notification.getNotification().getUser().getUserdetails().get(0).getFullname(), notification.getNotification().getUser().getInstitute().getInstitutedetails().get(0).getAlias()));
            } else {
                tvTitle.setText(R.string.pref_title_notification);
            }
            tvMessage.setText(notification.getNotification().getMessage());
        }
    }
}
