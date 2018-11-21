package com.pws.pateast.fragment.presenter;

import com.pws.pateast.R;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.exception.RetrofitException;
import com.pws.pateast.api.model.Tag;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.APIService;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.utils.Preference;

import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by planet on 8/29/2017.
 */

public class TagPresenter extends AppPresenter<TagView> {

    private final boolean openDialog;

    @Inject
    public Preference preference;

    @Inject
    public ServiceBuilder serviceBuilder;

    protected User user;

    private TagView mView;
    private APIService apiService;

    @Override
    public TagView getView() {
        return mView;
    }

    public TagPresenter(boolean openDialog) {
        this.openDialog = openDialog;
    }

    public boolean isOpenDialog() {
        return openDialog;
    }

    @Override
    public void attachView(TagView view) {
        this.mView = view;
        getComponent().inject(this);
        user = preference.getUser();
    }

    public void getTags(int masterId,int tagId){
        if (isOpenDialog())
            getView().showProgressDialog(getString(R.string.wait));

        apiService = serviceBuilder.createService(APIService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("masterId", String.valueOf(masterId));
        params.put("type", String.valueOf(tagId));

        disposable = apiService.getTagList(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Tag>() {

                    @Override
                    public boolean openDialog() {
                        return isOpenDialog();
                    }


                    @Override
                    public void onResponse(Tag response) {
                        if (response.getData().isEmpty()) {
                            onError(new RetrofitException(getString(R.string.no_tag_found)));
                        } else {
                            TagPresenter.this.getView().setTagData(response.getData());
                        }
                    }

                    @Override
                    public AppPresenter getPresenter() {
                        return TagPresenter.this;
                    }
                });
    }
}
