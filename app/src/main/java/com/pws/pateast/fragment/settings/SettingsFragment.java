package com.pws.pateast.fragment.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.pws.pateast.R;
import com.pws.pateast.activity.tasks.DriverTaskActivity;
import com.pws.pateast.activity.tasks.ParentTaskActivity;
import com.pws.pateast.activity.tasks.StudentTaskActivity;
import com.pws.pateast.activity.tasks.TeacherTaskActivity;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.model.UserInfo;
import com.pws.pateast.base.AppDialogFragment;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.base.Extras;
import com.pws.pateast.base.FragmentActivity;
import com.pws.pateast.enums.TaskType;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.fragment.privacy.WebFragment;
import com.pws.pateast.fragment.profile.ProfilePresenter;
import com.pws.pateast.fragment.profile.ProfileView;
import com.pws.pateast.fragment.profile.reset.ResetFragment;
import com.pws.pateast.listener.AdapterListener;

/**
 * Created by pws-A on 3/14/2017.
 */

public class SettingsFragment extends AppFragment implements SettingsView, ProfileView, View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private LinearLayout layoutProfile, editProfile, changePassword, notification, changeLanguage, privacyPolicy, terms, logout;
    private SwitchCompat switchNotification;

    private SettingsPresenter mPresenter;
    private ProfilePresenter profilePresenter;

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_settings;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        getAppListener().setTitle(R.string.menu_settings);
        layoutProfile = (LinearLayout) findViewById(R.id.layout_profile);
        editProfile = (LinearLayout) findViewById(R.id.edit_profile);
        changePassword = (LinearLayout) findViewById(R.id.change_password);
        notification = (LinearLayout) findViewById(R.id.notification);
        changeLanguage = (LinearLayout) findViewById(R.id.change_language);
        privacyPolicy = (LinearLayout) findViewById(R.id.privacy_policy);
        terms = (LinearLayout) findViewById(R.id.terms);
        logout = (LinearLayout) findViewById(R.id.logout);

        switchNotification = (SwitchCompat) findViewById(R.id.switch_notification);

        editProfile.setOnClickListener(this);
        changePassword.setOnClickListener(this);
        notification.setOnClickListener(this);
        changeLanguage.setOnClickListener(this);
        privacyPolicy.setOnClickListener(this);
        terms.setOnClickListener(this);
        logout.setOnClickListener(this);


        mPresenter = new SettingsPresenter();
        mPresenter.attachView(this);
        profilePresenter = new ProfilePresenter();
        profilePresenter.attachView(this);

        switchNotification.setOnCheckedChangeListener(this);
    }

    @Override
    public void onActionClick() {
        if (profilePresenter != null && profilePresenter.getUserInfo() == null)
            profilePresenter.getUserDetails(true);
    }

    @Override
    public void setNotification(boolean notification) {
        switchNotification.setChecked(notification);
    }

    @Override
    public void setSettingsVisibility(UserType userType) {
        switch (userType) {
            case PARENT:
                changePassword.setVisibility(View.GONE);
                break;
            case STUDENT:
                editProfile.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void setData(User user, UserInfo userInfo) {

    }

    @Override
    public boolean loadFromPreference() {
        return false;
    }

    @Override
    public void showDialog(String title, String message, AdapterListener<String> listener, int... args) {
        super.showDialog(title, message, new AdapterListener<String>() {
            @Override
            public void onClick(int id, String value) {
                profilePresenter.getUserDetails(true);
            }
        }, args);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_profile:
                ResetFragment resetFragment = AppDialogFragment.newInstance(ResetFragment.class, getAppListener());
                resetFragment.getArguments().putBoolean(Extras.RESET_PROFILE, true);
                resetFragment.attachView(this);
                resetFragment.show(getFragmentManager(), ResetFragment.class.getSimpleName());
                break;
            case R.id.change_password:
                resetFragment = AppDialogFragment.newInstance(ResetFragment.class, getAppListener());
                resetFragment.getArguments().putBoolean(Extras.RESET_PASSWORD, true);
                resetFragment.attachView(this);
                resetFragment.show(getFragmentManager(), ResetFragment.class.getSimpleName());
                break;
            case R.id.notification:
                mPresenter.changeNotification();
                break;
            case R.id.change_language:
                mPresenter.showLanguageDialog(true);
                break;
            case R.id.logout:
                mPresenter.logout();
                break;
            case R.id.privacy_policy:
                startPrivacyTosIntent(WebFragment.TYPE_PRIVACY);
                break;
            case R.id.terms:
                startPrivacyTosIntent(WebFragment.TYPE_TOS);
                break;
        }
    }


    private void startPrivacyTosIntent(int taskType) {
        Bundle bundle = new Bundle();
        bundle.putInt(FragmentActivity.EXTRA_TASK_TYPE, TaskType.PRIVACY_TOS.getValue());
        bundle.putInt(FragmentActivity.EXTRA_DATA, taskType);
        switch (UserType.getUserType(profilePresenter.getUser().getData().getUser_type())) {
            case STUDENT:
                getAppListener().openActivity(StudentTaskActivity.class, bundle);
                break;
            case PARENT:
                getAppListener().openActivity(ParentTaskActivity.class, bundle);
                break;
            case TEACHER:
                getAppListener().openActivity(TeacherTaskActivity.class, bundle);
                break;
            case DRIVER:
                getAppListener().openActivity(DriverTaskActivity.class, bundle);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.switch_notification:
                mPresenter.setNotificationStatus(b ? 1 : 0);
                break;
        }
    }

    @Override
    public void onNotificationStatusChange(int status) {
        switchNotification.setOnCheckedChangeListener(null);
        switchNotification.setChecked(status == 1);
        mPresenter.setNotification(status == 1);
        switchNotification.setOnCheckedChangeListener(this);
    }

    @Override
    public void onDestroy() {
        if (mPresenter != null)
            mPresenter.detachView();
        super.onDestroy();
    }


}
