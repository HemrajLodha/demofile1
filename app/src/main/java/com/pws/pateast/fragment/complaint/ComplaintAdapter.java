package com.pws.pateast.fragment.complaint;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.adapter.viewholder.BaseItemViewHolder;
import com.pws.pateast.R;
import com.pws.pateast.api.model.Complaint;
import com.pws.pateast.enums.UserType;

/**
 * Created by intel on 09-Feb-18.
 */

public class ComplaintAdapter extends BaseRecyclerAdapter<Complaint, ComplaintAdapter.ComplaintHolder> {

    public ComplaintAdapter(Context context, OnItemClickListener onItemClickListener) {
        super(context, onItemClickListener);
    }

    @Override
    protected int getItemResourceLayout(int viewType) {
        return R.layout.item_complaint;
    }

    @Override
    public ComplaintHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ComplaintHolder(getView(parent, viewType), mItemClickListener);
    }

    class ComplaintHolder extends BaseItemViewHolder<Complaint> {
        private TextView tvComplaintBy, tvComplaintByName, tvPenalty, tvComplaint /*tvPaymentStatus*/;

        public ComplaintHolder(View itemView, OnItemClickListener itemClickListener) {
            super(itemView, itemClickListener);
            tvComplaintBy = (TextView) findViewById(R.id.tv_complaint_by);
            tvComplaintByName = (TextView) findViewById(R.id.tv_complaint_by_name);
            tvPenalty = (TextView) findViewById(R.id.tv_penalty);
            //tvPaymentStatus = (TextView) findViewById(R.id.tv_payment_status);
            tvComplaint = (TextView) findViewById(R.id.tv_complaint);
        }

        @Override
        public void bind(Complaint complaint) {
            complaint = complaint.getComplaint();
            tvComplaintBy.setText(getString(R.string.label_complaint_by));
            tvComplaintByName.setText(UserType.getUserType(complaint.getUser().getUser_type()).getName());
            if (complaint.getIs_penalty() == 1) {
//                tvPaymentStatus.setVisibility(View.GONE);
//                tvPaymentStatus.setText(complaint.getPenalty_status() == 2 ? R.string.status_pending : R.string.status_paid);
//                tvPaymentStatus.setTextColor(ContextCompat.getColor(getContext(), complaint.getPenalty_status() == 2 ? R.color.md_red_700 : R.color.md_green_700));

                tvPenalty.setText(String.format("%s %d", Html.fromHtml("&#8377"), (int) complaint.getFine_amount()));
                ((LinearLayout) tvPenalty.getParent()).setVisibility(View.VISIBLE);
            } else {
//                tvPaymentStatus.setVisibility(View.GONE);
                ((LinearLayout) tvPenalty.getParent()).setVisibility(View.GONE);
            }
            tvComplaint.setText(complaint.getComplaint_detail());
        }
    }
}
