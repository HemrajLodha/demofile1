package com.pws.pateast.fragment.presenter;

import com.pws.pateast.R;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.exception.RetrofitException;
import com.pws.pateast.api.model.Schedule;
import com.pws.pateast.api.model.TeacherClass;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.model.Ward;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.StudentService;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.utils.Preference;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by intel on 02-Sep-17.
 */

public class ExamHeadPresenter<V extends ExamHeadView> extends AppPresenter<V> {
    @Inject
    protected Preference preference;

    @Inject
    protected ServiceBuilder serviceBuilder;

    V view;

    protected User user;

    protected Ward ward;

    protected StudentService studentService;

    private HashMap<Integer, List<Schedule>> examHeadData;

    private boolean openDialog;

    public ExamHeadPresenter(boolean openDialog) {
        this.openDialog = openDialog;
    }

    public boolean isOpenDialog() {
        return openDialog;
    }

    @Override
    public V getView() {
        return view;
    }

    public User getUser() {
        return user;
    }

    @Override
    public void attachView(V view) {
        this.view = view;
        getComponent().inject((ExamHeadPresenter<ExamHeadView>) this);
        user = preference.getUser();
        ward = preference.getWard();
        examHeadData = new HashMap<>();
    }

    protected void getStudentExamHead(boolean isSchedule) {
        getExamHead(user.getUserdetails().getBcsMapId(), user.getData().getMasterId(), user.getUserdetails().getBoardId(), user.getUserdetails().getClassId(), user.getUserdetails().getAcademicSessionId(), isSchedule);
    }

    protected void getWardExamHead(boolean isSchedule) {
        getExamHead(ward.getUserInfo().getBcsMapId(), ward.getMasterId(), ward.getUserInfo().getBoardId(), ward.getUserInfo().getClassId(), ward.getAcademicSessionId(), isSchedule);
    }

    public void getTeacherExamHead(TeacherClass teacherClass, boolean isSchedule) {
        getExamHead(teacherClass.getBcsMapId(), user.getData().getMasterId(), teacherClass.getBcsmap().getBoardId(), teacherClass.getBcsmap().getClassId(), user.getUserdetails().getAcademicSessionId(), isSchedule);
    }

    private void getExamHead(final int bcsMapId, int masterId, int boardId, int classId, int academicSessionId, final boolean isSchedule) {
        if (examHeadData.containsKey(bcsMapId)) {
            getView().setExamHeads(examHeadData.get(bcsMapId), false);
            return;
        }

        if (isOpenDialog()) {
            getView().showProgressDialog(getString(R.string.loading));
        }

        studentService = serviceBuilder.createService(StudentService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("masterId", String.valueOf(masterId));
        params.put("boardId", String.valueOf(boardId));
        params.put("classId", String.valueOf(classId));
        params.put("academicSessionId", String.valueOf(academicSessionId));

        disposable = studentService.getExamScheduleHead(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Schedule>() {

                    @Override
                    public boolean openDialog() {
                        return isOpenDialog();
                    }

                    @Override
                    public void onResponse(Schedule response) {
                        if (response.getData() == null || response.getData().isEmpty()) {
                            onError(new RetrofitException(getString(R.string.no_exam_schedule),RetrofitException.ERROR_TYPE_MESSAGE));
                        } else {
                            examHeadData.put(bcsMapId, response.getData());
                            getView().setExamHeads(examHeadData.get(bcsMapId), isSchedule);
                        }
                    }

                    @Override
                    public ExamHeadPresenter getPresenter() {
                        return ExamHeadPresenter.this;
                    }
                });
    }
}
