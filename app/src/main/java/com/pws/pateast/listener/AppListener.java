package com.pws.pateast.listener;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ProgressBar;

import com.pws.pateast.MyApplication;
import com.pws.pateast.di.component.ActivityComponent;
import com.pws.pateast.enums.TaskType;

/**
 * Created by intel on 18-Apr-17.
 */

public interface AppListener {
    ProgressDialog getProgressDialog();

    ProgressBar getProgressBar();

    ActivityComponent getComponent();

    MyApplication getApp();

    Intent startService(Class activityClass, Bundle extras);

    Intent openActivity(Class activityClass);

    Intent openActivityOnTop(Class activityClass);

    Intent openActivityOnTop(Class activityClass, Bundle extras);


    Intent openActivity(Class activityClass, TaskType taskType);

    Intent openActivity(Class activityClass, int taskType);

    Intent openActivity(Class activityClass, Uri uri);

    Intent openActivity(Class activityClass, Bundle extras);

    Intent openActivity(Class activityClass, Bundle extras, Uri uri);

    Intent openActivityForResult(Class activityClass, int requestCode);

    Intent openActivityForResult(Class activityClass, Bundle extras, int requestCode);

    void changeFragment(Fragment fragment, String TAG);

    void changeFragmentBack(Fragment fragment, String TAG);

    //void setupToolbar(Toolbar toolbar);

    void setTitle(String title);

    void setTitle(int title);

    void showMessage(String message, boolean show, int color);

    void showMessage(int message, boolean show, int color);

    void showMessage(String message, boolean visible);

    void showDialog(String title, String message, AdapterListener<String> listener, int... args);

    void hideDialog();

    void showAction(String text, boolean visible, View.OnClickListener onClickListener);

    void showIcon(int icon, boolean visible);

    void showActionButton(boolean visible);

    void showResponseView(boolean visible);

    void onActionClick();

    void closeDrawer();

    void bindSocketService();

    void unBindSocketService();

}
