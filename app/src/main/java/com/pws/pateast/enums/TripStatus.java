package com.pws.pateast.enums;

/**
 * Created by intel on 15-Sep-17.
 */

public enum  TripStatus
{
    NONE(-1),
    NOT_STARTED(0),
    PICKUP_STARTED(1),
    PICKUP_STOPPED(2),
    DROP_STARTED(3),
    DROP_STOPPED(4);

    private final int status;

    TripStatus(int i) {
        this.status = i;
    }

    public int getStatus() {
        return status;
    }

    public static TripStatus getStatus(int value) {
        for (TripStatus type : values()) {
            if (type.getStatus() == value) {
                return type;
            }
        }
        return NONE;
    }
}
