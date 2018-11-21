package com.pws.pateast.provider.table;

import android.net.Uri;
import android.provider.BaseColumns;

import static com.pws.pateast.provider.DatabaseContract.AUTHORITY;

/**
 * Created by intel on 25-Jul-17.
 */

public interface Users extends BaseColumns
{
    String TABLE_NAME = "user";
    String BASE_PATH = "users";
    String ALIAS = "U";

    Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    String COLUMN_USER_ID = "user_id";
    String COLUMN_USER_NAME = "user_name";
    String COLUMN_USER_IMAGE = "user_image";
    String COLUMN_USER_TYPE = "user_type";

    int INDEX_COLUMN_ID = 0;
    int INDEX_COLUMN_USER_ID = 1;
    int INDEX_COLUMN_USER_NAME = 2;
    int INDEX_COLUMN_USER_IMAGE = 3;
    int INDEX_COLUMN_USER_TYPE  = 4;
    int INDEX_COLUMN_COUNT = 5;

    String[] PROJECTION =
            {
                    _ID,
                    COLUMN_USER_ID,
                    COLUMN_USER_NAME,
                    COLUMN_USER_IMAGE,
                    COLUMN_USER_TYPE,
            };

    String[] PROJECTION_USER_ID =
            {
                    COLUMN_USER_ID
            };


    String CREATE_TABLE_QUERY = "create table " + TABLE_NAME
            + " ( " + _ID + " INTEGER PRIMARY KEY, "
            + COLUMN_USER_ID + " INTEGER, "
            + COLUMN_USER_NAME + " VARCHAR, "
            + COLUMN_USER_IMAGE + " VARCHAR, "
            + COLUMN_USER_TYPE + " VARCHAR)";

    String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

}
