package com.pws.pateast.fragment.suggestion;

import com.pws.pateast.R;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.model.Language;
import com.pws.pateast.api.model.Response;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.model.Ward;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.ParentService;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.fragment.privacy.PrivacyWebView;
import com.pws.pateast.utils.Preference;

import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by intel on 19-Aug-17.
 */

public class WebPresenter extends AppPresenter<PrivacyWebView> {
    private PrivacyWebView view;
    @Inject
    Preference preference;

    @Override
    public PrivacyWebView getView() {
        return view;
    }

    @Override
    public void attachView(PrivacyWebView view) {
        this.view = view;
        getComponent().inject(this);
    }

    public String getLanguage(){
        return preference.getLanguage();
    }

    public String getLanguageId(){
        return preference.getLanguageID();
    }

}
