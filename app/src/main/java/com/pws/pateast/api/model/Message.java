package com.pws.pateast.api.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.pws.pateast.provider.table.Chat;
import com.pws.pateast.provider.table.UserChats;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.pws.pateast.enums.MessageType.TEXT;

/**
 * Created by intel on 25-Jul-17.
 */

public class Message extends Response<String> implements Parcelable {
    int id, uid, chatId, senderId, receiverId, masterId, msg_status, type, uploadPercentage, unread;
    long createdAt;
    long updatedAt;
    boolean incomingMessage;
    String url, uri, uploadStatus;

    UserInfo sender, receiver;

    ArrayList<Message> conversations, messages;
    ArrayList<String> permissions;

    public Message() {

    }

    protected Message(Parcel in) {
        id = in.readInt();
        uid = in.readInt();
        chatId = in.readInt();
        senderId = in.readInt();
        receiverId = in.readInt();
        masterId = in.readInt();
        msg_status = in.readInt();
        type = in.readInt();
        uploadPercentage = in.readInt();
        unread = in.readInt();
        createdAt = in.readLong();
        updatedAt = in.readLong();
        incomingMessage = in.readByte() != 0;
        url = in.readString();
        uri = in.readString();
        uploadStatus = in.readString();
        sender = in.readParcelable(UserInfo.class.getClassLoader());
        receiver = in.readParcelable(UserInfo.class.getClassLoader());
        conversations = in.createTypedArrayList(Message.CREATOR);
        messages = in.createTypedArrayList(Message.CREATOR);
        permissions = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(uid);
        dest.writeInt(chatId);
        dest.writeInt(senderId);
        dest.writeInt(receiverId);
        dest.writeInt(masterId);
        dest.writeInt(msg_status);
        dest.writeInt(type);
        dest.writeInt(uploadPercentage);
        dest.writeInt(unread);
        dest.writeLong(createdAt);
        dest.writeLong(updatedAt);
        dest.writeByte((byte) (incomingMessage ? 1 : 0));
        dest.writeString(url);
        dest.writeString(uri);
        dest.writeString(uploadStatus);
        dest.writeParcelable(sender, flags);
        dest.writeParcelable(receiver, flags);
        dest.writeTypedList(conversations);
        dest.writeTypedList(messages);
        dest.writeStringList(permissions);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    public ArrayList<Message> getConversations() {
        return conversations;
    }

    public void setConversations(ArrayList<Message> conversations) {
        this.conversations = conversations;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public ArrayList<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(ArrayList<String> permissions) {
        this.permissions = permissions;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public int getMasterId() {
        return masterId;
    }

    public void setMasterId(int masterId) {
        this.masterId = masterId;
    }

    public int getMsg_status() {
        return msg_status;
    }

    public void setMsg_status(int msg_status) {
        this.msg_status = msg_status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UserInfo getSender() {
        return sender;
    }

    public void setSender(UserInfo sender) {
        this.sender = sender;
    }

    public UserInfo getReceiver() {
        return receiver;
    }

    public void setReceiver(UserInfo receiver) {
        this.receiver = receiver;
    }

    public boolean isSystemMessage() {
        return getSenderId() == 0;
    }

    public boolean isIncomingMessage() {
        return incomingMessage;
    }

    public void setIncomingMessage(boolean incomingMessage) {
        this.incomingMessage = incomingMessage;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getUploadPercentage() {
        return uploadPercentage;
    }

    public void setUploadPercentage(int uploadPercentage) {
        this.uploadPercentage = uploadPercentage;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public String getUploadStatus() {
        return uploadStatus;
    }

    public void setUploadStatus(String uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

    public static Message getUserChat(Cursor cursor) {
        Message message = new Message();
        message.setId(cursor.getInt(UserChats.INDEX_COLUMN_MESSAGE_ID));
        message.setData(cursor.getString(UserChats.INDEX_COLUMN_MESSAGE));
        message.setMsg_status(cursor.getInt(UserChats.INDEX_COLUMN_MESSAGE_STATUS));
        message.setType(cursor.getInt(UserChats.INDEX_COLUMN_MESSAGE_TYPE));
        message.setSenderId(cursor.getInt(UserChats.INDEX_COLUMN_SENDER_ID));
        message.setReceiverId(cursor.getInt(UserChats.INDEX_COLUMN_RECEIVER_ID));
        message.setCreatedAt(cursor.getLong(UserChats.INDEX_COLUMN_CREATE_DATE));
        message.setUpdatedAt(cursor.getLong(UserChats.INDEX_COLUMN_UPDATE_DATE));
        message.setSender(new UserInfo(cursor));

        return message;
    }

    public static int getSenderId(Cursor cursor) {
        if (cursor.getString(UserChats.INDEX_COLUMN_USER_NAME) != null)
            return 0;
        return cursor.getInt(UserChats.INDEX_COLUMN_SENDER_ID);
    }

    public static Message getChat(Cursor cursor) {
        Message message = new Message();
        message.setChatId(cursor.getInt(Chat.INDEX_COLUMN_ID));
        message.setSenderId(cursor.getInt(Chat.INDEX_COLUMN_SENDER_ID));
        message.setReceiverId(cursor.getInt(Chat.INDEX_COLUMN_RECEIVER_ID));
        message.setId(cursor.getInt(Chat.INDEX_COLUMN_MESSAGE_ID));
        message.setData(cursor.getString(Chat.INDEX_COLUMN_MESSAGE));
        message.setUrl(cursor.getString(Chat.INDEX_COLUMN_URL));
        message.setUri(cursor.getString(Chat.INDEX_COLUMN_URI));
        message.setType(cursor.getInt(Chat.INDEX_COLUMN_MESSAGE_TYPE));
        message.setMsg_status(cursor.getInt(Chat.INDEX_COLUMN_MESSAGE_STATUS));
        message.setUploadStatus(cursor.getString(Chat.INDEX_COLUMN_UPLOAD_STATUS));
        message.setUploadPercentage(cursor.getInt(Chat.INDEX_COLUMN_UPLOAD_PERCENTAGE));
        message.setCreatedAt(cursor.getLong(Chat.INDEX_COLUMN_CREATE_DATE));
        message.setUpdatedAt(cursor.getLong(Chat.INDEX_COLUMN_UPDATE_DATE));
        //message.setSender(new UserInfo(cursor));

        return message;
    }

    public JSONObject getMessageJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            if (getType() == TEXT.getValue())
                jsonObject.put("data", getData());
            else
                jsonObject.put("data", getUrl());
            jsonObject.put("type", getType());
            jsonObject.put("createdAt", getCreatedAt());
            jsonObject.put("receiverId", getReceiverId());
            jsonObject.put("uid", getChatId());
            jsonObject.put("msg_status", getMsg_status());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public static Message getMessageObject(JSONObject jsonObjects) {
        Message message = new Message();
        try {
            message.setChatId(jsonObjects.getInt("uid"));
            message.setId(jsonObjects.getInt("id"));
            message.setMsg_status(jsonObjects.getInt("status"));
            message.setCreatedAt(jsonObjects.getLong("createdAt"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message;
    }

    public static Message updateMessageStatus(int id, int msg_status) {
        Message message = new Message();
        message.setId(id);
        message.setMsg_status(msg_status);
        return message;
    }
}
