package com.pws.pateast.enums;

/**
 * Created by intel on 15-Sep-17.
 */

public enum BoardStatus {
    CANNOT_BOARD(-1),
    NONE(0),
    PARENT_ON_BOARD_DROP(1),
    DRIVER_ON_BOARD_PICKUP(2),
    DRIVER_OFF_BOARD_PICKUP(3),
    DRIVER_ON_BOARD_DROP(4),
    PARENT_PICKUP_EARLY(5),
    DRIVER_OFF_BOARD_DROP(6);

    private final int status;

    BoardStatus(int i) {
        this.status = i;
    }

    public int getStatus() {
        return status;
    }

    public static BoardStatus getStatus(int value) {
        for (BoardStatus type : values()) {
            if (type.getStatus() == value) {
                return type;
            }
        }
        return NONE;
    }
}
