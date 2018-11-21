package com.pws.pateast.fragment.events.detail;

import com.pws.pateast.R;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.exception.RetrofitException;
import com.pws.pateast.api.model.Events;
import com.pws.pateast.api.model.Response;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.model.Ward;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.APIService;
import com.pws.pateast.api.service.ParentService;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.utils.Preference;

import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.pws.pateast.api.exception.RetrofitException.ERROR_TYPE_MESSAGE;

public class EventDetailPresenter extends AppPresenter<EventDetailView> {

    private EventDetailView mView;
    @Inject
    Preference preference;

    @Inject
    ServiceBuilder serviceBuilder;
    private User user;
    private Ward ward;
    private APIService apiService;

    @Override
    public EventDetailView getView() {
        return mView;
    }

    @Override
    public void attachView(EventDetailView view) {
        mView = view;
        getComponent().inject(this);
        user = preference.getUser();
        ward = preference.getWard();
        getView().onActionClick();
    }

    public void getEventDetails() {
        apiService = serviceBuilder.createService(APIService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        if (ward != null)
            params.put("masterId", String.valueOf(ward.getMasterId()));
        else
            params.put("masterId", String.valueOf(user.getData().getMasterId()));
        params.put("id", String.valueOf(getView().getEventID()));

        disposable = apiService.getEventsDetails(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Response<Events>>() {
                    @Override
                    public void onResponse(Response<Events> response) {
                        if (response.getData() == null) {
                            onError(new RetrofitException(getString(R.string.no_events_found), ERROR_TYPE_MESSAGE));
                        } else {
                            getView().setEventDetail(response.getData());
                        }
                    }

                    @Override
                    public EventDetailPresenter getPresenter() {
                        return EventDetailPresenter.this;
                    }
                });

    }
}
