package com.pws.pateast.fragment.notification;

import com.pws.pateast.R;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.exception.RetrofitException;
import com.pws.pateast.api.model.Notification;
import com.pws.pateast.api.model.NotificationData;
import com.pws.pateast.api.model.Response;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.model.Ward;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.APIService;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.utils.Preference;

import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by intel on 18-Aug-17.
 */

public class ParentNotificationListPresenter extends AppPresenter<ParentNotificationListView> {
    @Inject
    protected Preference preference;

    @Inject
    protected ServiceBuilder serviceBuilder;

    protected User user;

    protected Ward ward;

    protected APIService apiService;

    ParentNotificationListView view;

    @Override
    public ParentNotificationListView getView() {
        return view;
    }

    @Override
    public void attachView(ParentNotificationListView view) {
        this.view = view;
        getComponent().inject(this);
        user = preference.getUser();
    }

    protected void getNotificationList() {
        apiService = serviceBuilder.createService(APIService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("userId", String.valueOf(user.getData().getId()));

        HashMap<String, String> queryParams = new HashMap<>();

        queryParams.put("page", String.valueOf(getView().getPage()));

        dispose();
        disposable = apiService.getNotificationList(params, queryParams)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Notification>() {

                    @Override
                    public void onResponse(Notification response) {
                        if (response.getData().isEmpty()) {
                            onError(new RetrofitException(getString(R.string.no_notification)));
                        } else {
                            getView().setPageCount(response.getPageCount());
                            ParentNotificationListPresenter.this.getView().setListAdapter(response.getData());
                        }
                    }

                    @Override
                    public ParentNotificationListPresenter getPresenter() {
                        return ParentNotificationListPresenter.this;
                    }
                });
    }

    protected void setNotificationStatus(final NotificationData data, final int position, int status) {
        dispose();
        getView().showProgressDialog(getString(R.string.wait));

        apiService = serviceBuilder.createService(APIService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("receiverId", String.valueOf(user.getData().getId()));
        params.put("notificationId", String.valueOf(data.getNotificationId()));
        params.put("is_notification", String.valueOf(status));


        disposable = apiService.setNotificationStatus(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Response>() {

                    @Override
                    public void onResponse(Response response) {
                        getView().onStatusChange(position);
                    }

                    @Override
                    public ParentNotificationListPresenter getPresenter() {
                        return ParentNotificationListPresenter.this;
                    }

                    @Override
                    public boolean openDialog() {
                        return true;
                    }

                });
    }

}
