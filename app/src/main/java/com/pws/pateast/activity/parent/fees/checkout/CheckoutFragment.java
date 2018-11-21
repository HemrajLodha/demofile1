package com.pws.pateast.activity.parent.fees.checkout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.ListPopupWindow;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.pws.pateast.R;
import com.pws.pateast.activity.parent.fees.WardFeesView;
import com.pws.pateast.base.AppDialogFragment;
import com.pws.pateast.base.Extras;
import com.pws.pateast.fragment.complaint.detail.ComplaintDetailView;

import java.util.List;

/**
 * Created by intel on 02-Feb-18.
 */

public class CheckoutFragment extends AppDialogFragment implements CheckoutView {
    private TextView tvSelectPayment;
    private ListPopupWindow popUpPayment;
    private Button btnCheckout;
    private CheckoutPresenter mPresenter;
    private ArrayAdapter<String> paymentModeAdapter;
    private WardFeesView feesView;
    private ComplaintDetailView detailView;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_checkout;
    }

    public void attachView(WardFeesView feesView) {
        this.feesView = feesView;
    }

    public void attachView(ComplaintDetailView detailView) {
        this.detailView = detailView;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);
        setTitle(R.string.checkout);
        tvSelectPayment = (TextView) findViewById(R.id.tv_select_payment);

        btnCheckout = (Button) findViewById(R.id.btn_checkout);
        mPresenter = new CheckoutPresenter();
        mPresenter.attachView(this);

        btnCheckout.setOnClickListener(this);
        tvSelectPayment.setOnClickListener(this);
    }

    @Override
    public boolean isError() {
        boolean isError = getPaymentMode() == null;
        tvSelectPayment.setError(null);
        if (isError) {
            tvSelectPayment.setError(getString(R.string.validate_spinner, getString(R.string.hint_payment_mode)));
        }
        return isError;
    }

    @Override
    public void setPaymentMode(String paymentMode) {
        if (paymentMode != null) {
            tvSelectPayment.setText(paymentMode);
        } else {
            tvSelectPayment.setText(R.string.hint_select_payment);
        }
        getArguments().putString(Extras.PAYMENT_MODE, paymentMode);
    }

    @Override
    public String getPaymentMode() {
        return getArguments().getString(Extras.PAYMENT_MODE);
    }

    @Override
    public void setPaymentModeAdapter(List<String> modes) {
        if (popUpPayment == null) {
            popUpPayment = new ListPopupWindow(getContext());
            popUpPayment.setAnchorView(tvSelectPayment);
            popUpPayment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (paymentModeAdapter != null) {
                        setPaymentMode(paymentModeAdapter.getItem(position));
                    } else {
                        setPaymentMode(null);
                    }
                    popUpPayment.dismiss();
                }
            });
        }
        if (paymentModeAdapter == null) {
            paymentModeAdapter = new ArrayAdapter(getContext(), R.layout.item_spinner_dropdown, R.id.tv_spinner, modes);
            String paymentMode = getPaymentMode();
            if (paymentMode != null) {
                setPaymentMode(paymentMode);
            }
        }

        popUpPayment.setAdapter(paymentModeAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_select_payment:
                if (popUpPayment != null)
                    popUpPayment.show();
                break;
            case R.id.btn_checkout:
                if (!isError() && feesView != null) {
                    dismiss();
                    feesView.doCheckout(getPaymentMode());
                }
                else if (!isError() && detailView!= null) {
                    dismiss();
                    detailView.doCheckout(getPaymentMode());
                }
                break;
        }
    }
}
