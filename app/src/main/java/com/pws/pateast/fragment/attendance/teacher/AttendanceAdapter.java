package com.pws.pateast.fragment.attendance.teacher;

import android.content.Context;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.adapter.viewholder.BaseItemViewHolder;
import com.pws.pateast.R;
import com.pws.pateast.api.model.Student;
import com.pws.pateast.fragment.attendance.teacher.addupdate.AttendanceTakerView;
import com.pws.pateast.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by intel on 28-Apr-17.
 */

public class AttendanceAdapter extends BaseRecyclerAdapter<Student, AttendanceAdapter.StudentAttendanceHolder> {

    protected List<Student> mStudentFilterList;

    public AttendanceAdapter(Context context) {
        super(context);
        mStudentFilterList = new ArrayList<>();
    }

    @Override
    protected int getItemResourceLayout(int viewType) {
        return R.layout.item_attendance;
    }

    @Override
    public StudentAttendanceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StudentAttendanceHolder(getView(parent, viewType), mItemClickListener);
    }

    class StudentAttendanceHolder extends BaseItemViewHolder<Student> {
        private CircleImageView imgProfile;
        private TextView tvStudentName, tvPhoneNo;
        private View vwViewReport;
        private ViewGroup vwLeaveStatus;
        private View vwPresent, vwLeave, vwAbsent;

        public StudentAttendanceHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView, onItemClickListener);
            imgProfile = (CircleImageView) findViewById(R.id.img_profile);
            tvPhoneNo = (TextView) findViewById(R.id.tv_phone);
            tvStudentName = (TextView) findViewById(R.id.tv_student_name);
            vwViewReport = findViewById(R.id.vw_view_report);

            vwPresent = findViewById(R.id.vw_present);
            vwLeave = findViewById(R.id.vw_leave);
            vwAbsent = findViewById(R.id.vw_absent);
            vwLeaveStatus = (ViewGroup) findViewById(R.id.rg_leave_status);
            vwViewReport.setOnClickListener(this);
            vwPresent.setOnClickListener(this);
            vwLeave.setOnClickListener(this);
            vwAbsent.setOnClickListener(this);
        }

        @Override
        public void bind(Student student) {
            ImageUtils.setImageUrl(getContext(), imgProfile, student.getStudent().getUser().getUser_image(), R.drawable.avatar1);
            tvStudentName.setText(student.getStudent().getUser().getUserdetails().get(0).getFullname());
            tvPhoneNo.setText(student.getStudent().getFather_contact());
            setAttendance(student.getStudent().getAttendancerecord() == null ? AttendanceTakerView.SELECTED_PRESENT : student.getStudent().getAttendancerecord().getIs_present());
        }

        private void setAttendance(int isPresent) {
            for (int i = 0; i < vwLeaveStatus.getChildCount(); i++) {
                AppCompatRadioButton radioButton = (AppCompatRadioButton) ((ViewGroup) vwLeaveStatus.getChildAt(i)).getChildAt(0);
                radioButton.setChecked((i + 1) == isPresent);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mStudentFilterList.size();
    }

    @Override
    public Student getItem(int position) {
        return mStudentFilterList.get(position);
    }

    @Override
    public void update(List<Student> items) {
        mStudentFilterList = items;
        super.update(items);
    }

    public void updateSearch(List<Student> items) {
        mStudentFilterList = items;
        notifyDataSetChanged();
    }

    @Override
    public List<Student> getDatas() {
        return mStudentFilterList;
    }


}


