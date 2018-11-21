package com.pws.pateast.fragment.student.report;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.pws.calender.domain.Event;
import com.pws.pateast.R;
import com.pws.pateast.api.model.Student;
import com.pws.pateast.api.model.Tag;
import com.pws.pateast.fragment.attendance.teacher.addupdate.AttendanceTagAdapter;
import com.pws.pateast.fragment.attendance.teacher.addupdate.AttendanceTakerView;
import com.pws.pateast.utils.DateUtils;
import com.pws.pateast.widget.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by intel on 10-Jan-18.
 */

public class StudentReportAdapter extends PagerAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<Event> attendance;
    private List<Tag> tags;

    public StudentReportAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        attendance = new ArrayList<>();
    }

    public void setAttendance(ArrayList<Event> attendance) {
        this.attendance = attendance;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public int getCount() {
        return attendance.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.item_student_report, container, false);
        ViewHolder holder = new ViewHolder(itemView);
        holder.bind((Student) attendance.get(position).getData());
        container.addView(itemView);

        return itemView;
    }

    class ViewHolder {
        private TextView tvDate, tvPeriod, tvAttendanceStatus;
        private RecyclerView rvTag;
        private ChipsLayoutManager clm;
        private AttendanceTagAdapter tagAdapter;

        public ViewHolder(View view) {
            tvDate = view.findViewById(R.id.tv_date);
            tvPeriod = view.findViewById(R.id.tv_period);
            tvAttendanceStatus = view.findViewById(R.id.tv_attendance_status);
            rvTag = view.findViewById(R.id.rv_tag);
            clm = ChipsLayoutManager.newBuilder(mContext).build();
            rvTag.setLayoutManager(clm);
            rvTag.addItemDecoration(new SpaceItemDecoration(mContext, R.dimen.size_4, 1, true));
        }

        public void bind(Student student) {
            tvDate.setText(DateUtils.toDate(student.getDate()));
            tvPeriod.setText(String.valueOf(student.getPeriod()));
            setAttendanceStatus(student.getAttendancerecords().get(0));
        }

        public void setAttendanceStatus(Student student) {
            int textColor = R.color.colorPrimaryStudent;
            int text = R.string.leave_status_present;
            switch (student.getIs_present()) {
                case AttendanceTakerView.SELECTED_PRESENT:
                    textColor = R.color.colorPrimaryStudent;
                    text = R.string.leave_status_present;
                    break;
                case AttendanceTakerView.SELECTED_ABSENT:
                    textColor = R.color.colorAccentStudent;
                    text = R.string.leave_status_absent;
                    break;
                case AttendanceTakerView.SELECTED_LEAVE:
                    textColor = R.color.colorLogin;
                    text = R.string.leave_status_late;
                    break;
            }
            tvAttendanceStatus.setText(text);
            tvAttendanceStatus.setTextColor(ContextCompat.getColor(mContext, textColor));
            setTagAdapter(TextUtils.isEmpty(student.getTags()) ? null : student.getTags().split(","));
        }

        public void setTagAdapter(String[] selected) {
            if (selected == null || tags == null) {
                rvTag.setVisibility(View.GONE);
                return;
            }
            rvTag.setVisibility(View.VISIBLE);
            if (rvTag.getAdapter() == null) {
                tagAdapter = new AttendanceTagAdapter(mContext);
                rvTag.setAdapter(tagAdapter);
            }
            tagAdapter.update(tags);
            tagAdapter.setTags(Arrays.asList(selected));
        }
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
