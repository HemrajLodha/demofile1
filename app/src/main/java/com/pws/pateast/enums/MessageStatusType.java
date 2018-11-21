package com.pws.pateast.enums;

/**
 * Created by intel on 31-Jul-17.
 */

public enum  MessageStatusType
{
    STATUS_SAVE(0),
    STATUS_SENT(1),
    STATUS_RECEIVED(2),
    STATUS_SEEN(3),
    NONE(-1);

    private final int value;

    MessageStatusType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static MessageStatusType getMessageStatusType(int value)
    {
        MessageStatusType[] types = values();
        for (MessageStatusType type : types)
        {
            if(type.getValue() == value)
                return  type;
        }
        return NONE;
    }
}
