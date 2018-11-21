package com.pws.pateast.activity.parent.fees.checkout;

import com.paynimo.android.payment.PaymentActivity;
import com.pws.pateast.base.AppPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by intel on 02-Feb-18.
 */

public class CheckoutPresenter extends AppPresenter<CheckoutView> {
    private CheckoutView mView;

    @Override
    public CheckoutView getView() {
        return mView;
    }

    @Override
    public void attachView(CheckoutView view) {
        mView = view;
        getView().setPaymentModeAdapter(getPaymentModes());
    }

    private List<String> getPaymentModes() {
        List<String> list = new ArrayList<>();
        list.add(PaymentActivity.PAYMENT_METHOD_CARDS);
        list.add(PaymentActivity.PAYMENT_METHOD_NETBANKING);
        return list;
    }
}
