package com.pws.pateast.api.model;

import java.util.ArrayList;

/**
 * Created by intel on 17-May-17.
 */

public class ExamMarks extends Response<ArrayList<ExamMarks>> {
    int id, masterId, examScheduleId, bcsMapId, subjectId, markId, studentId, subject_rank, result_is;
    double max_mark, min_passing_mark;
    Double obtained_mark, subject_percent;
    String date, createdAt, updatedAt, enrollment_no, exam_type;

    Subject subject;
    Student student;
    ArrayList<ExamMarks> markrecords;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMasterId() {
        return masterId;
    }

    public void setMasterId(int masterId) {
        this.masterId = masterId;
    }

    public int getExamScheduleId() {
        return examScheduleId;
    }

    public void setExamScheduleId(int examScheduleId) {
        this.examScheduleId = examScheduleId;
    }

    public int getBcsMapId() {
        return bcsMapId;
    }

    public void setBcsMapId(int bcsMapId) {
        this.bcsMapId = bcsMapId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public double getMax_mark() {
        return max_mark;
    }

    public void setMax_mark(double max_mark) {
        this.max_mark = max_mark;
    }

    public double getMin_passing_mark() {
        return min_passing_mark;
    }

    public void setMin_passing_mark(double min_passing_mark) {
        this.min_passing_mark = min_passing_mark;
    }

    public int getMarkId() {
        return markId;
    }

    public void setMarkId(int markId) {
        this.markId = markId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public double getObtained_mark() {
        return obtained_mark == null ? -1 : obtained_mark;
    }

    public void setObtained_mark(double obtained_mark) {
        this.obtained_mark = obtained_mark;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getEnrollment_no() {
        return enrollment_no;
    }

    public void setEnrollment_no(String enrollment_no) {
        this.enrollment_no = enrollment_no;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public ArrayList<ExamMarks> getMarkrecords() {
        return markrecords;
    }

    public void setMarkrecords(ArrayList<ExamMarks> markrecords) {
        this.markrecords = markrecords;
    }

    public boolean getResult() {
        return getMarkrecords().get(0).getObtained_mark() >= getMin_passing_mark();
    }

    public int getSubject_rank() {
        return subject_rank;
    }

    public void setSubject_rank(int subject_rank) {
        this.subject_rank = subject_rank;
    }

    public double getSubject_percent() {
        return subject_percent == null ? -1 : subject_percent;
    }

    public void setSubject_percent(double subject_percent) {
        this.subject_percent = subject_percent;
    }

    public int getResult_is() {
        return result_is;
    }

    public void setResult_is(int result_is) {
        this.result_is = result_is;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getExam_type() {
        return exam_type;
    }

    public void setExam_type(String exam_type) {
        this.exam_type = exam_type;
    }
}
