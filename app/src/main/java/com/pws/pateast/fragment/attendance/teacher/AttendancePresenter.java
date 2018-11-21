package com.pws.pateast.fragment.attendance.teacher;

import android.text.TextUtils;
import android.widget.Filter;

import com.pws.pateast.R;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.exception.RetrofitException;
import com.pws.pateast.api.model.Response;
import com.pws.pateast.api.model.Student;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.TeacherService;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.utils.Preference;
import com.pws.pateast.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.pws.pateast.api.exception.RetrofitException.ERROR_TYPE_MESSAGE;

/**
 * Created by intel on 27-Apr-17.
 */

public class AttendancePresenter extends AppPresenter<AttendanceView> {
    @Inject
    Preference preference;

    @Inject
    ServiceBuilder serviceBuilder;

    private User user;

    private TeacherService apiService;

    private AttendanceView mAttendanceView;
    private StudentFilter studentFilter;
    private AttendanceObserver observer;

    @Override
    public AttendanceView getView() {
        return mAttendanceView;
    }

    public void setObserver(AttendanceObserver observer) {
        this.observer = observer;
    }

    public AttendanceObserver getObserver() {
        return observer;
    }

    @Override
    public void attachView(AttendanceView view) {
        mAttendanceView = view;
        getComponent().inject(this);
        user = preference.getUser();
    }

    public void getStudentsAttendance(int bcsMapId, int subjectId, int subjectOrder, String date, String enrollNumber, String studentName) {
        apiService = serviceBuilder.createService(TeacherService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("userId", String.valueOf(user.getUserdetails().getUserId()));
        params.put("academicSessionId", String.valueOf(user.getUserdetails().getAcademicSessionId()));
        params.put("masterId", String.valueOf(user.getData().getMasterId()));
        params.put("bcsMapId", String.valueOf(bcsMapId));
        params.put("subjectId", String.valueOf(subjectId));
        params.put("period", String.valueOf(subjectOrder));
        params.put("date", date);


        if (!TextUtils.isEmpty(enrollNumber))
            params.put("enrollment_no", enrollNumber);
        if (!TextUtils.isEmpty(studentName))
            params.put("name", studentName);

        setObserver(new AttendanceObserver());
        disposable = apiService.getStudentsAttendance(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getObserver());
    }

    public void submitStudentsAttendance(List<Student> students) {
        getView().showProgressDialog(getString(R.string.wait));
        apiService = serviceBuilder.createService(TeacherService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("userId", String.valueOf(user.getUserdetails().getUserId()));
        params.put("updatedbyId", String.valueOf(user.getData().getId()));
        params.put("academicSessionId", String.valueOf(user.getUserdetails().getAcademicSessionId()));
        params.put("masterId", String.valueOf(user.getData().getMasterId()));
        params.put("bcsMapId", String.valueOf(getView().getClassId()));
        params.put("subjectId", String.valueOf(getView().getSubjectId()));
        params.put("period", String.valueOf(getView().getSubjectOrder()));
        params.put("date", getView().getDate());
        if (getView().isUpdate())
            params.put("id", String.valueOf(getView().getAttendanceId()));


        JSONObject jsonObject = new JSONObject(params);
        try {
            jsonObject.put("attendancerecord", getAttendance(students, getView().getSubjectId()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        disposable = apiService.submitStudentsAttendance(jsonObject.toString())
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Response>() {

                    @Override
                    public boolean openDialog() {
                        return true;
                    }

                    @Override
                    public void onResponse(Response response) {
                        AttendancePresenter.this.getView().onActionClick();
                        Utils.showToast(getContext(), response.getMessage());
                    }

                    @Override
                    public AttendancePresenter getPresenter() {
                        return AttendancePresenter.this;
                    }
                });
    }

    public JSONArray getAttendance(List<Student> students, int subjectId) {
        JSONArray jsonArray = new JSONArray();
        for (Student student : students) {
            JSONObject jsonObject = new JSONObject();
            Student studentRecord = student.getStudent();
            Student attendanceRecord = studentRecord.getAttendancerecord();
            try {
                jsonObject.put("studentId", studentRecord.getId());
                jsonObject.put("subjectId", subjectId);

                if (attendanceRecord != null) {
                    if (getView().isUpdate()) {
                        jsonObject.put("attendanceId", getView().getAttendanceId());
                        jsonObject.put("id", attendanceRecord.getId());
                    }
                    jsonObject.put("is_present", attendanceRecord.getIs_present());
                    jsonObject.put("tags", attendanceRecord.getTags());
                }

                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return jsonArray;
    }

    private void setStudentAttendance(List<Student> studentList) {
        if (studentList.isEmpty()) {
            getObserver().onError(new RetrofitException(getString(R.string.no_students)));
        } else {
            getView().updateAttendanceAdapter(studentList);
            getObserver().hide();
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
            setStudentAttendance(mStudentFilterList);
        }
    }

    class AttendanceObserver extends AppSingleObserver<Student> {

        @Override
        public void onResponse(Student response) {
            if (response.getStudents() == null || response.getStudents().isEmpty()) {
                String errorMessage;
                if (response.isHoliday() && response.getHoliday_data() != null)
                    errorMessage = getString(R.string.holiday_message, response.getHoliday_data().getHolidaydetails().get(0).getName());
                else
                    errorMessage = getString(R.string.no_students);

                onError(new RetrofitException(errorMessage, ERROR_TYPE_MESSAGE));
                mAttendanceView.setSubmitVisible(false);
                mAttendanceView.setAttendanceId(response.getAttendanceId());
            } else {
                mAttendanceView.setAttendanceAdapter(response.getStudents());
                mAttendanceView.setAttendanceTags(response.getTagsData());
                mAttendanceView.setSubmitVisible(true);
                mAttendanceView.setAttendanceId(response.getAttendanceId());

            }
        }

        @Override
        public AttendancePresenter getPresenter() {
            return AttendancePresenter.this;
        }
    }
}
