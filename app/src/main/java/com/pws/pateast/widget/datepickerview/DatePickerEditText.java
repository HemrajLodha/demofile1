package com.pws.pateast.widget.datepickerview;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.FragmentManager;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;

import com.pws.pateast.R;
import com.pws.pateast.listener.OnDateChangeListener;
import com.pws.pateast.utils.DateUtils;
import com.pws.pateast.widget.fragment.DatePickerFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.view.View.OnFocusChangeListener;


public class DatePickerEditText extends TextInputEditText
        implements View.OnClickListener, OnFocusChangeListener,
        DatePickerDialog.OnDateSetListener {
    private static final String TAG = DatePickerEditText.class.getSimpleName();

    private OnFocusChangeListener onFocusChangedListener;
    private OnDateChangeListener onDateChangeListener;

    private FragmentManager manager;

    private Integer themeId;

    private String dateFormat;
    private String minDate;
    private String maxDate;
    private DatePickerFragment datePickerFragment;
    private Calendar date;


    public DatePickerEditText(Context context) {
        super(context);
        init();
    }

    public DatePickerEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        handleAttributes(attrs);
        init();
    }

    public DatePickerEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        handleAttributes(attrs);
        init();
    }


    private void init() {
        setFocusable(false);
        setOnClickListener(this);
        setOnFocusChangeListener(this);
        setInputType(InputType.TYPE_NULL);
    }

    private void handleAttributes(@NonNull AttributeSet attributeSet) {
        try {
            final TypedArray array = getContext().obtainStyledAttributes(attributeSet, R.styleable.DateTimePickerView);

            themeId = array.getResourceId(R.styleable.DateTimePickerView_theme, 0);

            dateFormat = array.getString(R.styleable.DateTimePickerView_dateFormat);
            minDate = array.getString(R.styleable.DateTimePickerView_minDate);
            maxDate = array.getString(R.styleable.DateTimePickerView_maxDate);

            array.recycle();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onFocusChange(View view, boolean isFocused) {
        final InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindowToken(), 0);

       /* if (isFocused) {
            if (date == null && !TextUtils.isEmpty(getText())) {
                date = Calendar.getInstance();
                date.setTime(DateUtils.parse(getText().toString(), dateFormat));
            }
            datePickerFragment = new DatePickerFragment()
                    .setDate(date)
                    .setDateFormat(dateFormat)
                    .setThemeId(themeId)
                    .setOnDateSetListener(this)
                    .setMinDate(minDate)
                    .setMaxDate(maxDate);
        }
*/
        if (null != onFocusChangedListener) {
            onFocusChangedListener.onFocusChange(view, isFocused);
        }

    }


    public void show() {
        if (date == null && !TextUtils.isEmpty(getText())) {
            date = Calendar.getInstance();
            date.setTime(DateUtils.parse(getText().toString(), dateFormat));
        }
        datePickerFragment = new DatePickerFragment()
                .setDate(date)
                .setDateFormat(dateFormat)
                .setThemeId(themeId)
                .setOnDateSetListener(this)
                .setMinDate(minDate)
                .setMaxDate(maxDate);
        if (datePickerFragment != null)
            datePickerFragment.show(manager, TAG);
    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        final Calendar calendar = Calendar.getInstance(Locale.getDefault());

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        setText(DateUtils.toDate(calendar.getTime(), dateFormat, Locale.getDefault()));
        date = calendar;
        if (onDateChangeListener != null) {
            onDateChangeListener.onDateChange(this, date, DateUtils.toDate(calendar.getTime(), dateFormat, Locale.getDefault()));
        }
    }

    public FragmentManager getManager() {
        return manager;
    }

    public DatePickerEditText setManager(@NonNull FragmentManager manager) {
        this.manager = manager;
        return this;
    }

    public DatePickerEditText setManager(@NonNull FragmentManager manager, OnDateChangeListener onDateChangeListener) {
        this.manager = manager;
        this.onDateChangeListener = onDateChangeListener;
        return this;
    }

    public Calendar getDate() {
        if (date == null && !TextUtils.isEmpty(getText())) {
            date = Calendar.getInstance();
            date.setTime(DateUtils.parse(getText().toString(), dateFormat));
        }
        return date;
    }

    public Date getTime() {
        if (getDate() != null)
            return getDate().getTime();
        return Calendar.getInstance().getTime();
    }

    public DatePickerEditText setDate(@NonNull Calendar date) {
        this.date = date;
        return this;
    }

    public DatePickerEditText setDate(@NonNull Date date) {
        this.date = Calendar.getInstance();
        this.date.setTime(date);
        return this;
    }

    public void setOnDateChangeListener(OnDateChangeListener onDateChangeListener) {
        this.onDateChangeListener = onDateChangeListener;
    }

    public OnFocusChangeListener getOnFocusChangedListener() {
        return onFocusChangedListener;
    }

    public DatePickerEditText setOnFocusChangedListener(OnFocusChangeListener onFocusChangedListener) {
        this.onFocusChangedListener = onFocusChangedListener;
        return this;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public DatePickerEditText setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
        return this;
    }

    public Integer getThemeId() {
        return themeId;
    }

    public DatePickerEditText setThemeId(Integer themeId) {
        this.themeId = themeId;
        return this;
    }

    public String getMaxDate() {
        return maxDate;
    }

    public DatePickerEditText setMaxDate(String maxDate) {
        this.maxDate = maxDate;
        return this;
    }

    public DatePickerEditText setMaxDate(Date maxDate) {
        this.maxDate = new SimpleDateFormat(getDateFormat()).format(maxDate);
        return this;
    }

    public String getMinDate() {
        return minDate;
    }

    public DatePickerEditText setMinDate(String minDate) {
        this.minDate = minDate;
        return this;
    }

    public DatePickerEditText setMinDate(Date minDate) {
        this.minDate = new SimpleDateFormat(getDateFormat()).format(minDate);
        return this;
    }

    public String getDateString() {
        return DateUtils.toDate(getTime(), dateFormat);
    }

    public String getDateString(String dateFormat) {
        return DateUtils.toDate(getTime(), dateFormat);
    }

    @Override
    public void onClick(View view) {
        show();
    }
}