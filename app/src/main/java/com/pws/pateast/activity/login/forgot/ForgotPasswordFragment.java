package com.pws.pateast.activity.login.forgot;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ViewFlipper;

import com.pws.pateast.R;
import com.pws.pateast.activity.login.LoginView;
import com.pws.pateast.api.model.OTP;
import com.pws.pateast.base.AppDialogFragment;
import com.pws.pateast.utils.Utils;

/**
 * Created by intel on 02-May-17.
 */

public class ForgotPasswordFragment extends AppDialogFragment implements ForgotPasswordView, View.OnClickListener {
    private ViewFlipper flipResetPassword;
    private EditText etUserName, etOTP, etNewPassword, etRepeatPassword;
    private Button btnSubmit, btnResend;

    private LoginView mLoginView;
    private ForgotPasswordPresenter passwordPresenter;
    private CountDownTimer countDownTimer = null;
    private boolean passwordShown = false;

    public void attachView(LoginView mLoginView) {
        this.mLoginView = mLoginView;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_forgot_password;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);
        setCancelable(false);
        setTitle(R.string.forgot_password);

        flipResetPassword = (ViewFlipper) findViewById(R.id.flip_reset_password);
        etUserName = (EditText) findViewById(R.id.et_user_name);
        etOTP = (EditText) findViewById(R.id.et_otp);
        etNewPassword = (EditText) findViewById(R.id.et_new_password);
        etRepeatPassword = (EditText) findViewById(R.id.et_repeat_password);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        btnResend = (Button) findViewById(R.id.btn_resend);

        btnSubmit.setOnClickListener(this);

        passwordPresenter = new ForgotPasswordPresenter();
        passwordPresenter.attachView(this);
    }

    @Override
    public void setActionVisibility(OTP otp) {
        if (!passwordShown) {
            flipResetPassword.showNext();
            passwordShown = true;
        }
        btnResend.setVisibility(View.VISIBLE);
        btnSubmit.setText(R.string.reset_password);
        etUserName.setFocusable(false);
        etUserName.setFocusableInTouchMode(false);
        btnSubmit.setTag(otp);
    }

    @Override
    public void startTimer(OTP otp) {
        setActionVisibility(otp);
        countDownTimer = new CountDownTimer(otp.getRetry_in() * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                btnResend.setOnClickListener(null);
                btnResend.setText(DateUtils.formatElapsedTime(millisUntilFinished / 1000));
            }

            public void onFinish() {
                btnResend.setOnClickListener(ForgotPasswordFragment.this);
                btnResend.setText(R.string.resend_otp);
                countDownTimer = null;
            }
        };
        countDownTimer.start();
    }

    @Override
    public boolean isError() {
        boolean isError = false;
        if (TextUtils.isEmpty(etUserName.getText())) {
            isError = true;
            setError(R.id.et_user_name, getString(R.string.validate_edittext, getString(R.string.username)));
        } else if (btnSubmit.getTag() != null) {
            if (TextUtils.isEmpty(etOTP.getText())) {
                isError = true;
                setError(R.id.et_otp, getString(R.string.validate_edittext, getString(R.string.otp)));
            } else if (TextUtils.isEmpty(etNewPassword.getText())) {
                isError = true;
                setError(R.id.et_new_password, getString(R.string.validate_edittext, getString(R.string.new_password_et)));
            } else if (!Utils.isValidPassword(etNewPassword.getText())) {
                isError = true;
                setError(R.id.et_new_password, getString(R.string.valid_password));
            } else if (TextUtils.isEmpty(etRepeatPassword.getText())) {
                isError = true;
                setError(R.id.et_repeat_password, getString(R.string.validate_edittext, getString(R.string.repeat_password_et)));
            } else if (!TextUtils.equals(etNewPassword.getText(), etRepeatPassword.getText())) {
                isError = true;
                setError(R.id.et_repeat_password, getString(R.string.validate_repeat_password, getString(R.string.new_password_et), getString(R.string.repeat_password_et)));
            }
        }
        return isError;
    }

    @Override
    public void setError(int id, String error) {
        switch (id) {
            case R.id.et_user_name:
                etUserName.setError(error);
                break;
            case R.id.et_otp:
                etOTP.setError(error);
                break;
            case R.id.et_new_password:
                etNewPassword.setError(error);
                break;
            case R.id.et_repeat_password:
                etRepeatPassword.setError(error);
                break;
        }
    }

    @Override
    public void navigateToLogin(String message) {
        mLoginView.showDialog(getString(R.string.app_name), message, null, R.string.ok);
        dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                if (v.getTag() == null)
                    passwordPresenter.validUserName(etUserName.getText());
                else
                    passwordPresenter.forgotPassword(etUserName.getText(), etNewPassword.getText(), etOTP.getText(), (OTP) v.getTag());
                break;
            case R.id.btn_resend:
                btnSubmit.setTag(null);
                passwordPresenter.validUserName(etUserName.getText());
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null)
            countDownTimer.cancel();
    }
}
