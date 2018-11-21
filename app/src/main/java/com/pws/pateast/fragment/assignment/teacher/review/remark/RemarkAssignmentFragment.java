package com.pws.pateast.fragment.assignment.teacher.review.remark;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration;
import com.pws.pateast.R;
import com.pws.pateast.api.model.Student;
import com.pws.pateast.api.model.Tag;
import com.pws.pateast.base.AppDialogFragment;
import com.pws.pateast.base.Extras;
import com.pws.pateast.fragment.assignment.teacher.review.ReviewAssignmentView;

import java.util.List;

/**
 * Created by intel on 07-Sep-17.
 */

public class RemarkAssignmentFragment extends AppDialogFragment implements RemarkAssignmentView, BaseRecyclerAdapter.OnItemClickListener, View.OnClickListener {

    private TextView tvStudentName;

    private RecyclerView rvTag;
    private ChipsLayoutManager clm;
    private RemarkAssignmentAdapter tagAdapter;
    private Button btnUpdate;

    private RemarkAssignmentPresenter mPresenter;
    private ReviewAssignmentView assignmentView;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_remark_assignment;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);
        setTitle(R.string.title_remark);

        tvStudentName = (TextView) findViewById(R.id.tv_student_name);
        rvTag = (RecyclerView) findViewById(R.id.rv_tag);
        clm = ChipsLayoutManager.newBuilder(getContext()).build();
        rvTag.setLayoutManager(clm);
        rvTag.addItemDecoration(new SpacingItemDecoration(getResources().getDimensionPixelOffset(R.dimen.size_4), getResources().getDimensionPixelOffset(R.dimen.size_4)));
        btnUpdate = (Button) findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(this);

        setActionButtonText(R.string.submit);
        mPresenter = new RemarkAssignmentPresenter();
        mPresenter.attachView(this);

    }

    public void attachView(ReviewAssignmentView assignmentView) {
        this.assignmentView = assignmentView;
    }

    @Override
    public void setActionButtonText(int res) {
        btnUpdate.setText(res);
    }

    @Override
    public Student getStudent() {
        return getArguments().getParcelable(Extras.STUDENT);
    }

    @Override
    public List<Tag> getTags() {
        return getArguments().getParcelableArrayList(Extras.ASSIGNMENT_TAGS);
    }

    @Override
    public void setData(Student student) {
        tvStudentName.setText(student.getStudent().getUser().getUserdetails().get(0).getFullname());
        if (tagAdapter != null && !TextUtils.isEmpty(student.getStudent().getAssignmentremark().getTags()))
            tagAdapter.setTag(new Tag(Integer.valueOf(student.getStudent().getAssignmentremark().getTags())));
    }

    @Override
    public void setTagAdapter(List<Tag> tags) {
        if (!tags.isEmpty()) {
            if (rvTag.getAdapter() == null) {
                tagAdapter = new RemarkAssignmentAdapter(getContext());
                tagAdapter.setOnItemClickListener(this);
                rvTag.setAdapter(tagAdapter);
            }
            tagAdapter.update(tags);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_update:
                Student student = getStudent();
                if (tagAdapter != null && tagAdapter.getTag() != null)
                    student.getStudent().getAssignmentremark().setTags(String.valueOf(tagAdapter.getTag().getId()));
                assignmentView.setAssignmentAdapter(student);
                dismiss();
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (view.getId()) {
            case R.id.tv_tag:
                if (tagAdapter != null) {
                    tagAdapter.setTag(tagAdapter.getItem(position));
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }


}
