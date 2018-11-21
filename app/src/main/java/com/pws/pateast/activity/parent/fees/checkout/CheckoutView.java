package com.pws.pateast.activity.parent.fees.checkout;

import android.view.View;

import com.pws.pateast.base.AppView;

import java.util.List;

/**
 * Created by intel on 02-Feb-18.
 */

public interface CheckoutView extends AppView, View.OnClickListener {
    boolean isError();

    void setPaymentMode(String paymentMode);

    String getPaymentMode();

    void setPaymentModeAdapter(List<String> modes);
}
