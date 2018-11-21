package com.pws.pateast.api.model;

import android.os.Parcel;
import android.os.Parcelable;

public class NotificationData implements Parcelable {
    private int id, receiverId, status, notificationId, masterId;
    private String senderId, type, message;
    private NotificationData notification;
    private UserInfo user;


    protected NotificationData(Parcel in) {
        id = in.readInt();
        receiverId = in.readInt();
        status = in.readInt();
        notificationId = in.readInt();
        masterId = in.readInt();
        senderId = in.readString();
        type = in.readString();
        message = in.readString();
        notification = in.readParcelable(NotificationData.class.getClassLoader());
        user = in.readParcelable(User.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(receiverId);
        dest.writeInt(status);
        dest.writeInt(notificationId);
        dest.writeInt(masterId);
        dest.writeString(senderId);
        dest.writeString(type);
        dest.writeString(message);
        dest.writeParcelable(notification, flags);
        dest.writeParcelable(user, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NotificationData> CREATOR = new Creator<NotificationData>() {
        @Override
        public NotificationData createFromParcel(Parcel in) {
            return new NotificationData(in);
        }

        @Override
        public NotificationData[] newArray(int size) {
            return new NotificationData[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public int getMasterId() {
        return masterId;
    }

    public void setMasterId(int masterId) {
        this.masterId = masterId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationData getNotification() {
        return notification;
    }

    public void setNotification(NotificationData notification) {
        this.notification = notification;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }
}
