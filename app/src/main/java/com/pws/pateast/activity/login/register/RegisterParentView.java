package com.pws.pateast.activity.login.register;

import com.pws.pateast.api.model.OTP;
import com.pws.pateast.base.AppView;

/**
 * Created by intel on 09-Sep-17.
 */

public interface RegisterParentView extends AppView {

    void setError(int id, String error);

    OTP getOtp();
    String getCountryCode();
    String getMobileNumber();

    void startTimer(OTP otp);
    void updateTimer();
    void setMobileNumber(String mobileNumber);

    void registerParent(String name, String email, String enteredOTP);
}
