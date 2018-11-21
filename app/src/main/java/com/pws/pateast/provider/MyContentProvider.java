package com.pws.pateast.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.pws.pateast.api.model.User;
import com.pws.pateast.chat.UserChatHelper;
import com.pws.pateast.provider.table.Chat;
import com.pws.pateast.provider.table.Downloads;
import com.pws.pateast.provider.table.UserChats;
import com.pws.pateast.provider.table.Users;

public class MyContentProvider extends ContentProvider {
    SQLiteHelper sqLiteHelper;
    // used for the UriMacher
    private static final int DOWNLOAD = 1;
    private static final int DOWNLOAD_ITEM_ID = 2;
    private static final int USER = 3;
    private static final int USER_ID = 4;
    private static final int USER_CHAT = 5;
    private static final int USER_CHAT_SENDER_ID = 6;
    private static final int CHAT = 7;
    private static final int CHAT_ID = 8;
    private static final int CHAT_MESSAGE_ID = 9;


    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(DatabaseContract.AUTHORITY, Downloads.BASE_PATH, DOWNLOAD);
        sURIMatcher.addURI(DatabaseContract.AUTHORITY, Downloads.BASE_PATH + "/#", DOWNLOAD_ITEM_ID);
        sURIMatcher.addURI(DatabaseContract.AUTHORITY, Users.BASE_PATH, USER);
        sURIMatcher.addURI(DatabaseContract.AUTHORITY, Users.BASE_PATH + "/#", USER_ID);
        sURIMatcher.addURI(DatabaseContract.AUTHORITY, UserChats.BASE_PATH, USER_CHAT);
        sURIMatcher.addURI(DatabaseContract.AUTHORITY, UserChats.BASE_PATH + "/#", USER_CHAT_SENDER_ID);
        sURIMatcher.addURI(DatabaseContract.AUTHORITY, Chat.BASE_PATH, CHAT);
        sURIMatcher.addURI(DatabaseContract.AUTHORITY, Chat.BASE_PATH + "/#", CHAT_ID);
        sURIMatcher.addURI(DatabaseContract.AUTHORITY, Chat.BASE_PATH + "/" + Chat.COLUMN_MESSAGE_ID + "/#", CHAT_MESSAGE_ID);
    }

    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = sqLiteHelper.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case DOWNLOAD:
                rowsDeleted = sqlDB.delete(Downloads.TABLE_NAME, selection,
                        selectionArgs);
                break;
            case DOWNLOAD_ITEM_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(
                            Downloads.TABLE_NAME,
                            Downloads.COLUMN_ITEM_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(
                            Downloads.TABLE_NAME,
                            Downloads.COLUMN_ITEM_ID + "=" + id
                                    + " AND " + selection,
                            selectionArgs);
                }
                break;
            case USER:
                rowsDeleted = sqlDB.delete(Users.TABLE_NAME, selection,
                        selectionArgs);
                break;
            case CHAT:
                rowsDeleted = sqlDB.delete(Chat.TABLE_NAME, selection,
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = sqLiteHelper.getWritableDatabase();
        Uri returnUri = null;
        switch (uriType) {
            case DOWNLOAD:
                long id = sqlDB.insert(Downloads.TABLE_NAME, null, values);
                returnUri = Uri.parse(Downloads.BASE_PATH + "/" + id);
                break;
            case USER:
                id = sqlDB.insert(Users.TABLE_NAME, null, values);
                returnUri = Uri.parse(Users.BASE_PATH + "/" + id);
                break;
            case USER_CHAT:
                id = sqlDB.insert(UserChats.TABLE_NAME, null, values);
                returnUri = Uri.parse(UserChats.BASE_PATH + "/" + id);
                break;
            case CHAT:
                id = sqlDB.insert(Chat.TABLE_NAME, null, values);
                returnUri = ContentUris.withAppendedId(Chat.CONTENT_URI, id);
                returnUri = ContentUris.withAppendedId(returnUri,values.getAsInteger(Chat.COLUMN_SENDER_ID));
                returnUri = ContentUris.withAppendedId(returnUri,values.getAsInteger(Chat.COLUMN_RECEIVER_ID));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public boolean onCreate() {
        sqLiteHelper =  SQLiteHelper.getSqLiteHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // Uisng SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        // check if the caller has requested a column which does not exists
        // Set the table
        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case DOWNLOAD:
                queryBuilder.setTables(Downloads.TABLE_NAME);
                break;
            case DOWNLOAD_ITEM_ID:
                // adding the ID to the original query
                queryBuilder.setTables(Downloads.TABLE_NAME);
                queryBuilder.appendWhere(Downloads.COLUMN_ITEM_ID + "="
                        + uri.getLastPathSegment());
                break;
            case USER:
                break;
            case USER_ID:
                queryBuilder.setTables(Users.TABLE_NAME);
                queryBuilder.appendWhere(Users.COLUMN_USER_ID + "="
                        + uri.getLastPathSegment());
                break;
            case USER_CHAT:
                String table = UserChats.TABLE_NAME +" " +UserChats.ALIAS+" " +
                        "LEFT JOIN "+Users.TABLE_NAME +" "+ Users.ALIAS +" ON " +
                        UserChats.ALIAS + "." + UserChats.COLUMN_SENDER_ID+" = "+Users.ALIAS + "." + Users.COLUMN_USER_ID+" " +
                        "LEFT JOIN "+Chat.TABLE_NAME +" "+ Chat.ALIAS +" ON " +
                        UserChats.ALIAS + "." + UserChats.COLUMN_MESSAGE_ID+" = "+Chat.ALIAS + "." + Chat._ID;
                queryBuilder.setTables(table);
                break;
            case USER_CHAT_SENDER_ID:
                queryBuilder.setTables(UserChats.TABLE_NAME);
                queryBuilder.appendWhere(UserChats.COLUMN_SENDER_ID + "="
                        + uri.getLastPathSegment());
                break;
            case CHAT:
                queryBuilder.setTables(Chat.TABLE_NAME);
                break;
            case CHAT_ID:
                queryBuilder.setTables(Chat.TABLE_NAME);
                queryBuilder.appendWhere(Chat._ID + "="
                        + uri.getLastPathSegment());
                break;
            case CHAT_MESSAGE_ID:
                queryBuilder.setTables(Chat.TABLE_NAME);
                queryBuilder.appendWhere(Chat.COLUMN_MESSAGE_ID + "="
                        + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }


        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = sqLiteHelper.getWritableDatabase();
        int rowsUpdated = 0;
        switch (uriType) {
            case DOWNLOAD:
                rowsUpdated = sqlDB.update(Downloads.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            case DOWNLOAD_ITEM_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(Downloads.TABLE_NAME,
                            values,
                            Downloads.COLUMN_ITEM_ID + "=?" ,
                            new String[]{id});
                } else {
                    rowsUpdated = sqlDB.update(Downloads.TABLE_NAME,
                            values,
                            Downloads.COLUMN_ITEM_ID + "=" + id
                                    + " AND "
                                    + selection,
                            selectionArgs);
                }
                break;
            case USER:
                rowsUpdated = sqlDB.update(Users.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            case USER_CHAT:
                rowsUpdated = sqlDB.update(UserChats.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            case CHAT:
                rowsUpdated = sqlDB.update(Chat.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            case CHAT_ID:
                rowsUpdated = sqlDB.update(Chat.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

}
