package com.pws.pateast.activity.login;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import com.hbb20.CountryCodePicker;
import com.pws.pateast.R;
import com.pws.pateast.activity.driver.home.DriverHomeActivity;
import com.pws.pateast.activity.login.forgot.ForgotPasswordFragment;
import com.pws.pateast.activity.login.register.RegisterParentFragment;
import com.pws.pateast.activity.parent.home.ParentHomeActivity;
import com.pws.pateast.activity.parent.ward.WardSelectionActivity;
import com.pws.pateast.activity.student.home.StudentHomeActivity;
import com.pws.pateast.activity.teacher.home.TeacherHomeActivity;
import com.pws.pateast.api.model.OTP;
import com.pws.pateast.base.AppDialogFragment;
import com.pws.pateast.base.Extras;
import com.pws.pateast.base.FragmentActivity;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.listener.SmsListener;
import com.pws.pateast.utils.Utils;
import com.pws.pateast.widget.FontTabLayout;

import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by intel on 19-Apr-17.
 */

@RuntimePermissions
public class LoginActivity extends FragmentActivity implements LoginView, View.OnClickListener, SmsListener, TabLayout.OnTabSelectedListener {

    private FontTabLayout tabLogin;
    private EditText etUserName, etPassword, etMobile, etOTP;
    private Button btnForgotPassword;
    private Button btnLogin;
    private Button btnVerify;
    private Button btnResend;
    private ViewFlipper flipLogin;
    private CountryCodePicker spCountryCode;
    private LinearLayout layoutOTP;
    private CountDownTimer countDownTimer = null;

    private LoginPresenter mLoginPresenter;
    private UserType userType;
    private boolean isParent = true;

    private RegisterParentFragment registerParentFragment;

