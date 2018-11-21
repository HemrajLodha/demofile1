package com.pws.pateast.activity.parent.fees;

import android.view.View;

import com.paynimo.android.payment.model.Checkout;
import com.pws.pateast.api.model.Fees;
import com.pws.pateast.api.model.FeesOld;
import com.pws.pateast.base.AppView;

/**
 * Created by intel on 01-Feb-18.
 */

public interface WardFeesView extends AppView, View.OnClickListener {

    int HEADS = 0;
    int PENALTY = 1;
    int DISCOUNT = 2;
    int TOTAL = 3;


    Fees getFees();

    void setFeesDetail(Fees feesOld);

    void doCheckout(String paymentMode);

    void setPayFeeID(String payFeeID);

    String getPayFeeID();

    void proceedCheckout(String paymentMode, Checkout checkout);

    void navigateToPayment(String message);
}
