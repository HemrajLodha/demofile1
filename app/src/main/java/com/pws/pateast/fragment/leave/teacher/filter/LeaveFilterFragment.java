package com.pws.pateast.fragment.leave.teacher.filter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pws.pateast.listener.OnDateChangeListener;
import com.pws.pateast.R;
import com.pws.pateast.base.AppDialogFragment;
import com.pws.pateast.fragment.leave.teacher.approve.ClassLeaveView;
import com.pws.pateast.widget.datepickerview.DatePickerEditText;

import java.util.Calendar;

/**
 * Created by intel on 03-Jul-17.
 */

public class LeaveFilterFragment extends AppDialogFragment implements LeaveFilterView,View.OnClickListener,OnDateChangeListener
{
    private Button btnApply, btnReset;

    private EditText etStudentName;
    private DatePickerEditText etStartDate,etEndDate;

    private LeaveFilterPresenter filterPresenter;

    private ClassLeaveView mLeaveView;

    public void attachView(ClassLeaveView mLeaveView)
    {
        this.mLeaveView = mLeaveView;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_leave_filter;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);
        setTitle(R.string.filter);
        btnApply = (Button) findViewById(R.id.btn_apply);
        btnReset = (Button) findViewById(R.id.btn_reset);

        etStudentName = (EditText) findViewById(R.id.et_student_name);
        etStartDate = (DatePickerEditText) findViewById(R.id.et_start_date);
        etEndDate = (DatePickerEditText) findViewById(R.id.et_end_date);

        etStartDate.setManager(getFragmentManager(),this);
        etEndDate.setManager(getFragmentManager(),this);

        btnApply.setOnClickListener(this);
        btnReset.setOnClickListener(this);

        filterPresenter = new LeaveFilterPresenter();
        filterPresenter.attachView(this);
        filterPresenter.setFilterData(mLeaveView);
    }

    @Override
    public void setDate(int id, String date)
    {
        switch (id)
        {
            case R.id.et_start_date:
                etStartDate.setText(date);
                break;
            case R.id.et_end_date:
                etEndDate.setText(date);
                break;
        }
    }

    @Override
    public void setStudentName(String studentName)
    {
        etStudentName.setText(studentName);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_apply:
                if (mLeaveView != null)
                {
                    mLeaveView.setFilter(etStudentName.getText().toString(),etStartDate.getText().toString(),etEndDate.getText().toString());
                    dismiss();
                }
                break;
            case R.id.btn_reset:
                if (mLeaveView != null) {
                    mLeaveView.clearFilter();
                    filterPresenter.setFilterData(mLeaveView);
                }
                break;
        }
    }

    @Override
    public void onDateChange(View view, Calendar time, String timeString)
    {
        setDate(view.getId(),timeString);
        switch (view.getId())
        {
            case R.id.et_start_date:
                Calendar calendar = (Calendar) time.clone();
                if (etEndDate.getDate() != null && etEndDate.getDate().getTime().before(time.getTime())) {
                    etEndDate.setText("");
                }
                etEndDate.setMinDate(calendar.getTime());
                etEndDate.setDate(calendar);
                break;
            case R.id.et_end_date:

                break;
        }
    }
}
