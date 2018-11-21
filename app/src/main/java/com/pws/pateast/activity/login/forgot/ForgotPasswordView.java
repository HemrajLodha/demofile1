package com.pws.pateast.activity.login.forgot;

import com.pws.pateast.api.model.OTP;
import com.pws.pateast.base.AppView;

/**
 * Created by intel on 02-May-17.
 */

public interface ForgotPasswordView extends AppView {

    void setActionVisibility(OTP otp);

    void startTimer(OTP otp);

    boolean isError();

    void setError(int id, String error);

    void navigateToLogin(String message);
}
