package com.pws.pateast.fragment.leave;

import com.pws.pateast.api.model.Tag;
import com.pws.pateast.base.AppView;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by intel on 28-Jun-17.
 */

public interface LeaveApplyView extends AppView {
    boolean isError();

    void setError(int id, String error);

    void setText(int id, String date);

    void setCalenderDate(Date minDate, Date maxDate);

    void setDateFormat(String dateFormat);

    void setLeaveReasonAdapter(ArrayList<Tag> leaveReason);

    void setTypeLeaveAdapter(ArrayList<Tag> leaveType);

    void setLeaveDurationAdapter(ArrayList<Tag> leaveDuration);

    void leaveApplied(String message);
}
