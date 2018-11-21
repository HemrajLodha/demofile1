package com.pws.pateast.enums;

/**
 * Created by planet on 8/29/2017.
 */

public enum TagType {
    DEFAULT(-1),
    ATTENDANCE(0),
    EXAM(1),
    LEAVE(2),
    ASSIGNMENT(3);

    private final int value;

    TagType(int i) {
        this.value = i;
    }

    public int getValue() {
        return value;
    }

    public static TagType getTagType(int value) {
        for (TagType type : TagType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        return DEFAULT;
    }
}
