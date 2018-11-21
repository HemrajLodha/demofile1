package com.pws.pateast.fragment.schedule.parent.holiday;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.adapter.viewholder.BaseItemViewHolder;
import com.pws.pateast.R;
import com.pws.pateast.api.model.Holiday;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.utils.DateUtils;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by intel on 25-Aug-17.
 */

public class HolidayAdapter extends BaseRecyclerAdapter<Object, BaseItemViewHolder> {
    private UserType userType;

    public HolidayAdapter(Context context) {
        super(context);
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    @Override
    protected int getItemResourceLayout(int viewType) {
        return R.layout.item_holiday;
    }

    @Override
    public BaseItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HolidayHolder(getView(parent, viewType));
    }

    class HolidayHolder extends BaseItemViewHolder {
        protected TextView tvHoliday, tvStartDate, tvStartDay, tvEndDate, tvEndDay;
        protected Calendar calendarStart, calenderEnd;

        public HolidayHolder(View view) {
            super(view);
            tvHoliday = (TextView) findViewById(R.id.tv_holiday);
            tvStartDate = (TextView) findViewById(R.id.tv_start_date);
            tvStartDay = (TextView) findViewById(R.id.tv_start_day);
            tvEndDate = (TextView) findViewById(R.id.tv_end_date);
            tvEndDay = (TextView) findViewById(R.id.tv_end_day);
            calendarStart = Calendar.getInstance();
            calenderEnd = Calendar.getInstance();
        }

        @Override
        public void bind(Object o) {
            Holiday holiday = (Holiday) o;
            tvHoliday.setText(holiday.getHolidaydetails().get(0).getName());
            calendarStart.setTime(DateUtils.parse(holiday.getStart_date(), DateUtils.DATE_FORMAT_PATTERN));
            calenderEnd.setTime(DateUtils.parse(holiday.getEnd_date(), DateUtils.DATE_FORMAT_PATTERN));

            tvStartDate.setText(DateUtils.toDate(calendarStart.getTime(), "dd", Locale.getDefault()));
            tvStartDay.setText(calendarStart.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));
            if (!calendarStart.getTime().equals(calenderEnd.getTime())) {
                tvEndDate.setText(DateUtils.toDate(calenderEnd.getTime(), "dd", Locale.getDefault()));
                tvEndDay.setText(calenderEnd.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));
                ((LinearLayout) tvEndDate.getParent()).setVisibility(View.VISIBLE);
            } else {
                ((LinearLayout) tvEndDate.getParent()).setVisibility(View.GONE);
            }
        }
    }
}
