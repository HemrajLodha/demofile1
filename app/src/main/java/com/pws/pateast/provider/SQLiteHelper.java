package com.pws.pateast.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.pws.pateast.api.model.User;
import com.pws.pateast.provider.table.Chat;
import com.pws.pateast.provider.table.Downloads;
import com.pws.pateast.provider.table.UserChats;
import com.pws.pateast.provider.table.Users;

/**
 * Created by pws on 2/2/2017.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    private Context context;

    private static SQLiteHelper sqLiteHelper;

    private SQLiteHelper(Context context) {
        super(context, DatabaseContract.DATABASE_NAME, null, DatabaseContract.DATABASE_VERSION);
        this.context = context;
    }

    public static SQLiteHelper getSqLiteHelper(Context context)
    {
        if(sqLiteHelper == null)
            sqLiteHelper = new SQLiteHelper(context);
        return sqLiteHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Downloads.CREATE_TABLE_QUERY);
        db.execSQL(Users.CREATE_TABLE_QUERY);
        db.execSQL(UserChats.CREATE_TABLE_QUERY);
        db.execSQL(Chat.CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                Log.i("onUpgrade", oldVersion + "");
            case 2:
                Log.i("onUpgrade", oldVersion + "");
        }
    }

    /**
     * delete database
     *
     * @param context
     */
    public static void deleteDatabase(Context context) {
        getSqLiteHelper(context).close();
        context.deleteDatabase(DatabaseContract.DATABASE_NAME);
    }
}

