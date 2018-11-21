package com.pws.pateast.activity.attendance;

import android.content.Intent;
import android.os.Bundle;

import com.pws.pateast.activity.tasks.TaskActivity;
import com.pws.pateast.api.model.Schedule;
import com.pws.pateast.api.model.Subject;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.base.Extras;
import com.pws.pateast.enums.TaskType;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.fragment.attendance.student.StudentAttendanceFragment;
import com.pws.pateast.fragment.presenter.SubjectPresenter;
import com.pws.pateast.fragment.schedule.student.exam.ExamScheduleFragment;

import java.util.ArrayList;
import java.util.List;

import static com.pws.pateast.base.FragmentActivity.EXTRA_TASK_TYPE;

/**
 * Created by intel on 06-Sep-17.
 */

public class AttendanceReportPresenter extends SubjectPresenter<AttendanceReportView>
{

    private UserType mUserType;

    public AttendanceReportPresenter(boolean openDialog) {
        super(openDialog);
    }

    public void setUserType() {
        mUserType = UserType.getUserType(user.getData().getUser_type());
        getView().onActionClick();
    }

    public void getSubject()
    {
        switch (mUserType) {
            case STUDENT:
                getSubjectsByStudent();
                break;
            case PARENT:
                getSubjectsByWard();
                break;
            case TEACHER:
                getSubjectsByStudentForTeacher(getView().getClassId(), getView().getStudentId());
                break;
        }
    }

    public List<AppFragment> getAttendanceReportFragments(List<Subject> subjects) {
        List<AppFragment> appFragments = new ArrayList<>();

        for (Subject subject : subjects) {
            Bundle bundle = new Bundle();
            bundle.putString(Extras.USER_TYPE, mUserType.getValue());
            bundle.putInt(Extras.SUBJECT_ID, subject.getSubjectId());
            bundle.putInt(Extras.CLASS_ID, getView().getClassId());
            bundle.putParcelable(Extras.STUDENT, getView().getStudent());
            bundle.putString(Extras.TITLE, subject.getSubject().getSubjectdetails().get(0).getName());
            appFragments.add(AppFragment.newInstance(StudentAttendanceFragment.class, bundle));
        }

        return appFragments;
    }
}
