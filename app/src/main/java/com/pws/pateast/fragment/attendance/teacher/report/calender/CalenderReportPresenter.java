package com.pws.pateast.fragment.attendance.teacher.report.calender;

import android.support.v4.content.ContextCompat;

import com.pws.calender.domain.Event;
import com.pws.pateast.R;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.exception.RetrofitException;
import com.pws.pateast.api.model.Student;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.model.Ward;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.TeacherService;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.fragment.attendance.teacher.addupdate.AttendanceTakerView;
import com.pws.pateast.utils.Preference;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by intel on 31-Aug-17.
 */

public class CalenderReportPresenter<V extends CalenderReportView> extends AppPresenter<V> {
    @Inject
    Preference preference;

    @Inject
    ServiceBuilder serviceBuilder;

    protected User user;

    protected Ward ward;

    private TeacherService apiService;

    private V view;

    int totalAbsent;
    int totalPresent;
    int totalLeave;

    @Override
    public V getView() {
        return view;
    }

    @Override
    public void attachView(V view) {
        this.view = view;
        getComponent().inject((CalenderReportPresenter<CalenderReportView>) this);
        user = preference.getUser();
        ward = preference.getWard();
    }

    private void initialDays() {
        totalAbsent = 0;
        totalPresent = 0;
        totalLeave = 0;
    }

    public String getTotalDays() {
        return String.valueOf(totalPresent + totalAbsent + totalLeave);
    }

    public String getTotalAbsent() {
        return String.valueOf(totalAbsent);
    }

    public String getTotalLeave() {
        return String.valueOf(totalLeave);
    }

    public String getTotalPresent() {
        return String.valueOf(totalPresent);
    }


    private void getAttendanceReport(int academicSessionId, int masterId, int bcsMapId, int subjectId, int studentId) {
        initialDays();
        apiService = serviceBuilder.createService(TeacherService.class);

        HashMap<String, String> params = serviceBuilder.getParams();

        params.put("academicSessionId", String.valueOf(academicSessionId));
        params.put("masterId", String.valueOf(masterId));

        params.put("bcsMapId", bcsMapId == 0 ? "" : String.valueOf(bcsMapId));

        params.put("subjectId", subjectId == 0 ? "" : String.valueOf(subjectId));

        params.put("studentId", studentId == 0 ? "" : String.valueOf(studentId));


        disposable = apiService.getAttendanceReportByStudent(new JSONObject(params).toString())
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Student>() {

                    @Override
                    public void onResponse(Student response) {
                        if (response.getData().isEmpty()) {
                            onError(new RetrofitException(getString(R.string.no_attendance_report_found), RetrofitException.ERROR_TYPE_MESSAGE));
                        } else {
                            CalenderReportPresenter.this.getView().setReportCalender(response.getData());
                            CalenderReportPresenter.this.getView().setAttendanceTags(response.getTagsData());
                        }
                    }

                    @Override
                    public CalenderReportPresenter getPresenter() {
                        return CalenderReportPresenter.this;
                    }
                });
    }


    public ArrayList<Event> getEvents(List<Student> students) {
        ArrayList<Event> events = new ArrayList<>();
        for (Student student : students) {
            if (student.getDate() != null)
                events.add(new Event(ContextCompat.getColor(getContext(), getColor(student.getAttendancerecords().get(0))), student.getDate().getTime(), student));
        }

        return events;
    }

    public int getColor(Student student) {
        switch (student.getIs_present()) {
            case AttendanceTakerView.SELECTED_PRESENT:
                totalPresent++;
                return R.color.colorPrimaryStudent;
            case AttendanceTakerView.SELECTED_ABSENT:
                totalAbsent++;
                return R.color.colorAccentStudent;
            case AttendanceTakerView.SELECTED_LEAVE:
                totalLeave++;
                return R.color.colorLogin;
            default:
                return 0;
        }
    }

    public void getAttendanceReportForTeacher() {
        getAttendanceReport(user.getUserdetails().getAcademicSessionId(),
                user.getData().getMasterId(),
                getView().getClassId(),
                getView().getSubjectId(),
                getView().getStudentId());
    }

    public void getAttendanceReportForStudent() {
        getAttendanceReport(user.getUserdetails().getAcademicSessionId(),
                user.getData().getMasterId(),
                user.getUserdetails().getBcsMapId(),
                getView().getSubjectId(),
                user.getUserdetails().getUserId());
    }

    public void getAttendanceReportForWard() {
        getAttendanceReport(ward.getAcademicSessionId(),
                ward.getMasterId(),
                ward.getBcsMapId(),
                getView().getSubjectId(),
                ward.getStudentId());
    }

}
