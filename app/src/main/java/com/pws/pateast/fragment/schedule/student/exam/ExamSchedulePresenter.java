package com.pws.pateast.fragment.schedule.student.exam;

import com.pws.pateast.R;
import com.pws.pateast.api.exception.RetrofitException;
import com.pws.pateast.api.model.Schedule;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.StudentService;
import com.pws.pateast.fragment.presenter.ExamHeadPresenter;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by intel on 16-May-17.
 */

public class ExamSchedulePresenter extends ExamHeadPresenter<ExamScheduleView> {

    public ExamSchedulePresenter(boolean openDialog) {
        super(openDialog);
    }

    public void getMyExamSchedule() {
        switch (getView().getTaskType()) {
            case STUDENT_EXAM_SCHEDULE:
                getStudentExamSchedule();
                break;
            case WARD_EXAM_SCHEDULE:
                getWardExamSchedule();
                break;
            case TEACHER_EXAM_SCHEDULE:
                getTeacherExamSchedule();
                break;
        }
    }

    private void getExamSchedule(int masterId, int boardId, int classId, int academicSessionId) {
        if (isOpenDialog()) {
            getView().showProgressDialog(getString(R.string.loading));
        }

        studentService = serviceBuilder.createService(StudentService.class);
        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("masterId", String.valueOf(masterId));
        params.put("boardId", String.valueOf(boardId));
        params.put("classId", String.valueOf(classId));
        params.put("academicSessionId", String.valueOf(academicSessionId));
        params.put("examheadId", String.valueOf(getView().getExamHeadID()));
        dispose();

        disposable = studentService.getExamSchedule(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Schedule>() {

                    @Override
                    public boolean openDialog() {
                        return isOpenDialog();
                    }

                    @Override
                    public void onResponse(Schedule response) {
                        if (response.getData().isEmpty()) {
                            onError(new RetrofitException(getString(R.string.no_exam_schedule),RetrofitException.ERROR_TYPE_MESSAGE));
                            ExamSchedulePresenter.this.getView().onError(getString(R.string.no_exam_schedule));
                        } else {
                            ExamSchedulePresenter.this.getView().setExamScheduleAdapter(response.getData());
                        }
                    }

                    @Override
                    public ExamSchedulePresenter getPresenter() {
                        return ExamSchedulePresenter.this;
                    }
                });
    }

    private void getStudentExamSchedule() {
        getExamSchedule(user.getData().getMasterId(), user.getUserdetails().getBoardId(), user.getUserdetails().getClassId(), user.getUserdetails().getAcademicSessionId());
    }

    private void getWardExamSchedule() {
        getExamSchedule(ward.getMasterId(), ward.getUserInfo().getBoardId(), ward.getUserInfo().getClassId(), ward.getAcademicSessionId());
    }

    private void getTeacherExamSchedule() {
        getExamSchedule(user.getData().getMasterId(), getView().getTeacherClass().getBcsmap().getBoardId(), getView().getTeacherClass().getBcsmap().getClassId(), user.getUserdetails().getAcademicSessionId());
    }

}
