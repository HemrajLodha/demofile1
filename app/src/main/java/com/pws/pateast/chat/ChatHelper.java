package com.pws.pateast.chat;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;

import com.pws.pateast.api.model.Message;
import com.pws.pateast.enums.MessageStatusType;
import com.pws.pateast.provider.DatabaseContract;
import com.pws.pateast.provider.table.Chat;

import java.util.ArrayList;

import io.socket.client.Socket;

import static com.pws.pateast.enums.MessageType.TEXT;
import static com.pws.pateast.events.SocketEvent.EVENT_MESSAGE_RECEIVED;

/**
 * Created by intel on 28-Jul-17.
 */

public class ChatHelper {

    public static ContentProviderResult[] insertChat(Context context, Message message, Socket mSocket) {
        ArrayList<Message> messages = new ArrayList<>();
        messages.add(message);
        return insertChats(context, messages, mSocket);
    }

    public static ContentProviderResult[] updateChat(Context context, Message message, boolean isChatId) {
        ArrayList<Message> messages = new ArrayList<>();
        messages.add(message);
        return updateChats(context, messages, isChatId);
    }

    public static ContentProviderResult[] insertChats(Context context, ArrayList<Message> messages, Socket mSocket) {
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();
        for (Message message : messages) {
            if (isChatExist(context, message, mSocket == null)) {
                operations.add(updateOperation(message, mSocket == null));
            } else {
                operations.add(insertOperation(message));
                if (mSocket != null)
                    mSocket.emit(EVENT_MESSAGE_RECEIVED, message.getId());
            }
        }
        try {
            return context.getContentResolver().applyBatch(DatabaseContract.AUTHORITY, operations);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ContentProviderResult[] updateChats(Context context, ArrayList<Message> messages, boolean isChatId) {
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();
        for (Message message : messages) {
            if (isChatExist(context, message, isChatId)) {
                operations.add(updateOperation(message, isChatId));
            }
        }
        try {
            return context.getContentResolver().applyBatch(DatabaseContract.AUTHORITY, operations);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static ContentProviderOperation insertOperation(Message message) {
        return ContentProviderOperation.newInsert(Chat.CONTENT_URI)
                .withValues(getInsertContentValues(message))
                .withYieldAllowed(true)
                .build();
    }

    private static ContentProviderOperation updateOperation(Message message, boolean isChatId) {
        String selection = isChatId ? Chat._ID + "=?" : Chat.COLUMN_MESSAGE_ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(isChatId ? message.getChatId() : message.getId())};
        return ContentProviderOperation.newUpdate(Chat.CONTENT_URI)
                .withSelection(selection, selectionArgs)
                .withValues(getUpdateContentValues(message))
                .withYieldAllowed(true)
                .build();
    }

    private static ContentValues getInsertContentValues(Message message) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Chat.COLUMN_SENDER_ID, message.getSenderId());
        contentValues.put(Chat.COLUMN_RECEIVER_ID, message.getReceiverId());
        contentValues.put(Chat.COLUMN_MESSAGE, message.getData());
        if (message.getType() != TEXT.getValue())
            contentValues.put(Chat.COLUMN_URL, message.getData());
        contentValues.put(Chat.COLUMN_URI, message.getUri());
        contentValues.put(Chat.COLUMN_MESSAGE_TYPE, message.getType());
        contentValues.put(Chat.COLUMN_MESSAGE_STATUS, message.getMsg_status());
        if (message.getUploadStatus() != null) {
            contentValues.put(Chat.COLUMN_UPLOAD_STATUS, message.getUploadStatus());
            contentValues.put(Chat.COLUMN_UPLOAD_PERCENTAGE, message.getUploadPercentage());
        }
        contentValues.put(Chat.COLUMN_CREATE_DATE, message.getCreatedAt());
        contentValues.put(Chat.COLUMN_UPDATE_DATE, message.getCreatedAt());
        if (message.getId() != 0)
            contentValues.put(Chat.COLUMN_MESSAGE_ID, message.getId());
        return contentValues;
    }

    private static ContentValues getUpdateContentValues(Message message) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Chat.COLUMN_MESSAGE_STATUS, message.getMsg_status());
        if (message.getId() != 0)
            contentValues.put(Chat.COLUMN_MESSAGE_ID, message.getId());
        if (message.getCreatedAt() != 0)
            contentValues.put(Chat.COLUMN_UPDATE_DATE, message.getCreatedAt());
        return contentValues;
    }

    public static void persistUploadingStatus(Context context, Message message) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Chat.COLUMN_UPLOAD_STATUS, message.getUploadStatus());
        contentValues.put(Chat.COLUMN_UPLOAD_PERCENTAGE, message.getUploadPercentage());
        context.getContentResolver().update(Chat.CONTENT_URI, contentValues,
                Chat._ID + "=?",
                new String[]{String.valueOf(message.getChatId())});
    }

    public static void persistUploadingUrl(Context context, Message message) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Chat.COLUMN_URL, message.getUrl());
        contentValues.put(Chat.COLUMN_UPLOAD_STATUS, message.getUploadStatus());
        contentValues.put(Chat.COLUMN_UPLOAD_PERCENTAGE, message.getUploadPercentage());
        context.getContentResolver().update(Chat.CONTENT_URI, contentValues,
                Chat._ID + "=?",
                new String[]{String.valueOf(message.getChatId())});
    }


    private static boolean isChatExist(Context context, Message message, boolean isChatId) {
        boolean isExist = false;

        Uri CONTENT_URI = isChatId ? Uri.parse(Chat.CONTENT_URI + "/" + message.getChatId()) : Uri.parse(Chat.CONTENT_URI + "/" + Chat.COLUMN_MESSAGE_ID + "/" + message.getId());
        Cursor cursorExist = context.getContentResolver().query(
                CONTENT_URI,
                Chat.PROJECTION_ID,
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


    public static Message getChat(Context context, Message message, boolean isChatId) {

        Uri CONTENT_URI = isChatId ? Uri.parse(Chat.CONTENT_URI + "/" + message.getChatId()) : Uri.parse(Chat.CONTENT_URI + "/" + Chat.COLUMN_MESSAGE_ID + "/" + message.getId());
        Cursor cursorExist = context.getContentResolver().query(
                CONTENT_URI,
                Chat.PROJECTION,
                null,
                null,
                null);
        if (cursorExist != null) {
            if (cursorExist.moveToFirst()) {
                message = Message.getChat(cursorExist);
                cursorExist.close();
                return message;
            }
            return null;
        }

        return null;
    }

    public static int getUnreadChatCount(Context context, int sender_id, int receiver_id) {
        int unreadCount = 0;
        String selection = Chat.COLUMN_SENDER_ID + "=? AND " + Chat.COLUMN_RECEIVER_ID + "=? AND ("
                + Chat.COLUMN_MESSAGE_STATUS + "=? OR " + Chat.COLUMN_MESSAGE_STATUS + "=?)";
        Cursor cursorExist = context.getContentResolver().query(
                Chat.CONTENT_URI,
                Chat.PROJECTION_COUNT,
                selection,
                new String[]{String.valueOf(sender_id), String.valueOf(receiver_id), String.valueOf(MessageStatusType.STATUS_RECEIVED.getValue()), String.valueOf(MessageStatusType.STATUS_SENT.getValue())},
                null);
        if (cursorExist != null) {
            if (cursorExist.moveToFirst()) {
                unreadCount = cursorExist.getInt(0);
            }
            cursorExist.close();
        }
        return unreadCount;
    }

    /**
     * get all unreaded messages count
     *
     * @param context
     * @return
     */
    public static int getUnreadChatCount(Context context) {
        int unreadCount = 0;
        Cursor cursorExist = context.getContentResolver().query(
                Chat.CONTENT_URI,
                Chat.PROJECTION_COUNT,
                Chat.COLUMN_MESSAGE_STATUS + "=?",
                new String[]{String.valueOf(MessageStatusType.STATUS_RECEIVED.getValue())},
                null);
        if (cursorExist != null) {
            if (cursorExist.moveToFirst()) {
                unreadCount = cursorExist.getInt(0);
            }
            cursorExist.close();
        }
        return unreadCount;
    }

    public static CursorLoader getChat(Context context, int sender_id, int receiver_id) {
        String selection = Chat.COLUMN_SENDER_ID + "=? AND " + Chat.COLUMN_RECEIVER_ID + "=? OR " + Chat.COLUMN_RECEIVER_ID + "=? AND " + Chat.COLUMN_SENDER_ID + "=?";

        return new CursorLoader(context, Chat.CONTENT_URI, Chat.PROJECTION, selection, new String[]{String.valueOf(sender_id), String.valueOf(receiver_id), String.valueOf(sender_id), String.valueOf(receiver_id)}, Chat.COLUMN_UPDATE_DATE + " ASC");
    }
}
