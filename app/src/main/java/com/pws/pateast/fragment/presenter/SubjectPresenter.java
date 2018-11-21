package com.pws.pateast.fragment.presenter;

import com.pws.pateast.R;
import com.pws.pateast.api.exception.RetrofitException;
import com.pws.pateast.api.model.Subject;
import com.pws.pateast.api.model.SubjectStudent;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.StudentService;
import com.pws.pateast.api.service.TeacherService;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by intel on 27-Apr-17.
 */

public class SubjectPresenter<V extends SubjectView> extends StudentPresenter<V> {


    public SubjectPresenter(boolean openDialog) {
        super(openDialog);
    }


    public void getSubject(String bcsMapId) {
        if (isOpenDialog())
            getView().showProgressDialog(getString(R.string.wait));
        teacherApiService = serviceBuilder.createService(TeacherService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("userId", String.valueOf(user.getUserdetails().getUserId()));
        params.put("academicSessionId", String.valueOf(user.getUserdetails().getAcademicSessionId()));
        params.put("masterId", String.valueOf(user.getData().getMasterId()));
        params.put("bcsMapId", bcsMapId);

        disposable = teacherApiService.getTeacherSubject(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Subject>() {

                    @Override
                    public boolean openDialog() {
                        return isOpenDialog();
                    }

                    @Override
                    public void onResponse(Subject response) {
                        if (response.getData().isEmpty()) {
                            onError(new RetrofitException(getString(R.string.no_subject)));
                        } else {
                            getView().setSubjectAdapter(response.getData());
                        }
                    }

                    @Override
                    public SubjectPresenter getPresenter() {
                        return SubjectPresenter.this;
                    }

                });
    }

    public void getSubjectWithStudent(String bcsMapId) {
        if (isOpenDialog())
            getView().showProgressDialog(getString(R.string.wait));
        teacherApiService = serviceBuilder.createService(TeacherService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("userId", String.valueOf(user.getUserdetails().getUserId()));
        params.put("academicSessionId", String.valueOf(user.getUserdetails().getAcademicSessionId()));
        params.put("masterId", String.valueOf(user.getData().getMasterId()));
        params.put("bcsMapId", bcsMapId);

        disposable = teacherApiService.getSubjectWithStudent(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<SubjectStudent>() {

                    @Override
                    public boolean openDialog() {
                        return isOpenDialog();
                    }

                    @Override
                    public void onResponse(SubjectStudent response) {
                        if (response.getSubjects().isEmpty() && response.getStudents().isEmpty()) {
                            onError(new RetrofitException(getString(R.string.no_subject_student)));
                        } else {
                            getView().setSubjectAdapter(response.getSubjects());
                            getView().setStudentAdapter(response.getStudents());
                        }
                    }

                    @Override
                    public SubjectPresenter getPresenter() {
                        return SubjectPresenter.this;
                    }

                });
    }


    public void getSubjectsByStudent() {
        getSubjectsByStudent(user.getUserdetails().getBcsMapId(), user.getUserdetails().getUserId(),
                user.getData().getMasterId(), user.getUserdetails().getAcademicSessionId());
    }

    public void getSubjectsByWard() {
        getSubjectsByStudent(ward.getBcsMapId(), ward.getStudentId(),
                ward.getMasterId(), ward.getAcademicSessionId());
    }

    public void getSubjectsByStudentForTeacher(int bcsMapId, int studentId) {
        getSubjectsByStudent(bcsMapId, studentId,
                user.getData().getMasterId(), user.getUserdetails().getAcademicSessionId());
    }

    public void getSubjectsByStudent(int bcsMapId, int userId, int masterId, int sessionId) {
        if (isOpenDialog())
            getView().showProgressDialog(getString(R.string.wait));
        studentApiService = serviceBuilder.createService(StudentService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("userId", String.valueOf(userId));
        params.put("academicSessionId", String.valueOf(sessionId));
        params.put("masterId", String.valueOf(masterId));
        params.put("bcsMapId", String.valueOf(bcsMapId));

        disposable = studentApiService.getSubjectsByStudent(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Subject>() {

                    @Override
                    public boolean openDialog() {
                        return isOpenDialog();
                    }

                    @Override
                    public void onResponse(Subject response) {
                        if (response.getData().isEmpty()) {
                            onError(new RetrofitException(getString(R.string.no_subject)));
                        } else {
                            getView().setSubjectAdapter(response.getData());
                        }
                    }

                    @Override
                    public SubjectPresenter getPresenter() {
                        return SubjectPresenter.this;
                    }

                });
    }
}
