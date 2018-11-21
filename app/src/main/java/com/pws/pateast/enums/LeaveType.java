package com.pws.pateast.enums;

/**
 * Created by intel on 06-Jul-17.
 */

public enum LeaveType {
    PENDING("Pending"),
    APPROVED("Approved"),
    CANCELED("Canceled"),
    REJECTED("Rejected"),
    NONE("none");

    private final String value;

    LeaveType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static LeaveType getLeaveType(String value) {
        switch (value) {
            case "Pending":
                return PENDING;
            case "Approved":
                return APPROVED;
            case "Canceled":
                return CANCELED;
            case "Rejected":
                return REJECTED;
            default:
                return NONE;
        }
    }
}
