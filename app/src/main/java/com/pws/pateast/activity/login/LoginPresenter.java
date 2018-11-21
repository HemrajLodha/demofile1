package com.pws.pateast.activity.login;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.pws.pateast.R;
import com.pws.pateast.activity.parent.ward.WardSelectionActivity;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.exception.RetrofitException;
import com.pws.pateast.api.model.OTP;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.model.Ward;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.APIService;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.utils.Preference;
import com.pws.pateast.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by intel on 19-Apr-17.
 */

public class LoginPresenter extends AppPresenter<LoginView> {
    @Inject
    ServiceBuilder serviceBuilder;
    @Inject
    Preference preference;

    private APIService apiService;
    private LoginView mLoginView;

    private OTP otpResponse;
    private FirebaseAuth mAuth;


    @Override
    public LoginView getView() {
        return mLoginView;
    }

    @Override
    public void attachView(LoginView loginView) {
        mLoginView = loginView;
        getComponent().inject(this);
        getView().setTabLayout();
        mAuth = FirebaseAuth.getInstance();
    }

    public Ward getWard() {
        return preference.getWard();
    }

    public UserType getUserType() {
        if (preference.getUser() == null)
            return UserType.NONE;
        return UserType.getUserType(preference.getUser().getData().getUser_type());
    }

    /***
     * signout if already sign in
     */
    private void signOutFirebaseUser() {
        if (mAuth.getCurrentUser() != null) {
            mAuth.signOut();
        }
    }

    public void clear() {
        preference.clear();
        getView().showMessage("Invalid login details.", true, R.attr.colorPrimary);
    }

    public List<LoginTab> getLoginTabs() {
        List<LoginTab> loginTabs = new ArrayList<>();
        loginTabs.add(new LoginTab.Builder().setUserType(UserType.PARENT).setText(UserType.PARENT.getName()).setIcon(R.drawable.login_parent).build());
        loginTabs.add(new LoginTab.Builder().setUserType(UserType.STUDENT).setText(UserType.STUDENT.getName()).setIcon(R.drawable.login_student).build());
        loginTabs.add(new LoginTab.Builder().setUserType(UserType.TEACHER).setText(UserType.TEACHER.getName()).setIcon(R.drawable.login_teacher).build());
        loginTabs.add(new LoginTab.Builder().setUserType(UserType.DRIVER).setText(UserType.DRIVER.getName()).setIcon(R.drawable.login_driver).build());
        return loginTabs;
    }

    public void validateCredentials(CharSequence username, CharSequence password) {
        if (mLoginView != null) {

            if (TextUtils.isEmpty(username)) {
                mLoginView.setError(getString(R.string.enter_username));
            } else if (TextUtils.isEmpty(password.toString().trim())) {
                mLoginView.setError(getString(R.string.enter_password));
            } else if (!Utils.isValidPassword(password)) {
                mLoginView.setError(getString(R.string.valid_password));
            } else {
                login(username, password);
            }
        }

    }

    public void verifyPhoneNumber(CharSequence mobile) {
        if (mLoginView != null) {
            if (TextUtils.isEmpty(mobile)) {
                mLoginView.setError(getString(R.string.validate_edittext, getString(R.string.user_mobile)));
            } else if (!Utils.isValidMobile(mobile.toString().trim())) {
                mLoginView.setError(getString(R.string.valid_mobile));
            } else {
                LoginActivityPermissionsDispatcher.readSMSWithCheck((LoginActivity) getView(), mobile.toString());
            }
        }
    }

    public void validateMobile(CharSequence mobile) {
        if (mLoginView != null) {
            if (TextUtils.isEmpty(mobile)) {
                mLoginView.setError(getString(R.string.validate_edittext, getString(R.string.user_mobile)));
            } else if (!Utils.isValidMobile(mobile.toString().trim())) {
                mLoginView.setError(getString(R.string.valid_mobile));
            } else {
                LoginActivityPermissionsDispatcher.readSMSWithCheck((LoginActivity) getView(), mobile.toString());
            }
        }
    }

    public void validateOTP(CharSequence mobile, String countryCode, CharSequence enteredOTP, OTP otp) {
        if (mLoginView != null) {
            if (TextUtils.isEmpty(enteredOTP)) {
                mLoginView.setError(getString(R.string.validate_edittext, getString(R.string.otp)));
            } else {
                verifyOtp(mobile.toString(), countryCode, enteredOTP.toString(), otp);
            }
        }
    }

