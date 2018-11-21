package com.pws.pateast.fragment.assignment.teacher.review;

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

/**
 * Created by intel on 07-Sep-17.
 */

public class ReviewAssignmentPresenter extends AppPresenter<ReviewAssignmentView> {
    @Inject
    protected Preference preference;

    @Inject
    protected ServiceBuilder serviceBuilder;

    protected User user;

    private ReviewAssignmentView view;

    protected TeacherService apiService;

    private StudentFilter studentFilter;
    private AssignmentObserver observer;

    @Override
    public ReviewAssignmentView getView() {
        return view;
    }

    @Override
    public void attachView(ReviewAssignmentView view) {
        this.view = view;
        getComponent().inject(this);
        user = preference.getUser();
        getView().onActionClick();
    }

    public void setObserver(AssignmentObserver observer) {
        this.observer = observer;
    }

    public AssignmentObserver getObserver() {
        return observer;
    }

    public void getStudentsAssignment() {
        apiService = serviceBuilder.createService(TeacherService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("userId", String.valueOf(user.getUserdetails().getUserId()));
        params.put("academicSessionId", String.valueOf(user.getUserdetails().getAcademicSessionId()));
        params.put("masterId", String.valueOf(user.getData().getMasterId()));
        params.put("bcsMapId", String.valueOf(getView().getClassId()));
        params.put("assignmentId", String.valueOf(getView().getAssignmentId()));


        setObserver(new AssignmentObserver());
        disposable = apiService.getStudentsAssignment(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getObserver());
    }

    public void submitStudentRemark() {
        getView().showProgressDialog(getString(R.string.wait));
        apiService = serviceBuilder.createService(TeacherService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("userId", String.valueOf(user.getUserdetails().getUserId()));
        params.put("academicSessionId", String.valueOf(user.getUserdetails().getAcademicSessionId()));
        params.put("masterId", String.valueOf(user.getData().getMasterId()));
        params.put("assignmentId", String.valueOf(getView().getAssignmentId()));


        JSONObject jsonObject = new JSONObject(params);
        try {
            jsonObject.put("assignmentremark", getAssignmentRemark());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        disposable = apiService.submitStudentsRemark(jsonObject.toString())
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Response>() {

                    @Override
                    public boolean openDialog() {
                        return true;
                    }

                    @Override
                    public void onResponse(Response response) {
                        Utils.showToast(getContext(), response.getMessage());
                        getView().assignmentReviewed();
                    }

                    @Override
                    public ReviewAssignmentPresenter getPresenter() {
                        return ReviewAssignmentPresenter.this;
                    }
                });

    }

    public void updateStudentRemark() {
        getView().showProgressDialog(getString(R.string.wait));
        apiService = serviceBuilder.createService(TeacherService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("userId", String.valueOf(user.getUserdetails().getUserId()));
        params.put("academicSessionId", String.valueOf(user.getUserdetails().getAcademicSessionId()));
        params.put("masterId", String.valueOf(user.getData().getMasterId()));
        params.put("assignmentId", String.valueOf(getView().getAssignmentId()));


        JSONObject jsonObject = new JSONObject(params);
        try {
            jsonObject.put("assignmentremark", getAssignmentRemark());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        disposable = apiService.updateStudentsRemark(jsonObject.toString())
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Response>() {

                    @Override
                    public boolean openDialog() {
                        return true;
                    }

                    @Override
                    public void onResponse(Response response) {
                        Utils.showToast(getContext(), response.getMessage());
                        getView().assignmentReviewed();
                    }

                    @Override
                    public ReviewAssignmentPresenter getPresenter() {
                        return ReviewAssignmentPresenter.this;
                    }
                });

    }


    private void setStudentAssignment(List<Student> studentList) {
        if (studentList.isEmpty()) {
            getObserver().onError(new RetrofitException(getString(R.string.no_students)));
        } else {
            getView().updateAssignmentAdapter(studentList);
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
            setStudentAssignment(mStudentFilterList);
        }
    }


    class AssignmentObserver extends AppSingleObserver<Student> {

        @Override
        public void onResponse(Student response) {
            if (response.getData() == null || response.getData().isEmpty()) {
                onError(new RetrofitException(getString(R.string.no_students)));
                view.setAssignmentAdapter(new ArrayList<Student>());
            } else if (!response.getData().isEmpty()) {
                view.setAssignmentTags(response.getTagsData());
                view.setAssignmentAdapter(response.getData());
            }
        }

        @Override
        public ReviewAssignmentPresenter getPresenter() {
            return ReviewAssignmentPresenter.this;
        }
    }


    private JSONArray getAssignmentRemark() {
        JSONArray jsonArray = new JSONArray();
        for (Student student : getView().getStudents()) {
            student = student.getStudent();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("studentId", student.getId());
                jsonObject.put("assignmentId", getView().getAssignmentId());
                jsonObject.put("tags", student.getAssignmentremark().getTags());

                if (student.getAssignmentremark().getId() != 0)
                    jsonObject.put("id", student.getAssignmentremark().getId());

                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return jsonArray;
    }
}
