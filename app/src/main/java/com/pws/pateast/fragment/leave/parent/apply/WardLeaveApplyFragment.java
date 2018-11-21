package com.pws.pateast.fragment.leave.parent.apply;

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
import com.pws.pateast.api.model.Tag;
import com.pws.pateast.api.model.Ward;
import com.pws.pateast.base.AppFragment;
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
 * Created by intel on 19-Aug-17.
 */

public class WardLeaveApplyFragment extends AppFragment implements WardLeaveApplyView, OnDateChangeListener, View.OnClickListener {
    /*private ImageView imgWardProfile;
    private TextView tvWardName, tvWardClass, tvWardInstitute;*/
    private EditText etReasonOther;
    private DatePickerEditText etStartDate, etEndDate;
    private TextView tvDuration, tvSpecifyTime, tvSelectReason;
    private LinearLayout layoutDuration, layoutSpecifyTime, layoutOtherReason;
    private Button btnSubmit;

    private WardLeaveApplyPresenter mPresenter;

    private LeaveReasonAdapter reasonAdapter, typeAdapter, durationAdapter;
    private ListPopupWindow popUpReason, popUpType, popUpDuration;

    private Tag reasonTag, typeTag, timeTag;
    private boolean isSameDay,isBetween;

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_student_leave_apply;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        getAppListener().setTitle(R.string.title_leave_apply);

        etReasonOther = (EditText) findViewById(R.id.et_reason_other);
        etStartDate = (DatePickerEditText) findViewById(R.id.et_start_date);
        etEndDate = (DatePickerEditText) findViewById(R.id.et_end_date);

        tvDuration = (TextView) findViewById(R.id.tv_duration);
        tvSpecifyTime = (TextView) findViewById(R.id.tv_specify_time);
        tvSelectReason = (TextView) findViewById(R.id.tv_select_reason);

        layoutDuration = (LinearLayout) findViewById(R.id.layout_duration);
        layoutSpecifyTime = (LinearLayout) findViewById(R.id.layout_specify_time);
        layoutOtherReason = (LinearLayout) findViewById(R.id.layout_other_reason);


        btnSubmit = (Button) findViewById(R.id.btn_submit);

        etStartDate.setManager(getFragmentManager(), this);
        etEndDate.setManager(getFragmentManager(), this);
        etStartDate.setThemeId(R.style.DatePickerThemeParent);
        etEndDate.setThemeId(R.style.DatePickerThemeParent);

        btnSubmit.setOnClickListener(this);
        tvDuration.setOnClickListener(this);
        tvSpecifyTime.setOnClickListener(this);
        tvSelectReason.setOnClickListener(this);

        mPresenter = new WardLeaveApplyPresenter();
        mPresenter.attachView(this);
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
    public void setWardData(Ward ward) {

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
                mPresenter.wardLeaveApply(reasonTag, typeTag, timeTag, etStartDate.getDateString(SERVER_DATE_TEMPLATE), etEndDate.getDateString(SERVER_DATE_TEMPLATE), etReasonOther.getText().toString());
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
        }
    }
}
