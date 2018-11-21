package com.pws.pateast.activity.parent.fees;

import android.support.annotation.IntDef;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.paynimo.android.payment.PaymentActivity;
import com.paynimo.android.payment.model.Checkout;
import com.paynimo.android.payment.model.response.ResponsePayload;
import com.paynimo.android.payment.model.response.g;
import com.pws.pateast.R;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.model.FeesOld;
import com.pws.pateast.api.model.FeesPayment;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.model.Ward;
import com.pws.pateast.api.service.ParentService;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.utils.Preference;

import org.json.JSONException;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import javax.inject.Inject;

import static android.graphics.Typeface.BOLD;
import static com.pws.pateast.activity.parent.fees.WardFeesView.DISCOUNT;
import static com.pws.pateast.activity.parent.fees.WardFeesView.HEADS;
import static com.pws.pateast.activity.parent.fees.WardFeesView.PENALTY;
import static com.pws.pateast.activity.parent.fees.WardFeesView.TOTAL;

/**
 * Created by intel on 01-Feb-18.
 */

public class WardFeesPresenter extends AppPresenter<WardFeesView> {
    @Inject
    Preference preference;

    @Inject
    ServiceBuilder serviceBuilder;
    private User user;
    private Ward ward;
    private ParentService apiService;

    private WardFeesView mFeesView;

    @Override
    public WardFeesView getView() {
        return mFeesView;
    }

    @Override
    public void attachView(WardFeesView view) {
        mFeesView = view;
        getComponent().inject(this);
        user = preference.getUser();
        ward = preference.getWard();

        getView().setFeesDetail(getView().getFees());
    }


    public void doCheckout(final String paymentMode) {
        /*getView().showProgressDialog(getString(R.string.wait));
        apiService = serviceBuilder.createService(ParentService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("full_host", API_BASE_URL);
        params.put("parent_id", String.valueOf(user.getData().getId()));
        params.put("user_id", String.valueOf(ward.getUserId()));
        params.put("studentId", String.valueOf(ward.getUserInfo().getUserId()));
        params.put("masterId", String.valueOf(ward.getMasterId()));
        params.put("bcsMapId", String.valueOf(ward.getUserInfo().getBcsMapId()));
        params.put("academicSessionId", String.valueOf(ward.getAcademicSessionId()));
        params.put("amount", String.valueOf(ward.getAcademicSessionId()));
        params.put("amount", String.valueOf(ward.getAcademicSessionId()));
        Fees feesOld = getView().getFees();
        params.put("fee_id", String.valueOf(feesOld.getFee_id()));

        try {
            JsonArray jsonArray = new JsonArray();
            jsonArray.add(feesOld.getMonth());
            params.put("monthIds", jsonArray.toString());
            params.put("penalty", getFeeDetails(feesOld.getPenalty()).toString());
            params.put("discount", getFeeDetails(feesOld.getDiscount()).toString());
            params.put("heads", getFeeDetails(feesOld.getHeads()).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        disposable = apiService.payFee(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Response<FeesOld>>() {

                    @Override
                    public boolean openDialog() {
                        return true;
                    }

                    @Override
                    public void onResponse(Response<FeesOld> response) {
                        if (response.getData() != null) {
                            getView().setPayFeeID(String.valueOf(response.getData().getPay_fee_id()));
                            proceedCheckout(paymentMode);
                        }
                    }

                    @Override
                    public WardFeesPresenter getPresenter() {
                        return WardFeesPresenter.this;
                    }

                });*/
    }

    public void updateCheckout(Checkout checkout) {
       /* getView().showProgressDialog(getString(R.string.wait));

        apiService = serviceBuilder.createService(ParentService.class);
        FeesPayment feesPayment = getFeesPayment(checkout);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("pay_fee_id", getView().getPayFeeID());
        params.put("pay_log", feesPayment.getPayLoad());
        params.put("status", feesPayment.getStatus());
        disposable = apiService.payDone(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<FeesOld>() {

                    @Override
                    public boolean openDialog() {
                        return true;
                    }

                    @Override
                    public void onResponse(FeesOld response) {
                        getView().navigateToPayment(response.getMessage());
                    }

                    @Override
                    public WardFeesPresenter getPresenter() {
                        return WardFeesPresenter.this;
                    }

                });*/
    }

    private FeesPayment getFeesPayment(Checkout checkout) {
        FeesPayment feesPayment = new FeesPayment();
        ResponsePayload payload = checkout.getMerchantResponsePayload();
        g transaction = payload.getPaymentMethod().getPaymentTransaction();
        feesPayment.setStatus(transaction.getStatusCode().equals(PaymentActivity.TRANSACTION_STATUS_SALES_DEBIT_SUCCESS) ? "1" : "3");
        feesPayment.setPayLoad(payload.toString());
        return feesPayment;
    }


