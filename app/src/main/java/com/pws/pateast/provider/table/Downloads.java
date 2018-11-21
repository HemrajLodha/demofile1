package com.pws.pateast.provider.table;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import static com.pws.pateast.provider.DatabaseContract.AUTHORITY;

/**
 * Created by intel on 25-Jul-17.
 */

public interface Downloads extends BaseColumns
{
    String TABLE_NAME = "download";
    String COLUMN_USER_ID = "user_id";
    String COLUMN_ITEM_ID = "item_id";
    String COLUMN_ITEM_TYPE = "item_type";
    String COLUMN_DOWNLOAD_ID = "download_id";
    String COLUMN_DOWNLOAD_STATUS = "download_status";
    String COLUMN_PERCENTAGE = "percentage";
    String COLUMN_FILE_NAME = "file_name";
    String COLUMN_FILE_URI = "file_uri";
    String COLUMN_FILE_URL = "file_url";
    String COLUMN_CREATE_DATE = "created_at";

    int INDEX_COLUMN_ID = 0;
    int INDEX_COLUMN_USER_ID = 1;
    int INDEX_COLUMN_ASSIGNMENT_ID = 2;
    int INDEX_COLUMN_DOWNLOAD_ID = 3;
    int INDEX_COLUMN_DOWNLOAD_STATUS = 4;
    int INDEX_COLUMN_PERCENTAGE = 5;
    int INDEX_COLUMN_FILE_NAME = 6;
    int INDEX_COLUMN_FILE_URI = 7;
    int INDEX_COLUMN_FILE_URL = 8;
    int INDEX_COLUMN_CREATE_DATE = 9;

    String[] PROJECTION =
            {
                    _ID,
                    COLUMN_USER_ID,
                    COLUMN_ITEM_ID,
                    COLUMN_ITEM_TYPE,
                    COLUMN_DOWNLOAD_ID,
                    COLUMN_DOWNLOAD_STATUS,
                    COLUMN_PERCENTAGE,
                    COLUMN_FILE_NAME,
                    COLUMN_FILE_URI,
                    COLUMN_FILE_URL,
                    COLUMN_CREATE_DATE,
            };

    String[] PROJECTION_ID =
            {
                    _ID
            };

    String[] PROJECTION_DOWNLOADS =
            {
                    COLUMN_DOWNLOAD_ID,
                    COLUMN_DOWNLOAD_STATUS,
                    COLUMN_PERCENTAGE,
                    COLUMN_FILE_NAME,
                    COLUMN_FILE_URI,
                    COLUMN_FILE_URL
            };

    String[] PROJECTION_DOWNLOAD_STATUS =
            {
                    COLUMN_DOWNLOAD_STATUS
            };

    String[] PROJECTION_DOWNLOAD_PERCENTAGE =
            {
                    COLUMN_PERCENTAGE
            };

    String[] PROJECTION_FILE_URI =
            {
                    COLUMN_FILE_URI
            };

    String BASE_PATH = "downloads";
    Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH);
    String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/" + BASE_PATH;
    String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/" + TABLE_NAME;

    String CREATE_TABLE_QUERY = "create table " + TABLE_NAME
            + " ( " + _ID + " INTEGER PRIMARY KEY, "
            + COLUMN_USER_ID + " INTEGER, "
            + COLUMN_ITEM_ID + " INTEGER, "
            + COLUMN_ITEM_TYPE + " VARCHAR, "
            + COLUMN_DOWNLOAD_ID + " INTEGER, "
            + COLUMN_DOWNLOAD_STATUS + " VARCHAR, "
            + COLUMN_PERCENTAGE + " INTEGER, "
            + COLUMN_FILE_NAME + " VARCHAR, "
            + COLUMN_FILE_URI + " VARCHAR, "
            + COLUMN_FILE_URL + " VARCHAR, "
            + COLUMN_CREATE_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP)";

    String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

}
