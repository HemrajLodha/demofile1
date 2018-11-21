package com.pws.pateast.service.sync;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.pws.pateast.MyApplication;
import com.pws.pateast.api.model.UserInfo;
import com.pws.pateast.base.Extras;
import com.pws.pateast.chat.UserHelper;
import com.pws.pateast.di.component.ApplicationComponent;
import com.pws.pateast.provider.table.UserChats;

import java.util.ArrayList;


public class SyncService extends IntentService implements SyncView {
    public final static int CHAT_LIST = 0;
    public final static int CHAT_USERS = 1;

    private SyncPresenter mPresenter;

    public SyncService() {
        super("SyncService");
    }

    public static void startSyncService(Context context, Bundle syncBundle) {
        Intent intent = new Intent(context, SyncService.class);
        intent.putExtra(Extras.CHAT_SYNC_BUNDLE, syncBundle);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            Bundle syncBundle = intent.getBundleExtra(Extras.CHAT_SYNC_BUNDLE);
            Log.d("Sync", "onHandleIntent");
            switch (syncBundle.getInt(Extras.CHAT_SYNC_TYPE, CHAT_LIST)) {
                case CHAT_LIST:
                    getPresenter().getMessagesFromServer();
                    //getPresenter().getInbox();
                    break;
                case CHAT_USERS:
                    String senderIds = syncBundle.getString(Extras.CHAT_SYNC_DATA);
                    if (!TextUtils.isEmpty(senderIds))
                        getPresenter().getChatUsers(senderIds);
                    break;
            }
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public MyApplication getApp() {
        return (MyApplication) getApplication();
    }

    @Override
    public ApplicationComponent getApplicationComponent() {
        return getApp().getApplicationComponent();
    }

    @Override
    public void updateUser(ArrayList<UserInfo> userInfos) {
        Log.d("Sync", "" + userInfos.size());
        UserHelper.insertUsers(getContext(), userInfos);
        getContext().getContentResolver().notifyChange(UserChats.CONTENT_URI, null);
        //getPresenter().getMessagesFromServer();
    }

    private SyncPresenter getPresenter() {
        if (mPresenter == null || mPresenter.getView() == null) {
            mPresenter = new SyncPresenter();
            mPresenter.attachView(this);
        }
        return mPresenter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPresenter().detachView();
        Log.d("Sync", "onDestroy");
    }
}
