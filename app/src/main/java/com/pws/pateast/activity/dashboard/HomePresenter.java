package com.pws.pateast.activity.dashboard;

import com.pws.pateast.R;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.model.DashboardEvent;
import com.pws.pateast.api.model.ExamMarks;
import com.pws.pateast.api.model.Schedule;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.model.Ward;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.APIService;
import com.pws.pateast.api.service.ParentService;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.fragment.assignment.AssignmentPresenter;
import com.pws.pateast.fragment.assignment.AssignmentView;
import com.pws.pateast.utils.DateUtils;
import com.pws.pateast.utils.Preference;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by intel on 20-Apr-17.
 */

public class HomePresenter extends AppPresenter<HomeView> {

    @Inject
    protected ServiceBuilder serviceBuilder;

    @Inject
    protected Preference preference;

    @Inject
    EventBus eventBus;

    protected Ward ward;
    protected User user;

    private ParentService apiService;
    private APIService service;

    private HomeView mHomeView;
    private AssignmentPresenter assignmentPresenter;

    @Override
    public HomeView getView() {
        return mHomeView;
    }

    @Override
    public void attachView(HomeView mHomeView) {
        this.mHomeView = mHomeView;
        getComponent().inject(this);
        ward = preference.getWard();
        user = preference.getUser();
        getView().getAppListener().bindSocketService();
        setHomeMenu();
    }

    public User getUser() {
        return user;
    }

    public Ward getWard() {
        return ward;
    }

    public int getReceiverId() {
        return getUser().getData().getId();
    }

    public void getStudentAssignment(AssignmentView assignmentView, int tagId, int subjectId, String dueDate) {
        assignmentPresenter = new AssignmentPresenter();
        assignmentPresenter.attachView(assignmentView);
        switch (UserType.getUserType(user.getData().getUser_type())) {
            case PARENT:
                assignmentPresenter.getStudentAssignment(ward.getUserInfo().getUserId(), ward.getUserInfo().getBcsMapId(), ward.getUser_type(),
                        ward.getAcademicSessionId(), ward.getMasterId(), subjectId, tagId, dueDate);
                break;
            case STUDENT:
                assignmentPresenter.getStudentAssignment(user.getUserdetails().getUserId(), user.getUserdetails().getBcsMapId(),
                        user.getData().getUser_type(), user.getUserdetails().getAcademicSessionId(), user.getData().getMasterId(), subjectId, tagId, dueDate);
                break;
        }
    }

