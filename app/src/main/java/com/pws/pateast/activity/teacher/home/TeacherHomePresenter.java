package com.pws.pateast.activity.teacher.home;

import com.pws.pateast.api.ServiceAction;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.ServiceModel;
import com.pws.pateast.api.model.TeacherDashboard;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.APIService;
import com.pws.pateast.api.service.TeacherService;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.utils.NetworkUtil;
import com.pws.pateast.utils.Preference;

import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by intel on 20-Apr-17.
 */

public class TeacherHomePresenter extends AppPresenter<TeacherHomeView> {
    @Inject
    protected ServiceBuilder serviceBuilder;

    @Inject
    protected Preference preference;

    TeacherService apiService;
    APIService service;
    TeacherHomeView homeView;
    User user;

    @Override
    public TeacherHomeView getView() {
        return homeView;
    }

    public int getReceiverId() {
        return user.getData().getId();
    }

    @Override
    public void attachView(TeacherHomeView view) {
        homeView = view;
        getComponent().inject(this);
        user = preference.getUser();
        getView().getAppListener().bindSocketService();
    }

    @Override
    public void detachView() {
        getView().getAppListener().unBindSocketService();
        super.detachView();
    }

    public void getDashboardData() {
        getUserDetails(user.getData().getId(), user.getData().getMasterId(), preference.getLanguageID());

        apiService = serviceBuilder.createService(TeacherService.class);
        HashMap<String, String> params = serviceBuilder.getParams(ServiceModel.PROFILE, ServiceAction.VIEW);
        params.put("userId", String.valueOf(user.getData().getId()));

        disposable = apiService.getTeacherDashboard(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<TeacherDashboard>() {

                    @Override
                    public void onResponse(TeacherDashboard response) {
                        TeacherHomePresenter.this.getView().setNotificationCount(response);
                    }

                    @Override
                    public TeacherHomePresenter getPresenter() {
                        return TeacherHomePresenter.this;
                    }

                });
    }


    public void getUserDetails(int id, int masterId, String langId) {
        if (!NetworkUtil.isOnline(getContext()))
            getView().setData(user.getUserdetails());

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
                    public TeacherHomePresenter getPresenter() {
                        return TeacherHomePresenter.this;
                    }

                });
    }
}
