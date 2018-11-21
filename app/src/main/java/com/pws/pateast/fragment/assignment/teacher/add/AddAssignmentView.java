package com.pws.pateast.fragment.assignment.teacher.add;

import com.pws.pateast.fragment.presenter.SubjectView;

import java.util.Date;

/**
 * Created by planet on 5/3/2017.
 */

public interface AddAssignmentView extends SubjectView {

    void openDialog(String message);

    void setData();

    void setDate(int id, String date);

    void setCalenderDate(Date minDate, Date maxDate);

    void setDateFormat(String dateFormat);

    void setFileData();

    void onSubmit(boolean isUpdate);

    void setError(int id, String error);
}
