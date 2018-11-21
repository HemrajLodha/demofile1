package com.pws.pateast.api.model;

/**
 * Created by planet on 10/7/2017.
 */

public class TeacherDashboard extends Response<TeacherDashboard> {
    private int notification;

    public int getNotification() {
        return notification;
    }

    public void setNotification(int notification) {
        this.notification = notification;
    }
}
