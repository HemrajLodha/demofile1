package com.pws.pateast.fragment.fees.invoice;

import com.pws.pateast.R;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.model.Fees;
import com.pws.pateast.api.model.Response;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.model.Ward;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.ParentService;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.utils.Preference;

import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by intel on 05-Feb-18.
 */

public class FeesInvoicePresenter extends AppPresenter<FeesInvoiceView> {
    @Inject
    ServiceBuilder serviceBuilder;
    @Inject
    Preference preference;
    private User user;
    private Ward ward;
    private ParentService apiService;

    private FeesInvoiceView mView;

    @Override
    public FeesInvoiceView getView() {
        return mView;
    }

    @Override
    public void attachView(FeesInvoiceView view) {
        mView = view;
        getComponent().inject(this);
        user = preference.getUser();
        ward = preference.getWard();
    }

    public void sendMail(CharSequence email) {
        if (getView().isError())
            return;

        getView().showProgressDialog(getString(R.string.wait));
        apiService = serviceBuilder.createService(ParentService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("academicSessionId", String.valueOf(ward.getAcademicSessionId()));
        params.put("masterId", String.valueOf(ward.getMasterId()));
        params.put("email", email.toString());
        params.put("feesubmissionId", String.valueOf(getView().getPayFeeId()));
        Single<Response<Fees>> single = null;
        if (getView().isInvoice())
            single = apiService.sendInvoiceEmail(params);
        else
            single = apiService.sendChallanEmail(params);
        disposable = single
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Response<Fees>>() {

                    @Override
                    public boolean openDialog() {
                        return true;
                    }

                    @Override
                    public void onResponse(Response<Fees> response) {
                        getView().navigateToPayment(response.getMessage());
                    }

                    @Override
                    public FeesInvoicePresenter getPresenter() {
                        return FeesInvoicePresenter.this;
                    }
                });
    }

}
