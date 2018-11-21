package com.pws.pateast.service.upload;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.pws.pateast.MyApplication;
import com.pws.pateast.api.model.Message;
import com.pws.pateast.base.Extras;
import com.pws.pateast.chat.ChatHelper;
import com.pws.pateast.di.component.ApplicationComponent;
import com.pws.pateast.enums.UploadingStatus;
import com.pws.pateast.events.UploadEvent;

import java.util.LinkedList;
import java.util.Queue;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;
import static com.pws.pateast.events.SocketEvent.EVENT_FILE_MESSAGE;


public class UploadService extends Service implements UploadView {
    private static final String TAG = UploadService.class.getSimpleName();

    private UploadPresenter mPresenter;
    private Queue<Message> mQueue;

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private Message mCurrentItem;

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {

        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(android.os.Message msg) {
            Message message = msg.getData().getParcelable(Extras.MESSAGE);
            mQueue.add(message);
            if (mCurrentItem == null)
                uploadStart(mCurrentItem = mQueue.poll());
        }
    }


    private UploadPresenter getPresenter() {
        return mPresenter;
    }

    public static void startUploadService(Context context, Message message) {
        Intent intent = new Intent(context, UploadService.class);
        intent.putExtra(Extras.MESSAGE, message);

        context.startService(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPresenter = new UploadPresenter();
        mPresenter.attachView(this);

        HandlerThread thread = new HandlerThread(TAG + "Args",
                THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
        mQueue = new LinkedList<>();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onHandleIntent");
        if (intent != null) {
            android.os.Message msg = mServiceHandler.obtainMessage();
            msg.arg1 = startId;
            msg.setData(intent.getExtras());
            mServiceHandler.sendMessage(msg);
        }
        return START_REDELIVER_INTENT;
    }

    @Override
    public void uploadStart(Message message) {
        if (message.getUrl() == null) {
            message.setUploadStatus(UploadingStatus.IN_PROGRESS.getUploadStatus());
            ChatHelper.persistUploadingStatus(getContext(), message);
            getPresenter().onUploadEnqueued(message);
        } else {
            message.setUploadStatus(UploadingStatus.UPLOADED.getUploadStatus());
            ChatHelper.persistUploadingStatus(getContext(), message);
            getPresenter().onEvent(new UploadEvent(EVENT_FILE_MESSAGE, message));
            uploadNext();
        }
    }

    @Override
    public void uploadNext() {
        if (mQueue.size() != 0) {
            mServiceHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    uploadStart(mCurrentItem = mQueue.poll());
                }
            }, 1000);
        } else {
            mCurrentItem = null;
            stopSelf();
        }
    }

    @Override
    public void uploading(long bytesWritten, long contentLength) {

    }

    @Override
    public void uploadSuccess(Message message) {
        message.setChatId(message.getUid());
        message.setUrl(message.getUrl());
        message.setUploadStatus(UploadingStatus.UPLOADED.getUploadStatus());
        ChatHelper.persistUploadingUrl(getContext(), message);
        getPresenter().onEvent(new UploadEvent(EVENT_FILE_MESSAGE, message));
    }

    @Override
    public void uploadFail(String error) {
        Log.d(TAG, error);
        if (mCurrentItem != null) {
            mCurrentItem.setUploadStatus(UploadingStatus.NOT_UPLOADED.getUploadStatus());
            ChatHelper.persistUploadingStatus(getContext(), mCurrentItem);
        }
        uploadNext();
    }

    @Override
    public void uploadComplete() {
        uploadNext();
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
    public void onDestroy() {
        super.onDestroy();
        mQueue.clear();
        mCurrentItem = null;
        if (getPresenter() != null)
            getPresenter().detachView();
        Log.d(TAG, "onDestroy");
    }


}

