package com.pws.pateast.activity;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.maps.android.PolyUtil;
import com.pws.pateast.activity.driver.home.DriverHomeActivity;
import com.pws.pateast.activity.login.LoginActivity;
import com.pws.pateast.activity.parent.home.ParentHomeActivity;
import com.pws.pateast.activity.parent.ward.WardSelectionActivity;
import com.pws.pateast.activity.student.home.StudentHomeActivity;
import com.pws.pateast.activity.teacher.home.TeacherHomeActivity;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.model.User;
import com.pws.pateast.base.AppActivity;
import com.pws.pateast.base.Extras;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.events.DeviceTokenEvent;
import com.pws.pateast.utils.AESCrypt;
import com.pws.pateast.utils.NotificationHelper;
import com.pws.pateast.utils.Preference;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

public class SplashActivity extends AppActivity {
    private static final String TAG = SplashActivity.class.getSimpleName();
    @Inject
    EventBus eventBus;
    @Inject
    Preference preference;

    @Override
    protected int getResourceLayout() {
        return 0;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        getComponent().inject(this);
        eventBus.register(this);
        eventBus.post(new DeviceTokenEvent(FirebaseInstanceId.getInstance().getToken()));
        /*try {
            String encrypt = AESCrypt.getInstance().encryptData("Hi, PKS");
            String decrypt = AESCrypt.getInstance().decryptData(encrypt);

            Log.d("Encrypt", encrypt);
            Log.d("Encrypt", decrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        eventBus.unregister(this);
    }

    @Subscribe
    public void onEvent(DeviceTokenEvent event) {
        if (event.getDeviceToken() != null) {
            ServiceBuilder.DEVICE_TOKEN = event.getDeviceToken();
            User user = preference.getUser();
            if (user == null) {
                openActivity(LoginActivity.class);
            } else {
                if (getIntent().getStringExtra(Extras.EXTRA_TYPE) != null) {
                    NotificationHelper.showNotificationIntent(this, getIntent().getStringExtra(Extras.EXTRA_TYPE), true);
                } else {
                    switch (UserType.getUserType(user.getData().getUser_type())) {
                        case STUDENT:
                            openActivity(StudentHomeActivity.class);
                            break;
                        case PARENT:
                            openActivity(preference.getWard() == null ? WardSelectionActivity.class : ParentHomeActivity.class);
                            break;
                        case TEACHER:
                            openActivity(TeacherHomeActivity.class);
                            break;
                        case DRIVER:
                            openActivity(DriverHomeActivity.class);
                            break;
                    }
                }
            }
            finish();
        }
    }


}
