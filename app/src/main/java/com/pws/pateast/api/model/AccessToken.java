package com.pws.pateast.api.model;

import android.os.Parcel;
import android.os.Parcelable;

public class AccessToken extends Response<AccessToken> implements Parcelable
{
    public static final String ACCESS_TOKEN = "password";
    public static final String REFRESH_TOKEN = "refresh_token";

    private String access_token;
    private String token_type;
    private Integer expires_in;
    private String refresh_token;

    protected AccessToken(Parcel in) {
        access_token = in.readString();
        token_type = in.readString();
        refresh_token = in.readString();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(access_token);
        dest.writeString(token_type);
        dest.writeString(refresh_token);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AccessToken> CREATOR = new Creator<AccessToken>() {
        @Override
        public AccessToken createFromParcel(Parcel in) {
            return new AccessToken(in);
        }

        @Override
        public AccessToken[] newArray(int size) {
            return new AccessToken[size];
        }
    };

    public String getAccessToken() {
        return access_token;
    }

    public void setAccessToken(String access_token) {
        this.access_token = access_token;
    }

    public String getTokenType() {
        // OAuth requires uppercase Authorization HTTP header value for token type
        if(!Character.isUpperCase(token_type.charAt(0))) {
            token_type = Character.toString(token_type.charAt(0)).toUpperCase() + token_type.substring(1);
        }

        return token_type;
    }

    public void setTokenType(String token_type) {
        this.token_type = token_type;
    }

    public int getExpiry() {
        return expires_in;
    }

    public void setExpiry(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefreshToken() {
        return refresh_token;
    }

    public void setRefreshToken(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String bearer() {
        return String.format("%s %s", this.getTokenType(),this.getAccessToken());
    }


}
