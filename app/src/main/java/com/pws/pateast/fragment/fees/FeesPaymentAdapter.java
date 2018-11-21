package com.pws.pateast.fragment.fees;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.adapter.viewholder.BaseItemViewHolder;
import com.pws.pateast.R;
import com.pws.pateast.api.model.Fees;
import com.pws.pateast.utils.DateUtils;

import static com.pws.pateast.api.model.Fees.FeesStatus.CHALLAN;
import static com.pws.pateast.api.model.Fees.FeesStatus.PAID;
import static com.pws.pateast.api.model.Fees.FeesStatus.UNPAID;
import static com.pws.pateast.utils.DateUtils.CHAT_DATE_TEMPLATE;
import static com.pws.pateast.utils.DateUtils.SERVER_DATE_TEMPLATE;

/**
 * Created by intel on 02-Feb-18.
 */

public class FeesPaymentAdapter extends BaseRecyclerAdapter<Fees, FeesPaymentAdapter.FeesHolder> {

    public FeesPaymentAdapter(Context context, OnItemClickListener onItemClickListener) {
        super(context, onItemClickListener);
    }

    @Override
    protected int getItemResourceLayout(int viewType) {
        switch (viewType) {
            case PAID:
                return R.layout.item_fees_paid;
            case UNPAID:
                return R.layout.item_fees_unpaid;
            case CHALLAN:
                return R.layout.item_fees_paid;
        }
        return R.layout.item_fees_paid;
    }

    @Override
    public FeesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case PAID:
                return new PaidFeesHolder(getView(parent, viewType), mItemClickListener);
            case UNPAID:
                return new UnPaidFeesHolder(getView(parent, viewType), mItemClickListener);
            case CHALLAN:
                return new ChallanHolder(getView(parent, viewType), mItemClickListener);
        }
        return new FeesHolder(getView(parent, viewType), mItemClickListener);
    }

    class PaidFeesHolder extends FeesHolder {
        protected TextView tvAmountPaid, tvPaymentStatus, tvSubmissionDate;
        protected TextView btnDownloadInvoice, btnDownloadChallan, btnSendInvoice, btnSendChallan;

        public PaidFeesHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView, onItemClickListener);
            tvAmountPaid = (TextView) findViewById(R.id.tv_amount_paid);
            tvSubmissionDate = (TextView) findViewById(R.id.tv_submission_date);
            tvPaymentStatus = (TextView) findViewById(R.id.tv_payment_status);
            btnDownloadInvoice = (TextView) findViewById(R.id.btn_download_invoice);
            btnDownloadChallan = (TextView) findViewById(R.id.btn_download_challan);
            btnSendInvoice = (TextView) findViewById(R.id.btn_send_invoice);
            btnSendChallan = (TextView) findViewById(R.id.btn_send_challan);
        }

        @Override
        public void bind(Fees fees) {
            super.bind(fees);
            if (fees.getSubmissionDate() != null && !TextUtils.isEmpty(fees.getSubmissionDate()))
                tvSubmissionDate.setText(DateUtils.toDate(DateUtils.parse(fees.getSubmissionDate(), SERVER_DATE_TEMPLATE), CHAT_DATE_TEMPLATE));
            else
                tvSubmissionDate.setText(R.string.not_available);
            tvAmountPaid.setText(String.format("%s %.2f", Html.fromHtml("&#8377"), fees.getSubmittedAmount()));
            tvPaymentStatus.setText(R.string.title_paid);
            btnDownloadInvoice.setOnClickListener(this);
            btnSendInvoice.setOnClickListener(this);
            btnDownloadChallan.setVisibility(View.GONE);
            btnSendChallan.setVisibility(View.GONE);
            btnDownloadInvoice.setVisibility(View.VISIBLE);
            btnSendInvoice.setVisibility(View.VISIBLE);
        }
    }

    class UnPaidFeesHolder extends FeesHolder {
        private TextView tvDueFee, tvPaymentStatus, tvDueDate;

        public UnPaidFeesHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView, onItemClickListener);
            tvDueDate = (TextView) findViewById(R.id.tv_due_date);
            tvDueFee = (TextView) findViewById(R.id.tv_due_fee);
            tvPaymentStatus = (TextView) findViewById(R.id.tv_payment_status);
        }

        @Override
        public void bind(Fees fees) {
            super.bind(fees);
            if (fees.getDate() != null && !TextUtils.isEmpty(fees.getDate()))
                tvDueDate.setText(DateUtils.toDate(DateUtils.parse(fees.getDate(), SERVER_DATE_TEMPLATE), CHAT_DATE_TEMPLATE));
            else
                tvDueDate.setText(R.string.not_available);
            tvDueFee.setText(String.format("%s %.2f", Html.fromHtml("&#8377"), fees.getAmount()));
            tvPaymentStatus.setText(R.string.title_unpaid);
        }
    }

    class ChallanHolder extends PaidFeesHolder {
        public ChallanHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView, onItemClickListener);

        }

        @Override
        public void bind(Fees fees) {
            super.bind(fees);
            tvPaymentStatus.setText(R.string.title_challan);
            btnDownloadChallan.setOnClickListener(this);
            btnSendChallan.setOnClickListener(this);
            btnDownloadInvoice.setVisibility(View.GONE);
            btnSendInvoice.setVisibility(View.GONE);
            btnDownloadChallan.setVisibility(View.VISIBLE);
            btnSendChallan.setVisibility(View.VISIBLE);
        }
    }


    class FeesHolder extends BaseItemViewHolder<Fees> {
        private TextView tvFeeHead, tvInstallment;

        public FeesHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView, onItemClickListener);
            tvFeeHead = (TextView) findViewById(R.id.tv_fee_head);
            tvInstallment = (TextView) findViewById(R.id.tv_installment);
        }

        @Override
        public void bind(Fees fees) {
            tvFeeHead.setText(fees.getFeehead().getFeeheaddetails().get(0).getName());
            tvInstallment.setText(String.valueOf(fees.getInstallment() + 1));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getFeeStatus();
    }
}
