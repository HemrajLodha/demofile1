package com.pws.pateast.enums;

public enum TripRecordType {
    NONE(-1),
    PICKUP_ONLY(1),
    DROP_OFF_ONLY(2),
    BOTH(3);

    private final int status;

    TripRecordType(int i) {
        this.status = i;
    }

    public int getStatus() {
        return status;
    }

    public static TripRecordType getStatus(int value) {
        for (TripRecordType type : values()) {
            if (type.getStatus() == value) {
                return type;
            }
        }
        return NONE;
    }
}
