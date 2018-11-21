package com.pws.pateast.api.service;

import com.pws.pateast.api.model.DashboardEvent;
import com.pws.pateast.api.model.Events;
import com.pws.pateast.api.model.ExamMarks;
import com.pws.pateast.api.model.Fees;
import com.pws.pateast.api.model.FeesOld;
import com.pws.pateast.api.model.Holiday;
import com.pws.pateast.api.model.Response;
import com.pws.pateast.api.model.RouteMap;
import com.pws.pateast.api.model.Schedule;
import com.pws.pateast.api.model.Ward;

import java.util.HashMap;

import io.reactivex.Single;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * Created by pws on 8/11/2017.
 */

public interface ParentService {


    @FormUrlEncoded
    @POST("/admin/parent/ward_list")
    Single<Ward> getWardList(@FieldMap HashMap<String, String> params);


    @FormUrlEncoded
    @POST("admin/parent/dashboard")
    Single<DashboardEvent> getParentDashBoard(
            @FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("admin/parent/dashboard-list-attendance")
    Single<Schedule> getParentAttendance(
            @FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("admin/parent/dashboard-list-exam")
    Single<Schedule> getExamSchedule(
            @FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("admin/parent/dashboard-list-marks")
    Single<ExamMarks> getExamMarks(
            @FieldMap HashMap<String, String> params);


    @FormUrlEncoded
    @POST("admin/parent/isHoliday")
    Single<Holiday> isHoliday(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("/admin/parent/holidays")
    Single<Holiday> getHolidays(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("admin/transport/student-rvdhsmap")
    Single<RouteMap> getRouteMap(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("admin/feedback/save")
    Single<Response> saveSuggestion(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("admin/feesubmission/fee-allocations")
    Single<Fees> getAllFee(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("admin/parent/pay-fee")
    Single<Response<FeesOld>> payFee(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("admin/parent/pay-done")
    Single<FeesOld> payDone(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("admin/feesubmission/send-invoice")
    Single<Response<Fees>> sendInvoiceEmail(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("/admin/feechallan/mail")
    Single<Response<Fees>> sendChallanEmail(@FieldMap HashMap<String, String> params);

}
