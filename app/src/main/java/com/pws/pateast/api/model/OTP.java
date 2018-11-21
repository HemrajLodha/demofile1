package com.pws.pateast.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by pws on 8/11/2017.
 */

public class OTP extends Response<ArrayList<OTP>> implements Parcelable{

    private int verify,retry_in,expires_in,attempt;
    private OTP otp;
    private String site;
    private String app_key;
    private String service;
    private String reason;
    private String token;
    private String country_code;
    private String phone;
    private String rstatus;
    private String verificationId;
    private String smsCode;
    private String uid;
    private String idToken;

    protected OTP(Parcel in) {
        verify = in.readInt();
        retry_in = in.readInt();
        expires_in = in.readInt();
        attempt = in.readInt();
        otp = in.readParcelable(OTP.class.getClassLoader());
        site = in.readString();
        app_key = in.readString();
        service = in.readString();
        reason = in.readString();
        token = in.readString();
        country_code = in.readString();
        phone = in.readString();
        rstatus = in.readString();
        verificationId = in.readString();
        smsCode = in.readString();
        uid = in.readString();
        idToken = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(verify);
        dest.writeInt(retry_in);
        dest.writeInt(expires_in);
        dest.writeInt(attempt);
        dest.writeParcelable(otp, flags);
        dest.writeString(site);
        dest.writeString(app_key);
        dest.writeString(service);
        dest.writeString(reason);
        dest.writeString(token);
        dest.writeString(country_code);
        dest.writeString(phone);
        dest.writeString(rstatus);
        dest.writeString(verificationId);
        dest.writeString(smsCode);
        dest.writeString(uid);
        dest.writeString(idToken);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OTP> CREATOR = new Creator<OTP>() {
        @Override
        public OTP createFromParcel(Parcel in) {
            return new OTP(in);
        }

        @Override
        public OTP[] newArray(int size) {
            return new OTP[size];
        }
    };

    public int getVerify() {
        return verify;
    }

    public void setVerify(int verify) {
        this.verify = verify;
    }

    public int getRetry_in() {
        return retry_in;
    }

    public void setRetry_in(int retry_in) {
        this.retry_in = retry_in;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public int getAttempt() {
        return attempt;
    }

    public void setAttempt(int attempt) {
        this.attempt = attempt;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public OTP getOtp() {
        return otp;
    }

    public void setOtp(OTP otp) {
        this.otp = otp;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getApp_key() {
        return app_key;
    }

    public void setApp_key(String app_key) {
        this.app_key = app_key;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRstatus() {
        return rstatus;
    }

    public void setRstatus(String rstatus) {
        this.rstatus = rstatus;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getVerificationId() {
        return verificationId;
    }

    public void setVerificationId(String verificationId) {
        this.verificationId = verificationId;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}
