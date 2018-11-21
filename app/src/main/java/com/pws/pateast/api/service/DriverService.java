package com.pws.pateast.api.service;

import com.pws.pateast.api.model.Response;
import com.pws.pateast.api.model.RouteMap;
import com.pws.pateast.api.model.Trip;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Single;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by intel on 12-Sep-17.
 */

public interface DriverService {

    @FormUrlEncoded
    @POST("admin/transport/driver-dashboard")
    Single<RouteMap> getDriverDashBoard(
            @FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("admin/transport/trip")
    Single<Response<Trip>> getTrip(
            @FieldMap HashMap<String, String> params);

}
