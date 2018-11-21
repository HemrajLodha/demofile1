package com.pws.pateast.api.service;

import com.pws.pateast.api.model.AccessToken;
import com.pws.pateast.api.model.ChatPermission;
import com.pws.pateast.api.model.ChatUser;
import com.pws.pateast.api.model.Circular;
import com.pws.pateast.api.model.Events;
import com.pws.pateast.api.model.Feeds;
import com.pws.pateast.api.model.Message;
import com.pws.pateast.api.model.Notification;
import com.pws.pateast.api.model.OTP;
import com.pws.pateast.api.model.Response;
import com.pws.pateast.api.model.Tag;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.model.UserInfo;
import com.pws.pateast.fragment.assignment.teacher.add.AddAssignmentResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by intel on 06-Apr-17.
 */

public interface APIService {
    @FormUrlEncoded
    @POST("/oauth/token")
    Call<AccessToken> generateAccessToken(
            @Field("grant_type") String grant_type,
            @Field("device_id") String device_id,
            @Field("username") String username,
            @Field("password") String password,
            @Field("client_id") String client_id,
            @Field("client_secret") String client_secret);

    @FormUrlEncoded
    @POST("/oauth/token")
    Call<AccessToken> refreshAccessToken(
            @Field("grant_type") String grant_type,
            @Field("device_id") String device_id,
            @Field("username") String username,
            @Field("password") String password,
            @Field("client_id") String client_id,
            @Field("client_secret") String client_secret,
            @Field("refresh_token") String refresh_token);

    @FormUrlEncoded
    @POST("/login")
    Single<User> login(
            @Field("username") String username,
            @Field("userpassword") String password,
            @Field("device_type") String deviceType,
            @Field("deviceId") String deviceId,
            @Field("lang") String lang);


