package com.pws.pateast.fragment.presenter;

import com.pws.pateast.R;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.exception.RetrofitException;
import com.pws.pateast.api.model.TeacherClass;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.model.Ward;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.StudentService;
import com.pws.pateast.api.service.TeacherService;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.utils.Preference;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by intel on 20-Apr-17.
 */

public class ClassPresenter<V extends ClassView> extends AppPresenter<V> {
    @Inject
    public Preference preference;

    @Inject
    public ServiceBuilder serviceBuilder;

    protected User user;

    protected Ward ward;

    protected TeacherService teacherApiService;

    protected StudentService studentApiService;

    private V myClassesView;

    private boolean openDialog;

    public ClassPresenter(boolean openDialog) {
        this.openDialog = openDialog;
    }

    public boolean isOpenDialog() {
        return openDialog;
    }

    @Override
    public V getView() {
        return myClassesView;
    }

    @Override
    public void attachView(V myClassesView) {
        this.myClassesView = myClassesView;
        getComponent().inject((ClassPresenter<ClassView>) this);
        user = preference.getUser();
        ward = preference.getWard();
    }

    public void getMyClasses() {
        if (isOpenDialog())
            getView().showProgressDialog(getString(R.string.wait));

        teacherApiService = serviceBuilder.createService(TeacherService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("userId", String.valueOf(user.getUserdetails().getUserId()));
        params.put("academicSessionId", String.valueOf(user.getUserdetails().getAcademicSessionId()));
        params.put("masterId", String.valueOf(user.getData().getMasterId()));

        disposable = teacherApiService.getMyClasses(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<TeacherClass>() {

                    @Override
                    public boolean openDialog() {
                        return openDialog;
                    }

                    @Override
                    public void onResponse(TeacherClass response) {
                        if (response.getData().isEmpty()) {
                            onError(new RetrofitException(getString(R.string.no_classes)));
                        } else {
                            myClassesView.setClassAdapter(response.getData());
                        }
                    }

                    @Override
                    public ClassPresenter getPresenter() {
                        return ClassPresenter.this;
                    }
                });
    }


    public int getSelectedItemPosition(List list, Object o) {
        return list.indexOf(o) + 1;
    }
}
