package com.pws.pateast.fragment.fees.invoice;

import android.view.View;

import com.pws.pateast.base.AppView;

/**
 * Created by intel on 05-Feb-18.
 */

public interface FeesInvoiceView extends AppView, View.OnClickListener {
    boolean isError();

    boolean isInvoice();

    int getPayFeeId();

    void navigateToPayment(String message);
}
