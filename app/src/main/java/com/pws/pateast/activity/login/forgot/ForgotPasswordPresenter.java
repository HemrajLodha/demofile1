package com.pws.pateast.activity.login.forgot;

import com.pws.pateast.R;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.model.OTP;
import com.pws.pateast.api.model.Response;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.APIService;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.utils.Preference;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by intel on 02-May-17.
 */

public class ForgotPasswordPresenter extends AppPresenter<ForgotPasswordView> {
    @Inject
    ServiceBuilder serviceBuilder;
    @Inject
    Preference preference;

    private APIService apiService;

    private ForgotPasswordView mPasswordView;

    @Override
    public ForgotPasswordView getView() {
        return mPasswordView;
    }

    @Override
    public void attachView(ForgotPasswordView view) {
        mPasswordView = view;
        getComponent().inject(this);
    }

    public void validUserName(CharSequence userName) {
        if (getView().isError())
            return;

        mPasswordView.showProgressDialog(getString(R.string.wait));
        apiService = serviceBuilder.createToken(APIService.class);

        disposable = apiService.checkUsername(ServiceBuilder.basic(), preference.getLanguage(), userName.toString())
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<OTP>() {
                    @Override
                    public boolean isDismiss() {
                        return false;
                    }

                    @Override
                    public boolean openDialog() {
                        return true;
                    }

                    @Override
                    public void onResponse(OTP response) {
                        OTP otp = response.getOtp();
                        if (otp.getRstatus().equals("SUCCESS")) {
                            getView().startTimer(otp);
                        } else if (otp.getRstatus().equals("ERROR")) {
                            if (otp.getMessage().equals("ERROR_INVALID_NUMBER"))
                                otp.setMessage(getString(R.string.valid_mobile));
                            getView().setError(R.id.et_user_name, otp.getMessage());
                        }
                    }

                    @Override
                    public ForgotPasswordPresenter getPresenter() {
                        return ForgotPasswordPresenter.this;
                    }
                });
    }

    public void forgotPassword(CharSequence userName, CharSequence password, CharSequence enteredOTP, OTP otp) {
        if (getView().isError())
            return;

        mPasswordView.showProgressDialog(getString(R.string.wait));
        apiService = serviceBuilder.createToken(APIService.class);

        disposable = apiService.resetPassword(preference.getLanguageID(), preference.getLanguage(), ServiceBuilder.DEVICE_TYPE, otp.getToken(), otp.getCountry_code(), enteredOTP.toString(), userName.toString(), password.toString())
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Response>() {
                    @Override
                    public boolean isDismiss() {
                        return false;
                    }

                    @Override
                    public boolean openDialog() {
                        return true;
                    }

                    @Override
                    public void onResponse(Response response) {
                        getView().navigateToLogin(response.getMessage());
                    }

                    @Override
                    public ForgotPasswordPresenter getPresenter() {
                        return ForgotPasswordPresenter.this;
                    }
                });
    }
}
