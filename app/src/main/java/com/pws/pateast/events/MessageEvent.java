package com.pws.pateast.events;

import com.google.gson.JsonArray;
import com.pws.pateast.api.model.Message;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by intel on 01-Aug-17.
 */

public class MessageEvent
{
    private Message message;
    private int receiverId;
    private JSONArray receiverIds;
    private String eventType;


    public MessageEvent(String eventType, Message message)
    {
        this.eventType = eventType;
        this.message = message;
    }

    public MessageEvent(String eventType, int receiverId)
    {
        this.eventType = eventType;
        this.receiverId = receiverId;
        this.receiverIds = new JSONArray();
        this.receiverIds.put(receiverId);
    }

    public MessageEvent(String eventType, JSONArray receiverIds)
    {
        this.eventType = eventType;
        this.receiverIds = receiverIds;
    }

    public Message getMessage() {
        return message;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public String getEventType() {
        return eventType;
    }

    public JSONArray getReceiverIds() {
        return receiverIds;
    }
}
