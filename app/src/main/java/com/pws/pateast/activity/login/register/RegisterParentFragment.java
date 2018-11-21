package com.pws.pateast.activity.login.register;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pws.pateast.R;
import com.pws.pateast.activity.login.LoginView;
import com.pws.pateast.api.model.OTP;
import com.pws.pateast.base.AppDialogFragment;
import com.pws.pateast.base.Extras;

/**
 * Created by intel on 09-Sep-17.
 */

public class RegisterParentFragment extends AppDialogFragment implements RegisterParentView, View.OnClickListener {
    private EditText etName, etEmail, etOTP, etMobile;
    private TextInputLayout tilName, tilEmail, tilOTP, tilMobile;

    private Button btnRegisterProfile;
    private Button btnResend;

    private CountDownTimer countDownTimer = null;

    private RegisterParentPresenter mPresenter;

    private LoginView mLoginView;

    public void attachView(LoginView mLoginView) {
        this.mLoginView = mLoginView;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_register_parent;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);
        setTitle(R.string.parent_register);

        etName = (EditText) findViewById(R.id.et_name);
        etEmail = (EditText) findViewById(R.id.et_email);
        etOTP = (EditText) findViewById(R.id.et_otp);
        etMobile = (EditText) findViewById(R.id.et_mobile);

        tilName = (TextInputLayout) findViewById(R.id.til_name);
        tilEmail = (TextInputLayout) findViewById(R.id.til_email);
        tilOTP = (TextInputLayout) findViewById(R.id.til_otp);
        tilMobile = (TextInputLayout) findViewById(R.id.til_mobile);

        btnRegisterProfile = (Button) findViewById(R.id.btn_register);
        btnResend = (Button) findViewById(R.id.btn_resend);

        etName.addTextChangedListener(new OnTextChangedListener(etName.getId()));
        etEmail.addTextChangedListener(new OnTextChangedListener(etEmail.getId()));
        etOTP.addTextChangedListener(new OnTextChangedListener(etOTP.getId()));
        etMobile.addTextChangedListener(new OnTextChangedListener(etMobile.getId()));

        btnRegisterProfile.setOnClickListener(this);


        mPresenter = new RegisterParentPresenter();
        mPresenter.attachView(this);

        // set otp if auto verified
        setOtp();
    }

    @Override
    public void setError(int id, String error) {
        switch (id) {
            case R.id.et_name:
                tilName.setError(error);
                tilName.setErrorEnabled(error != null);
                break;
            case R.id.et_email:
                tilEmail.setError(error);
                tilEmail.setErrorEnabled(error != null);
                break;
            case R.id.et_otp:
                tilOTP.setError(error);
                tilOTP.setErrorEnabled(error != null);
                break;
            case R.id.et_mobile:
                tilMobile.setError(error);
                tilMobile.setErrorEnabled(error != null);
                break;

        }
    }

    @Override
    public OTP getOtp() {
        return getArguments().getParcelable(Extras.OTP);
    }

    @Override
    public String getCountryCode() {
        return getArguments().getString(Extras.COUNTRY_CODE);
    }

    @Override
    public String getMobileNumber() {
        return getArguments().getString(Extras.MOBILE_NUMBER);
    }

    @Override
    public void setMobileNumber(String mobileNumber) {
        etMobile.setText(mobileNumber);
        etMobile.setFocusable(false);
        etMobile.setFocusableInTouchMode(false);
        etMobile.setEnabled(false);
        setError(R.id.et_mobile, getString(R.string.mobile_unique));
    }

    public void setOtp() {
        OTP otp = getOtp();
        if (otp != null) {
            if (!TextUtils.isEmpty(otp.getUid())) {
                etOTP.setVisibility(View.GONE);
                btnResend.setVisibility(View.GONE);
            } else {
                etOTP.setVisibility(View.VISIBLE);
                etOTP.setText(getOtp().getSmsCode());
                btnResend.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void registerParent(String name, String email, String enteredOTP) {
        if (mLoginView != null) {
            mLoginView.registerParent(name, email, getMobileNumber(), getCountryCode(), enteredOTP, getOtp());
        }
    }

    @Override
    public void startTimer(OTP otp) {
        setMobileNumber(getMobileNumber());

        countDownTimer = new CountDownTimer(60 * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                btnResend.setOnClickListener(null);
                btnResend.setText(DateUtils.formatElapsedTime(millisUntilFinished / 1000));
            }

            public void onFinish() {
                btnResend.setOnClickListener(RegisterParentFragment.this);
                btnResend.setText(R.string.resend_otp);
                countDownTimer = null;
            }
        };
        countDownTimer.start();
    }

    @Override
    public void updateTimer() {
        startTimer(getOtp());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                if (mPresenter != null) {
                    mPresenter.validateRegisterProfile(etName.getText(), etEmail.getText(), etOTP.getText());
                }
                break;
            case R.id.btn_resend:
                if (mLoginView != null)
                    mLoginView.resendOTP();
                break;
        }
    }

    class OnTextChangedListener implements TextWatcher {
        int id;

        public OnTextChangedListener(int id) {
            this.id = id;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            setError(id, null);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