    @FormUrlEncoded
    @POST("/admin/parent/verify_mobile")
    Single<OTP> verifyMobile(
            @Field("country_code") String country_code,
            @Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("/admin/parent/verify_mobile_v2")
    Single<OTP> verifyMobileV2(
            @Field("country_code") String country_code,
            @Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("/admin/parent/login")
    Single<User> loginParent(
            @Field("mobile") String userMobile,
            @Field("token") String verification_id,
            @Field("uid") String uid,
            @Field("country_code") String cCode,
            @Field("parent_registered") String parent_registered,
            @Field("device_type") String deviceType,
            @Field("deviceId") String deviceId,
            @Field("langId") String langId,
            @Field("lang") String lang);

    @FormUrlEncoded
    @POST("/admin/parent/login")
    Single<User> registerVerifiedParent(
            @Field("fullname") String fullname,
            @Field("email") String email,
            @Field("mobile") String userMobile,
            @Field("token") String verification_id,
            @Field("uid") String uid,
            @Field("country_code") String cCode,
            @Field("parent_registered") String parent_registered,
            @Field("device_type") String deviceType,
            @Field("deviceId") String deviceId,
            @Field("langId") String langId,
            @Field("lang") String lang);


    @FormUrlEncoded
    @POST("/admin/parent/verify-otp")
    Single<User> verifyOtp(
            @Field("mobile") String userMobile,
            @Field("token") String token,
            @Field("code") String otp,
            @Field("country_code") String cCode,
            @Field("parent_registered") String parent_registered,
            @Field("device_type") String deviceType,
            @Field("deviceId") String deviceId,
            @Field("langId") String langId,
            @Field("lang") String lang);

    @FormUrlEncoded
    @POST("/admin/parent/verify-otp")
    Single<User> registerParent(
            @Field("fullname") String fullname,
            @Field("email") String email,
            @Field("mobile") String userMobile,
            @Field("token") String token,
            @Field("code") String otp,
            @Field("country_code") String cCode,
            @Field("parent_registered") String parent_registered,
            @Field("device_type") String deviceType,
            @Field("deviceId") String deviceId,
            @Field("langId") String langId,
            @Field("lang") String lang);

    @FormUrlEncoded
    @POST("/check-username")
    Single<OTP> checkUsername(
            @Header("Authorization") String header,
            @Field("lang") String lang,
            @Field("username") String username);

    @FormUrlEncoded
    @POST("/reset-password-otp")
    Single<OTP> resetPassword(
            @Field("langId") String langId,
            @Field("lang") String lang,
            @Field("device_type") String deviceType,
            @Field("token") String token,
            @Field("country_code") String cCode,
            @Field("code") String otp,
            @Field("username") String username,
            @Field("password") String password);

    @FormUrlEncoded
    @POST("/admin/chat/nothing")
    Single<Response> nothing(
            @FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("/admin/dashboard/userinfo")
    Single<UserInfo> getUserInfo(
            @FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("/admin/profile")
    Single<UserInfo> getUserProfile(
            @FieldMap HashMap<String, String> params);


    @POST("admin/user/userdetails/{id}/{langId}/{masterId}")
    Single<User> getUserDetails(
            @Path("id") String id, @Path("langId") String langId, @Path("masterId") String masterId);

    @FormUrlEncoded
    @POST("admin/profile/changePassword")
    Single<Response> resetPassword(
            @FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("admin/profile/changeUserName")
    Single<Response> restUsername(
            @FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("admin/chat/permissions")
    Single<ChatPermission> getPermissions(
            @FieldMap HashMap<String, String> params);

    @Multipart
    @POST("admin/profile/save-user")
    Single<Response> updateProfile(@Part("data") RequestBody params, @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("/admin/chat/profiles")
    Single<ChatUser> getChatUsers(
            @FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("admin/chat")
    Single<Message> getInbox(
            @FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("admin/chat/messages")
    Single<Message> getMessages(
            @FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("/admin/chat/teachers")
    Single<ChatUser> getTeachers(
            @FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("/admin/chat/institute")
    Single<ChatUser> getInstitute(
            @FieldMap HashMap<String, String> params);


    @FormUrlEncoded
    @POST("/admin/chat/students")
    Single<ChatUser> getStudents(
            @FieldMap HashMap<String, String> params);


    @FormUrlEncoded
    @POST("/admin/chat/admins")
    Single<ChatUser> getAdmins(
            @FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("/admin/chat/parents")
    Single<ChatUser> getParents(
            @FieldMap HashMap<String, String> params);

    @Multipart
    @POST("/admin/chat/file")
    Observable<Message> uploadFile(
            @Part("uid") RequestBody params, @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("/admin/tag/list")
    Single<Tag> getTagList(
            @FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("/admin/notification")
    Single<Notification> getNotificationList(@FieldMap HashMap<String, String> params, @QueryMap HashMap<String, String> queryParams);

    @FormUrlEncoded
    @POST("/admin/notification/setNotificationStatus")
    Single<Response> setNotificationStatus(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("/admin/user/setNotificationStatus")
    Single<Response> changeFcmNotificationStatus(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("/admin/user/appLogOut")
    Single<Response> logOutUser(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("admin/profile/changeDefaults")
    Single<Response> changeLanguage(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("/admin/event")
    Single<Events> getEvents(@FieldMap HashMap<String, String> params, @QueryMap HashMap<String, String> queryParams);

    @FormUrlEncoded
    @POST("/admin/event/edit")
    Single<Response<Events>> getEventsDetails(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("/admin/circular")
    Single<Circular> getCircular(@FieldMap HashMap<String, String> params, @QueryMap HashMap<String, String> queryParams);

    @FormUrlEncoded
    @POST("/admin/circular/edit")
    Single<Response<Circular>> getCircularDetails(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("/admin/feed")
    Single<Feeds> getFeeds(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("/admin/feed/remove")
    Single<Response> deleteFeeds(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("/admin/feed/reject")
    Single<Response> rejectFeeds(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("/admin/feed/approve")
    Single<Response> approveFeeds(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("/admin/feed/like")
    Single<Response> likeFeeds(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("/admin/feed/unlike")
    Single<Response> unlikeFeeds(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("/admin/feed/control-users")
    Single<Response<ArrayList<UserInfo>>> getControlUsers(@FieldMap HashMap<String, String> params);

    @Multipart
    @POST("/admin/feed/save")
    Single<AddAssignmentResponse> postFeed(@PartMap HashMap<String, RequestBody> params, @Part List<MultipartBody.Part> files);


}
