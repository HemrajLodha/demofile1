package com.pws.pateast.events;

import android.net.Uri;

import com.pws.pateast.api.model.Message;

/**
 * Created by intel on 09-Aug-17.
 */

public class UploadEvent {

    private String eventType;
    private Message message;
    public UploadEvent(String eventType, Message message) {
        this.eventType = eventType;
        this.message = message;
    }

    public String getEventType() {
        return eventType;
    }

    public Message getMessage() {
        return message;
    }
}
