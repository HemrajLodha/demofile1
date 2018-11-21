package com.pws.pateast.events;

import com.pws.pateast.api.model.Message;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by intel on 31-Jul-17.
 */

public class SocketEvent {
    public static final String EVENT_ON_CONNECT = "on-connect";
    public static final String EVENT_ON_DISCONNECT = "on-disconnect";
    public static final String EVENT_IS_CONNECTED = "is-connected";
    public static final String EVENT_CONNECT = "connect";

    public static final String EVENT_FILE_MESSAGE = "file-message";
    public static final String EVENT_SEND_MESSAGE = "send-message";
    public static final String EVENT_MESSAGE_SENT = "message-sent";
    public static final String EVENT_MESSAGE_RECEIVED = "received";
    public static final String EVENT_MESSAGE_SEEN = "seen";
    public static final String EVENT_MESSAGE_READ = "read";
    public static final String EVENT_MY_MESSAGE = "my-message";
    public static final String EVENT_MY_MESSAGES = "my-messages";
    public static final String EVENT_TYPING = "started-typing";
    public static final String EVENT_STOP_TYPING = "stopped-typing";
    public static final String EVENT_ONLINE = "online";
    public static final String EVENT_OFFLINE = "offline";
    public static final String EVENT_IS_ONLINE = "is-online";
    public static final String EVENT_IS_BLOCKED = "is-blocked";
    public static final String EVENT_BLOCK_USER = "block";
    public static final String EVENT_UNBLOCK_USER = "unblock";

    private Message message;
    private ArrayList<Message> messages;
    private int receiverId;
    private JSONArray receiverIds;
    private String eventType;

    public SocketEvent(String eventType) {
        this.eventType = eventType;
    }

    public SocketEvent(String eventType, Message message) {
        this.eventType = eventType;
        this.message = message;
    }

    public SocketEvent(String eventType, ArrayList<Message> messages) {
        this.eventType = eventType;
        this.messages = messages;
    }

    public SocketEvent(String eventType, int receiverId) {
        this.eventType = eventType;
        this.receiverId = receiverId;
        this.receiverIds = new JSONArray();
        this.receiverIds.put(receiverId);
    }

    public SocketEvent(String eventType, JSONArray receiverIds) {
        this.eventType = eventType;
        this.receiverIds = receiverIds;
    }

    public Message getMessage() {
        return message;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public String getEventType() {
        return eventType;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public JSONArray getReceiverIds() {
        return receiverIds;
    }
}
