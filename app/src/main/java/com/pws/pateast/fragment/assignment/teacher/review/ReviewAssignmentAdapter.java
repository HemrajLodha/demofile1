package com.pws.pateast.fragment.assignment.teacher.review;

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
import com.pws.pateast.api.model.Tag;
import com.pws.pateast.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by intel on 07-Sep-17.
 */

public class ReviewAssignmentAdapter extends BaseRecyclerAdapter<Student, ReviewAssignmentAdapter.StudentAssignmentHolder> {
    protected List<Student> mStudentFilterList;
    protected List<Tag> mTags;

    public ReviewAssignmentAdapter(Context context, OnItemClickListener onItemClickListener) {
        super(context, onItemClickListener);
        mStudentFilterList = new ArrayList<>();
    }

    public void setTags(List<Tag> mTags) {
        this.mTags = mTags;
    }

    @Override
    protected int getItemResourceLayout(int viewType) {
        return R.layout.item_review_assignment;
    }

    @Override
    public StudentAssignmentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StudentAssignmentHolder(getView(parent, viewType), mItemClickListener);
    }

    class StudentAssignmentHolder extends BaseItemViewHolder<Student> {
        private CircleImageView imgProfile;
        private TextView tvStudentName, tvEnrollNo, tvPhoneNo;
        private TextView tvReview, tvReviewTag;
        private LinearLayout layoutOptions;

        public StudentAssignmentHolder(View itemView, BaseRecyclerAdapter.OnItemClickListener onItemClickListener) {
            super(itemView, onItemClickListener);
            imgProfile = (CircleImageView) findViewById(R.id.img_profile);
            tvEnrollNo = (TextView) findViewById(R.id.tv_enroll_number);
            tvPhoneNo = (TextView) findViewById(R.id.tv_phone);
            tvStudentName = (TextView) findViewById(R.id.tv_student_name);
            tvReview = (TextView) findViewById(R.id.tv_review);
            tvReviewTag = (TextView) findViewById(R.id.tv_review_tag);

            layoutOptions = (LinearLayout) findViewById(R.id.layout_options);

            tvReview.setOnClickListener(this);
        }

        @Override
        public void bind(Student student) {
            ImageUtils.setImageUrl(getContext(), imgProfile, student.getStudent().getUser().getUser_image(), R.drawable.avatar1);
            tvStudentName.setText(student.getStudent().getUser().getUserdetails().get(0).getFullname());
            tvEnrollNo.setText(student.getStudent().getEnrollment_no());
            tvPhoneNo.setText(student.getStudent().getFather_contact());
            if (mTags != null && !mTags.isEmpty()) {
                if (student.getStudent().getAssignmentremark() == null || TextUtils.isEmpty(student.getStudent().getAssignmentremark().getTags()))
                    tvReviewTag.setText(getString(R.string.not_available));
                else {
                    Tag tag = null;
                    try {
                        tag = mTags.get(mTags.indexOf(new Tag(Integer.valueOf(student.getStudent().getAssignmentremark().getTags()))));
                    } finally {
                        if (tag != null)
                            tvReviewTag.setText(tag.getTagdetails().get(0).getTitle());
                        else
                            tvReviewTag.setText(getString(R.string.not_available));
                    }
                }

            } else
                tvReviewTag.setText(getString(R.string.not_available));
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
