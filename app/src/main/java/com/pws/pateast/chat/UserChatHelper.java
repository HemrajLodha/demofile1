package com.pws.pateast.chat;

import android.app.ProgressDialog;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;

import com.pws.pateast.provider.DatabaseContract;
import com.pws.pateast.provider.table.Chat;
import com.pws.pateast.provider.table.UserChats;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by intel on 26-Jul-17.
 */

public class UserChatHelper {

    public static void insertUsersChat(Context context, int senderId, int receiverId) {
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();
        if (!isUserChatExist(context, senderId)) {
            operations.add(insertOperation(senderId, receiverId, 0));
        }
        try {
            context.getContentResolver().applyBatch(DatabaseContract.AUTHORITY, operations);

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
    }

    public static void insertUsersChat(Context context, int user_id, ContentProviderResult[] results) {
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();
        ArrayList<Integer> senderIds = new ArrayList<>();
        if (results != null) {
            for (ContentProviderResult result : results) {
                if (result.uri != null) {
                    List<String> list = result.uri.getPathSegments();
                    int chatId = Integer.parseInt(list.get(1));
                    int senderId = Integer.parseInt(list.get(2));
                    int receiverId = Integer.parseInt(list.get(3));
                    senderId = user_id == senderId ? receiverId : senderId;
                    receiverId = user_id;
                    if (isUserChatExist(context, senderId) || senderIds.contains(senderId)) {
                        if (chatId != 0)
                            operations.add(updateOperation(senderId, chatId));
                    } else {
                        operations.add(insertOperation(senderId, receiverId, chatId));
                        senderIds.add(senderId);
                    }

                }
            }
        }


        try {
            context.getContentResolver().applyBatch(DatabaseContract.AUTHORITY, operations);

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
    }

    private static ContentProviderOperation insertOperation(int senderId, int receiverId, int chatId) {
        return ContentProviderOperation.newInsert(UserChats.CONTENT_URI)
                .withValues(getInsertContentValues(senderId, receiverId, chatId))
                .withYieldAllowed(true)
                .build();
    }

    private static ContentProviderOperation updateOperation(int senderId, int chatId) {
        return ContentProviderOperation.newUpdate(UserChats.CONTENT_URI)
                .withSelection(UserChats.COLUMN_SENDER_ID + "=?", new String[]{String.valueOf(senderId)})
                .withValues(getUpdateContentValues(chatId))
                .withYieldAllowed(true)
                .build();
    }

    private static ContentValues getInsertContentValues(int senderId, int receiverId, int chatId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserChats.COLUMN_SENDER_ID, senderId);
        contentValues.put(UserChats.COLUMN_RECEIVER_ID, receiverId);
        contentValues.put(UserChats.COLUMN_MESSAGE_ID, chatId);
        return contentValues;
    }

    private static ContentValues getUpdateContentValues(int chat_id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserChats.COLUMN_MESSAGE_ID, chat_id);
        return contentValues;
    }

    private static boolean isUserChatExist(Context context, int sender_id) {
        boolean isExist = false;
        Uri CONTENT_URI = Uri.parse(UserChats.CONTENT_URI + "/" + sender_id);
        Cursor cursorExist = context.getContentResolver().query(
                CONTENT_URI,
                UserChats.PROJECTION_ID,
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

    public static CursorLoader getUserChats(Context context) {
        return new CursorLoader(context, UserChats.CONTENT_URI, UserChats.PROJECTION, null, null, Chat.ALIAS + "." + Chat.COLUMN_UPDATE_DATE + " DESC");
    }


}
