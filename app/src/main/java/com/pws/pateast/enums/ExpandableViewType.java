package com.pws.pateast.enums;

public enum ExpandableViewType {
    DEFAULT(-1), SCHEDULE(4), ATTENDANCE(5), ASSIGNMENT(6), EXAM_SCHEDULE(7), EXAM_MARKS(8);
    private final int value;

    ExpandableViewType(int i) {
        this.value = i;
    }

    public int getValue() {
        return value;
    }

    public static ExpandableViewType getExpandableViewType(int value) {
        for (ExpandableViewType type : ExpandableViewType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        return DEFAULT;
    }

}