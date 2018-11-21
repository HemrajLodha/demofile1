package com.pws.pateast.enums;

/**
 * Created by planet on 9/27/2017.
 */

public enum NotificationType {
    NONE("none"),
    EXAM_SYLLABUS("examsyllabus"),
    EXAM_SCHEDULE("examschedule"),
    TRANSPORT("transport"),
    ATTENDANCE("attendance"),
    LEAVE_REJECTED("leave_rejected"),
    LEAVE_APPROVED("leave_approved"),
    TIMETABLE("timetable"),
    EVENT("event"),
    ASSIGNMENT("assignment"),
    ASSIGNMENT_REMARK("assignmentremark"),
    STUDENT_LEAVE("studentleave"),
    START_PICK_UP("start-pick-up"),
    CONFIRM_PICKUP_ON_BOARD("confirm-pick-up-on-board"),
    EXAM_MARKS("exam_mark"),
    MESSAGE("message"),
    COMPLAINT("complaint"),
    CIRCULAR("circular");

    private String val;

    NotificationType(String val) {
        this.val = val;
    }

    public String getValue() {
        return val;
    }

    public static NotificationType getNotificationType(String value) {
        for (NotificationType notificationType : values()) {
            if (notificationType.getValue().equalsIgnoreCase(value))
                return notificationType;
        }
        return NONE;
    }
}
