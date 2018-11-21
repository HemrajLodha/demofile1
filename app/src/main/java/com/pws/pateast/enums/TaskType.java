package com.pws.pateast.enums;

/**
 * Created by planet on 4/20/2017.
 */

public enum TaskType {
    DEFAULT(0),
    MY_MESSAGE(1),
    GALLERY(2),
    MY_POLLS(3),
    MY_EVENTS(4),
    STUDENT_EXAM_SCHEDULE(5),
    TEACHER_STUDENTS(6),
    TEACHER_CLASSES(7),
    HOLIDAY(8),
    ASSIGNMENT(9),
    MY_PROFILE(10),
    SETTINGS(11),
    CLASS_ATTENDANCE(12),
    ADD_ASSIGNMENT(13),
    ATTENDANCE_REPORT(14),
    STUDENT_ATTENDANCE(15),
    STUDENT_ASSIGNMENT(16),
    STUDENT_PROFILE(17),
    STUDENT_MARKS(18),
    CLASS_TIME_TABLE(19),
    TEACHER_ATTENDANCE(20),
    EMPLOYEE_LEAVE_APPLY(21),
    STUDENT_LEAVE_APPLY(22),
    PARENT_LEAVE_APPLY(23),
    TEACHER_SCHEDULE(24),
    STUDENT_SCHEDULE(25),
    WARD_SCHEDULE(26),
    UPLOAD_MARKS(27),
    CLASS_LEAVE(28),
    STUDENT_LEAVE(29),
    TEACHER_LEAVE_INFO(30),
    PARENT_ASSIGNMENT(31),
    ASSIGNMENT_LIST(32),
    WARD_EXAM_SCHEDULE(33),
    TEACHER_EXAM_SCHEDULE(34),
    TEACHER_PROFILE(35),
    WARD_MARKS(36),
    TEACHER_MARKS(37),
    REVIEW_ASSIGNMENT(38),
    WARD_ATTENDANCE(39),
    PARENT_PROFILE(40),
    DRIVER_PROFILE(41),
    WARD_TRACK_ROUTE(42),
    STUDENT_ON_BOARD(43),
    PARENT_SUGGESTION(44),
    PRIVACY_TOS(45),
    ASSIGNMENT_DETAIL(46),
    NOTIFICATION_LIST(47),
    PAYMENT_HISTORY(48),
    COMPLAINTS(49),
    COMPLAINT_DETAIL(50),
    COMPLAINT_REPORT(51),
    PARENT_EVENTS(52),
    STUDENT_EVENTS(53),
    TEACHER_EVENTS(54),
    EVENTS_DETAILS(55),
    PARENT_CIRCULAR(56),
    STUDENT_CIRCULAR(57),
    TEACHER_CIRCULAR(58),
    CIRCULAR_DETAILS(59),
    ADD_FEED(60),;

    private final int value;

    TaskType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static TaskType getTaskType(int value) {
        for (TaskType type : TaskType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        return DEFAULT;
    }

}
