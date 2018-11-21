package com.pws.pateast.fragment.student;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.adapter.viewholder.BaseItemViewHolder;
import com.pws.pateast.R;
import com.pws.pateast.api.model.Student;
import com.pws.pateast.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by intel on 26-Apr-17.
 */

public class MyStudentAdapter extends BaseRecyclerAdapter<Student, MyStudentAdapter.StudentHolder> {
    protected List<Student> mStudentFilterList;
    private boolean isAddUser;

    public MyStudentAdapter(Context context) {
        super(context);
        mStudentFilterList = new ArrayList<>();
    }

    public boolean isAddUser() {
        return isAddUser;
    }

    public void setAddUser(boolean addUser) {
        isAddUser = addUser;
    }

    @Override
    protected int getItemResourceLayout(int viewType) {
        return R.layout.item_my_students;
    }

    @Override
    public StudentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StudentHolder(getView(parent, viewType), mItemClickListener);
    }

    class StudentHolder extends BaseItemViewHolder<Student> {
        private CircleImageView imgProfile;
        private TextView tvStudentName, tvEnrollNo, tvPhoneNo, tvFatherName, tvMotherName;
        private LinearLayout layoutOptions;
        private TextView tvSendMessage, tvAttendanceReport;

        public StudentHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView, onItemClickListener);
            imgProfile = (CircleImageView) findViewById(R.id.img_profile);
            tvEnrollNo = (TextView) findViewById(R.id.tv_enroll_number);
            tvStudentName = (TextView) findViewById(R.id.tv_student_name);
            tvPhoneNo = (TextView) findViewById(R.id.tv_phone);
            tvFatherName = (TextView) findViewById(R.id.tv_father_name);
            tvMotherName = (TextView) findViewById(R.id.tv_mother_name);
            tvSendMessage = (TextView) findViewById(R.id.tv_send_message);
            tvAttendanceReport = (TextView) findViewById(R.id.tv_attendance_report);

            layoutOptions = (LinearLayout) findViewById(R.id.layout_options);
            tvSendMessage.setOnClickListener(this);
            tvAttendanceReport.setOnClickListener(this);
            layoutOptions.setVisibility(isAddUser() ? View.GONE : View.VISIBLE);
        }

        @Override
        public void bind(Student student) {
            ImageUtils.setImageUrl(getContext(), imgProfile, student.getStudent().getUser().getUser_image(), R.drawable.avatar1);

            tvStudentName.setText(student.getStudent().getUser().getUserdetails().get(0).getFullname());
            if (!TextUtils.isEmpty(student.getStudent().getStudentdetails().get(0).getFather_name())) {
                tvFatherName.setText(student.getStudent().getStudentdetails().get(0).getFather_name());
            } else {
                tvFatherName.setText(R.string.not_available);
            }
            if (!TextUtils.isEmpty(student.getStudent().getStudentdetails().get(0).getMother_name())) {
                tvMotherName.setText(student.getStudent().getStudentdetails().get(0).getMother_name());
            } else {
                tvMotherName.setText(R.string.not_available);
            }
            tvPhoneNo.setText(student.getStudent().getFather_contact());
            tvEnrollNo.setText(student.getStudent().getEnrollment_no());
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

}
