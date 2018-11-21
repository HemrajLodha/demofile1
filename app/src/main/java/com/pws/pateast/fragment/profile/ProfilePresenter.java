package com.pws.pateast.fragment.profile;

import android.text.TextUtils;

import com.pws.pateast.R;
import com.pws.pateast.api.ServiceAction;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.ServiceModel;
import com.pws.pateast.api.model.Subject;
import com.pws.pateast.api.model.TeacherClass;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.model.UserInfo;
import com.pws.pateast.api.model.Ward;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.APIService;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.fragment.profile.student.StudentProfileView;
import com.pws.pateast.utils.Preference;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by intel on 20-Apr-17.
 */

public class ProfilePresenter<V extends ProfileView> extends AppPresenter<V> {
    @Inject
    EventBus eventBus;

    @Inject
    ServiceBuilder serviceBuilder;

    @Inject
    Preference preference;

    private User user;
    private Ward ward;
    private UserInfo userInfo;
    private UserType userType;

    private APIService apiService;

    private V mProfileView;


    @Override
    public V getView() {
        return mProfileView;
    }

    @Override
    public void attachView(V mProfileView) {
        this.mProfileView = mProfileView;
        getComponent().inject((ProfilePresenter<ProfileView>) this);
        user = preference.getUser();
        ward = preference.getWard();
        userInfo = preference.getUserInfo();
        userType = UserType.getUserType(user.getData().getUser_type());
        if (userInfo != null && getView().loadFromPreference()) {
            getView().setData(user, userInfo.getData());
            getUserDetails(true);
        } else {
            getView().onActionClick();
        }

    }

    public User getUser() {
        return user;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public String getSubjects(ArrayList<Subject> list) {
        ArrayList<String> subjects = new ArrayList<>();
        for (Subject subject : list) {
            subjects.add(subject.getSubject().getSubjectdetails().get(0).getName());
        }
        return TextUtils.join(", ", subjects);
    }

    public void getUserDetails(final boolean isOpenDialog) {

        if (isOpenDialog)
            getView().showProgressDialog(getString(R.string.loading));

        apiService = serviceBuilder.createService(APIService.class);

        HashMap<String, String> params = serviceBuilder.getParams(ServiceModel.PROFILE, ServiceAction.VIEW);
        if (userType == UserType.PARENT && getView() instanceof StudentProfileView && ((StudentProfileView) getView()).isStudent()) {
            params.put("id", String.valueOf(ward.getUserId()));
            params.put("user_type", ward.getUser_type());
        } else {
            params.put("id", String.valueOf(user.getData().getId()));
            params.put("user_type", userType.getValue());
        }

        disposable = apiService.getUserProfile(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<UserInfo>() {

                    @Override
                    public boolean openDialog() {
                        return isOpenDialog;
                    }

                    @Override
                    public void onResponse(UserInfo response) {
                        if (!(getView() instanceof StudentProfileView) || !((StudentProfileView) getView()).isStudent()) {
                            preference.setUserInfo(response);
                        }
                        getView().setData(user, response.getData());
                    }

                    @Override
                    public ProfilePresenter getPresenter() {
                        return ProfilePresenter.this;
                    }

                });
    }


    public String getClassName(TeacherClass classes) {
        return getString(R.string.class_name, classes.getBoard().getBoarddetails().get(0).getAlias(), classes.getClasses().getClassesdetails().get(0).getName(), classes.getSection().getSectiondetails().get(0).getName());
    }
}
