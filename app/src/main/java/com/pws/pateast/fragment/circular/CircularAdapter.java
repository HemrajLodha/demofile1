package com.pws.pateast.fragment.circular;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.adapter.viewholder.BaseItemViewHolder;
import com.pws.pateast.R;
import com.pws.pateast.api.model.Circular;
import com.pws.pateast.utils.DateUtils;

import static com.pws.pateast.utils.DateUtils.CHAT_DATE_TEMPLATE;
import static com.pws.pateast.utils.DateUtils.SERVER_DATE_TEMPLATE;

public class CircularAdapter extends BaseRecyclerAdapter<Circular, CircularAdapter.CircularHolder> {


    public CircularAdapter(Context context, OnItemClickListener onItemClickListener) {
        super(context, onItemClickListener);
    }

    @Override
    protected int getItemResourceLayout(int viewType) {
        return R.layout.item_circular;
    }

    @Override
    public CircularHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CircularHolder(getView(parent, viewType), mItemClickListener);
    }

    class CircularHolder extends BaseItemViewHolder<Circular> {
        private TextView tvCircularTitle, tvCircularDate, tvCircularNo;

        public CircularHolder(View itemView, OnItemClickListener itemClickListener) {
            super(itemView, itemClickListener);
            tvCircularTitle = (TextView) findViewById(R.id.tv_circular_title);
            tvCircularDate = (TextView) findViewById(R.id.tv_circular_date);
            tvCircularNo = (TextView) findViewById(R.id.tv_circular_no);
        }

        @Override
        public void bind(Circular events) {
            tvCircularTitle.setText(events.getCirculardetails().get(0).getTitle());
            tvCircularNo.setText(events.getNumber());
            tvCircularDate.setText(DateUtils.toDate(DateUtils.parse(events.getDate(), SERVER_DATE_TEMPLATE), CHAT_DATE_TEMPLATE));
        }
    }
}
