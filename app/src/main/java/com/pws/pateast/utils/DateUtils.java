package com.pws.pateast.utils;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public final class DateUtils {
    private static final String LOG_TAG = DateUtils.class.getSimpleName();

    public static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String TIME_FORMAT_PATTERN = "HH:mm:ss";
    public static final String MONTH_FORMAT_PATTERN = "MMM - yyyy";
    public static final String MONTH_FORMAT_PATTERN_2 = "MMMM yyyy";
    public static final String DAY_FORMAT_PATTERN = "dd MMM";
    public static final String WEEK_DAY_FORMAT_PATTERN = "EEE";
    public static final String DATE_TEMPLATE = "dd/MM/yyyy";
    public static final String FEE_DATE_TEMPLATE = "dd-MM-yyyy";
    public static final String SERVER_DATE_TEMPLATE = "yyyy-MM-dd";
    public static final String CHAT_DATE_TEMPLATE = "MMM dd, yyyy";
    public static final String TIME_TEMPLATE = "HH:mm";
    public static final String TIME_TEMPLATE_12_HOUR = "HH:mm a";
    public static final String DATE_TIME_TEMPLATE = CHAT_DATE_TEMPLATE + " " + TIME_TEMPLATE_12_HOUR;

    private DateUtils() {

    }

    private static String format(@NonNull final Date date, @NonNull final String template) {
        return new SimpleDateFormat(template, Locale.ENGLISH).format(date);
    }

    private static String format(@NonNull final Date date, @NonNull final String template, Locale locale) {
        return new SimpleDateFormat(template, locale).format(date);
    }

    public static String toDate(@NonNull final Date date, @Nullable final String template) {
        return format(date, null != template ? template : DATE_TEMPLATE);
    }

    public static String toDate(@NonNull final Date date, @Nullable final String template, Locale locale) {
        return format(date, null != template ? template : DATE_TEMPLATE, locale);
    }

    public static String toDate(@NonNull final Date date) {
        return format(date, DATE_TEMPLATE);
    }

    public static String toTime(@NonNull final Date date, @Nullable final String template) {
        return format(date, null != template ? template : TIME_TEMPLATE);
    }

    public static String toTime(@NonNull final Date date, @Nullable final String template, Locale locale) {
        return format(date, null != template ? template : TIME_TEMPLATE, locale);
    }

    public static String toTime(@NonNull final Date date) {
        return format(date, TIME_TEMPLATE);
    }

    public static Date parse(@Nullable final String dateString) {
        return parse(dateString, DATE_TEMPLATE);
    }

    public static Date parse(@Nullable final String dateString, @Nullable final String template) {
        final SimpleDateFormat sdf = new SimpleDateFormat(null != template ? template : DATE_TEMPLATE, Locale.getDefault());
        Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (ParseException ex) {
            Log.e(LOG_TAG, ex.getMessage(), ex.getCause());
        }
        return date;
    }

    /***
     * compare to sting dates (return true if date1 is before date2)
     * @param strDate1
     * @param strDate2
     * @return
     */

    public static boolean compareStringDate(String strDate1, String strDate2) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(DATE_TEMPLATE, Locale.getDefault());
            Date date1 = formatter.parse(strDate1);
            Date date2 = formatter.parse(strDate2);
            if (date1.compareTo(date2) < 0) {
                return true;
            }
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return false;
    }

    /**
     * get millies from minutes
     *
     * @param minutes
     * @return
     */
    public static long getMillisFromMinutes(int minutes) {
        return minutes * 60 * 1000;
    }

    public static long getDateTimestamp(String dateStr, String format) {
        DateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        try {
            Date date = formatter.parse(dateStr);
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar.getTime().getTime();
    }

    public static long getDateTimestamp(Calendar calendar) {

        return calendar.getTime().getTime();
    }

    public static String duration(String startDateString, String endDateString) {
        Date startDate = DateUtils.parse(startDateString, DateUtils.TIME_FORMAT_PATTERN);
        Date endDate = DateUtils.parse(endDateString, DateUtils.TIME_FORMAT_PATTERN);
        long diffInSeconds = (endDate.getTime() - startDate.getTime());
        return HumanTime.exactly(diffInSeconds);
    }

    public static long getDaysCountBetweenDate(String startDate, String endDate, String pattern) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(parse(startDate, pattern));
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(parse(endDate, pattern));
        cal2.add(Calendar.DATE, 1);
        long diff = cal2.getTime().getTime() - cal1.getTime().getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public static String getWeekDayNameByDate(Context context) {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
    }

    public static int getCurrentDayDate() {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        return calendar.get(Calendar.DAY_OF_MONTH);
    }
}
