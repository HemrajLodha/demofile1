package com.pws.pateast.api.comparator;

import android.content.Context;

import com.pws.pateast.api.model.Schedule;
import com.pws.pateast.R;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by intel on 25-Apr-17.
 */

public class ScheduleComparator implements Comparator<Schedule>
{
    private Map<String, Integer> dayToOrder;

    public ScheduleComparator(Context context)
    {
        dayToOrder = new HashMap<>();
        for (String day: context.getResources().getStringArray(R.array.week))
            dayToOrder.put(day, dayToOrder.size());
    }

    public Map<String, Integer> getDayOrder() {
        return dayToOrder;
    }

    @Override
    public int compare(Schedule o1, Schedule o2) {
        return dayToOrder.get(o1.getWeekday()) - dayToOrder.get(o2.getWeekday());
    }
}
