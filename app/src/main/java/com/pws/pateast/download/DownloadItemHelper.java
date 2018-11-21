package com.pws.pateast.download;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.pws.pateast.api.model.Assignment;
import com.pws.pateast.enums.DownloadItemType;
import com.pws.pateast.enums.DownloadingStatus;
import com.pws.pateast.fragment.assignment.teacher.add.AddAssignmentResponse;
import com.pws.pateast.provider.table.Downloads;

/**
 * Created by anshul on 14/2/17.
 */

public class DownloadItemHelper {


    private static final String TAG = DownloadItemHelper.class.getSimpleName();

    /**
     * This method returns the downloadable Item with the latest percent and downloading status
     *
     * @param context
     * @param
     * @return
     */
  /*public static DownloadableItem getItem(Context context, DownloadableItem downloadableItem) {
   *//* if (context == null || downloadableItem == null) {
      return downloadableItem;
    }
    String downloadingStatus = DownloadItemHelper.getDownloadStatus(context, downloadableItem.getId());
    int downloadPercent = DownloadItemHelper.getDownloadPercent(context, downloadableItem.getId());
    downloadableItem.setDownloadingStatus(DownloadingStatus.getValue(downloadingStatus));
    downloadableItem.setItemDownloadPercent(downloadPercent);
    return downloadableItem;*//*
  }*/
    public static void saveDownloadItem(Context context, int userId, Assignment downloadableItem) {
        saveDownloadItem(context, userId, downloadableItem, null);
    }

    public static void saveDownloadItem(Context context, int userId, AddAssignmentResponse downloadableItem, Uri fileUri) {
        saveDownloadItem(context, userId,
                new Assignment(downloadableItem.getId(),
                        downloadableItem.getDownloadId(),
                        DownloadingStatus.DOWNLOADED,
                        downloadableItem.getItemDownloadPercent(),
                        downloadableItem.getAssignment_file(),
                        downloadableItem.getAssignment_file_name()),
                fileUri);
    }