    public void getUserDetails(int id, int masterId, String langId) {

        service = serviceBuilder.createService(APIService.class);

        disposable = service.getUserDetails(String.valueOf(id), langId, String.valueOf(masterId))
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<User>() {

                    @Override
                    public void onResponse(User response) {
                        if (response.getData() != null) {
                            preference.setDateFormat(response.getData().getDate_format());
                        }
                        getView().setData(response.getData());
                    }

                    @Override
                    public HomePresenter getPresenter() {
                        return HomePresenter.this;
                    }

                });
    }


    public void getDashbordDetails() {

        apiService = serviceBuilder.createService(ParentService.class);
        HashMap<String, String> params = serviceBuilder.getParams();

        switch (UserType.getUserType(user.getData().getUser_type())) {
            case PARENT:
                getUserDetails(ward.getUserId(), ward.getMasterId(), preference.getLanguageID());
                params.put("user_id", String.valueOf(ward.getUserId()));
                params.put("studentId", String.valueOf(ward.getUserInfo().getUserId()));
                params.put("masterId", String.valueOf(ward.getMasterId()));
                params.put("bcsMapId", String.valueOf(ward.getUserInfo().getBcsMapId()));
                params.put("academicSessionId", String.valueOf(ward.getAcademicSessionId()));
                break;
            case STUDENT:
                getUserDetails(user.getData().getId(), user.getData().getMasterId(), preference.getLanguageID());
                params.put("user_id", String.valueOf(user.getData().getId()));
                params.put("studentId", String.valueOf(user.getUserdetails().getUserId()));
                params.put("masterId", String.valueOf(user.getData().getMasterId()));
                params.put("bcsMapId", String.valueOf(user.getUserdetails().getBcsMapId()));
                params.put("academicSessionId", String.valueOf(user.getUserdetails().getAcademicSessionId()));
                break;
        }


        disposable = apiService.getParentDashBoard(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<DashboardEvent>() {
                    @Override
                    public void onResponse(DashboardEvent response) {
                        getView().setDashboardEventAdapter((DashboardEvent) response.getData());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        getView().setDashboardEventAdapter(new DashboardEvent());
                    }

                    @Override
                    public HomePresenter getPresenter() {
                        return HomePresenter.this;
                    }
                });

    }

    public void getAttendance() {
        apiService = serviceBuilder.createService(ParentService.class);
        HashMap<String, String> params = serviceBuilder.getParams();


        switch (UserType.getUserType(user.getData().getUser_type())) {
            case PARENT:
                params.put("studentId", String.valueOf(ward.getUserInfo().getUserId()));
                params.put("bcsMapId", String.valueOf(ward.getUserInfo().getBcsMapId()));
                params.put("masterId", String.valueOf(ward.getMasterId()));
                params.put("academicSessionId", String.valueOf(ward.getAcademicSessionId()));
                break;
            case STUDENT:
                params.put("studentId", String.valueOf(user.getUserdetails().getUserId()));
                params.put("bcsMapId", String.valueOf(user.getUserdetails().getBcsMapId()));
                params.put("masterId", String.valueOf(user.getData().getMasterId()));
                params.put("academicSessionId", String.valueOf(user.getUserdetails().getAcademicSessionId()));
                break;
        }


        disposable = apiService.getParentAttendance(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Schedule>() {
                    @Override
                    public void onResponse(Schedule response) {
                        getView().setDashboardAttendance(response.getData());
                        //preference.setUserInfo(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        getView().setDashboardAttendance(null);
                    }

                    @Override
                    public HomePresenter getPresenter() {
                        return HomePresenter.this;
                    }
                });
    }

    public void getExamSchedule() {
        apiService = serviceBuilder.createService(ParentService.class);
        HashMap<String, String> params = serviceBuilder.getParams();

        switch (UserType.getUserType(user.getData().getUser_type())) {
            case PARENT:
                params.put("user_id", String.valueOf(ward.getUserInfo().getUserId()));
                params.put("bcsMapId", String.valueOf(ward.getUserInfo().getBcsMapId()));
                params.put("masterId", String.valueOf(ward.getMasterId()));
                params.put("academicSessionId", String.valueOf(ward.getAcademicSessionId()));
                params.put("boardId", String.valueOf(ward.getUserInfo().getBoardId()));
                params.put("classId", String.valueOf(ward.getUserInfo().getClassId()));
                break;
            case STUDENT:
                params.put("user_id", String.valueOf(user.getUserdetails().getUserId()));
                params.put("bcsMapId", String.valueOf(user.getUserdetails().getBcsMapId()));
                params.put("masterId", String.valueOf(user.getData().getMasterId()));
                params.put("academicSessionId", String.valueOf(user.getUserdetails().getAcademicSessionId()));
                params.put("boardId", String.valueOf(user.getUserdetails().getBoardId()));
                params.put("classId", String.valueOf(user.getUserdetails().getClassId()));
                break;
        }


        Calendar currentDate = Calendar.getInstance();
        params.put("date", String.valueOf(DateUtils.toDate(currentDate.getTime(), "yyyy-MM-dd")));

        disposable = apiService.getExamSchedule(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Schedule>() {
                    @Override
                    public void onResponse(Schedule response) {
                        getView().setExamSchedule(response.getData());
                        //preference.setUserInfo(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        getView().setExamSchedule(null);
                    }

                    @Override
                    public HomePresenter getPresenter() {
                        return HomePresenter.this;
                    }
                });
    }

    public void getExamMarks() {
        apiService = serviceBuilder.createService(ParentService.class);
        HashMap<String, String> params = serviceBuilder.getParams();

        switch (UserType.getUserType(user.getData().getUser_type())) {
            case PARENT:
                params.put("studentId", String.valueOf(ward.getUserInfo().getUserId()));
                params.put("bcsMapId", String.valueOf(ward.getUserInfo().getBcsMapId()));
                params.put("masterId", String.valueOf(ward.getMasterId()));
                params.put("academicSessionId", String.valueOf(ward.getAcademicSessionId()));
                break;
            case STUDENT:
                params.put("studentId", String.valueOf(user.getUserdetails().getUserId()));
                params.put("bcsMapId", String.valueOf(user.getUserdetails().getBcsMapId()));
                params.put("masterId", String.valueOf(user.getData().getMasterId()));
                params.put("academicSessionId", String.valueOf(user.getUserdetails().getAcademicSessionId()));
                break;
        }

        disposable = apiService.getExamMarks(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<ExamMarks>() {
                    @Override
                    public void onResponse(ExamMarks response) {
                        getView().setExamMarks(response.getData());
                        //preference.setUserInfo(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        getView().setExamMarks(null);
                    }

                    @Override
                    public HomePresenter getPresenter() {
                        return HomePresenter.this;
                    }
                });
    }

    public void setHomeMenu() {
        switch (UserType.getUserType(user.getData().getUser_type())) {
            case STUDENT:
                mHomeView.inflateMenu(R.menu.menu_student);
                break;
            case TEACHER:
                mHomeView.inflateMenu(R.menu.menu_teacher);
                break;
            case PARENT:
            default:
                mHomeView.inflateMenu(R.menu.menu_parent);
                break;
        }
    }

    @Override
    public void detachView() {
        getView().getAppListener().unBindSocketService();
        super.detachView();
    }
}
