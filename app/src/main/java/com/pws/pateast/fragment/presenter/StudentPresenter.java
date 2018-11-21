package com.pws.pateast.fragment.presenter;

import com.pws.pateast.R;
import com.pws.pateast.api.exception.RetrofitException;
import com.pws.pateast.api.model.Student;
import com.pws.pateast.api.model.TeacherClass;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.TeacherService;

import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by intel on 10-May-17.
 */

public class StudentPresenter<V extends StudentView> extends ClassPresenter<V> {
    private HashMap<Integer, List<Student>> studentData;


    public StudentPresenter(boolean openDialog) {
        super(openDialog);
        studentData = new HashMap<>();
    }


    public void getStudents(final TeacherClass classes) {
        if (studentData.containsKey(classes.getBcsMapId())) {
            getView().setStudentAdapter(studentData.get(classes.getBcsMapId()));
            return;
        }

        if (isOpenDialog())
            getView().showProgressDialog(getString(R.string.wait));
        teacherApiService = serviceBuilder.createService(TeacherService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("userId", String.valueOf(user.getUserdetails().getUserId()));
        params.put("academicSessionId", String.valueOf(user.getUserdetails().getAcademicSessionId()));
        params.put("masterId", String.valueOf(user.getData().getMasterId()));
        params.put("bcsMapId", String.valueOf(classes.getBcsMapId()));

        disposable = teacherApiService.getMyStudent(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Student>() {
                    @Override
                    public boolean openDialog() {
                        return isOpenDialog();
                    }

                    @Override
                    public void onResponse(Student response) {
                        if (response.getData().isEmpty()) {
                            onError(new RetrofitException(getString(R.string.no_students)));
                        } else {
                            studentData.put(classes.getBcsMapId(), response.getData());
                            getView().setStudentAdapter(studentData.get(classes.getBcsMapId()));
                        }
                    }

                    @Override
                    public StudentPresenter getPresenter() {
                        return StudentPresenter.this;
                    }
                });
    }
}
