package com.pws.pateast.fragment.assignment;

import android.app.DownloadManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.pws.pateast.MyApplication;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.model.Assignment;
import com.pws.pateast.enums.DownloadingStatus;
import com.pws.pateast.download.ItemDownloadPercentObserver;
import com.pws.pateast.utils.Preference;
import com.pws.pateast.Constants;
import com.pws.pateast.api.model.User;
import com.pws.pateast.download.DownloadItemHelper;
import com.pws.pateast.download.DownloadRequestsSubscriber;
import com.pws.pateast.download.ItemDownloadCallback;
import com.pws.pateast.download.ItemPercentCallback;
import com.pws.pateast.download.RxDownloadManagerHelper;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.Queue;

import javax.inject.Inject;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

/**
 * Created by planet on 5/16/2017.
 */

public class AssignmentDownloadService extends Service implements ItemDownloadCallback<Assignment>,
        ItemPercentCallback<Assignment> {

    public static final String EXTRA_DATA = "extra_data_assignment";
    public static final String EXTRA_DOWNLOAD_STATUS = "extra_download_status";
    private static final String TAG = AssignmentDownloadService.class.getSimpleName();
    @Inject
    protected Preference preference;

    protected User user;
    private ItemDownloadPercentObserver<Assignment> mItemDownloadPercentObserver;
    private DownloadRequestsSubscriber<Assignment> mDownloadRequestsSubscriber;
    private WeakReference<Context> weakReference;
    private DownloadManager downloadManager;
    private int currentDownloadsCount = 0;

    private Queue<Assignment> mQueue;
    private Assignment mCurrentItem;

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {

        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            Assignment downloadableItem = msg.getData().getParcelable(EXTRA_DATA);
            Log.w(TAG, "count " + mQueue.size());
            mQueue.add(downloadableItem);
            if (mCurrentItem == null) {
                mCurrentItem = mQueue.poll();
                startDownload(mCurrentItem);
            } else {
                if (downloadableItem != null) {
                    downloadableItem.setDownloadStatus(DownloadManager.STATUS_PAUSED);
                    downloadableItem.setDownloadingStatus(DownloadingStatus.WAITING);
                }
                DownloadItemHelper.saveDownloadItem(weakReference.get(), user.getId(), downloadableItem);
                updateDownloadableItem(downloadableItem);
            }
        }
    }

    private void startDownload(Assignment downloadableItem) {
        if (downloadableItem != null) {
            if (downloadableItem.getDownloadingStatus() == DownloadingStatus.IN_PROGRESS) {
                getDownloadPercentage(downloadableItem);
                Log.i(TAG, "download progress");
            } else {
                onDownloadEnqueued(downloadableItem);
                Log.i(TAG, "download start");
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.get(this).getApplicationComponent().inject(this);
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        weakReference = new WeakReference(this);
        //Observable for percent of individual downloads.
        mItemDownloadPercentObserver = new ItemDownloadPercentObserver(this);
        //Observable for download request
        mDownloadRequestsSubscriber = new DownloadRequestsSubscriber(this);
        user = preference.getUser();

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
        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job

        Bundle data = new Bundle();

        if (intent.getExtras() != null) {
            data.putParcelable(EXTRA_DATA, intent.getParcelableExtra(EXTRA_DATA));
        }

        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        msg.setData(data);
        mServiceHandler.sendMessage(msg);

        // If we get killed, after returning from here, restart
        return START_REDELIVER_INTENT;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDownloadEnqueued(Assignment downloadableItem) {
        Log.i(TAG, "onDownloadEnqueued");
        mDownloadRequestsSubscriber.emitNextItem(downloadableItem);
        updateDownloadableItem(downloadableItem);
    }

    @Override
    public void onDownloadStarted(Assignment downloadableItem) {
        //Increment the current number of downloads by 1
        currentDownloadsCount++;
        Log.i(TAG, "onDownloadStarted - currentDownloadsCount " + currentDownloadsCount);
        String downloadUrl = ServiceBuilder.IMAGE_URL + downloadableItem.getAssignment_file();
        String destFileName = downloadableItem.getAssignment_file_name();
        Uri destUri = RxDownloadManagerHelper.getDestinationUri(Constants.ASSIGNMENT_FILES_DIR, destFileName);
        long downloadId = RxDownloadManagerHelper.enqueueDownload(downloadManager, downloadUrl, destUri);
        if (downloadId == DownloadRequestsSubscriber.INVALID_ID) {
            Log.i(TAG, "INVALID_DOWNLOAD_ID");
            return;
        }
        Log.w(TAG, "downloadId : " + downloadId);
        downloadableItem.setDownloadId(downloadId);
        downloadableItem.setDownloadingStatus(DownloadingStatus.IN_PROGRESS);
        DownloadItemHelper.saveDownloadItem(weakReference.get(), user.getId(), downloadableItem, destUri);
        updateDownloadableItem(downloadableItem);
        RxDownloadManagerHelper.queryDownloadPercents(downloadManager, downloadableItem, mItemDownloadPercentObserver.getPercentageObservableEmitter());
        Log.i(TAG, "queryDownloadPercents");
    }


    public void getDownloadPercentage(Assignment downloadableItem) {
        RxDownloadManagerHelper.queryDownloadPercents(downloadManager, downloadableItem, mItemDownloadPercentObserver.getPercentageObservableEmitter());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mItemDownloadPercentObserver.performCleanUp();
        mDownloadRequestsSubscriber.performCleanUp();
        DownloadItemHelper.updateAssignmentDownloadStatus(weakReference.get());
        Log.w(TAG, "onDestroy");
    }

    @Override
    public void onDownloadComplete() {
        //Decrement the current number of downloads by 1
        currentDownloadsCount--;
        Log.i(TAG, "onDownloadComplete - currentDownloadsCount " + currentDownloadsCount);
        mDownloadRequestsSubscriber.requestDownload(currentDownloadsCount);
    }

    @Override
    public void onError() {
        if (mCurrentItem != null) {
            mCurrentItem.setDownloadingStatus(DownloadingStatus.NOT_DOWNLOADED);
            mCurrentItem.setItemDownloadPercent(0);
            DownloadItemHelper.persistItemState(weakReference.get(), mCurrentItem);
            Intent intent = new Intent(Constants.DOWNLOAD_BROADCAST_ACTION);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
        proceedNextItem();
    }

    private void proceedNextItem() {
        if (mQueue.size() != 0) {
            mServiceHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startDownload(mCurrentItem = mQueue.poll());
                }
            }, 1000);
        } else {
            mCurrentItem = null;
            stopSelf();
        }
    }

    @Override
    public void updateDownloadableItem(Assignment downloadableItem) {
        if (downloadableItem == null && weakReference.get() != null) {
            return;
        }

        //Log.w(TAG, downloadableItem.getDownloadingStatus() + "");
        Intent intent = new Intent(Constants.DOWNLOAD_BROADCAST_ACTION);

        //Log.w(TAG, "status - " + downloadableItem.getDownloadStatus());
        switch (downloadableItem.getDownloadStatus()) {
            case DownloadManager.STATUS_FAILED:
            case DownloadManager.ERROR_FILE_ERROR:
                Log.i(TAG, "failed to download");
                DownloadItemHelper.persistItemState(weakReference.get(), DownloadingStatus.NOT_DOWNLOADED.getDownloadStatus(),
                        downloadableItem.getDownloadId());
                proceedNextItem();
                break;
            case DownloadManager.STATUS_PAUSED:
                if (downloadableItem.getDownloadId() != 0) {
                    Log.i(TAG, "paused " + downloadableItem.getDownloadId());
                    mCurrentItem.setDownloadId(downloadableItem.getDownloadId());
                    downloadableItem.setDownloadingStatus(DownloadingStatus.WAITING);
                    DownloadItemHelper.persistItemState(weakReference.get(), downloadableItem);
                    mServiceHandler.postDelayed(mRunnable, 1000 * 30); // wait for 30 seconds and cancel downloading if not start after that time
                }
                break;
            case DownloadManager.STATUS_SUCCESSFUL:
                intent.putExtra(EXTRA_DOWNLOAD_STATUS, DownloadManager.STATUS_SUCCESSFUL);
                DownloadItemHelper.persistItemState(weakReference.get(),
                        DownloadingStatus.DOWNLOADED.getDownloadStatus(),
                        downloadableItem.getDownloadId());
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                onDownloadComplete();
                Log.i(TAG, "download completed " + downloadableItem.getDownloadId());
                //Log.i(TAG, "size " + mQueue.size());
                proceedNextItem();
                break;
            case DownloadManager.STATUS_RUNNING:
            default:
                mServiceHandler.removeCallbacks(mRunnable);
                Log.w(TAG, "running =" + downloadableItem.getItemDownloadPercent() + "=" + downloadableItem.getId());
                downloadableItem.setDownloadingStatus(DownloadingStatus.IN_PROGRESS);
                DownloadItemHelper.persistItemState(weakReference.get(), downloadableItem);
                break;
        }

        intent.putExtra(EXTRA_DATA, downloadableItem);
        intent.putExtra(EXTRA_DOWNLOAD_STATUS, downloadableItem.getDownloadStatus());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            // network not found please try again and cancel all
            Toast.makeText(weakReference.get(), "Whoops! Error Occur, Try Again.", Toast.LENGTH_SHORT).show();
            clearQueueAndResetDownloading();
        }
    };

    /**
     * clear queue and reset downloading ia paused for a long time
     */
    private void clearQueueAndResetDownloading() {
        downloadManager.remove(mCurrentItem.getDownloadId()); // remove current downloading
        DownloadItemHelper.updateAssignmentDownloadStatus(weakReference.get());
        Intent intent = new Intent(Constants.DOWNLOAD_BROADCAST_ACTION);
        intent.putExtra(EXTRA_DOWNLOAD_STATUS, DownloadManager.STATUS_FAILED);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        mQueue.clear();
        mCurrentItem = null;
        stopSelf();
    }
}
