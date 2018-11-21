package com.pws.pateast;

import android.content.Context;
import android.os.Build;
import android.support.annotation.VisibleForTesting;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.pws.pateast.di.component.ApplicationComponent;
import com.pws.pateast.di.component.DaggerApplicationComponent;
import com.pws.pateast.di.module.ApplicationModule;
import com.pws.pateast.events.AuthenticationErrorEvent;
import com.pws.pateast.listener.AdapterListener;
import com.pws.pateast.provider.SQLiteHelper;
import com.pws.pateast.utils.DialogUtils;
import com.pws.pateast.utils.FileUtils;
import com.pws.pateast.utils.LocaleHelper;
import com.pws.pateast.utils.Preference;
import com.pws.pateast.utils.ShortcutUtils;
import com.pws.pateast.utils.TypefaceUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import im.ene.toro.exoplayer.ToroExo;
import io.fabric.sdk.android.Fabric;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

import static com.pws.pateast.utils.TypefaceUtil.SERIF;

/**
 * Created by planet on 4/13/2017.
 */

public class MyApplication extends MultiDexApplication {
    private Context mRunningActivity;
    private boolean isDialogShowing;
    public static final String DEFAULT_LANGUAGE = "en";
    public static final int DEFAULT_LANGUAGE_ID = 1;
    @Inject
    ShortcutUtils shortcutUtils;
    @Inject
    EventBus mEventBus;
    @Inject
    Preference preference;

    private Scheduler mScheduler;
    private ApplicationComponent mApplicationComponent;
    private static MyApplication baseApplication;

    public static MyApplication get(Context context) {
        return (MyApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        TypefaceUtil.overrideFont(getApplicationContext(), SERIF);
        Fabric.with(this, new Crashlytics());
        FileUtils.createApplicationFolder();
        setApplicationComponent(DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build());

        mApplicationComponent.inject(this);

        mEventBus.register(this);
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

    @VisibleForTesting
    public void setApplicationComponent(ApplicationComponent applicationComponent) {
        this.mApplicationComponent = applicationComponent;
    }

    public Scheduler getSubscribeScheduler() {
        if (mScheduler == null) {
            mScheduler = Schedulers.computation();
        }
        return mScheduler;
    }

    @Override
    public void onTerminate() {
        mEventBus.unregister(this);
        super.onTerminate();
    }

    public void setRunningActivity(Context mRunningActivity) {
        this.mRunningActivity = mRunningActivity;
    }

    @Subscribe
    public void onEvent(AuthenticationErrorEvent event) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            shortcutUtils.removeShortcuts();
            shortcutUtils.enablePinnedShortcuts(false);
        }
        SQLiteHelper.deleteDatabase(getApplicationContext());
        if (mRunningActivity != null) {
            preference.clear();
            logoutWithPrompt();
        } else {
            preference.logout();
        }
    }

    // TODO can do it in better way
    private void logoutWithPrompt() {
        if (isDialogShowing) {
            return;
        }
        isDialogShowing = true;
        DialogUtils.showDialog(mRunningActivity, getString(R.string.menu_logout), getString(R.string.logout_message),
                new AdapterListener<String>() {
                    @Override
                    public void onClick(int id, String value) {
                        preference.startLoginActivity();
                        isDialogShowing = false;
                    }
                }, R.string.ok);
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, DEFAULT_LANGUAGE));
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level >= TRIM_MEMORY_BACKGROUND)
            ToroExo.with(this).cleanUp();
    }
}
