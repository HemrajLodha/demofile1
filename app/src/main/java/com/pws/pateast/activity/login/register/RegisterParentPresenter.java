package com.pws.pateast.activity.login.register;

import android.text.TextUtils;
import android.view.View;

import com.pws.pateast.R;
import com.pws.pateast.api.model.OTP;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.utils.Utils;

/**
 * Created by intel on 09-Sep-17.
 */

public class RegisterParentPresenter extends AppPresenter<RegisterParentView> {
    RegisterParentView parentView;

    @Override
    public RegisterParentView getView() {
        return parentView;
    }

    @Override
    public void attachView(RegisterParentView view) {
        parentView = view;
        getView().startTimer(getView().getOtp());
    }


    public void validateRegisterProfile(CharSequence name, CharSequence email, CharSequence otp) {
        OTP otpObj = getView().getOtp();

        if (TextUtils.isEmpty(name)) {
            getView().setError(R.id.et_name, getString(R.string.validate_edittext, getString(R.string.name)));
        } /*else if (TextUtils.isEmpty(email)) {
            getView().setError(R.id.et_email, getString(R.string.validate_edittext, getString(R.string.email)));
        } else if (!Utils.isValidEmail(email)) {
            getView().setError(R.id.et_email, getString(R.string.valid_email));
        }*/ else if (TextUtils.isEmpty(otp) && TextUtils.isEmpty(otpObj.getUid())) {
            getView().setError(R.id.et_otp, getString(R.string.validate_edittext, getString(R.string.otp)));
        } else {
            getView().registerParent(name.toString(), email.toString(), otp.toString());
        }
    }
}
