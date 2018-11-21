package com.pws.pateast.fragment.leave.teacher.apply;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.ListPopupWindow;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pws.pateast.R;
import com.pws.pateast.api.model.LeaveType;
import com.pws.pateast.api.model.Tag;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.fragment.assignment.teacher.add.AddAssignmentFragment;
import com.pws.pateast.fragment.leave.LeaveReasonAdapter;
import com.pws.pateast.listener.OnDateChangeListener;
import com.pws.pateast.utils.Utils;
import com.pws.pateast.widget.datepickerview.DatePickerEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.pws.pateast.utils.DateUtils.SERVER_DATE_TEMPLATE;

/**
 * Created by intel on 23-Aug-17.
 */

public class TeacherLeaveApplyFragment extends AppFragment implements TeacherLeaveApplyView, OnDateChangeListener, View.OnClickListener {
    private EditText etReasonOther;
    private DatePickerEditText etStartDate, etEndDate;
    private TextView tvDuration, tvSpecifyTime, tvLeaveType, tvSelectReason, tvLeaveBalance;
    private LinearLayout layoutDuration, layoutSpecifyTime, layoutOtherReason;
    private Button btnSubmit;

    private TeacherLeaveApplyPresenter mPresenter;

    private LeaveReasonAdapter reasonAdapter, typeAdapter, durationAdapter;
    private ListPopupWindow popUpLeaveType, popUpReason, popUpType, popUpDuration;
    private LeaveTypeAdapter leaveTypeAdapter;
    private Tag reasonTag, typeTag, timeTag;
    private LeaveType leaveType;
    private boolean isSameDay, isBetween;

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_teacher_leave_apply;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        getAppListener().setTitle(R.string.title_leave_apply);

        etReasonOther = (EditText) findViewById(R.id.et_reason_other);
        etStartDate = (DatePickerEditText) findViewById(R.id.et_start_date);
        etEndDate = (DatePickerEditText) findViewById(R.id.et_end_date);

        tvDuration = (TextView) findViewById(R.id.tv_duration);
        tvSpecifyTime = (TextView) findViewById(R.id.tv_specify_time);
        tvLeaveType = (TextView) findViewById(R.id.tv_leave_type);
        tvSelectReason = (TextView) findViewById(R.id.tv_select_reason);
        tvLeaveBalance = (TextView) findViewById(R.id.tv_leave_balance);

        layoutDuration = (LinearLayout) findViewById(R.id.layout_duration);
        layoutSpecifyTime = (LinearLayout) findViewById(R.id.layout_specify_time);
        layoutOtherReason = (LinearLayout) findViewById(R.id.layout_other_reason);

        tvLeaveBalance.setText(getString(R.string.title_balance, "0.00"));


        btnSubmit = (Button) findViewById(R.id.btn_submit);

        etStartDate.setManager(getFragmentManager(), this);
        etEndDate.setManager(getFragmentManager(), this);

        btnSubmit.setOnClickListener(this);
        tvDuration.setOnClickListener(this);
        tvSpecifyTime.setOnClickListener(this);
        tvLeaveType.setOnClickListener(this);
        tvSelectReason.setOnClickListener(this);

