package com.pws.pateast.fragment.profile.reset;

import android.net.Uri;
import android.text.TextUtils;

import com.pws.pateast.R;
import com.pws.pateast.api.ServiceAction;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.ServiceModel;
import com.pws.pateast.api.model.Response;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.model.UserInfo;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.APIService;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.utils.FileUtils;
import com.pws.pateast.utils.Preference;
import com.pws.pateast.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by intel on 15-May-17.
 */

public class ResetPresenter extends AppPresenter<ResetView> {
    @Inject
    ServiceBuilder serviceBuilder;

    @Inject
    Preference preference;

    private APIService apiService;

    private User user;
    private UserInfo userInfo;

    private Uri imageUri;

    private ResetView passwordView;

    @Override
    public ResetView getView() {
        return passwordView;
    }

    @Override
    public void attachView(ResetView view) {
        passwordView = view;
        getComponent().inject(this);
        user = preference.getUser();
        if (preference.getUserInfo() != null)
            userInfo = preference.getUserInfo().getData();
    }

    public UserType getUserType() {
        return UserType.getUserType(user.getData().getUser_type());
    }

    public void setProfileData() {
        getView().setProfileData(userInfo, getUserType());
    }

    public void setProfileImage(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public Uri getProfileImage() {
        return imageUri;
    }

    public void resetPassword(String currentPassword, String newPassword, String repeatPassword) {
        getView().showProgressDialog(getString(R.string.wait));
        apiService = serviceBuilder.createService(APIService.class);

        HashMap<String, String> params = serviceBuilder.getParams(ServiceModel.PROFILE, ServiceAction.ADD);
        params.put("id", String.valueOf(user.getData().getId()));
        params.put("curr_password", currentPassword);
        params.put("password", newPassword);
        params.put("confirm_password", repeatPassword);


        disposable = apiService.resetPassword(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Response>() {
                    @Override
                    public boolean isDismiss() {
                        return false;
                    }

                    @Override
                    public boolean openDialog() {
                        return true;
                    }

                    @Override
                    public void onResponse(Response response) {
                        ResetPresenter.this.getView().navigateToProfile(response.getMessage());
                    }

                    @Override
                    public ResetPresenter getPresenter() {
                        return ResetPresenter.this;
                    }
                });
    }

    public void resetUsername(String username, String password) {
        getView().showProgressDialog(getString(R.string.wait));
        apiService = serviceBuilder.createService(APIService.class);

        HashMap<String, String> params = serviceBuilder.getParams(ServiceModel.PROFILE, ServiceAction.ADD);
        params.put("id", String.valueOf(user.getData().getId()));
        params.put("user_name", username);
        params.put("curr_password", password);


        disposable = apiService.restUsername(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Response>() {
                    @Override
                    public boolean isDismiss() {
                        return false;
                    }

                    @Override
                    public boolean openDialog() {
                        return true;
                    }

                    @Override
                    public void onResponse(Response response) {
                        ResetPresenter.this.getView().navigateToProfile(response.getMessage());
                    }

                    @Override
                    public ResetPresenter getPresenter() {
                        return ResetPresenter.this;
                    }
                });
    }

    public void updateProfile(String name, String email, String mobile) {
        getView().showProgressDialog(getString(R.string.wait));
        apiService = serviceBuilder.createService(APIService.class);

        HashMap<String, String> params = serviceBuilder.getParams(ServiceModel.PROFILE, ServiceAction.EDIT);
        params.put("langId", "1");
        params.put("id", String.valueOf(user.getData().getId()));
        params.put("user_type", user.getData().getUser_type());
        params.put("email", email);
        params.put("mobile", mobile);

        JSONObject data = new JSONObject(params);
        JSONObject userDetails = new JSONObject();
        try {
            userDetails.put("id", userInfo.getUserdetails().get(0).getId());
            userDetails.put("fullname", name);
            data.put("userdetail", userDetails);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = serviceBuilder.prepareStringPart(data.toString());

        MultipartBody.Part fileBody = null;
        if (getProfileImage() != null) {
            String path = FileUtils.getPath(getContext(), getProfileImage());
            Uri destUri = FileUtils.saveFile(path);
            setProfileImage(destUri);
            if (getProfileImage() != null) {
                fileBody = serviceBuilder.prepareFilePart(getContext(), "profile_picture", getProfileImage());
            }
        }

        disposable = apiService.updateProfile(requestBody, fileBody)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Response>() {
                    @Override
                    public void onResponse(Response response) {

                        ResetPresenter.this.getView().navigateToProfile(response.getMessage());
                    }

                    @Override
                    public ResetPresenter getPresenter() {
                        return ResetPresenter.this;
                    }

                    @Override
                    public boolean openDialog() {
                        return true;
                    }

                    @Override
                    public boolean isDismiss() {
                        return false;
                    }
                });
    }

    public void validateResetPassword(CharSequence currentPassword, CharSequence newPassword, CharSequence repeatPassword) {
        if (TextUtils.isEmpty(currentPassword)) {
            getView().setError(R.id.et_current_password, getString(R.string.validate_edittext, getString(R.string.current_password_et)));
        } else if (TextUtils.isEmpty(newPassword)) {
            getView().setError(R.id.et_new_password, getString(R.string.validate_edittext, getString(R.string.new_password_et)));
        } else if (!Utils.isValidPassword(newPassword)) {
            getView().setError(R.id.et_new_password, getString(R.string.valid_password));
        } else if (TextUtils.isEmpty(repeatPassword)) {
            getView().setError(R.id.et_repeat_password, getString(R.string.validate_edittext, getString(R.string.repeat_password_et)));
        } else if (!newPassword.toString().equals(repeatPassword.toString())) {
            getView().setError(R.id.et_repeat_password, getString(R.string.validate_repeat_password, getString(R.string.new_password_et), getString(R.string.repeat_password_et)));
        } else {
            resetPassword(currentPassword.toString(), newPassword.toString(), repeatPassword.toString());
        }
    }

    public void validateResetUsername(CharSequence username, CharSequence password) {
        if (TextUtils.isEmpty(username)) {
            getView().setError(R.id.et_user_name, getString(R.string.validate_edittext, getString(R.string.username)));
        } else if (TextUtils.isEmpty(password)) {
            getView().setError(R.id.et_password, getString(R.string.validate_edittext, getString(R.string.password)));
        } else {
            resetUsername(username.toString(), password.toString());
        }
    }

    public void validateResetProfile(CharSequence name, CharSequence email, CharSequence mobile) {
        if (TextUtils.isEmpty(name)) {
            getView().setError(R.id.et_name, getString(R.string.validate_edittext, getString(R.string.name)));
        } else if (TextUtils.isEmpty(email)) {
            getView().setError(R.id.et_email, getString(R.string.validate_edittext, getString(R.string.email)));
        } else if (!Utils.isValidEmail(email)) {
            getView().setError(R.id.et_email, getString(R.string.valid_email));
        } else if (TextUtils.isEmpty(mobile)) {
            getView().setError(R.id.et_mobile, getString(R.string.validate_edittext, getString(R.string.mobile)));
        } else {
            updateProfile(name.toString(), email.toString(), mobile.toString());
        }
    }

}
