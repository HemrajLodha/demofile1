package com.pws.pateast.fragment.student;

import android.text.TextUtils;
import android.widget.Filter;

import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.exception.RetrofitException;
import com.pws.pateast.api.model.MenuItem;
import com.pws.pateast.api.model.Student;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.TeacherService;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.utils.Preference;
import com.pws.pateast.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by intel on 25-Apr-17.
 */

public class MyStudentsPresenter extends AppPresenter<MyStudentView> {
    @Inject
    Preference preference;

    @Inject
    ServiceBuilder serviceBuilder;

    private User user;

    private TeacherService apiService;

    private MyStudentView myStudentView;
    private StudentObserver observer;

    @Override
    public MyStudentView getView() {
        return myStudentView;
    }

    @Override
    public void attachView(MyStudentView view) {
        myStudentView = view;
        getComponent().inject(this);
        user = preference.getUser();
    }

    public void setObserver(StudentObserver observer) {
        this.observer = observer;
    }

    public StudentObserver getObserver() {
        return observer;
    }

    public List<MenuItem> getMenuItems() {
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem.Builder(getContext()).setText(R.string.menu_message).build());
        menuItems.add(new MenuItem.Builder(getContext()).setText(R.string.menu_attendance_report).build());
        return menuItems;
    }

    public void getMyStudents(int bcsMapId, String enrollNumber, String studentName) {
        apiService = serviceBuilder.createService(TeacherService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("userId", String.valueOf(user.getUserdetails().getUserId()));
        params.put("academicSessionId", String.valueOf(user.getUserdetails().getAcademicSessionId()));
        params.put("masterId", String.valueOf(user.getData().getMasterId()));
        params.put("bcsMapId", bcsMapId == 0 ? "" : String.valueOf(bcsMapId));
        if (!TextUtils.isEmpty(enrollNumber))
            params.put("enrollment_no", enrollNumber);
        if (!TextUtils.isEmpty(studentName))
            params.put("name", studentName);

        setObserver(new StudentObserver());
        dispose();
        disposable = apiService.getMyStudent(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getObserver());
    }

    public void setMyStudents(List<Student> myStudents, boolean isSearch) {
        if (myStudents.isEmpty()) {
            getObserver().onError(new RetrofitException(getString(R.string.no_students)));
        } else {
            if (isSearch) {
                getView().updateStudentAdapter(myStudents);
                getObserver().hide();
            } else
                getView().setMyStudentAdapter(myStudents);
        }
    }

    public Filter getFilter(List<Student> originalList) {
        return new StudentFilter(originalList);
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
            setMyStudents(mStudentFilterList, true);
        }
    }

    class StudentObserver extends AppSingleObserver<Student> {

        @Override
        public void onResponse(Student response) {
            setMyStudents(response.getData(), false);
        }

        @Override
        public MyStudentsPresenter getPresenter() {
            return MyStudentsPresenter.this;
        }
    }
}
