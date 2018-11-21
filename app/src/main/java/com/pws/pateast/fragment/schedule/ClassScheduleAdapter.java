package com.pws.pateast.fragment.schedule;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.adapter.viewholder.BaseHeaderViewHolder;
import com.base.ui.adapter.viewholder.BaseItemViewHolder;
import com.pws.pateast.R;
import com.pws.pateast.api.model.Holiday;
import com.pws.pateast.api.model.Schedule;
import com.pws.pateast.enums.UserType;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by intel on 06-Jul-17.
 */

public class ClassScheduleAdapter extends BaseRecyclerAdapter<Object, BaseItemViewHolder> {
    private final int WEEK = 0;
    private final int SUBJECT = 1;
    private final int HOLIDAY = 2;

    private UserType userType;

    public ClassScheduleAdapter(Context context) {
        super(context);
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    @Override
    protected int getItemResourceLayout(int viewType) {
        switch (viewType) {
            case WEEK:
                return R.layout.item_header;
            case SUBJECT:
                if (userType == UserType.TEACHER) {
                    return R.layout.item_class_schedule;
                } else {
                    return R.layout.item_student_class_schedule;
                }
            case HOLIDAY:
                if (userType == UserType.TEACHER) {
                    return R.layout.item_class_schedule;
                } else {
                    return R.layout.item_student_class_schedule;
                }
        }
        return 0;
    }

    @Override
    public BaseItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case WEEK:
                return new ScheduleHeaderHolder(getView(parent, viewType), null);
            case SUBJECT:
                return new ScheduleHolder(getView(parent, viewType));
            case HOLIDAY:
                return new HolidayHolder(getView(parent, viewType));
        }
        return null;
    }

    class ScheduleHeaderHolder extends BaseHeaderViewHolder<Object> {
        private TextView tvHeader;

        public ScheduleHeaderHolder(View itemView, Bundle bundle) {
            super(itemView, bundle);
            tvHeader = (TextView) findViewById(R.id.tv_header);
        }

        @Override
        public void bind(Object o) {
            String week = (String) o;
            tvHeader.setText(week);
        }

        @Override
        public void show() {

        }
    }

    class ScheduleHolder extends BaseItemViewHolder<Object> {
        protected CardView cardSchedule;
        protected View vwTime, vwSubject, vwDay;
        protected TextView tvTeacher, tvTime, tvSubject, tvDay;
        protected DateFormat format1, format2;

        public ScheduleHolder(View view) {
            super(view);
            vwTime = findViewById(R.id.time_view);
            vwSubject = findViewById(R.id.subject_view);
            vwDay = findViewById(R.id.day_view);
            cardSchedule = (CardView) findViewById(R.id.card_schedule);
            tvTeacher = (TextView) findViewById(R.id.tv_class);
            tvTime = (TextView) findViewById(R.id.tv_time);
            tvSubject = (TextView) findViewById(R.id.tv_subject);
            tvDay = (TextView) findViewById(R.id.tv_day);
            format1 = new SimpleDateFormat("HH:mm:ss");
            format2 = new SimpleDateFormat("hh:mm a");
        }

        @Override
        public void bind(Object o) {
            Schedule schedule = (Schedule) o;
            switch (userType) {
                case STUDENT:
                    bindStudent(schedule);
                    break;
                case TEACHER:
                    bindTeacher(schedule);
                    break;
                case PARENT:
                    bindWard(schedule);
                    break;
            }
        }

        private void bindStudent(Schedule schedule) {
            if (schedule.getIs_break() == 1) {
                cardSchedule.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryLightStudent));
                tvTime.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor));
                tvTeacher.setText(getString(R.string.hint_break));
                vwTime.setVisibility(View.GONE);
                vwSubject.setVisibility(View.GONE);
                vwDay.setVisibility(View.GONE);
            } else {
                cardSchedule.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                tvTime.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor));
                tvTeacher.setText(schedule.getTeacher() == null ? "N/A" : schedule.getTeacher().getUser().getUserdetails().get(0).getFullname());
                tvSubject.setText(schedule.getSubject() == null ? "N/A" : schedule.getSubject().getSubjectdetails().get(0).getName());
                tvDay.setText(schedule.getWeekday());
                tvTime.setText(schedule.getStartTime(format1, format2));
                vwTime.setVisibility(View.VISIBLE);
                vwSubject.setVisibility(View.VISIBLE);
                vwDay.setVisibility(View.VISIBLE);
            }
        }

        private void bindTeacher(Schedule schedule) {
            if (schedule.getIs_break() == 1) {
                cardSchedule.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryLightTeacher));
                tvTime.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor));
                tvTeacher.setText(getString(R.string.hint_break));
                vwTime.setVisibility(View.GONE);
                vwSubject.setVisibility(View.GONE);
                vwDay.setVisibility(View.GONE);
            } else {
                cardSchedule.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                tvTime.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor));
                tvTeacher.setText(schedule.getTimetable() == null || schedule.getTimetable().getBcsmap() == null ? "N/A" : getString(R.string.class_name, schedule.getTimetable().getBcsmap().getBoard().getBoarddetails().get(0).getAlias(), schedule.getTimetable().getBcsmap().getClasses().getClassesdetails().get(0).getName(), schedule.getTimetable().getBcsmap().getSection().getSectiondetails().get(0).getName()));
                tvSubject.setText(schedule.getSubject() == null ? "N/A" : schedule.getSubject().getSubjectdetails().get(0).getName());
                tvDay.setText(schedule.getWeekday());
                tvTime.setText(schedule.getStartTime(format1, format2));
                vwTime.setVisibility(View.VISIBLE);
                vwSubject.setVisibility(View.VISIBLE);
                vwDay.setVisibility(View.VISIBLE);
            }
        }

        private void bindWard(Schedule schedule) {
            if (schedule.getIs_break() == 1) {
                cardSchedule.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryLightParent));
                tvTime.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor));
                tvTeacher.setText(getString(R.string.hint_break));
                vwTime.setVisibility(View.GONE);
                vwSubject.setVisibility(View.GONE);
                vwDay.setVisibility(View.GONE);
            } else {
                cardSchedule.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                tvTime.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor));
                tvTeacher.setText(schedule.getTeacher() == null ? "N/A" : schedule.getTeacher().getUser().getUserdetails().get(0).getFullname());
                tvSubject.setText(schedule.getSubject() == null ? "N/A" : schedule.getSubject().getSubjectdetails().get(0).getName());
                tvDay.setText(schedule.getWeekday());
                tvTime.setText(schedule.getStartTime(format1, format2));
                vwTime.setVisibility(View.VISIBLE);
                vwSubject.setVisibility(View.VISIBLE);
                vwDay.setVisibility(View.VISIBLE);
            }
        }
    }

    class HolidayHolder extends ScheduleHolder {

        public HolidayHolder(View view) {
            super(view);
            vwSubject.setVisibility(View.GONE);
            vwTime.setVisibility(View.GONE);
            vwDay.setVisibility(View.GONE);
        }

        @Override
        public void bind(Object o) {
            Holiday holiday = (Holiday) o;
            //cardBackgroundColor();
            tvTeacher.setText(getString(R.string.holiday_message, holiday.getHolidaydetails().get(0).getName()));
            tvTeacher.setGravity(Gravity.CENTER);
        }

        private void cardBackgroundColor() {
            switch (userType) {
                case STUDENT:
                    cardSchedule.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryLightStudent));
                    break;
                case TEACHER:
                    cardSchedule.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryLightTeacher));
                    break;
                case PARENT:
                    cardSchedule.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryLightParent));
                    break;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof String)
            return WEEK;
        else if (getItem(position) instanceof Schedule)
            return SUBJECT;
        else if (getItem(position) instanceof Holiday)
            return HOLIDAY;
        else
            return WEEK;
    }
}
