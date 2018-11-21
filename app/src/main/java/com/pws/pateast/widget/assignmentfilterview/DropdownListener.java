package com.pws.pateast.widget.assignmentfilterview;

import android.view.View;

import java.util.Calendar;

/**
 * Created by planet on 8/19/2017.
 */

public interface DropdownListener {
    void onClick(View view);
    void onSubjectItemClick(View view, int position);
    void onRemarkItemClick(View view, int position);
    void onDateChange(View view, Calendar time, String timeString);
}
