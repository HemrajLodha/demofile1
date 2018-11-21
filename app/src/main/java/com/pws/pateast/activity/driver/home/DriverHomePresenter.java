package com.pws.pateast.activity.driver.home;

import com.pws.pateast.R;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.exception.RetrofitException;
import com.pws.pateast.api.model.Response;
import com.pws.pateast.api.model.RouteMap;
import com.pws.pateast.api.model.Trip;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.APIService;
import com.pws.pateast.api.service.DriverService;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.utils.Preference;

import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.pws.pateast.api.exception.RetrofitException.ERROR_TYPE_MESSAGE;

/**
 * Created by intel on 20-Apr-17.
 */

public class DriverHomePresenter extends AppPresenter<DriverHomeView> {

    @Inject
    protected ServiceBuilder serviceBuilder;
    @Inject
    Preference preference;

    private DriverService apiService;
    private APIService service;

    private DriverHomeView homeView;
    private User user;


    @Override
    public DriverHomeView getView() {
        return homeView;
    }

    @Override
    public void attachView(DriverHomeView view) {
        homeView = view;
        getComponent().inject(this);
        getView().inflateMenu(R.menu.menu_driver);
        user = preference.getUser();
        getView().getAppListener().bindSocketService();
        getView().onActionClick();
    }

    public User getUser() {
        return user;
    }

    public void getUserDetails() {
        getUserDetails(user.getData().getId(), user.getData().getMasterId(), preference.getLanguageID());
    }

    private void getUserDetails(int id, int masterId, String langId) {

        service = serviceBuilder.createService(APIService.class);

        disposable = service.getUserDetails(String.valueOf(id), langId, String.valueOf(masterId))
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<User>() {

                    @Override
                    public boolean openDialog() {
                        return true;
                    }

                    @Override
                    public void onSuccess(User value) {
                        dispose();
                        if (value.isStatus()) {
                            onResponse(value);
                        } else {
                            onError(new RetrofitException(value.getMessage()));
                        }
                    }

                    @Override
                    public void onResponse(User response) {
                        if (response.getData() != null) {
                            preference.setDateFormat(response.getData().getDate_format());
                        }
                        getView().setData(response.getData());
                    }

                    @Override
                    public DriverHomePresenter getPresenter() {
                        return DriverHomePresenter.this;
                    }

                });
    }

    public void getDashboardDetails() {
        apiService = serviceBuilder.createService(DriverService.class);
        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("masterId", String.valueOf(user.getData().getMasterId()));
        params.put("userId", String.valueOf(user.getData().getId()));
        params.put("user_type", String.valueOf(user.getData().getUser_type()));
        params.put("academicSessionId", String.valueOf(user.getUserdetails().getAcademicSessionId()));


        disposable = apiService.getDriverDashBoard(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<RouteMap>() {
                    @Override
                    public void onResponse(RouteMap response) {
                        if (response.getRouteMaps() != null && !response.getRouteMaps().isEmpty()) {
                            getView().setRouteMapsAdapter(response.getRouteMaps());
                        } else {
                            onError(new RetrofitException(getString(R.string.no_routes), ERROR_TYPE_MESSAGE));
                        }
                    }

                    @Override
                    public DriverHomePresenter getPresenter() {
                        return DriverHomePresenter.this;
                    }
                });
    }

    public void getTrip(RouteMap routeMap) {
        getView().showProgressDialog(getString(R.string.wait));

        apiService = serviceBuilder.createService(DriverService.class);
        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("masterId", String.valueOf(user.getData().getMasterId()));
        params.put("rvdhsmapId", String.valueOf(routeMap.getId()));

        disposable = apiService.getTrip(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Response<Trip>>() {
                    @Override
                    public boolean openDialog() {
                        return true;
                    }

                    @Override
                    public void onResponse(Response<Trip> response) {
                        if (response.getData() != null) {
                            Trip trip = response.getData();
                            trip.setRouteMap(routeMap);
                            getView().startTrip(trip);
                        } else {
                            onError(new RetrofitException(getString(R.string.no_trip)));
                        }
                    }

                    @Override
                    public DriverHomePresenter getPresenter() {
                        return DriverHomePresenter.this;
                    }
                });
    }

    @Override
    public void detachView() {
        getView().getAppListener().unBindSocketService();
        super.detachView();
    }
}
