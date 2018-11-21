package com.pws.pateast.provider.table;

import android.net.Uri;
import android.provider.BaseColumns;

import static com.pws.pateast.provider.DatabaseContract.AUTHORITY;

/**
 * Created by intel on 26-Jul-17.
 */

public interface UserChats extends BaseColumns
{
    String TABLE_NAME = "user_chat";
    String BASE_PATH = "user_chats";
    String ALIAS = "UC";

    Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    String COLUMN_SENDER_ID = "sender_id";
    String COLUMN_RECEIVER_ID = "receiver_id";
    String COLUMN_MESSAGE_ID = "message_id";

    int INDEX_COLUMN_SENDER_ID = 0;
    int INDEX_COLUMN_RECEIVER_ID = 1;
    int INDEX_COLUMN_MESSAGE_ID = 2;
    int INDEX_COLUMN_MESSAGE= 3;
    int INDEX_COLUMN_MESSAGE_STATUS= 4;
    int INDEX_COLUMN_MESSAGE_TYPE= 5;
    int INDEX_COLUMN_CREATE_DATE = 6;
    int INDEX_COLUMN_UPDATE_DATE = 7;
    int INDEX_COLUMN_USER_ID = 8;
    int INDEX_COLUMN_USER_NAME = 9;
    int INDEX_COLUMN_USER_IMAGE = 10;
    int INDEX_COLUMN_USER_TYPE  = 11;
    int INDEX_COLUMN_ID = 12;
    int INDEX_COLUMN_COUNT= 13;

    String[] PROJECTION =
            {
                    ALIAS + "." + COLUMN_SENDER_ID,
                    ALIAS + "." + COLUMN_RECEIVER_ID,
                    ALIAS + "." + COLUMN_MESSAGE_ID,
                    Chat.ALIAS + "." + Chat.COLUMN_MESSAGE,
                    Chat.ALIAS + "." + Chat.COLUMN_MESSAGE_STATUS,
                    Chat.ALIAS + "." + Chat.COLUMN_MESSAGE_TYPE,
                    Chat.ALIAS + "." + Chat.COLUMN_CREATE_DATE,
                    Chat.ALIAS + "." + Chat.COLUMN_UPDATE_DATE,
                    Users.ALIAS + "." + Users.COLUMN_USER_ID,
                    Users.ALIAS + "." + Users. COLUMN_USER_NAME,
                    Users.ALIAS + "." + Users.COLUMN_USER_IMAGE,
                    Users.ALIAS + "." + Users.COLUMN_USER_TYPE,
                    Users.ALIAS + "." + Users._ID
            };

    String[] PROJECTION_ID =
            {
                    COLUMN_SENDER_ID
            };


    String CREATE_TABLE_QUERY = "create table " + TABLE_NAME
            + " ( " + COLUMN_SENDER_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_RECEIVER_ID + " INTEGER, "
            + COLUMN_MESSAGE_ID + " INTEGER)";

    String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
}
