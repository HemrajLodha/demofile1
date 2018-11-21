package com.pws.pateast.download;

import android.app.DownloadManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.pws.pateast.api.model.Assignment;
import com.pws.pateast.utils.FileUtils;

import io.reactivex.ObservableEmitter;

/**
 * Created by anshul on 6/2/17.
 */

public class RxDownloadManagerHelper {

    private final static int DOWNLOAD_QUERY_DELAY_PARAM = 200;
    public final static int DOWNLOAD_COMPLETE_PERCENT = 100;
    private final static int PERCENT_MULTIPLIER = 100;
    private final static int MIN_DOWNLOAD_PERCENT_DIFF = 3;
    private final static int INVALID_DOWNLOAD_ID = -1;
    private final static String TAG = RxDownloadManagerHelper.class.getSimpleName();

    /**
     * @param downloadManager - Android's Download Manager.
     * @param downloadUrl     - The url of the item to be downloaded.
     * @return - the download id of the download.
     */
    public static long enqueueDownload(DownloadManager downloadManager, String downloadUrl, Uri destUri) {
        if (downloadManager == null || downloadUrl == null || downloadUrl.equals("")) {
            return INVALID_DOWNLOAD_ID;
        }
        Uri uri = Uri.parse(downloadUrl);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setDestinationUri(destUri);
        return downloadManager.enqueue(request);
    }

    /**
     * @param downloadManager - Android's Download Manager.
     * @param downloadUrl     - The url of the item to be downloaded.
     * @return - the download id of the download.
     */
    public static long enqueueDownload(DownloadManager downloadManager, String downloadUrl, String directory, String fileName) {
        if (downloadManager == null || downloadUrl == null || downloadUrl.equals("")) {
            return INVALID_DOWNLOAD_ID;
        }
        Uri destUri = getDestinationUri(directory, fileName);
        Uri uri = Uri.parse(downloadUrl);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(fileName);
        request.setDescription(fileName);
        request.setDestinationUri(destUri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        return downloadManager.enqueue(request);
    }

    /**
     * @param downloadManager - Android's Download Manager.
     * @param downloadUrl     - The url of the item to be downloaded.
     * @param header          - The header to very request over https://.
     * @return - the download id of the download.
     */
    public static long enqueueDownload(DownloadManager downloadManager, String header, String downloadUrl, String directory, String fileName) {
        if (downloadManager == null || downloadUrl == null || downloadUrl.equals("")) {
            return INVALID_DOWNLOAD_ID;
        }
        Uri destUri = getDestinationUri(directory, fileName);
        Uri uri = Uri.parse(downloadUrl);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.addRequestHeader("Authorization", header);
        request.setTitle(fileName);
        request.setDescription(fileName);
        request.setDestinationUri(destUri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        return downloadManager.enqueue(request);
    }

    public static Uri getDestinationUri(String directory, String fileName) {
        return FileUtils.getUri(FileUtils.getOutputMediaFile(directory, fileName));
    }

    /**
     * This method will be called upon every 'x' milliseconds to know the percentage of a download.
     * This method will only emit the percent download, if the current percent download is 5%
     * greater than the previous percent download.
     *
     * @param downloadManager
     * @param downloadableItem
     * @param percentFlowableEmiitter
     */
    public static void queryDownloadPercents(final DownloadManager downloadManager,
                                             final Assignment downloadableItem,
                                             final ObservableEmitter percentFlowableEmiitter) {

        //If the emitter has been disposed, then return.
        if (downloadManager == null || downloadableItem == null || percentFlowableEmiitter == null
                || percentFlowableEmiitter.isDisposed()) {
            return;
        }

        long lastEmittedDownloadPercent = downloadableItem.getLastEmittedDownloadPercent();


        DownloadableResult downloadableResult = getDownloadResult(downloadManager, downloadableItem
                .getDownloadId());

        if (downloadableResult == null) {
            return;
        }

        //Get the current DownloadPercent and download status
        int currentDownloadPercent = downloadableResult.getPercent();
        int downloadStatus = downloadableResult.getStatus();
        downloadableItem.setDownloadStatus(downloadStatus);
        downloadableItem.setItemDownloadPercent(currentDownloadPercent);
        if ((currentDownloadPercent - lastEmittedDownloadPercent >= MIN_DOWNLOAD_PERCENT_DIFF) ||
                currentDownloadPercent == DOWNLOAD_COMPLETE_PERCENT) {
            percentFlowableEmiitter.onNext(downloadableItem);
            downloadableItem.setLastEmittedDownloadPercent(currentDownloadPercent);
        }
        Log.d(TAG,
                " Querying the DB: DownloadStatus is " + downloadStatus + " and downloadPercent is " +
                        "" + currentDownloadPercent);
        switch (downloadStatus) {
            case DownloadManager.STATUS_FAILED:
            case DownloadManager.STATUS_SUCCESSFUL:
            case DownloadManager.ERROR_FILE_ERROR:
                percentFlowableEmiitter.onNext(downloadableItem);
                break;
            case DownloadManager.STATUS_PAUSED:
            case DownloadManager.STATUS_PENDING:
            case DownloadManager.STATUS_RUNNING:
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        queryDownloadPercents(downloadManager, downloadableItem, percentFlowableEmiitter);
                    }
                }, DOWNLOAD_QUERY_DELAY_PARAM);
                break;
        }
    }

    /**
     * @param downloadManager - Android's DownloadManager
     * @param downloadId      - The downloadId for which the progress has to be fetched from the db.
     * @return
     */
    private static DownloadableResult getDownloadResult(DownloadManager downloadManager,
                                                        long downloadId) {
        //Create a query with downloadId as the filter.
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);

        //Create an instance of downloadable result
        DownloadableResult downloadableResult = new DownloadableResult();

        Cursor cursor = null;
        try {
            cursor = downloadManager.query(query);
            if (cursor == null || !cursor.moveToFirst()) {
                return downloadableResult;
            }
            //Get the download percent
            float bytesDownloaded =
                    cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
            float bytesTotal =
                    cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
            int downloadPercent = (int) ((bytesDownloaded / bytesTotal) * PERCENT_MULTIPLIER);
            if (downloadPercent <= DOWNLOAD_COMPLETE_PERCENT) {
                downloadableResult.setPercent(downloadPercent);
            }
            //Get the download status
            int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
            int downloadStatus = cursor.getInt(columnIndex);
            downloadableResult.setStatus(downloadStatus);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return downloadableResult;
    }


}
