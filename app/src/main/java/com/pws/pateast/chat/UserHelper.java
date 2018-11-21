package com.pws.pateast.chat;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;

import com.pws.pateast.api.model.UserInfo;
import com.pws.pateast.provider.DatabaseContract;
import com.pws.pateast.provider.table.Users;

import java.util.ArrayList;

/**
 * Created by intel on 25-Jul-17.
 */

public class UserHelper {

    public static void insertUser(Context context, UserInfo userInfo) {
        ArrayList<UserInfo> userInfos = new ArrayList<>();
        userInfos.add(userInfo);
        insertUsers(context,userInfos);
    }

    public static void insertUsers(Context context, ArrayList<UserInfo> userInfos) {
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();
        for (UserInfo userInfo : userInfos) {
            if (isUserExist(context, userInfo)) {
                operations.add(updateOperation(userInfo));
            } else {
                operations.add(insertOperation(userInfo));
            }
        }
        try {
            context.getContentResolver().applyBatch(DatabaseContract.AUTHORITY,operations);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
    }

    private static ContentProviderOperation insertOperation(UserInfo userInfo)
    {
        return ContentProviderOperation.newInsert(Users.CONTENT_URI)
                .withValues(getContentValues(userInfo))
                .withYieldAllowed(true)
                .build();
    }

    private static ContentProviderOperation updateOperation(UserInfo userInfo)
    {
        return ContentProviderOperation.newUpdate(Users.CONTENT_URI)
                .withSelection( Users.COLUMN_USER_ID + "=?",new String[]{String.valueOf(userInfo.getId())})
                .withValues(getContentValues(userInfo))
                .withYieldAllowed(true)
                .build();
    }

    private static ContentValues getContentValues(UserInfo userInfo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Users.COLUMN_USER_ID, userInfo.getId());
        contentValues.put(Users.COLUMN_USER_NAME, userInfo.getUserdetails().get(0).getFullname());
        contentValues.put(Users.COLUMN_USER_IMAGE, userInfo.getUser_image());
        contentValues.put(Users.COLUMN_USER_TYPE, userInfo.getUser_type());
        return contentValues;
    }

    private static boolean isUserExist(Context context, UserInfo userInfo) {
        boolean isExist = false;

        Uri CONTENT_URI = Uri.parse(Users.CONTENT_URI + "/" + userInfo.getId());
        Cursor cursorExist = context.getContentResolver().query(
                CONTENT_URI,
                Users.PROJECTION_USER_ID,
                null,
                null,
                null);
        if (cursorExist != null) {
            if (cursorExist.moveToFirst()) {
                isExist = true;
            }
            cursorExist.close();
        }

        return isExist;
    }
}
