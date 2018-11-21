package com.pws.pateast.listener;

import android.view.View;

import java.util.Calendar;

/**
 * Created by intel on 28-Apr-17.
 */

public interface OnDateChangeListener
{
    void onDateChange(View view,Calendar time, String timeString);
}
