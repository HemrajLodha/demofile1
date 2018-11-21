package com.pws.pateast.fragment.fees.invoice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pws.pateast.R;
import com.pws.pateast.base.AppDialogFragment;
import com.pws.pateast.base.Extras;
import com.pws.pateast.fragment.fees.FeesPaymentView;
import com.pws.pateast.utils.Utils;

/**
 * Created by intel on 05-Feb-18.
 */

public class FeesInvoiceFragment extends AppDialogFragment implements FeesInvoiceView {
    private EditText etEmail;
    private Button btnSend;
    private FeesPaymentView paymentView;

    private FeesInvoicePresenter mPresenter;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_fees_invoice;
    }

    public void attachView(FeesPaymentView paymentView) {
        this.paymentView = paymentView;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);
        setTitle(isInvoice() ? R.string.label_send_invoice : R.string.label_send_challan);
        etEmail = (EditText) findViewById(R.id.et_email);
        btnSend = (Button) findViewById(R.id.btn_send);

        mPresenter = new FeesInvoicePresenter();
        mPresenter.attachView(this);

        btnSend.setOnClickListener(this);
    }

    @Override
    public boolean isError() {
        boolean isError = false;
        etEmail.setError(null);
        if (TextUtils.isEmpty(etEmail.getText())) {
            etEmail.setError(getString(R.string.enter_email));
            isError = true;
        } else if (!Utils.isValidEmail(etEmail.getText())) {
            etEmail.setError(getString(R.string.valid_email));
            isError = true;
        }
        return isError;
    }

    @Override
    public boolean isInvoice() {
        return getArguments().getBoolean(Extras.PAYMENT_MODE);
    }

    @Override
    public int getPayFeeId() {
        return getArguments().getInt(Extras.PAY_FEE_ID);
    }

    @Override
    public void navigateToPayment(String message) {
        paymentView.showDialog(getString(R.string.app_name), message, null, R.string.ok);
        dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                mPresenter.sendMail(etEmail.getText());
                break;
        }
    }


}
