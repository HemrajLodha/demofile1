package com.pws.pateast.fragment.suggestion;

import com.pws.pateast.R;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.model.Response;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.model.Ward;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.ParentService;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.utils.Preference;

import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by intel on 19-Aug-17.
 */

public class ParentSuggestionPresenter extends AppPresenter<ParentSuggestionView> {
    private ParentSuggestionView view;

    @Inject
    Preference preference;

    @Inject
    ServiceBuilder serviceBuilder;
    private User user;
    private Ward ward;

    @Override
    public ParentSuggestionView getView() {
        return view;
    }

    @Override
    public void attachView(ParentSuggestionView view) {
        this.view = view;
        getComponent().inject(this);
        user = preference.getUser();
        ward = preference.getWard();
    }


    public void sendSuggestion(String subject, String description) {
        getView().showProgressDialog(getString(R.string.loading));

        ParentService parentService = serviceBuilder.createService(ParentService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("masterId", String.valueOf(ward.getMasterId()));
        params.put("userId", String.valueOf(user.getData().getId()));
        params.put("subject", subject);
        params.put("message", description);

        disposable = parentService.saveSuggestion(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Response>() {
                    @Override
                    public boolean openDialog() {
                        return true;
                    }

                    @Override
                    public void onResponse(Response response) {
                        ParentSuggestionPresenter.this.getView().onSuccess(response.getMessage());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        ParentSuggestionPresenter.this.getView().onError(e.getLocalizedMessage());
                    }

                    @Override
                    public ParentSuggestionPresenter getPresenter() {
                        return ParentSuggestionPresenter.this;
                    }
                });
    }
}
