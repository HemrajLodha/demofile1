package com.pws.pateast.fragment.attendance.teacher.report;

import android.support.v4.content.ContextCompat;
import android.widget.Filter;

import com.pws.calender.domain.Event;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.model.Student;
import com.pws.pateast.fragment.student.MyStudentsPresenter;
import com.pws.pateast.utils.Preference;
import com.pws.pateast.R;
import com.pws.pateast.api.exception.RetrofitException;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.TeacherService;
import com.pws.pateast.base.AppPresenter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by intel on 10-May-17.
 */

public class AttendanceReportPresenter extends AppPresenter<AttendanceReportView> {

    @Inject
    Preference preference;

    @Inject
    ServiceBuilder serviceBuilder;

    private User user;

    private TeacherService apiService;

    private AttendanceReportView reportView;
    private AttendanceReportObserver observer;
    private StudentFilter studentFilter;

    @Override
    public AttendanceReportView getView() {
        return reportView;
    }

    @Override
    public void attachView(AttendanceReportView view) {
        reportView = view;
        getComponent().inject(this);
        user = preference.getUser();
    }

    public void setObserver(AttendanceReportObserver observer) {
        this.observer = observer;
    }

    public AttendanceReportObserver getObserver() {
        return observer;
    }


    public void getAttendanceReport(int bcsMapId, int subjectId, int studentId, String startDate, String endDate) {
        apiService = serviceBuilder.createService(TeacherService.class);

        HashMap<String, String> params = serviceBuilder.getParams();

        params.put("academicSessionId", String.valueOf(user.getUserdetails().getAcademicSessionId()));
        params.put("masterId", String.valueOf(user.getData().getMasterId()));

        params.put("bcsMapId", bcsMapId == 0 ? "" : String.valueOf(bcsMapId));

        params.put("subjectId", subjectId == 0 ? "" : String.valueOf(subjectId));
        params.put("studentId", studentId == 0 ? "" : String.valueOf(studentId));

        params.put("startDate", startDate == null ? "" : startDate);
        params.put("endDate", endDate == null ? "" : endDate);

        setObserver(new AttendanceReportObserver());
        disposable = apiService.getAttendanceReport(new JSONObject(params).toString())
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getObserver());
    }

    public void setAttendanceReport(List<Student> myStudents, boolean isSearch) {
        if (myStudents.isEmpty()) {
            getObserver().onError(new RetrofitException(getString(R.string.no_attendance_report_found), RetrofitException.ERROR_TYPE_MESSAGE));
        } else {
            if (isSearch) {
                getView().updateReportAdapter(myStudents);
                getObserver().hide();
            } else
                getView().setReportAdapter(myStudents);
        }
    }

    public Filter getFilter(List<Student> originalList) {
        if (studentFilter == null)
            studentFilter = new StudentFilter(originalList);
        return studentFilter;
    }

    class StudentFilter extends Filter {
        private List<Student> originalList;
        private List<Student> mStudentFilterList;

        public StudentFilter(List<Student> originalList) {
            this.originalList = originalList;
            this.mStudentFilterList = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String charString = constraint.toString();

            if (charString.isEmpty()) {

                mStudentFilterList = originalList;
            } else {

                ArrayList<Student> filteredList = new ArrayList<>();

                for (Student student : originalList) {

                    if (student.getStudent().getUser().getUserdetails().get(0).getFullname().toLowerCase().contains(charString.toLowerCase())) {

                        filteredList.add(student);
                    }
                }

                mStudentFilterList = filteredList;
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = mStudentFilterList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mStudentFilterList = (ArrayList<Student>) results.values;
            setAttendanceReport(mStudentFilterList, true);
        }
    }

    class AttendanceReportObserver extends AppSingleObserver<Student> {

        @Override
        public void onResponse(Student response) {
            if (response.getData() == null || response.getData().size() == 0) {
                onError(new RetrofitException(getString(R.string.no_attendance_report_found), RetrofitException.ERROR_TYPE_MESSAGE));
            } else {
                setAttendanceReport(response.getData(), false);
            }
        }

        @Override
        public AttendanceReportPresenter getPresenter() {
            return AttendanceReportPresenter.this;
        }
    }

}
