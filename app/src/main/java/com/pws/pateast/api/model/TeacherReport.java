package com.pws.pateast.api.model;

/**
 * Created by intel on 05-Jul-17.
 */

public class TeacherReport extends Response<TeacherReport>
{
    int is_locked,id;
    String content;
    TeacherReport report;


    public int getIs_locked() {
        return is_locked;
    }

    public void setIs_locked(int is_locked) {
        this.is_locked = is_locked;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public TeacherReport getReport() {
        return report;
    }

    public void setReport(TeacherReport report) {
        this.report = report;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
