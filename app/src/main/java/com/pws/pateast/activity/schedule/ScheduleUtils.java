package com.pws.pateast.activity.schedule;

import android.content.Context;

import com.pws.pateast.R;
import com.pws.pateast.api.model.Session;
import com.pws.pateast.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by intel on 21-Aug-17.
 */

public class ScheduleUtils {

    private Calendar FIRST_DAY_OF_TIME;
    private Calendar LAST_DAY_OF_TIME;
    private int DAYS_OF_TIME;
    private int MONTH_OF_TIME;


    public void setSession(Session session)
    {
        FIRST_DAY_OF_TIME = Calendar.getInstance();
        FIRST_DAY_OF_TIME.setTime(getFormattedDate(session.getStart_date(), DateUtils.DATE_FORMAT_PATTERN));
        LAST_DAY_OF_TIME = Calendar.getInstance();
        LAST_DAY_OF_TIME.setTime(getFormattedDate(session.getEnd_date(), DateUtils.DATE_FORMAT_PATTERN));
        DAYS_OF_TIME = (int) ((LAST_DAY_OF_TIME.getTimeInMillis() - FIRST_DAY_OF_TIME.getTimeInMillis()) / (24 * 60 * 60 * 1000));
        MONTH_OF_TIME = (int) Math.floor(DAYS_OF_TIME / 30);
    }

    public int getDays() {
        return DAYS_OF_TIME;
    }

    public int getMonth() {
        return MONTH_OF_TIME;
    }

    /**
     * Get the position in the ViewPager for a given day
     *
     * @param day
     * @return the position or 0 if day is null
     */
    public int getPositionForDay(Calendar day) {
        if (day != null) {
            return (int) ((day.getTimeInMillis() - FIRST_DAY_OF_TIME.getTimeInMillis())
                    / (24 * 60 * 60 * 1000)
            );
        }
        return 0;
    }

    /**
     * Get the position in the ViewPager for a given day
     *
     * @param day
     * @return the position or 0 if day is null
     */
    public int getPositionForMonth(Calendar day) {
        if (day != null) {
            return (int) Math.floor(((day.getTimeInMillis() - FIRST_DAY_OF_TIME.getTimeInMillis())
                    / (24 * 60 * 60 * 1000) / 30)
            );
        }
        return 0;
    }

    /**
     * Get the day for a given position in the ViewPager
     *
     * @param position
     * @return the day
     * @throws IllegalArgumentException if position is negative
     */
    public Calendar getDayForPosition(int position) throws IllegalArgumentException {
        if (position < 0) {
            throw new IllegalArgumentException("position cannot be negative");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(FIRST_DAY_OF_TIME.getTimeInMillis());
        cal.add(Calendar.DAY_OF_YEAR, position);
        return cal;
    }

    /**
     * Get the day for a given position in the ViewPager
     *
     * @param position
     * @return the day
     * @throws IllegalArgumentException if position is negative
     */
    public Calendar getMonthForPosition(int position) throws IllegalArgumentException {
        if (position < 0) {
            throw new IllegalArgumentException("position cannot be negative");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(FIRST_DAY_OF_TIME.getTimeInMillis());
        cal.add(Calendar.MONTH, position);
        return cal;
    }


    public static String getFormattedDate(Date date,String format) {
        return DateUtils.toDate(date,format);
    }

    public static Date getFormattedDate(String date,String format) {
        return DateUtils.parse(date,format);
    }

}
