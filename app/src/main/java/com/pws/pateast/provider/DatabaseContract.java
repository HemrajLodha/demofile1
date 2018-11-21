package com.pws.pateast.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by pws-A on 3/29/2017.
 */

public interface DatabaseContract {

    int DATABASE_VERSION = 1;
    String DATABASE_NAME = "pateast.db";
    String AUTHORITY = "com.pws.pateast.provider";

}
