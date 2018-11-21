package com.pws.pateast.fragment.circular;

import com.pws.pateast.R;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.exception.RetrofitException;
import com.pws.pateast.api.model.Circular;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.model.Ward;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.APIService;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.utils.Preference;

import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.pws.pateast.api.exception.RetrofitException.ERROR_TYPE_MESSAGE;

public class CircularPresenter extends AppPresenter<CircularView> {
    private CircularView mView;
    @Inject
    Preference preference;

    @Inject
    ServiceBuilder serviceBuilder;
    protected User user;
    protected Ward ward;
    private APIService apiService;

    @Override
    public CircularView getView() {
        return mView;
    }

    @Override
    public void attachView(CircularView view) {
        mView = view;
        getComponent().inject(this);
        user = preference.getUser();
        ward = preference.getWard();
        getView().onActionClick();
    }

    protected void getCircular(String masterId, String academicSessionId, HashMap<String, String> queryParams) {
        apiService = serviceBuilder.createService(APIService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("masterId", masterId);
        params.put("academicSessionId", academicSessionId);

        disposable = apiService.getCircular(params, queryParams)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Circular>() {
                    @Override
                    public void onResponse(Circular response) {
                        if (response.getData().isEmpty()) {
                            onError(new RetrofitException(getString(R.string.no_events_found), ERROR_TYPE_MESSAGE));
                        } else {
                            getView().setCircularAdapter(response.getData());
                        }
                    }

                    @Override
                    public CircularPresenter getPresenter() {
                        return CircularPresenter.this;
                    }
                });
    }
}
