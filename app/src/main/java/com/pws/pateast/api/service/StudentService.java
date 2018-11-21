package com.pws.pateast.api.service;

import com.pws.pateast.api.model.Assignment;
import com.pws.pateast.api.model.Chapter;
import com.pws.pateast.api.model.ClassSchedule;
import com.pws.pateast.api.model.Complaint;
import com.pws.pateast.api.model.ExamHead;
import com.pws.pateast.api.model.ExamMarks;
import com.pws.pateast.api.model.Leave;
import com.pws.pateast.api.model.Response;
import com.pws.pateast.api.model.Schedule;
import com.pws.pateast.api.model.Student;
import com.pws.pateast.api.model.StudyMaterial;
import com.pws.pateast.api.model.Subject;
import com.pws.pateast.api.model.Tag;
import com.pws.pateast.api.model.Topic;

import java.util.HashMap;

import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by intel on 15-May-17.
 */

public interface StudentService {
    @FormUrlEncoded
    @POST("/admin/studentreport/getSubjectsByStudent")
    Single<Subject> getSubjectsByStudent(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("admin/studentattendancereport/getReport")
    Single<Student> getStudentAttendanceReport(@Field("data") String map);

    @FormUrlEncoded
    @POST("admin/studentattendancereport/getReportByStudent")
    Single<Student> getAttendanceReportForStudent(@Field("data") String map);

    @FormUrlEncoded
    @POST("admin/studentassignment")
    Single<Assignment> getStudentAssignment(@FieldMap HashMap<String, String> params, @QueryMap HashMap<String, String> queryParams);

    @FormUrlEncoded
    @POST("admin/examhead/list")
    Single<ExamHead> getExamHead(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("/admin/studentexamschedule/examSchedule")
    Single<Schedule> getExamSchedule(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("admin/studentmark/getExamScheduleHead")
    Single<Schedule> getExamScheduleHead(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("admin/studentmark/getMarks")
    Single<ExamMarks> getMarks(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("admin/studentleave")
    Single<Leave> getStudentLeave(@FieldMap HashMap<String, String> params, @QueryMap HashMap<String, String> queryParams);

    @FormUrlEncoded
    @POST("admin/studentleave/add")
    Single<Tag> getLeaveReason(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("admin/studentleave/save")
    Single<Response> studentLeaveApply(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("admin/studentleave/status/{id}")
    Single<Response> cancelStudentLeave(@FieldMap HashMap<String, String> params, @Path("id") String leaveId);

    @FormUrlEncoded
    @POST("admin/student/viewSchedule")
    Single<ClassSchedule> getClassSchedule(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("admin/lmsstudymaterial/getSubjects")
    Single<Subject> getSubject(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("admin/lmsstudymaterial/getChapters")
    Single<Chapter> getChapters(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("admin/lmsstudymaterial/getTopics")
    Single<Topic> getTopics(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("admin/lmsstudymaterial/loadStudyMaterial")
    Single<StudyMaterial> loadStudyMaterial(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("admin/studentcomplaint")
    Single<Complaint> getStudentComplaints(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("admin/studentcomplaint/sendemail")
    Single<Response> sendEmailForComplaint(@FieldMap HashMap<String, String> params);

}
