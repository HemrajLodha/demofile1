package com.pws.pateast.activity.login;

import android.support.annotation.IntegerRes;
import android.support.annotation.StringRes;

import com.pws.pateast.enums.UserType;

/**
 * Created by intel on 30-Aug-17.
 */

public class LoginTab {
    @IntegerRes
    private int icon;
    @StringRes
    private String text;
    private UserType userType;

    private LoginTab(Builder builder) {
        setText(builder.getText());
        setIcon(builder.getIcon());
        setUserType(builder.getUserType());
    }


    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getIcon() {
        return icon;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public UserType getUserType() {
        return userType;
    }

    public static class Builder {
        @IntegerRes
        private int icon;
        @StringRes
        private String text;
        private UserType userType;

        public Builder() {

        }

        public int getIcon() {
            return icon;
        }

        public Builder setIcon(int icon) {
            this.icon = icon;
            return this;
        }

        public String getText() {
            return text;
        }

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public UserType getUserType() {
            return userType;
        }

        public Builder setUserType(UserType userType) {
            this.userType = userType;
            return this;
        }

        public LoginTab build() {
            return new LoginTab(this);
        }
    }
}
