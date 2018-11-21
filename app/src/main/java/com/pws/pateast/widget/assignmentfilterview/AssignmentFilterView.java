package com.pws.pateast.widget.assignmentfilterview;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.ListPopupWindow;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.pws.pateast.R;
import com.pws.pateast.adapter.SpinnerAdapter;
import com.pws.pateast.adapter.SubjectAdapter;
import com.pws.pateast.api.model.Subject;
import com.pws.pateast.api.model.Tag;
import com.pws.pateast.api.model.User;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.listener.OnDateChangeListener;
import com.pws.pateast.utils.Preference;
import com.pws.pateast.widget.datepickerview.DatePickerTextView;

import java.util.Calendar;
import java.util.List;

/**
 * Created by planet on 8/18/2017.
 */

public class AssignmentFilterView extends ConstraintLayout implements View.OnClickListener {
    private View mView;
    private TextView tvFilterSubject, tvFilterRemark;
    private DatePickerTextView tvFilterDate;
    private DropdownListener onClickListener;
    private List<Subject> subjects;
    private List<Tag> tags;
    private Preference preference;
    private User user;

    public AssignmentFilterView(Context context) {
        super(context);
        init();
    }

    public AssignmentFilterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AssignmentFilterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        preference = Preference.get(getContext());
        user = preference.getUser();
        mView = LayoutInflater.from(getContext()).inflate(R.layout.assignment_filter_view, this, true);
        tvFilterSubject = mView.findViewById(R.id.tv_subject);
        tvFilterRemark = mView.findViewById(R.id.tv_remark);
        tvFilterDate = mView.findViewById(R.id.tv_due_date);
        tvFilterSubject.setOnClickListener(this);
        tvFilterRemark.setOnClickListener(this);
        tvFilterDate.setDateFormat(preference.getDateFormat());
        tvFilterDate.setMinDate(Calendar.getInstance().getTime());
        if (getContext() instanceof AppCompatActivity) {
            tvFilterDate.setManager(((AppCompatActivity) getContext()).getSupportFragmentManager(), onDateChangeListener);
        }
        switch (UserType.getUserType(user.getData().getUser_type())) {
            case STUDENT:
                tvFilterDate.setThemeId(R.style.DatePickerThemeStudent);
                break;
            case PARENT:
                tvFilterDate.setThemeId(R.style.DatePickerThemeParent);
                break;
        }
    }

    private void tintViewDrawable(AppCompatTextView view) {
        Drawable[] drawables = view.getCompoundDrawables();
        for (Drawable drawable : drawables) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary), PorterDuff.Mode.MULTIPLY));
            }
        }
    }

    @Override
    public void setVisibility(int visibility) {
        if (visibility == GONE) {
            tvFilterSubject.setText(R.string.hint_filter_by_subject);
            tvFilterRemark.setText(R.string.hint_filter_by_remark);
            tvFilterDate.setText(R.string.hint_filter_by_due_date);
        }
        super.setVisibility(visibility);
    }

    public void setDropdownListener(DropdownListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private OnDateChangeListener onDateChangeListener = new OnDateChangeListener() {
        @Override
        public void onDateChange(View view, Calendar time, String timeString) {
            tvFilterDate.setText(timeString);
            if (onClickListener != null) {
                onClickListener.onDateChange(view, time, timeString);
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_subject:
                if (onClickListener != null) {
                    onClickListener.onClick(v);
                }
                break;
            case R.id.tv_remark:
                if (onClickListener != null) {
                    onClickListener.onClick(v);
                }
                break;
            case R.id.tv_due_date:
                break;
        }
    }

    public void setSubjectsAdapter(List<Subject> subjects) {
        this.subjects = subjects;
        openSubjectDropdown(this.subjects, tvFilterSubject);
    }

    public void setTagsAdapter(List<Tag> tags) {
        this.tags = tags;
        openTagsDropdown(this.tags, tvFilterRemark);
    }

    private void openSubjectDropdown(List<Subject> list, final View anchor) {
        final ListPopupWindow popupWindow = new ListPopupWindow(getContext());
        final SubjectAdapter dataAdapter = new SubjectAdapter(getContext(), list);
        popupWindow.setAnchorView(anchor);
        popupWindow.setAdapter(dataAdapter);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (onClickListener != null) {
                    tvFilterSubject.setText(dataAdapter.getItem(position).getSubject().getSubjectdetails().get(0).getName());
                    onClickListener.onSubjectItemClick(view, position);
                }
                popupWindow.dismiss();
            }
        });
        popupWindow.show();
    }


    private void openTagsDropdown(List<Tag> list, final View anchor) {
        final ListPopupWindow popupWindow = new ListPopupWindow(getContext());
        final SpinnerAdapter dataAdapter = new SpinnerAdapter(getContext(), list);
        popupWindow.setAnchorView(anchor);
        popupWindow.setAdapter(dataAdapter);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (onClickListener != null) {
                    tvFilterRemark.setText(dataAdapter.getItem(position).getTagdetails().get(0).getTitle());
                    onClickListener.onRemarkItemClick(view, position);
                }
                popupWindow.dismiss();
            }
        });
        popupWindow.show();
    }

}
