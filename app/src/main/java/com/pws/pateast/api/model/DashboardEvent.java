package com.pws.pateast.api.model;

import com.base.ui.adapter.model.Parent;
import com.pws.pateast.activity.dashboard.ChildFooterItem;
import com.pws.pateast.enums.ExpandableViewType;

import java.util.List;

/**
 * Created by planet on 8/22/2017.
 */

public class DashboardEvent<T> extends Response<DashboardEvent> implements Parent<T, ChildFooterItem> {
    private static final int MAX_CHILD_DATA_COUNT = 4;
    private String title, user_image, institute, fullname, section, assignment, due_date;
    private String class_name;
    private int titleIcon, userId;
    private int message_count;
    private int icon_color;

    Schedule schedule;
    Schedule attendance;
    Assignment assignments;
    Schedule exam_schedules;
    ExamMarks exam_marks;
    FeesOld fee;
    private ExpandableViewType expandableViewType;
    private DashboardEvent user;
    private ChildFooterItem childFooterItem;


    public DashboardEvent() {
    }

    public DashboardEvent(String title, int titleIcon, ExpandableViewType expandableViewType, ChildFooterItem childFooterItem,int icon_color) {
        this.title = title;
        this.titleIcon = titleIcon;
        this.expandableViewType = expandableViewType;
        this.childFooterItem = childFooterItem;
        this.icon_color = icon_color;
    }

    @Override
    public List<T> getChildList() {
        switch (getExpandableViewType()) {
            case SCHEDULE:
                return (List<T>) getSchedule().getTimetableallocations();
            case ATTENDANCE:
                return (List<T>) getAttendance().getData();
            case ASSIGNMENT:
                return (List<T>) getAssignments().getData();
            case EXAM_SCHEDULE:
                return (List<T>) getExam_schedules().getData();
            case EXAM_MARKS:
                return (List<T>) getExam_marks().getData();
        }
        return null;
    }

    public FeesOld getFee() {
        return fee;
    }

    public void setFee(FeesOld fee) {
        this.fee = fee;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return getExpandableViewType() == ExpandableViewType.SCHEDULE;
    }

    @Override
    public ChildFooterItem getChildListFooter() {
        return childFooterItem;
    }

    @Override
    public boolean expandableViewFooterEnable() {
        return true;
    }

    @Override
    public int getMaxChildCount() {
        return MAX_CHILD_DATA_COUNT;
    }

    public int getIcon_color() {
        return icon_color;
    }

    public void setIcon_color(int icon_color) {
        this.icon_color = icon_color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getAssignment() {
        return assignment;
    }

    public void setAssignment(String assignment) {
        this.assignment = assignment;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public int getTitleIcon() {
        return titleIcon;
    }

    public void setTitleIcon(int titleIcon) {
        this.titleIcon = titleIcon;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMessage_count() {
        return message_count;
    }

    public void setMessage_count(int message_count) {
        this.message_count = message_count;
    }

    public Schedule getSchedule() {
        if (schedule == null) {
            schedule = new Schedule();
        }
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Schedule getAttendance() {
        if (attendance == null) {
            attendance = new Schedule();
        }
        return attendance;
    }

    public void setAttendance(Schedule attendance) {
        this.attendance = attendance;
    }

    public Assignment getAssignments() {
        if (assignments == null) {
            assignments = new Assignment();
        }
        return assignments;
    }

    public void setAssignments(Assignment assignments) {
        this.assignments = assignments;
    }

    public Schedule getExam_schedules() {
        if (exam_schedules == null) {
            exam_schedules = new Schedule();
        }
        return exam_schedules;
    }

    public void setExam_schedules(Schedule exam_schedules) {
        this.exam_schedules = exam_schedules;
    }

    public ExamMarks getExam_marks() {
        if (exam_marks == null) {
            exam_marks = new ExamMarks();
        }
        return exam_marks;
    }

    public void setExam_marks(ExamMarks exam_marks) {
        this.exam_marks = exam_marks;
    }

    public ExpandableViewType getExpandableViewType() {
        return expandableViewType;
    }

    public void setExpandableViewType(ExpandableViewType expandableViewType) {
        this.expandableViewType = expandableViewType;
    }

    public DashboardEvent getUser() {
        return user;
    }

    public void setUser(DashboardEvent user) {
        this.user = user;
    }

    public ChildFooterItem getChildFooterItem() {
        return childFooterItem;
    }

    public void setChildFooterItem(ChildFooterItem childFooterItem) {
        this.childFooterItem = childFooterItem;
    }
}
