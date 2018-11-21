package com.pws.pateast.enums;

/**
 * Created by planet on 8/24/2017.
 */

public enum ExamResult {
    FAILED(0),
    PASS(1);

    private final int value;

    ExamResult(int i) {
        this.value = i;
    }

    public int getValue() {
        return value;
    }

    public static ExamResult getExamResult(int value) {
        for (ExamResult type : ExamResult.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        return FAILED;
    }
}