    public static void saveDownloadItem(Context context, int userId, Assignment downloadableItem, Uri fileUri) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Downloads.COLUMN_USER_ID, userId);
        contentValues.put(Downloads.COLUMN_ITEM_ID, downloadableItem.getId());
        contentValues.put(Downloads.COLUMN_ITEM_TYPE, DownloadItemType.ASSIGNMENT.getType());
        contentValues.put(Downloads.COLUMN_DOWNLOAD_ID, downloadableItem.getDownloadId());
        contentValues.put(Downloads.COLUMN_PERCENTAGE, downloadableItem.getItemDownloadPercent());
        if (!TextUtils.isEmpty(downloadableItem.getAssignment_file_name())) {
            contentValues.put(Downloads.COLUMN_FILE_NAME, downloadableItem.getAssignment_file_name());
        }
        if (!TextUtils.isEmpty(downloadableItem.getAssignment_file())) {
            contentValues.put(Downloads.COLUMN_FILE_URL, downloadableItem.getAssignment_file());
        }
        if (fileUri != null) {
            contentValues.put(Downloads.COLUMN_FILE_URI, fileUri.toString());
        }

        contentValues.put(Downloads.COLUMN_DOWNLOAD_STATUS, downloadableItem.getDownloadingStatus().getDownloadStatus());

        Uri CONTENT_URI = Uri.parse(Downloads.CONTENT_URI + "/" + downloadableItem.getId());
        Cursor cursorExist = context.getContentResolver().query(CONTENT_URI
                , Downloads.PROJECTION_ID,
                Downloads.COLUMN_ITEM_TYPE + "=?",
                new String[]{DownloadItemType.ASSIGNMENT.getType()}, null);

        boolean isExist = false;
        if (cursorExist != null) {
            if (cursorExist.moveToFirst()) {
                isExist = true;
                int id = cursorExist.getInt(0);
                context.getContentResolver().update(Downloads.CONTENT_URI, contentValues,
                        Downloads._ID + "=?",
                        new String[]{String.valueOf(id)});
            }
            cursorExist.close();
        }

        if (!isExist) {
            context.getContentResolver().insert(Downloads.CONTENT_URI, contentValues);
        }
    }

    public static void persistItemState(Context context, Assignment downloadableItem) {
        Uri CONTENT_URI = Uri.parse(Downloads.CONTENT_URI + "/" + downloadableItem.getId());
        ContentValues contentValues = new ContentValues();
        contentValues.put(Downloads.COLUMN_DOWNLOAD_STATUS, downloadableItem.getDownloadingStatus().getDownloadStatus());
        contentValues.put(Downloads.COLUMN_PERCENTAGE, downloadableItem.getItemDownloadPercent());
        context.getContentResolver().update(CONTENT_URI, contentValues,
                Downloads.COLUMN_ITEM_TYPE + "=?",
                new String[]{DownloadItemType.ASSIGNMENT.getType()});
    }

    public static void persistItemState(Context context, String status, long downloadId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Downloads.COLUMN_DOWNLOAD_STATUS, status);
        context.getContentResolver().update(Downloads.CONTENT_URI, contentValues,
                Downloads.COLUMN_DOWNLOAD_ID + "=? AND "
                        + Downloads.COLUMN_ITEM_TYPE + "=?",
                new String[]{String.valueOf(downloadId), DownloadItemType.ASSIGNMENT.getType()});
    }

    public static DownloadableResult getDownloadedResult(Context context, int itemId) {
        Uri CONTENT_URI = Uri.parse(Downloads.CONTENT_URI + "/" + itemId);
        Cursor cursor = context.getContentResolver().query(CONTENT_URI, Downloads.PROJECTION_DOWNLOADS,
                Downloads.COLUMN_ITEM_TYPE + "=?",
                new String[]{DownloadItemType.ASSIGNMENT.getType()}, null);
        DownloadableResult result = new DownloadableResult(0, DownloadingStatus.NOT_DOWNLOADED.getDownloadStatus());
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result.setId(cursor.getInt(0));
                result.setDownloadStatus(cursor.getString(1));
                result.setPercent(cursor.getInt(2));
                result.setFileName(cursor.getString(3));
                result.setFileUri(cursor.getString(4));
                result.setFileUrl(cursor.getString(5));
            }
            cursor.close();
        }
        return result;
    }

    public static String getDownloadStatus(Context context, String itemId) {
        Uri CONTENT_URI = Uri.parse(Downloads.CONTENT_URI + "/" + itemId);
        Cursor cursor = context.getContentResolver().query(CONTENT_URI, Downloads.PROJECTION_DOWNLOAD_STATUS,
                Downloads.COLUMN_ITEM_TYPE + "=?",
                new String[]{DownloadItemType.ASSIGNMENT.getType()}, null);
        String status = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                status = cursor.getString(0);
            }
            cursor.close();
        }
        return status;
    }

    public static int getDownloadPercent(Context context, int itemId) {
        Uri CONTENT_URI = Uri.parse(Downloads.CONTENT_URI + "/" + itemId);
        Cursor cursor = context.getContentResolver().query(CONTENT_URI, Downloads.PROJECTION_DOWNLOAD_PERCENTAGE,
                Downloads.COLUMN_ITEM_TYPE + "=?",
                new String[]{DownloadItemType.ASSIGNMENT.getType()}, null);
        int percentage = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                percentage = cursor.getInt(0);
            }
            cursor.close();
        }
        return percentage;
    }

    public static Uri getDownloadedUri(Context context, int itemId) {
        Cursor cursor = context.getContentResolver().query(Downloads.CONTENT_URI,
                Downloads.PROJECTION_FILE_URI,
                Downloads.COLUMN_ITEM_TYPE + "=? AND "
                        + Downloads.COLUMN_ITEM_ID + "=? ",
                new String[]{DownloadItemType.ASSIGNMENT.getType(), String.valueOf(itemId)}, null);
        Uri uri = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String fileUri = cursor.getString(0);
                try {
                    uri = Uri.parse(fileUri);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
            cursor.close();
        }
        return uri;
    }

    public static boolean deleteDownloadedFile(Context context, int itemId) {
        Uri CONTENT_URI = Uri.parse(Downloads.CONTENT_URI + "/" + itemId);
        int deleted = context.getContentResolver().delete(CONTENT_URI,
                Downloads.COLUMN_ITEM_TYPE + "=?",
                new String[]{DownloadItemType.ASSIGNMENT.getType()});
        return deleted == 1;
    }

    public static void updateAssignmentDownloadStatus(Context context) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Downloads.COLUMN_DOWNLOAD_STATUS, DownloadingStatus.NOT_DOWNLOADED.getDownloadStatus());
        int updated = context.getContentResolver().update(Downloads.CONTENT_URI,
                contentValues,
                Downloads.COLUMN_ITEM_TYPE + "=? AND (" +
                        Downloads.COLUMN_DOWNLOAD_STATUS + "=? OR " +
                        Downloads.COLUMN_DOWNLOAD_STATUS + "=?) ",
                new String[]{DownloadItemType.ASSIGNMENT.getType(),
                        DownloadingStatus.IN_PROGRESS.getDownloadStatus(),
                        DownloadingStatus.WAITING.getDownloadStatus()});
    }
}
