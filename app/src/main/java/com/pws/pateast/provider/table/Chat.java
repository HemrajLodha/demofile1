package com.pws.pateast.provider.table;

import android.net.Uri;
import android.provider.BaseColumns;

import static com.pws.pateast.provider.DatabaseContract.AUTHORITY;

/**
 * Created by intel on 28-Jul-17.
 */

public interface Chat extends BaseColumns
{
    String TABLE_NAME = "chat";
    String BASE_PATH = "chats";
    String ALIAS = "C";

    Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    String COLUMN_SENDER_ID = "sender_id";
    String COLUMN_RECEIVER_ID = "receiver_id";
    String COLUMN_MESSAGE_ID = "message_id";
    String COLUMN_MESSAGE = "message";
    String COLUMN_URL = "url";
    String COLUMN_URI = "uri";
    String COLUMN_MESSAGE_TYPE = "message_type";
    String COLUMN_MESSAGE_STATUS = "message_status";
    String COLUMN_UPLOAD_STATUS = "upload_status";
    String COLUMN_UPLOAD_PERCENTAGE = "upload_percentage";
    String COLUMN_CREATE_DATE = "created_at";
    String COLUMN_UPDATE_DATE = "updated_at";

    int INDEX_COLUMN_ID = 0;
    int INDEX_COLUMN_SENDER_ID = 1;
    int INDEX_COLUMN_RECEIVER_ID = 2;
    int INDEX_COLUMN_MESSAGE_ID = 3;
    int INDEX_COLUMN_MESSAGE = 4;
    int INDEX_COLUMN_URL = 5;
    int INDEX_COLUMN_URI = 6;
    int INDEX_COLUMN_MESSAGE_TYPE = 7;
    int INDEX_COLUMN_MESSAGE_STATUS = 8;
    int INDEX_COLUMN_UPLOAD_STATUS = 9;
    int INDEX_COLUMN_UPLOAD_PERCENTAGE = 10;
    int INDEX_COLUMN_CREATE_DATE = 11;
    int INDEX_COLUMN_UPDATE_DATE  = 12;

    String[] PROJECTION =
            {
                    _ID,
                    COLUMN_SENDER_ID,
                    COLUMN_RECEIVER_ID,
                    COLUMN_MESSAGE_ID,
                    COLUMN_MESSAGE,
                    COLUMN_URL,
                    COLUMN_URI,
                    COLUMN_MESSAGE_TYPE,
                    COLUMN_MESSAGE_STATUS,
                    COLUMN_UPLOAD_STATUS ,
                    COLUMN_UPLOAD_PERCENTAGE,
                    COLUMN_CREATE_DATE,
                    COLUMN_UPDATE_DATE

            };

    String[] PROJECTION_ID =
            {
                    _ID
            };

    String[] PROJECTION_COUNT =
            {
                    "count(" + _ID + ")",
            };

    String CREATE_TABLE_QUERY = "create table " + TABLE_NAME
            + " ( " + _ID + " INTEGER PRIMARY KEY, "
            + COLUMN_SENDER_ID + " INTEGER, "
            + COLUMN_RECEIVER_ID + " INTEGER, "
            + COLUMN_MESSAGE_ID + " INTEGER, "
            + COLUMN_MESSAGE + " TEXT, "
            + COLUMN_URL + " TEXT, "
            + COLUMN_URI + " TEXT, "
            + COLUMN_MESSAGE_TYPE + " INTEGER, "
            + COLUMN_MESSAGE_STATUS + " INTEGER, "
            + COLUMN_UPLOAD_STATUS + " TEXT, "
            + COLUMN_UPLOAD_PERCENTAGE + " INTEGER, "
            + COLUMN_CREATE_DATE + " TIMESTAMP , "
            + COLUMN_UPDATE_DATE + " TIMESTAMP)";

    String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

}
