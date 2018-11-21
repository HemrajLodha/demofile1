package com.pws.pateast.model;

import com.pws.pateast.enums.TaskType;

/**
 * Created by planet on 4/14/2017.
 */

public class DashboardItem {
    private int id;
    private int title;
    private int icon;
    private TaskType taskType;

    public DashboardItem(int title, int icon, TaskType taskType) {
        this.title = title;
        this.icon = icon;
        this.taskType = taskType;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
