package com.pws.pateast.activity.parent.fees;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import com.paynimo.android.payment.PaymentActivity;
import com.paynimo.android.payment.PaymentModesActivity;
import com.paynimo.android.payment.model.Checkout;
import com.pws.pateast.Constants;
import com.pws.pateast.R;
import com.pws.pateast.activity.parent.fees.checkout.CheckoutFragment;
import com.pws.pateast.api.model.Fees;
import com.pws.pateast.base.AppDialogFragment;
import com.pws.pateast.base.Extras;
import com.pws.pateast.base.ResponseAppActivity;
import com.pws.pateast.listener.AdapterListener;
import com.pws.pateast.utils.DateUtils;

import java.util.Calendar;

import java8.util.stream.StreamSupport;

import static com.pws.pateast.utils.DateUtils.SERVER_DATE_TEMPLATE;


/**
 * Created by intel on 01-Feb-18.
 */

public class WardFeesActivity extends ResponseAppActivity implements WardFeesView {
    private TableLayout tableFees;
    private WardFeesPresenter mPresenter;
    private Intent intent;
    private LinearLayout layoutAction;
    private Button btnPayNow;

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public boolean isToolbarSetupEnabled() {
        return true;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.activity_ward_fees;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);
        intent = getIntent();
        tableFees = findViewById(R.id.table_fees);
        layoutAction = findViewById(R.id.layout_action);
        btnPayNow = findViewById(R.id.btn_pay);
        mPresenter = new WardFeesPresenter();
        mPresenter.attachView(this);

        btnPayNow.setOnClickListener(this);
    }

    @Override
    public Fees getFees() {
        return intent.getParcelableExtra(Extras.FEES);
    }

    @Override
    public void setFeesDetail(Fees feesDetail) {
        double amount = feesDetail.getAmount();
        double penalty = 0;
        double discount = 0;
        long days = DateUtils.getDaysCountBetweenDate(feesDetail.getDate(), DateUtils.toDate(Calendar.getInstance().getTime(), SERVER_DATE_TEMPLATE), SERVER_DATE_TEMPLATE);

        mPresenter.addFeeChargeLayout(tableFees, feesDetail.getFeehead().getFeeheaddetails().get(0).getName(), amount, HEADS);
        if (days > 0) {
            for (Fees fees : feesDetail.getFee().getFeeallocationpenalties()) {
                double amountPenalty = 0;
                Fees feePenalty = StreamSupport.stream(fees.getFeepenalty().getFeepenaltyslabs())
                        .sorted((o1, o2) -> {
                            Integer day1 = new Integer(o1.getDays());
                            Integer day2 = new Integer(o2.getDays());
                            return day1.compareTo(day2);
                        })
                        .filter(feesRecord -> {
                            return days <= feesRecord.getDays();
                        })
                        .findFirst()
                        .orElseGet(() -> {
                            int count = fees.getFeepenalty().getFeepenaltyslabs().size();
                            return fees.getFeepenalty().getFeepenaltyslabs().get(count - 1);
                        });
                if (feePenalty != null) {
                    amountPenalty = feePenalty.getAmount();
                    mPresenter.addFeeChargeLayout(tableFees, fees.getFeepenalty().getFeepenaltydetails().get(0).getName(), amountPenalty, PENALTY);
                    penalty += amountPenalty;
                }
            }
        }
        for (Fees fees : feesDetail.getFeehead().getFeediscounts()) {
            double value = fees.getValue();
            mPresenter.addFeeChargeLayout(tableFees, fees.getFeediscountdetails().get(0).getName(), value, DISCOUNT);
            discount += value;
        }
        mPresenter.addFeeChargeLayout(tableFees, getString(R.string.amount_payable), amount + penalty - discount, TOTAL);
        layoutAction.setVisibility(View.GONE);
        /*switch (feesOldDetail.getFee_status()) {
            case UNPAID:
                layoutAction.setVisibility(View.VISIBLE);
                return;
            case PAID:
                layoutAction.setVisibility(View.GONE);
                return;
        }*/


    }

    @Override
    public void doCheckout(String paymentMode) {
        mPresenter.doCheckout(paymentMode);
    }

    @Override
    public void setPayFeeID(String payFeeID) {
        intent.putExtra(Extras.PAY_FEE_ID, payFeeID);
    }

    @Override
    public String getPayFeeID() {
        return intent.getStringExtra(Extras.PAY_FEE_ID);
    }

    @Override
    public void proceedCheckout(String paymentMode, Checkout checkout) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(PaymentActivity.ARGUMENT_DATA_CHECKOUT, checkout);
        bundle.putString(PaymentActivity.EXTRA_PUBLIC_KEY, Constants.EXTRA_PUBLIC_KEY);
        bundle.putString(PaymentActivity.EXTRA_REQUESTED_PAYMENT_MODE, paymentMode);

        openActivityForResult(PaymentModesActivity.class, bundle, PaymentActivity.REQUEST_CODE);
    }

    @Override
    public void navigateToPayment(String message) {
        showDialog(getString(R.string.app_name),
                message,
                new AdapterListener<String>() {
                    @Override
                    public void onClick(int id, String value) {
                        setResult(RESULT_OK);
                        finish();
                    }
                }, R.string.ok);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pay:
                CheckoutFragment checkoutFragment = AppDialogFragment.newInstance(CheckoutFragment.class, this);
                checkoutFragment.attachView(this);
                checkoutFragment.show(getBaseFragmentManager(), CheckoutFragment.class.getSimpleName());
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PaymentActivity.REQUEST_CODE:
                if (resultCode == PaymentActivity.RESULT_OK) {
                    if (data != null) {
                        if (mPresenter != null) {
                            mPresenter.updateCheckout((Checkout) data.getSerializableExtra(PaymentActivity.ARGUMENT_DATA_CHECKOUT));
                            return;
                        }
                    }
                }
                break;
        }
    }
}
