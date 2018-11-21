package com.pws.pateast.fragment.settings;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;

import com.pws.pateast.R;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.model.Language;
import com.pws.pateast.api.model.Response;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.model.Ward;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.APIService;
import com.pws.pateast.base.AppActivity;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.fragment.assignment.AssignmentDownloadService;
import com.pws.pateast.listener.AdapterListener;
import com.pws.pateast.provider.SQLiteHelper;
import com.pws.pateast.utils.DialogUtils;
import com.pws.pateast.utils.Preference;
import com.pws.pateast.utils.ShortcutUtils;
import com.pws.pateast.utils.Utils;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by intel on 20-Apr-17.
 */

public class SettingsPresenter extends AppPresenter<SettingsView> {
    @Inject
    Preference preference;
    @Inject
    ShortcutUtils shortcutUtils;

    private User user;
    private Ward ward;

    @Inject
    protected ServiceBuilder serviceBuilder;

    APIService apiService;

    private SettingsView mSettingsView;
    private UserType userType;

    @Override
    public SettingsView getView() {
        return mSettingsView;
    }

    @Override
    public void attachView(SettingsView mSettingsView) {
        this.mSettingsView = mSettingsView;
        getComponent().inject(this);
        user = preference.getUser();
        ward = preference.getWard();
        userType = UserType.getUserType(user.getData().getUser_type());
        getView().setNotification(preference.getNotification());
        getView().setSettingsVisibility(userType);
    }

    public User getUser() {
        return user;
    }

    public void setNotification(boolean flag) {
        preference.setNotification(flag);
    }

    public void changeNotification() {
        getView().setNotification(!preference.getNotification());
    }

    public void showLanguageDialog(boolean cancelable) {
        List<Language> items = null;
        switch (userType) {
            case PARENT:
                items = ward.getLanguages();
                break;
            default:
                items = user.getLanguages();
                break;
        }

        final List<Language> finalItems = items;
        DialogUtils.showSingleChoiceDialog(getContext(),
                getString(R.string.action_change_language_title),
                Language.getNameArray(items),
                Language.getSelectedPosition(items, preference.getLanguage()),
                new AdapterListener<Integer>() {
                    @Override
                    public void onClick(int id, Integer value) {
                        if (id == AlertDialog.BUTTON_POSITIVE) {
                            if (userType != UserType.PARENT)
                                changeLanguage(finalItems.get(value).getCode(), finalItems.get(value).getId());
                            else {
                                preference.setLanguage(finalItems.get(value).getCode());
                                preference.setLanguageID(finalItems.get(value).getId());
                                getContext().sendBroadcast(new Intent(AppActivity.LANGUAGE));
                            }

                        }
                    }
                }, cancelable,
                getString(R.string.ok)
        );
    }


    public void logout() {
        DialogUtils.showDialog(getContext(),
                getString(R.string.pref_title_logout),
                getString(R.string.logout_prompt_message),
                new AdapterListener<String>() {
                    @Override
                    public void onClick(int id, String value) {
                        if (id == 0) {
                            removeToken();
                        }
                    }
                },
                R.string.pref_title_logout,
                R.string.cancel
        );
    }

    public void removeToken() {

        dispose();
        getView().showProgressDialog(getString(R.string.wait));

        apiService = serviceBuilder.createService(APIService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("userId", String.valueOf(user.getData().getId()));


        disposable = apiService.logOutUser(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Response>() {

                    @Override
                    public void onResponse(Response response) {
                        getContext().stopService(new Intent(getContext(), AssignmentDownloadService.class));
                        clearData();
                    }

                    @Override
                    public SettingsPresenter getPresenter() {
                        return SettingsPresenter.this;
                    }

                    @Override
                    public boolean openDialog() {
                        return true;
                    }
                });
    }

    public void changeLanguage(final String default_code, final int default_lang) {
        dispose();
        getView().showProgressDialog(getString(R.string.wait));
        apiService = serviceBuilder.createService(APIService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("id", String.valueOf(user.getData().getId()));
        params.put("default_lang", String.valueOf(default_lang));
        params.put("langId", String.valueOf(default_lang));
        params.put("lang", default_code);


        disposable = apiService.changeLanguage(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Response>() {

                    @Override
                    public void onResponse(Response response) {
                        Utils.showToast(getContext(), response.getMessage());
                        preference.setLanguage(default_code);
                        preference.setLanguageID(default_lang);
                        getContext().sendBroadcast(new Intent(AppActivity.LANGUAGE));
                    }

                    @Override
                    public SettingsPresenter getPresenter() {
                        return SettingsPresenter.this;
                    }

                    @Override
                    public boolean openDialog() {
                        return true;
                    }
                });
    }


    public void setNotificationStatus(final int status) {

        dispose();
        getView().showProgressDialog(getString(R.string.wait));

        apiService = serviceBuilder.createService(APIService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("userId", String.valueOf(user.getData().getId()));
        params.put("is_notification", String.valueOf(status));

        disposable = apiService.changeFcmNotificationStatus(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Response>() {

                    @Override
                    public void onResponse(Response response) {
                        SettingsPresenter.this.getView().onNotificationStatusChange(status);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        SettingsPresenter.this.getView().onNotificationStatusChange(status == 1 ? 0 : 1);
                    }

                    @Override
                    public SettingsPresenter getPresenter() {
                        return SettingsPresenter.this;
                    }

                    @Override
                    public boolean openDialog() {
                        return true;
                    }
                });
    }


    private void clearData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            shortcutUtils.removeShortcuts();
            shortcutUtils.enablePinnedShortcuts(false);
        }
        SQLiteHelper.deleteDatabase(getContext());
        preference.logout();
    }

}
