package com.pws.pateast.fragment.marks.student;

import com.pws.pateast.R;
import com.pws.pateast.api.model.ExamMarks;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.StudentService;
import com.pws.pateast.fragment.presenter.ExamHeadPresenter;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by intel on 17-May-17.
 */

public class ExamMarksPresenter extends ExamHeadPresenter<ExamMarksView> {

    public ExamMarksPresenter(boolean openDialog) {
        super(openDialog);
    }

    public void getMyExamMarks() {
        switch (getView().getTaskType()) {
            case STUDENT_MARKS:
                getStudentExamMarks();
                break;
            case WARD_MARKS:
                getWardExamMarks();
                break;
            case TEACHER_MARKS:
                getTeacherExamMarks();
                break;
        }
    }

    private void getMarks(int masterId, int studentId, int bcsMapId, int academicSessionId) {
        if (isOpenDialog()) {
            getView().showProgressDialog(getString(R.string.loading));
        }

        studentService = serviceBuilder.createService(StudentService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("academicSessionId", String.valueOf(academicSessionId));
        params.put("masterId", String.valueOf(masterId));
        params.put("studentId", String.valueOf(studentId));
        params.put("bcsMapId", String.valueOf(bcsMapId));
        params.put("examScheduleId", String.valueOf(getView().getExamScheduleID()));

        dispose();
        disposable = studentService.getMarks(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<ExamMarks>() {

                    @Override
                    public boolean openDialog() {
                        return isOpenDialog();
                    }

                    @Override
                    public void onResponse(ExamMarks response) {
                        if (response.getStudent() != null)
                            ExamMarksPresenter.this.getView().bindStudent(response.getStudent());
                        if (response.getData().isEmpty()) {
                            getView().setError(getString(R.string.no_exam_marks));
                        } else {
                            getView().setExamMarksAdapter(response.getData());
                        }
                    }

                    @Override
                    public ExamMarksPresenter getPresenter() {
                        return ExamMarksPresenter.this;
                    }
                });
    }

    private void getStudentExamMarks() {
        getMarks(user.getData().getMasterId(), user.getUserdetails().getUserId(), user.getUserdetails().getBcsMapId(), user.getUserdetails().getAcademicSessionId());
    }

    private void getWardExamMarks() {
        getMarks(ward.getMasterId(), ward.getStudentId(), ward.getUserInfo().getBcsMapId(), ward.getAcademicSessionId());
    }

    private void getTeacherExamMarks() {
        getMarks(user.getData().getMasterId(), getView().getStudent().getStudent().getId(), getView().getTeacherClass().getBcsMapId(), user.getUserdetails().getAcademicSessionId());
    }

}
