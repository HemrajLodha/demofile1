package com.pws.pateast.base;

import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.base.ui.BaseActivity;
import com.pws.pateast.MyApplication;
import com.pws.pateast.di.component.ActivityComponent;
import com.pws.pateast.di.component.DaggerActivityComponent;
import com.pws.pateast.di.module.ActivityModule;
import com.pws.pateast.enums.TaskType;
import com.pws.pateast.listener.AdapterListener;
import com.pws.pateast.listener.AppListener;
import com.pws.pateast.utils.DialogUtils;
import com.pws.pateast.utils.LocaleHelper;
import com.pws.pateast.utils.MessageUtils;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;
import static com.pws.pateast.MyApplication.DEFAULT_LANGUAGE;


/**
 * Created by intel on 18-Apr-17.
 */

public abstract class AppActivity extends BaseActivity implements AppListener {
    public static final String LANGUAGE = "Language.changed";

    private ActivityComponent mComponent;
    private Dialog mDialog;

    /**
     * The receiver that will handle the change of the language.
     */
    private BroadcastReceiver mLanguageChangedReceiver;


    private class AppHandler extends Handler {
        public AppHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {

                    // Define receiver
                    mLanguageChangedReceiver = new BroadcastReceiver() {

                        @Override
                        public void onReceive(final Context context, final Intent intent) {
                            recreate();
                        }
                    };

                    // Register receiver
                    registerReceiver(mLanguageChangedReceiver, new IntentFilter(LANGUAGE));

                    onViewReady(msg.getData());
                }
            });
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mComponent = DaggerActivityComponent
                .builder()
                .applicationComponent(getApp().getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .build();
        HandlerThread thread = new HandlerThread(AppActivity.class.getName() + "Args",
                THREAD_PRIORITY_BACKGROUND);
        thread.start();

        AppHandler handler = new AppHandler(thread.getLooper());
        Message message = handler.obtainMessage();
        message.setData(savedInstanceState);

        handler.sendMessage(message);
    }

    @Override
    public ProgressDialog getProgressDialog() {
        return null;
    }

    @Override
    public ProgressBar getProgressBar() {
        return null;
    }

    @Override
    public ActivityComponent getComponent() {
        return mComponent;
    }


    @Override
    public MyApplication getApp() {
        return (MyApplication) getApplicationContext();
    }

    @Override
    public void setTitle(String title) {
        if (mActionBar != null)
            mActionBar.setTitle(title);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getApplicationContext() != null) {
            ((MyApplication) getApplication()).setRunningActivity(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (getApplicationContext() != null) {
            ((MyApplication) getApplication()).setRunningActivity(null);
        }
    }

    @Override
    public void setTitle(int title) {
        setTitle(getString(title));
    }


    @Override
    public void changeFragment(Fragment fragment, String TAG) {

    }

    @Override
    public void changeFragmentBack(Fragment fragment, String TAG) {

    }

    /**
     * @param message to show in snack bar
     * @param show    to hold snack bar
     * @param color   background color of snack bar
     */
    @Override
    public final void showMessage(String message, boolean show, int color) {
        if (show)
            MessageUtils.snackBarWithAction(this, findViewById(android.R.id.content), message, color);
        else
            MessageUtils.snackBarWithoutAction(this, findViewById(android.R.id.content), message, color);
    }

    /**
     * @param message to show in snack bar
     * @param show    to hold snack bar
     * @param color   background color of snack bar
     */
    @Override
    public final void showMessage(int message, boolean show, int color) {
        showMessage(getString(message), show, color);
    }

    @Override
    public void showDialog(String title, String message, AdapterListener<String> listener, int... args) {
        hideDialog();
        mDialog = DialogUtils.showDialog(this, title, message, listener, args);
    }

    @Override
    public void hideDialog() {
        DialogUtils.cancel(mDialog);
    }

    @Override
    public void showMessage(String message, boolean visible) {
        // HAve Fun
    }

    @Override
    public void showAction(String text, boolean visible, View.OnClickListener onClickListener) {
        // HAve Fun
    }

    @Override
    public void showIcon(int icon, boolean visible) {
        // HAve Fun
    }

    @Override
    public void showActionButton(boolean visible) {
        // HAve Fun
    }

    @Override
    public void showResponseView(boolean visible) {
        // HAve Fun
    }

    @Override
    public final Intent startService(Class activityClass, Bundle extras) {
        Intent intent = new Intent(this, activityClass);
        intent.putExtras(extras);
        startService(intent);
        return intent;
    }

    @Override
    public final Intent openActivity(Class activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
        return intent;
    }

    @Override
    public final Intent openActivityOnTop(Class activityClass) {
        Intent intent = new Intent(this, activityClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        return intent;
    }

    @Override
    public final Intent openActivity(Class activityClass, TaskType taskType) {
        return openActivity(activityClass, taskType.getValue());
    }

    @Override
    public final Intent openActivity(Class activityClass, int taskType) {
        Intent intent = new Intent(this, activityClass);
        intent.putExtra(FragmentActivity.EXTRA_TASK_TYPE, taskType);
        startActivity(intent);
        return intent;
    }

    @Override
    public final Intent openActivity(Class activityClass, Uri uri) {
        Intent intent = new Intent(this, activityClass);
        intent.setData(uri);
        startActivity(intent);
        return intent;
    }

    @Override
    public final Intent openActivity(Class activityClass, Bundle extras) {
        Intent intent = new Intent(this, activityClass);
        intent.putExtras(extras);
        startActivity(intent);
        return intent;
    }

    @Override
    public final Intent openActivityOnTop(Class activityClass, Bundle extras) {
        Intent intent = new Intent(this, activityClass);
        intent.putExtras(extras);
        startActivity(intent);
        return intent;
    }

    @Override
    public final Intent openActivity(Class activityClass, Bundle extras, Uri uri) {
        Intent intent = new Intent(this, activityClass);
        intent.setData(uri);
        intent.putExtras(extras);
        startActivity(intent);
        return null;
    }

    @Override
    public final Intent openActivityForResult(Class activityClass, int requestCode) {
        Intent intent = new Intent(this, activityClass);
        startActivityForResult(intent, requestCode);
        return intent;
    }

    @Override
    public final Intent openActivityForResult(Class activityClass, Bundle extras, int requestCode) {
        Intent intent = new Intent(this, activityClass);
        intent.putExtras(extras);
        startActivityForResult(intent, requestCode);
        return intent;
    }

    @Override
    public void onActionClick() {

    }

    @Override
    public void closeDrawer() {

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, DEFAULT_LANGUAGE));
    }

    @Override
    public void bindSocketService() {

    }

    @Override
    public void unBindSocketService() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister receiver
        if (mLanguageChangedReceiver != null) {
            try {
                unregisterReceiver(mLanguageChangedReceiver);
                mLanguageChangedReceiver = null;
            } catch (final Exception e) {
            }
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("isMyServiceRunning?", true + "");
                return true;
            }
        }
        Log.i("isMyServiceRunning?", false + "");
        return false;
    }

}