    private void proceedCheckout(String paymentMode) {
       /* Fees feesOld = getView().getFees();
        String orderId = getView().getPayFeeID();
        String amount = "1";//new DecimalFormat("##.##").format(feesOld.getTotal());

        Checkout checkout = new Checkout();
        checkout.setMerchantIdentifier(Constants.MERCHANT_IDENTIFIER);
        checkout.setTransactionIdentifier(Constants.TXN + System.currentTimeMillis());
        checkout.setTransactionType(PaymentActivity.TRANSACTION_TYPE_SALE);
        checkout.setTransactionSubType(PaymentActivity.TRANSACTION_SUBTYPE_DEBIT);
        checkout.setTransactionCurrency(Constants.CURRENCY_TYPE);

        checkout.setTransactionReference(orderId);
        checkout.setTransactionAmount(amount);
        checkout.setTransactionDateTime(DateUtils.toDate(Calendar.getInstance().getTime(), FEE_DATE_TEMPLATE));

        checkout.setConsumerIdentifier("");
        checkout.setConsumerEmailID(user.getData().getEmail());
        checkout.setConsumerMobileNumber(user.getData().getMobile());
        checkout.setConsumerAccountNo("");

        //checkout.addCartItem(String.valueOf(feesOld.getFee_id()), amount, "0.0", "", "", "", "", "");
        checkout.addCartItem("TEST", amount, "0.0", "0.0", "", "", "", "");
        checkout.setCartDescription(feesOld.getFee_id() + " ::" + user.getUserdetails().getFullname());
        getView().proceedCheckout(paymentMode, checkout);*/
    }


    public void addFeeChargeLayout(TableLayout root, String name, double amount, @FeeType int feeType) {
        View view = View.inflate(getContext(), R.layout.item_ward_fees, null);
        LinearLayout layoutCharge = view.findViewById(R.id.layout_charge);
        TableRow rowCharge = view.findViewById(R.id.row_charge);
        TextView tvChargeName = view.findViewById(R.id.tv_charge_name);
        TextView tvChargeValue = view.findViewById(R.id.tv_charge_value);
        decorateFeeChargeLayout(layoutCharge, rowCharge, tvChargeName, tvChargeValue, name, amount, feeType);
        root.addView(view);
    }

    private void decorateFeeChargeLayout(LinearLayout layoutCharge, TableRow rowCharge, TextView tvChargeName, TextView tvChargeValue, String name, double amount, @FeeType int feeType) {
        switch (feeType) {
            case HEADS:
                tvChargeName.setText(name);
                tvChargeValue.setText(String.format("%s %d", Html.fromHtml("&#8377"), (int) amount));
                break;
            case PENALTY:
                tvChargeName.setText(name);
                tvChargeValue.setText(String.format("+ %s %d", Html.fromHtml("&#8377"), (int) amount));
                tvChargeValue.setTextColor(ContextCompat.getColor(getContext(), R.color.md_red_700));
                break;
            case DISCOUNT:
                tvChargeName.setText(name);
                tvChargeValue.setText(String.format("- %s %d", Html.fromHtml("&#8377"), (int) amount));
                tvChargeValue.setTextColor(ContextCompat.getColor(getContext(), R.color.md_green_700));
                break;
            case TOTAL:
                tvChargeName.setText(name);
                layoutCharge.setShowDividers(LinearLayout.SHOW_DIVIDER_BEGINNING | LinearLayout.SHOW_DIVIDER_END);
                rowCharge.setPadding(0, 10, 0, 10);
                tvChargeValue.setText(String.format("%s %d", Html.fromHtml("&#8377"), (int) amount));
                tvChargeName.setTypeface(ResourcesCompat.getFont(getContext(), R.font.proxima_regular), BOLD);
                tvChargeValue.setTypeface(ResourcesCompat.getFont(getContext(), R.font.proxima_regular), BOLD);
                break;
        }
    }

    private JsonArray getFeeDetails(List<FeesOld> feesOldList) throws JSONException {
        JsonArray feeDetails = new JsonArray();
        for (FeesOld feesOld : feesOldList) {
            JsonObject feeObject = new JsonObject();
            feeObject.addProperty("head_name", feesOld.getHead_name());
            feeObject.addProperty("amount", feesOld.getAmount());
            feeDetails.add(feeObject);
        }
        return feeDetails;
    }

    @IntDef({HEADS, PENALTY, DISCOUNT, TOTAL})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface FeeType {

    }
}
