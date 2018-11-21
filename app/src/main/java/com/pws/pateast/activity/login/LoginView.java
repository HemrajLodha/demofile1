package com.pws.pateast.activity.login;

import android.os.Bundle;

import com.pws.pateast.api.model.OTP;
import com.pws.pateast.api.model.User;
import com.pws.pateast.base.AppView;

import java.util.ArrayList;

/**
 * Created by intel on 19-Apr-17.
 */

public interface LoginView extends AppView {

    void setTabLayout();

    void setError(String error);

    void navigateToHome(Bundle bundle);

    void openForgotPassword();

    void openRegisterParent(OTP otp);

    void registerParent(String name, String email, String mobile, String countryCode, String enteredOTP, OTP otp);

    void readSMS(String mobile);

    void startTimer(OTP otp);

    void resendOTP();

    void setActionVisibility(OTP otp);

    String getMobileNumber();

    String getCountryCode();

}
