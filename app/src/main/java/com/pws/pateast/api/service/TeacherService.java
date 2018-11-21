package com.pws.pateast.api.service;

import com.pws.pateast.api.model.Assignment;
import com.pws.pateast.api.model.LeaveInfo;
import com.pws.pateast.api.model.Response;
import com.pws.pateast.api.model.Schedule;
import com.pws.pateast.api.model.Student;
import com.pws.pateast.api.model.SubjectStudent;
import com.pws.pateast.api.model.Tag;
import com.pws.pateast.api.model.TeacherDashboard;
import com.pws.pateast.fragment.assignment.teacher.add.AddAssignmentResponse;
import com.pws.pateast.api.model.Leave;
import com.pws.pateast.api.model.TeacherClass;
import com.pws.pateast.api.model.Subject;
import com.pws.pateast.api.model.TeacherReport;

import java.util.HashMap;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by intel on 20-Apr-17.
 */

public interface TeacherService {
    @FormUrlEncoded
    @POST("/admin/subject")
    Single<Subject> getSubject(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("admin/classes/teacherClass")
    Single<TeacherClass> getTeacherClasses(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("admin/utils/bcsByTeacher")
    Single<TeacherClass> getMyClasses(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("/admin/classes/teacherClassWeekly")
    Single<Schedule> getMySchedule(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("admin/mystudent/teacherStudent")
    Single<Student> getMyStudent(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("admin/utils/subjectByTeacher")
    Single<Subject> getTeacherSubject(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("/admin/attendancereport/getSubjectByTeacher")
    Single<SubjectStudent> getSubjectWithStudent(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("admin/attendance/getClassStudents")
    Single<Student> getStudentsAttendance(@FieldMap HashMap<String, String> params);


    @FormUrlEncoded
    @POST("admin/attendance/savenew")
    Single<Response> submitStudentsAttendance(@Field("data") String map);

    @FormUrlEncoded
    @POST("admin/attendance/update")
    Single<Response> updateStudentsAttendance(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("admin/assignment")
    Single<Assignment> getAssignment(@FieldMap HashMap<String,String> params, @QueryMap HashMap<String,String> queryParams);

    @FormUrlEncoded
    @POST("admin/assignment/status/{id}/{status}")
    Single<Response> changeAssignmentStatus(@FieldMap HashMap<String,String> params, @Path("id") String id, @Path("status") String status);

    @FormUrlEncoded
    @POST("admin/assignment/delete/{id}")
    Single<Response> deleteAssignment(@FieldMap HashMap<String,String> params, @Path("id") String id);

    @Multipart
    @POST("admin/assignment/save")
    Single<AddAssignmentResponse> saveAssignment(@Part("data") RequestBody params, @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("admin/assignment/getStudents")
    Single<Student> getStudentsAssignment(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("admin/assignment/saveRemark")
    Single<Response> submitStudentsRemark(@Field("data") String map);

    @FormUrlEncoded
    @POST("admin/assignment/updateRemark")
    Single<Response> updateStudentsRemark(@Field("data") String map);

    @FormUrlEncoded
    @POST("admin/attendancereport/getReport")
    Single<Student> getAttendanceReport(@Field("data") String map);

    @FormUrlEncoded
    @POST("admin/attendancereport/getReportByStudent")
    Single<Student> getAttendanceReportByStudent(@Field("data") String map);

    @FormUrlEncoded
    @POST("admin/studentleave/list")
    Single<Leave> getClassLeaves(@FieldMap HashMap<String,String> params, @QueryMap HashMap<String,String> queryParams);

    @FormUrlEncoded
    @POST("admin/studentleave/leavestatus/{id}/{status}")
    Single<Response> changeStudentLeaveStatus(@FieldMap HashMap<String,String> params,@Path("id") String leaveId,@Path("status") String status);

    @FormUrlEncoded
    @POST("admin/classreport/view/{bcsMapId}/{subjectId}/{date}/{order}")
    Single<TeacherReport> getClassReport(@FieldMap HashMap<String,String> params, @Path("bcsMapId") int bcsMapId, @Path("subjectId") int subjectId, @Path("date") String date,@Path("order") int order);

    @FormUrlEncoded
    @POST("admin/classreport/save")
    Single<TeacherReport> saveClassReport(@FieldMap HashMap<String,String> params);

    @FormUrlEncoded
    @POST("admin/mystudent/sendsms")
    Single<TeacherReport> sendMessageToStudent(@FieldMap HashMap<String,String> params);

    @FormUrlEncoded
    @POST("admin/empleave/apply")
    Single<Tag> getLeaveReasonAndType(@FieldMap HashMap<String,String> params);

    @FormUrlEncoded
    @POST("admin/empleave/save")
    Single<Response> applyTeacherLeave(@FieldMap HashMap<String,String> params);

    @FormUrlEncoded
    @POST("admin/empleave")
    Single<Leave> getTeacherLeave(@FieldMap HashMap<String,String> params,@QueryMap HashMap<String,String> queryParams);

    @FormUrlEncoded
    @POST("admin/empleave/status/{id}")
    Single<Response> cancelTeacherLeave(@FieldMap HashMap<String,String> params,@Path("id") String leaveId);

    @FormUrlEncoded
    @POST("admin/empleave/view/{id}")
    Single<LeaveInfo> getTeacherLeaveInfo(@FieldMap HashMap<String,String> params, @Path("id") String leaveId);

    @FormUrlEncoded
    @POST("admin/dashboard/teacher-app")
    Single<TeacherDashboard> getTeacherDashboard(@FieldMap HashMap<String,String> params);

}