    public void login(CharSequence username, CharSequence password) {
        mLoginView.showProgressDialog(getString(R.string.login_in));
        apiService = serviceBuilder.createLogin(APIService.class);

        disposable = apiService.login(username.toString(), password.toString(),
                ServiceBuilder.DEVICE_TYPE, ServiceBuilder.DEVICE_TOKEN,
                preference.getLanguage())
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<User>() {
                    @Override
                    public boolean openDialog() {
                        return true;
                    }

                    @Override
                    public void onResponse(User response) {
                        response = preference.getUser();
                        if (response != null && response.getPrimaryLang() != null) {
                            preference.setLanguageID(response.getData().getDefault_lang());
                            preference.setLanguage(response.getPrimaryLang().getCode());
                        }
                        mLoginView.navigateToHome(new Bundle());
                    }

                    @Override
                    public LoginPresenter getPresenter() {
                        return LoginPresenter.this;
                    }
                });
    }

    /**
     * validate firebase otp
     *
     * @param mobile
     * @param countryCode
     * @param enteredOTP
     */
    void validateFirebaseOTP(CharSequence mobile, String countryCode, CharSequence enteredOTP) {
        if (mLoginView != null) {
            if (TextUtils.isEmpty(enteredOTP)) {
                mLoginView.setError(getString(R.string.validate_edittext, getString(R.string.otp)));
            } else {
                signInWithPhoneAuthCredential(mobile, countryCode, enteredOTP);
            }
        }
    }


    private void loginOrRegisterParent(boolean autoLogin, boolean isRegistered, String fullName, String email) {
        if (otpResponse.getVerify() == 1) {
            if (isRegistered) {
                registerParentAndVerify(getView().getMobileNumber(), getView().getCountryCode(), fullName, email);
            } else {
                getView().openRegisterParent(otpResponse);
            }
        } else if (otpResponse.getVerify() == 2) {
            if (autoLogin) {
                verifyUser(getView().getMobileNumber(), getView().getCountryCode());
            } else {

                getView().startTimer(otpResponse);
            }
        }
    }

    private void signInWithAuthToken(PhoneAuthCredential phoneAuthCredential) {
        signInWithAuthToken(phoneAuthCredential, true, false, null, null);
    }

    private void signInWithAuthToken(PhoneAuthCredential phoneAuthCredential, boolean autoLogin, boolean isRegistered, String fullName, String email) {
        mLoginView.showProgressDialog(getString(R.string.wait));
        mAuth
                .signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(task -> {
                    mLoginView.hideProgressDialog();
                    if (task.isSuccessful()) {
                        if (task.getResult() != null) {
                            FirebaseUser user = task.getResult().getUser();
                            otpResponse.setUid(user.getUid());
                            user.getIdToken(true).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    if (task1.getResult() != null) {
                                        String idToken = task1.getResult().getToken();
                                        otpResponse.setIdToken(idToken);
                                    }
                                    signOutFirebaseUser();
                                    loginOrRegisterParent(autoLogin, isRegistered, fullName, email);
                                } else {
                                    LoginPresenter.this.getView().setError(getString(R.string.something_went_wrong));
                                }
                            });
                        }
                    } else {
                        Exception exception = task.getException();
                        Log.e(
                                "Login",
                                "signInWithPhoneNumber:autoVerified:signInWithCredential:onComplete:failure",
                                exception
                        );
                        LoginPresenter.this.getView().setError(exception.getLocalizedMessage());
                    }
                });
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mFirebaseCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            mLoginView.hideProgressDialog();
            if (phoneAuthCredential.getSmsCode() == null) {
                signInWithAuthToken(phoneAuthCredential);
            } else {
                otpResponse.setSmsCode(phoneAuthCredential.getSmsCode());
                if (otpResponse.getVerify() == 1) {
                    getView().openRegisterParent(otpResponse);
                } else if (otpResponse.getVerify() == 2) {
                    getView().startTimer(otpResponse);
                }
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            mLoginView.hideProgressDialog();
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                LoginPresenter.this.getView().setError(getString(R.string.message_invalid_request));
            } else if (e instanceof FirebaseTooManyRequestsException) {
                LoginPresenter.this.getView().setError(getString(R.string.message_sms_quota_exceeded));
            }
        }

        @Override
        public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(verificationId, forceResendingToken);
            mLoginView.showProgressDialog(getString(R.string.message_auto_detecting));
            otpResponse.setVerificationId(verificationId);
        }

        @Override
        public void onCodeAutoRetrievalTimeOut(String verificationId) {
            super.onCodeAutoRetrievalTimeOut(verificationId);
            mLoginView.hideProgressDialog();
            LoginPresenter.this.getView().setError(getString(R.string.message_auto_verification_failed));
            if (otpResponse.getVerify() == 1) {
                getView().openRegisterParent(otpResponse);
            } else if (otpResponse.getVerify() == 2) {
                getView().startTimer(otpResponse);
            }
        }
    };

    private void sendFirebaseOTP(String countryCode, String mobile) {
        mLoginView.showProgressDialog(getString(R.string.wait));
        signOutFirebaseUser();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                countryCode + mobile,        // Phone number to verify
                30,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                (Activity) getView(),               // Activity (for callback binding)
                mFirebaseCallbacks);        // OnVerificationStateChangedCallbacks
    }

    private void signInWithPhoneAuthCredential(CharSequence mobile, String countryCode, CharSequence code) {
        signInWithPhoneAuthCredential(mobile, countryCode, code, true, null, null);
    }

    void signInWithPhoneAuthCredential(CharSequence mobile, String countryCode, CharSequence code, boolean autoLogin, String fullName, String email) {

        // call server api if uid already exits
        if (!TextUtils.isEmpty(otpResponse.getUid())) {
            if (otpResponse.getVerify() == 1) {
                registerParentAndVerify(mobile.toString(), countryCode, fullName, email);
            } else if (otpResponse.getVerify() == 2) {
                verifyUser(mobile.toString(), countryCode);
            }
            return;
        }

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpResponse.getVerificationId(), code.toString());
        signInWithAuthToken(credential, autoLogin, true, fullName, email);
    }

    public void sendOTP(String countryCode, String mobile) {
        mLoginView.showProgressDialog(getString(R.string.wait));
        apiService = serviceBuilder.createToken(APIService.class);
        disposable = apiService.verifyMobileV2(countryCode, mobile)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<OTP>() {
                    @Override
                    public boolean openDialog() {
                        return true;
                    }

                    @Override
                    public void onResponse(OTP response) {
                        /*if (response.getRstatus() != null && !response.getData().isEmpty()) {
                            OTP otp = response.getData().get(0);
                            if (otp.getVerify() != 0) {
                                validateOTPResponse(otp.getVerify(), otp.getOtp());
                                return;
                            }
                        }*/
                        if (response.isStatus()) {
                            if (response.getVerify() != 0) {
                                otpResponse = response;
                                sendFirebaseOTP(countryCode, mobile);
                                return;
                            }
                        }
                        LoginPresenter.this.getView().setError(response.getMessage());
                    }


                    @Override
                    public LoginPresenter getPresenter() {
                        return LoginPresenter.this;
                    }
                });
    }

    private void verifyUser(String mobile, String countryCode) {
        getView().showProgressDialog(getString(R.string.login_in));
        apiService = serviceBuilder.createLogin(APIService.class);
        disposable = apiService.loginParent(mobile, otpResponse.getIdToken(), otpResponse.getUid(), countryCode, "2",
                ServiceBuilder.DEVICE_TYPE, ServiceBuilder.DEVICE_TOKEN,
                "1", preference.getLanguage())
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new LoginObserver());
    }

    private void registerParentAndVerify(String mobile, String countryCode, String fullname, String email) {
        getView().showProgressDialog(getString(R.string.login_in));
        apiService = serviceBuilder.createLogin(APIService.class);
        disposable = apiService.registerVerifiedParent(fullname, email, mobile, otpResponse.getIdToken(), otpResponse.getUid(), countryCode, "1",
                ServiceBuilder.DEVICE_TYPE, ServiceBuilder.DEVICE_TOKEN,
                "1", preference.getLanguage())
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new LoginObserver());
    }

    private void verifyOtp(String mobile, String countryCode, String enteredOTP, OTP otp) {
        getView().showProgressDialog(getString(R.string.login_in));
        apiService = serviceBuilder.createLogin(APIService.class);
        disposable = apiService.verifyOtp(mobile, otp.getToken(), enteredOTP, countryCode, "2",
                ServiceBuilder.DEVICE_TYPE, ServiceBuilder.DEVICE_TOKEN,
                "1", preference.getLanguage())
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new LoginObserver());
    }

    public void registerParent(String fullname, String email, String mobile, String countryCode, String enteredOTP, OTP otp) {
        getView().showProgressDialog(getString(R.string.login_in));
        apiService = serviceBuilder.createLogin(APIService.class);
        disposable = apiService.registerParent(fullname, email, mobile, otp.getToken(), enteredOTP, countryCode, "1",
                ServiceBuilder.DEVICE_TYPE, ServiceBuilder.DEVICE_TOKEN,
                "1", preference.getLanguage())
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new LoginObserver());
    }

    private void validateOTPResponse(int verify, OTP otp) {
        if (otp.getRstatus().equals("SUCCESS")) {
            if (verify == 1) {
                getView().openRegisterParent(otp);
            } else if (verify == 2) {
                getView().startTimer(otp);
            }
        } else if (otp.getRstatus().equals("ERROR")) {
            if (otp.getMessage().equals("ERROR_INVALID_NUMBER"))
                otp.setMessage(getString(R.string.valid_mobile));
            getView().setError(otp.getMessage());
        }
    }


    public void openForgotPassword() {
        mLoginView.openForgotPassword();
    }

    private void setWard(ArrayList<Ward> wards) {
        Bundle bundle = new Bundle();
        if (wards.size() > 1)
            bundle.putParcelableArrayList(WardSelectionActivity.EXTRA_WARD_LIST, wards);
        else if (wards.size() == 1) {
            preference.setWard(wards.get(0));
        }

        getView().navigateToHome(bundle);
    }

    class LoginObserver extends AppSingleObserver<User> {
        @Override
        public boolean openDialog() {
            return true;
        }

        @Override
        public void onResponse(User response) {
            if (response.getData() != null) {
                if (response.getPrimaryLang() != null) {
                    preference.setLanguage(response.getPrimaryLang().getCode());
                    preference.setLanguageID(response.getData().getDefault_lang());
                    setWard(response.getData().getWard_list());
                }
            } else {
                getView().setActionVisibility(null);
                onError(new RetrofitException(response.getMessage()));
            }
        }

        @Override
        public LoginPresenter getPresenter() {
            return LoginPresenter.this;
        }
    }

}
