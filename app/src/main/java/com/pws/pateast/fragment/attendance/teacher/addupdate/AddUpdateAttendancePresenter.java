package com.pws.pateast.fragment.attendance.teacher.addupdate;

import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.model.Student;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.utils.Preference;
import com.pws.pateast.R;
import com.pws.pateast.api.model.User;
import com.pws.pateast.fragment.attendance.teacher.AttendanceView;

import javax.inject.Inject;

/**
 * Created by planet on 5/1/2017.
 */

public class AddUpdateAttendancePresenter extends AppPresenter<AddUpdateAttendanceView> {

    @Inject
    Preference preference;

    @Inject
    ServiceBuilder serviceBuilder;

    private User user;

    private Student student;

    private AddUpdateAttendanceView addUpdateAttendanceView;

    @Override
    public AddUpdateAttendanceView getView() {
        return addUpdateAttendanceView;
    }

    @Override
    public void attachView(AddUpdateAttendanceView view) {
        this.addUpdateAttendanceView = view;
        getComponent().inject(this);
        user = preference.getUser();
    }

    public void setStudent(Student student) {
        this.student = student;
        getView().setData(student);
    }

    public Student getStudent() {
        return student;
    }

    public void setActionButtonText(boolean isUpdate) {
        getView().setActionButtonText(isUpdate ? R.string.update : R.string.submit);
    }

    public void getTags(AttendanceView attendanceView)
    {
        getView().setTagAdapter(attendanceView.getAttendanceTags());
    }
}
