package com.pws.pateast.enums;

/**
 * Created by intel on 31-Jul-17.
 */

public enum  MessageType
{
    TEXT(0),
    IMAGE(1),
    AUDIO(2),
    VIDEO(3),
    OTHER(4),
    NONE(-1);

    private final int value;

    MessageType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static MessageType getMessageType(int value) {
        MessageType[] messageTypes = values();
        for (MessageType messageType : messageTypes)
        {
            if(messageType.getValue() == value)
                return messageType;
        }
        return NONE;
    }
}
