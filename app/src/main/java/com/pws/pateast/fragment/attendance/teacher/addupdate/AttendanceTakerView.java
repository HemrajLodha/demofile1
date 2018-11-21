package com.pws.pateast.fragment.attendance.teacher.addupdate;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.pws.pateast.R;

/**
 * Created by planet on 5/1/2017.
 */

public class AttendanceTakerView extends LinearLayout implements View.OnClickListener {

    private static final boolean DEFAULT_ACT_AS_SELECTOR = false;
    private static final int DEFAULT_VISIBLE_COUNT = 3;
    public static final int SELECTED_PRESENT = 1, SELECTED_ABSENT = 3, SELECTED_LEAVE = 2;
    private boolean actAsSelector;
    private int selected;
    private Button mBtnPresent, mBtnAbsent, mBtnLeave;
    private ViewGroup buttonViewGroup;
    private int[] filledColorRes;
    private int[] blankColorRes;
    private int[] colorRes;
    private OnClickListener onClickListener;

    public AttendanceTakerView(Context context) {
        super(context);
        init(context, null);
    }

    public AttendanceTakerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AttendanceTakerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AttendanceTakerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private void init(Context context, AttributeSet attrs) {
        try {
            final TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.AttendanceTakerView);
            actAsSelector = array.getBoolean(R.styleable.AttendanceTakerView_act_as_selector, DEFAULT_ACT_AS_SELECTOR);
            selected = array.getInt(R.styleable.AttendanceTakerView_selected, SELECTED_PRESENT);
            filledColorRes = new int[]{R.drawable.round_fill_green, R.drawable.round_fill_yellow, R.drawable.round_fill_red};
            blankColorRes = new int[]{R.drawable.round_blank_green, R.drawable.round_blank_yellow, R.drawable.round_blank_red};
            colorRes = new int[]{R.color.md_green_800, R.color.md_yellow_800, R.color.md_red_800};
            array.recycle();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        View mView = LayoutInflater.from(getContext()).inflate(R.layout.attendance_taker_view, this, true);
        buttonViewGroup = (ViewGroup) mView.findViewById(R.id.button_v_group);
        mBtnPresent = (Button) mView.findViewById(R.id.present);
        mBtnAbsent = (Button) mView.findViewById(R.id.absent);
        mBtnLeave = (Button) mView.findViewById(R.id.leave);
       /* mBtnPresent.setOnClickListener(this);
        mBtnAbsent.setOnClickListener(this);
        mBtnLeave.setOnClickListener(this);*/
        setViewsVisibility();
    }

    public void setActAsSelector(boolean actAsSelector) {
        this.actAsSelector = actAsSelector;
        setViewsVisibility();
    }

    public void setSelected(int selected) {
        this.selected = selected;
        setViewsVisibility();
    }

    public int getSelected() {
        return selected;
    }

    private void setViewsVisibility() {
        if (buttonViewGroup == null) return;

        for (int i = SELECTED_PRESENT; i <= DEFAULT_VISIBLE_COUNT; i++) {
            Button child = (Button) buttonViewGroup.getChildAt(i - 1);
            if (actAsSelector) {
                if (selected == i) {
                    child.setVisibility(VISIBLE);
                    child.setOnClickListener(onClickListener);
                } else {
                    child.setVisibility(GONE);
                    child.setOnClickListener(null);
                }
            } else {
                child.setOnClickListener(this);
                child.setVisibility(VISIBLE);
            }
            if (i == selected) {
                child.setBackgroundResource(filledColorRes[i-1]);
                child.setTextColor(Color.WHITE);
            } else {
                child.setBackgroundResource(blankColorRes[i-1]);
                child.setTextColor(ContextCompat.getColor(getContext(), colorRes[i-1]));
            }
        }
    }

    @Override
    public void onClick(View v) {

        if (actAsSelector) return;

        switch (v.getId()) {
            case R.id.present:
                setSelected(SELECTED_PRESENT);
                break;
            case R.id.absent:
                setSelected(SELECTED_ABSENT);
                break;
            case R.id.leave:
                setSelected(SELECTED_LEAVE);
                break;
        }
        if(onClickListener != null)
            onClickListener.onClick(this);
    }
}
