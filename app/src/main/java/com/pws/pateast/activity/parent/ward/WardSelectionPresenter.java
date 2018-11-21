package com.pws.pateast.activity.parent.ward;

import com.pws.pateast.MyApplication;
import com.pws.pateast.R;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.exception.RetrofitException;
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
 * Created by intel on 29-Jun-17.
 */

public class WardSelectionPresenter extends AppPresenter<WardSelectionView> {
    @Inject
    Preference preference;

    @Inject
    ServiceBuilder serviceBuilder;

    private User user;

    private ParentService apiService;

    private WardSelectionView mSelectionView;

    @Override
    public WardSelectionView getView() {
        return mSelectionView;
    }

    @Override
    public void attachView(WardSelectionView view) {
        mSelectionView = view;
        getComponent().inject(this);
        user = preference.getUser();
    }

    public void setWardPreference(Ward ward) {
        preference.setLanguage(user.getPrimaryLang().getCode());
        preference.setLanguageID(user.getData().getDefault_lang());
        preference.setWard(ward);
    }

    public void getWardList() {

        apiService = serviceBuilder.createService(ParentService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("mobile", user.getData().getMobile());
        params.put("langId", "1");
        params.put("lang", "en");

        dispose();
        disposable = apiService.getWardList(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Ward>() {

                    @Override
                    public void onResponse(Ward response) {
                        if (response.getData() != null && !response.getData().isEmpty()) {
                            mSelectionView.setWardAdapter(response.getData());
                        } else {
                            onError(new RetrofitException(getString(R.string.no_result_found)));
                        }
                    }

                    @Override
                    public WardSelectionPresenter getPresenter() {
                        return WardSelectionPresenter.this;
                    }
                });
    }
}
