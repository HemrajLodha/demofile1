package com.pws.pateast.api.comparator;

import android.content.Context;

import com.pws.pateast.R;
import com.pws.pateast.api.model.Holiday;
import com.pws.pateast.api.model.Schedule;

import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by intel on 25-Aug-17.
 */

public class HolidayComparator implements Comparator<Holiday>
{
    private Map<String, Integer> monthToOrder;
    private Calendar calendar;

    public HolidayComparator(Context context, Calendar calendar)
    {
        monthToOrder = new HashMap<>();
        this.calendar = calendar;
        for (String day: context.getResources().getStringArray(R.array.month))
            monthToOrder.put(day, monthToOrder.size());
    }

    public Map<String, Integer> getDayOrder() {
        return monthToOrder;
    }

    @Override
    public int compare(Holiday o1, Holiday o2) {
        return monthToOrder.get(o1.getMonth(calendar)) - monthToOrder.get(o2.getMonth(calendar));
    }
}