        mPresenter = new TeacherLeaveApplyPresenter();
        mPresenter.attachView(this);
        if (mPresenter.getUserType() == UserType.TEACHER) {
            etEndDate.setThemeId(R.style.DatePickerThemeTeacher);
            etStartDate.setThemeId(R.style.DatePickerThemeTeacher);
        } else {
            etEndDate.setThemeId(R.style.DatePickerThemeDriver);
            etStartDate.setThemeId(R.style.DatePickerThemeDriver);
        }
    }

    @Override
    public void setCalenderDate(Date minDate, Date maxDate) {
        Date currentTime = Calendar.getInstance().getTime();
        isBetween = currentTime.after(minDate) && currentTime.before(maxDate);
        if (isBetween) {
            minDate = currentTime;
        }
        etStartDate.setMinDate(minDate);
        etStartDate.setMaxDate(maxDate);
        etEndDate.setMinDate(minDate);
        etEndDate.setMaxDate(maxDate);
    }

    @Override
    public void setDateFormat(String dateFormat) {
        etStartDate.setDateFormat(dateFormat);
        etEndDate.setDateFormat(dateFormat);
    }


    @Override
    public void leaveApplied(String message) {
        Utils.showToast(getContext(), message);
        getActivity().setResult(AddAssignmentFragment.ADD_RESPONSE);
        getActivity().finish();
    }

    @Override
    public boolean isError() {
        boolean isError = false;

        if (TextUtils.isEmpty(etStartDate.getText())) {
            isError = true;
            setError(R.id.et_start_date, getString(R.string.validate_date_time_picker, getString(R.string.hint_start_date)));
        } else {
            setError(R.id.et_start_date, null);
        }

        if (TextUtils.isEmpty(etEndDate.getText())) {
            isError = true;
            setError(R.id.et_end_date, getString(R.string.validate_date_time_picker, getString(R.string.hint_end_date)));
        } else {
            setError(R.id.et_end_date, null);
        }

        if (isSameDay) {
            if (typeTag == null) {
                isError = true;
                setError(R.id.tv_duration, getString(R.string.validate_spinner, getString(R.string.hint_duration)));
            } else {
                setError(R.id.tv_duration, null);
            }

            if (typeTag != null && typeTag.getId() != 0 && timeTag == null) {
                isError = true;
                setError(R.id.tv_specify_time, getString(R.string.validate_spinner, getString(R.string.hint_specify_time)));
            } else {
                setError(R.id.tv_specify_time, null);
            }
        }
        if (leaveType == null) {
            isError = true;
            setError(R.id.tv_leave_type, getString(R.string.validate_spinner, getString(R.string.label_leave_type)));
        } else {
            setError(R.id.tv_leave_type, null);
        }

        if (reasonTag == null) {
            isError = true;
            setError(R.id.tv_select_reason, getString(R.string.validate_spinner, getString(R.string.hint_reason)));
        } else {
            setError(R.id.tv_select_reason, null);
        }

        if (reasonTag != null && reasonTag.getId() == 0 && TextUtils.isEmpty(etReasonOther.getText())) {
            isError = true;
            setError(R.id.et_reason_other, getString(R.string.validate_edittext, getString(R.string.hint_comment)));
        } else {
            setError(R.id.et_reason_other, null);
        }
        if (!isBetween) {
            isError = true;
            showDialog(getString(R.string.app_name), getString(R.string.leave_not_apply), null, R.string.ok);
        }
        return isError;
    }

    @Override
    public void setError(int id, String error) {
        switch (id) {
            case R.id.tv_select_reason:
                tvSelectReason.setError(error);
                break;
            case R.id.tv_leave_type:
                tvLeaveType.setError(error);
                break;
            case R.id.tv_duration:
                tvDuration.setError(error);
                break;
            case R.id.tv_specify_time:
                tvSpecifyTime.setError(error);
                break;
            case R.id.et_reason_other:
                etReasonOther.setError(error);
                break;
            case R.id.et_start_date:
                etStartDate.setError(error);
                break;
            case R.id.et_end_date:
                etEndDate.setError(error);
                break;
        }
    }

    @Override
    public void setText(int id, String text) {
        setError(id, null);
        switch (id) {
            case R.id.et_start_date:
                etStartDate.setText(text);
                break;
            case R.id.et_end_date:
                etEndDate.setText(text);
                break;
            case R.id.tv_duration:
                tvDuration.setText(text);
                break;
            case R.id.tv_specify_time:
                tvSpecifyTime.setText(text);
                break;
            case R.id.tv_leave_type:
                tvLeaveType.setText(text);
                if (leaveType != null)
                    tvLeaveBalance.setText(getString(R.string.title_balance, String.valueOf(leaveType.getBalance())));
                else
                    tvLeaveType.setText(getString(R.string.title_balance, "0.00"));
                break;
            case R.id.tv_select_reason:
                tvSelectReason.setText(text);
                if (text == null)
                    etReasonOther.setText(null);
                break;
        }
    }

    @Override
    public void setLeaveReasonAdapter(ArrayList<Tag> leaveReason) {
        leaveReason.add(leaveReason.size(), Tag.getTag(0, getString(R.string.hint_other)));
        if (popUpReason == null) {
            popUpReason = new ListPopupWindow(getContext());
            popUpReason.setAnchorView(tvSelectReason);
            popUpReason.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (reasonAdapter != null) {
                        reasonTag = reasonAdapter.getItem(position);
                        layoutOtherReason.setVisibility(reasonTag.getId() == 0 ? View.VISIBLE : View.GONE);
                        setText(R.id.tv_select_reason, reasonTag.getTagdetails().get(0).getTitle());
                    } else {
                        reasonTag = null;
                        layoutOtherReason.setVisibility(View.GONE);
                        setText(R.id.tv_select_reason, null);
                    }
                    popUpReason.dismiss();
                }
            });
        }
        if (reasonAdapter == null) {
            reasonAdapter = new LeaveReasonAdapter(getContext());

        }
        reasonAdapter.update(leaveReason);
        popUpReason.setAdapter(reasonAdapter);
    }

    @Override
    public void setTypeLeaveAdapter(ArrayList<Tag> leaveType) {
        if (popUpType == null) {
            popUpType = new ListPopupWindow(getContext());
            popUpType.setAnchorView(tvDuration);
            popUpType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (typeAdapter != null) {
                        typeTag = typeAdapter.getItem(position);
                        layoutSpecifyTime.setVisibility(typeTag.getId() != 0 ? View.VISIBLE : View.GONE);
                        setText(R.id.tv_duration, typeTag.getTagdetails().get(0).getTitle());
                    } else {
                        typeTag = null;
                        layoutSpecifyTime.setVisibility(View.GONE);
                        setText(R.id.tv_duration, null);
                    }
                    popUpType.dismiss();
                }
            });
        }
        if (typeAdapter == null) {
            typeAdapter = new LeaveReasonAdapter(getContext());

        }
        typeAdapter.update(leaveType);
        popUpType.setAdapter(typeAdapter);

    }

    @Override
    public void setLeaveTypeAdapter(ArrayList<LeaveType> leaveTypes) {
        if (popUpLeaveType == null) {
            popUpLeaveType = new ListPopupWindow(getContext());
            popUpLeaveType.setAnchorView(tvLeaveType);
            popUpLeaveType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (leaveTypeAdapter != null) {
                        leaveType = leaveTypeAdapter.getItem(position);
                        setText(R.id.tv_leave_type, leaveType.getEmpleavetypedetails().get(0).getName());
                    } else {
                        leaveType = null;
                        setText(R.id.tv_leave_type, null);
                    }
                    popUpLeaveType.dismiss();
                }
            });
        }
        if (leaveTypeAdapter == null) {
            leaveTypeAdapter = new LeaveTypeAdapter(getContext());

        }
        leaveTypeAdapter.update(leaveTypes);
        popUpLeaveType.setAdapter(leaveTypeAdapter);
    }

    @Override
    public void setLeaveDurationAdapter(ArrayList<Tag> leaveDuration) {
        if (popUpDuration == null) {
            popUpDuration = new ListPopupWindow(getContext());
            popUpDuration.setAnchorView(tvSpecifyTime);
            popUpDuration.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (durationAdapter != null) {
                        timeTag = durationAdapter.getItem(position);
                        setText(R.id.tv_specify_time, timeTag.getTagdetails().get(0).getTitle());
                    } else {
                        timeTag = null;
                        setText(R.id.tv_specify_time, null);
                    }
                    popUpDuration.dismiss();
                }
            });
        }
        if (durationAdapter == null) {
            durationAdapter = new LeaveReasonAdapter(getContext());

        }
        durationAdapter.update(leaveDuration);
        popUpDuration.setAdapter(durationAdapter);
    }

    @Override
    public void onDateChange(View view, Calendar time, String timeString) {
        isSameDay = false;
        switch (view.getId()) {
            case R.id.et_start_date:
                setText(R.id.et_start_date, timeString);
                Calendar calendar = (Calendar) time.clone();
                if (etEndDate.getDate() != null && etEndDate.getDate().getTime().before(time.getTime())) {
                    etEndDate.setText("");
                }
                etEndDate.setMinDate(calendar.getTime());
                break;
            case R.id.et_end_date:
                setText(R.id.et_end_date, timeString);
                break;
        }
        if (!TextUtils.isEmpty(etStartDate.getText()) && !TextUtils.isEmpty(etEndDate.getText())) {
            if (etStartDate.getText().toString().equalsIgnoreCase(etEndDate.getText().toString())) {
                layoutDuration.setVisibility(View.VISIBLE);
                isSameDay = true;
            } else {
                layoutDuration.setVisibility(View.GONE);
                layoutSpecifyTime.setVisibility(View.GONE);
            }
        } else {
            layoutDuration.setVisibility(View.GONE);
            layoutSpecifyTime.setVisibility(View.GONE);
        }
        setText(R.id.tv_duration, null);
        setText(R.id.tv_specify_time, null);
        typeTag = null;
        timeTag = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                mPresenter.teacherLeaveApply(leaveType, reasonTag, typeTag, timeTag, etStartDate.getDateString(SERVER_DATE_TEMPLATE), etEndDate.getDateString(SERVER_DATE_TEMPLATE), etReasonOther.getText().toString());
                break;
            case R.id.tv_duration:
                popUpType.show();
                break;
            case R.id.tv_specify_time:
                popUpDuration.show();
                break;
            case R.id.tv_select_reason:
                if (popUpReason != null)
                    popUpReason.show();
                break;
            case R.id.tv_leave_type:
                if (popUpLeaveType != null)
                    popUpLeaveType.show();
                break;
        }
    }
}
