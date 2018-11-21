package com.pws.pateast.fragment.attendance.teacher.report;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.adapter.viewholder.BaseItemViewHolder;
import com.pws.pateast.api.model.Student;
import com.pws.pateast.utils.ColorUtil;
import com.pws.pateast.utils.FontManager;
import com.pws.pateast.R;
import com.pws.pateast.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by intel on 11-May-17.
 */

public class AttendanceReportAdapter extends BaseRecyclerAdapter<Student,AttendanceReportAdapter.ReportHolder>
{
    protected List<Student> mStudentFilterList;


    public AttendanceReportAdapter(Context context)
    {
        super(context);
        mStudentFilterList = new ArrayList<>();
    }

    public AttendanceReportAdapter(Context context,OnItemClickListener onItemClickListener)
    {
        super(context,onItemClickListener);
    }

    @Override
    protected int getItemResourceLayout(int viewType) {
        return R.layout.item_attendance_report;
    }

    @Override
    public ReportHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReportHolder(getView(parent,viewType), mItemClickListener);
    }

    class ReportHolder extends BaseItemViewHolder<Student>
    {
        private CircleImageView imgProfile;
        private TextView tvStudentName,tvEnrollNo,tvPhoneNo;
        private TextView tvViewReport;
        private LinearLayout layoutOptions;
        public ReportHolder(View itemView,OnItemClickListener onItemClickListener)
        {
            super(itemView,onItemClickListener);
            imgProfile = (CircleImageView) findViewById(R.id.img_profile);
            tvEnrollNo = (TextView) findViewById(R.id.tv_enroll_number);
            tvPhoneNo = (TextView) findViewById(R.id.tv_phone);
            tvStudentName = (TextView) findViewById(R.id.tv_student_name);
            tvViewReport = (TextView) findViewById(R.id.tv_view_report);

            layoutOptions = (LinearLayout) findViewById(R.id.layout_options);

            tvViewReport.setOnClickListener(this);

        }

        @Override
        public void bind(Student student)
        {
            ImageUtils.setImageUrl(getContext(),imgProfile,student.getStudent().getUser().getUser_image(),R.drawable.avatar1);

            tvStudentName.setText(student.getStudent().getUser().getUserdetails().get(0).getFullname());
            tvEnrollNo.setText(student.getStudent().getEnrollment_no());
            tvPhoneNo.setText(student.getStudent().getFather_contact());
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
    public void update(List<Student> items)
    {
        mStudentFilterList = items;
        super.update(items);
    }

    public void updateSearch(List<Student> items)
    {
        mStudentFilterList = items;
        notifyDataSetChanged();
    }

}