    @Override
    protected int getResourceLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);

        layoutOTP = findViewById(R.id.layout_otp);
        flipLogin = findViewById(R.id.flip_login);
        spCountryCode = findViewById(R.id.sp_country_code);
        tabLogin = findViewById(R.id.tab_login);
        etUserName = findViewById(R.id.et_user_name);
        etPassword = findViewById(R.id.et_password);
        etMobile = findViewById(R.id.et_mobile_number);
        etOTP = findViewById(R.id.et_otp);
        btnForgotPassword = findViewById(R.id.btn_forgot_password);
        btnLogin = findViewById(R.id.btn_login);
        btnVerify = findViewById(R.id.btn_verify);
        btnResend = findViewById(R.id.btn_resend);

        btnLogin.setOnClickListener(this);
        btnForgotPassword.setOnClickListener(this);
        btnVerify.setOnClickListener(this);

        tabLogin.addOnTabSelectedListener(this);

        mLoginPresenter = new LoginPresenter();
        mLoginPresenter.attachView(this);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void setTabLayout() {
        List<LoginTab> loginTabs = mLoginPresenter.getLoginTabs();
        for (LoginTab loginTab : loginTabs) {
            tabLogin.addTab(tabLogin.newTab().setTag(loginTab).setText(loginTab.getText()).setIcon(loginTab.getIcon()), loginTabs.indexOf(loginTab) == 0);
        }
    }

    @Override
    public void setError(String error) {
        Utils.keyboard(getContext(), btnLogin, false);
        showMessage(error, true, R.attr.colorPrimary);
    }

    @Override
    public void navigateToHome(Bundle bundle) {
        if (mLoginPresenter.getUserType() == userType) {
            Class activityClass = null;
            switch (userType) {
                case STUDENT:
                    activityClass = StudentHomeActivity.class;
                    break;
                case PARENT:
                    activityClass = mLoginPresenter.getWard() == null ? WardSelectionActivity.class : ParentHomeActivity.class;
                    break;
                case TEACHER:
                    activityClass = TeacherHomeActivity.class;
                    break;
                case DRIVER:
                    activityClass = DriverHomeActivity.class;
                    break;
            }
            openActivity(activityClass, bundle);
            finish();
        } else {
            mLoginPresenter.clear();
        }
    }

    @Override
    public void openForgotPassword() {
        ForgotPasswordFragment passwordFragment = AppDialogFragment.newInstance(ForgotPasswordFragment.class, this);
        passwordFragment.attachView(this);
        passwordFragment.show(getBaseFragmentManager(), ForgotPasswordFragment.class.getSimpleName());
    }

    @Override
    public void openRegisterParent(OTP otp) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Extras.OTP, otp);
        bundle.putString(Extras.MOBILE_NUMBER, etMobile.getText().toString());
        bundle.putString(Extras.COUNTRY_CODE, spCountryCode.getSelectedCountryCodeWithPlus());

        if (registerParentFragment == null || !registerParentFragment.isVisible()) {
            registerParentFragment = AppDialogFragment.newInstance(RegisterParentFragment.class, this, bundle);
            registerParentFragment.attachView(this);
            registerParentFragment.show(getBaseFragmentManager(), RegisterParentFragment.class.getSimpleName());
        } else {
            registerParentFragment.setArguments(bundle);
            registerParentFragment.updateTimer();
        }
    }


    @NeedsPermission({Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS})
    @Override
    public void readSMS(String mobile) {
        mLoginPresenter.sendOTP(spCountryCode.getSelectedCountryCodeWithPlus(), mobile);
        //mLoginPresenter.sendFirebaseOTP(spCountryCode.getSelectedCountryCodeWithPlus(), mobile);
    }

    @Override
    public void startTimer(OTP otp) {
        setActionVisibility(otp);
        countDownTimer = new CountDownTimer(60 * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                btnResend.setOnClickListener(null);
                btnResend.setText(DateUtils.formatElapsedTime(millisUntilFinished / 1000));
            }

            public void onFinish() {
                btnResend.setOnClickListener(LoginActivity.this);
                btnResend.setText(R.string.resend_otp);
                countDownTimer = null;
            }
        };
        countDownTimer.start();
    }

    @Override
    public void resendOTP() {
        mLoginPresenter.validateMobile(etMobile.getText());
    }

    @Override
    public void registerParent(String name, String email, String mobile, String countryCode, String enteredOTP, OTP otp) {
        if (registerParentFragment != null) {
            registerParentFragment.dismiss();
            mLoginPresenter.signInWithPhoneAuthCredential(mobile, countryCode, enteredOTP, false, name, email);
            registerParentFragment = null;
        }
    }


    @Override
    public void setActionVisibility(OTP otp) {
        if (otp != null) {
            layoutOTP.setVisibility(View.VISIBLE);
            btnResend.setVisibility(View.VISIBLE);
            btnVerify.setText(R.string.verify_otp);
            spCountryCode.setCcpClickable(false);
            etMobile.setFocusable(false);
            etMobile.setFocusableInTouchMode(false);
            btnVerify.setTag(otp);
        } else {
            layoutOTP.setVisibility(View.INVISIBLE);
            btnResend.setVisibility(View.GONE);
            btnVerify.setText(R.string.get_otp);
            spCountryCode.setCcpClickable(true);
            etMobile.setFocusable(true);
            etMobile.setFocusableInTouchMode(true);
            etMobile.setText("");
            etOTP.setText("");
            btnVerify.setTag(otp);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                mLoginPresenter.validateCredentials(etUserName.getText(), etPassword.getText());
                break;
            case R.id.btn_forgot_password:
                etUserName.clearFocus();
                etPassword.clearFocus();
                mLoginPresenter.openForgotPassword();
                break;
            case R.id.btn_verify:
                if (v.getTag() == null)
                    mLoginPresenter.validateMobile(etMobile.getText());
                else
                    //mLoginPresenter.validateOTP(etMobile.getText(), spCountryCode.getSelectedCountryCodeWithPlus(), etOTP.getText(), (OTP) v.getTag());
                    mLoginPresenter.validateFirebaseOTP(etMobile.getText(), spCountryCode.getSelectedCountryCodeWithPlus(), etOTP.getText());
                break;
            case R.id.btn_resend:
                resendOTP();
                break;
        }
    }

    @Override
    public String getMobileNumber() {
        return etMobile.getText().toString();
    }

    @Override
    public String getCountryCode() {
        return spCountryCode.getSelectedCountryCodeWithPlus();
    }

    @Override
    protected void onDestroy() {
        mLoginPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        userType = ((LoginTab) tab.getTag()).getUserType();
        switch (userType) {
            case PARENT:
                if (!isParent) {
                    setActionVisibility(null);
                    flipLogin.showPrevious();
                    isParent = true;
                    //SmsReceiver.bindListener(this);
                }
                break;
            default:
                if (isParent) {
                    flipLogin.showNext();
                    isParent = false;
                    //SmsReceiver.bindListener(null);
                }
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LoginActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void messageReceived(String messageText) {
        if (userType == UserType.PARENT) {
            etOTP.setText(messageText);
        }
    }
}
