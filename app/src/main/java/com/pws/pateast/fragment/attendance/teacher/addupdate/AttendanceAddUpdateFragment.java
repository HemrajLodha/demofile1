package com.pws.pateast.fragment.attendance.teacher.addupdate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration;
import com.pws.pateast.api.model.Student;
import com.pws.pateast.api.model.Tag;
import com.pws.pateast.R;
import com.pws.pateast.base.AppDialogFragment;
import com.pws.pateast.base.Extras;
import com.pws.pateast.fragment.attendance.teacher.AttendanceView;

import java.util.Arrays;
import java.util.List;

/**
 * Created by planet on 5/1/2017.
 */

public class AttendanceAddUpdateFragment extends AppDialogFragment implements AddUpdateAttendanceView, View.OnClickListener,BaseRecyclerAdapter.OnItemClickListener {
    private AddUpdateAttendancePresenter mPresenter;
    private TextView tvStudentName;
    private AttendanceTakerView attendanceTakerView;
    private AttendanceView attendanceView;
    private RecyclerView rvTag;
    private ChipsLayoutManager clm;
    private AttendanceTagAdapter tagAdapter;
    private Button btnUpdate;
    @Override
    protected int getContentLayout() {
        return R.layout.add_update_attendance_dialog;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    public void attachView(AttendanceView attendanceView) {
        this.attendanceView = attendanceView;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);
        setToolbarVisibility(View.GONE);

        tvStudentName = (TextView) findViewById(R.id.tv_student_name);
        attendanceTakerView = (AttendanceTakerView) findViewById(R.id.attendance_taker_view);
        rvTag = (RecyclerView) findViewById(R.id.rv_tag);
        clm = ChipsLayoutManager.newBuilder(getContext()).build();
        rvTag.setLayoutManager(clm);
        rvTag.addItemDecoration(new SpacingItemDecoration(getResources().getDimensionPixelOffset(R.dimen.size_4),getResources().getDimensionPixelOffset(R.dimen.size_4)));
        btnUpdate = (Button) findViewById(R.id.update);
        btnUpdate.setOnClickListener(this);
        attendanceTakerView.setOnClickListener(this);

        mPresenter = new AddUpdateAttendancePresenter();
        mPresenter.attachView(this);
        mPresenter.getTags(attendanceView);
        if (getArguments() != null) {
            Student student = getArguments().getParcelable(Extras.EXTRA_STUDENT_DATA);
            mPresenter.setStudent(student);
        }

        mPresenter.setActionButtonText(attendanceView.isUpdate());
    }


    @Override
    public void setActionButtonText(int res) {
        btnUpdate.setText(res);
    }

    @Override
    public void setData(Student student) {
        tvStudentName.setText(student.getStudent().getUser().getUserdetails().get(0).getFullname());
        attendanceTakerView.setSelected(student.getIs_present());
        if(tagAdapter != null && !TextUtils.isEmpty(student.getTags()))
            tagAdapter.setTags(Arrays.asList(student.getTags().split(",")));
    }

    @Override
    public void setTagAdapter(List<Tag> tags) {
        if(!tags.isEmpty())
        {
            if(rvTag.getAdapter() == null)
            {
                tagAdapter = new AttendanceTagAdapter(getContext());
                tagAdapter.setOnItemClickListener(this);
                rvTag.setAdapter(tagAdapter);
            }
            tagAdapter.update(tags);
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.update:
                if(tagAdapter != null && tagAdapter.getTags() != null)
                    mPresenter.getStudent().setTags(TextUtils.join(",",tagAdapter.getTags()));
                mPresenter.getStudent().setIs_present(attendanceTakerView.getSelected());
                attendanceView.setAttendanceAdapter(mPresenter.getStudent());
                dismiss();
                break;
            case R.id.attendance_taker_view:
                //setTagVisibility(attendanceTakerView.getSelected());
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (view.getId())
        {
            case R.id.tv_tag:
                if(tagAdapter != null)
                {
                    Tag tag = tagAdapter.getItem(position);
                    view.setSelected(!view.isSelected());
                    if(view.isSelected())
                        tagAdapter.getTags().add(String.valueOf(tag.getId()));
                    else
                        tagAdapter.getTags().remove(String.valueOf(tag.getId()));
                }
                break;
        }
    }
}
