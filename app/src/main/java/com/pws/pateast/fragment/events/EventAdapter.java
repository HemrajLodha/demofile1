package com.pws.pateast.fragment.events;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.adapter.viewholder.BaseItemViewHolder;
import com.pws.pateast.R;
import com.pws.pateast.api.model.Events;
import com.pws.pateast.utils.DateUtils;

import static com.pws.pateast.utils.DateUtils.CHAT_DATE_TEMPLATE;
import static com.pws.pateast.utils.DateUtils.SERVER_DATE_TEMPLATE;

public class EventAdapter extends BaseRecyclerAdapter<Events, EventAdapter.EventHolder> {


    public EventAdapter(Context context, OnItemClickListener onItemClickListener) {
        super(context, onItemClickListener);
    }

    @Override
    protected int getItemResourceLayout(int viewType) {
        return R.layout.item_events;
    }

    @Override
    public EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EventHolder(getView(parent, viewType), mItemClickListener);
    }

    class EventHolder extends BaseItemViewHolder<Events> {
        private TextView tvEventTitle, tvEventVenue, tvEventStart, tvEventEnd;

        public EventHolder(View itemView, OnItemClickListener itemClickListener) {
            super(itemView, itemClickListener);
            tvEventTitle = (TextView) findViewById(R.id.tv_event_title);
            tvEventVenue = (TextView) findViewById(R.id.tv_event_venue);
            tvEventStart = (TextView) findViewById(R.id.tv_event_start);
            tvEventEnd = (TextView) findViewById(R.id.tv_event_end);
        }

        @Override
        public void bind(Events events) {
            tvEventTitle.setText(events.getEventdetails().get(0).getTitle());
            if (!TextUtils.isEmpty(events.getEventdetails().get(0).getVenue())) {
                tvEventVenue.setText(events.getEventdetails().get(0).getVenue());
            } else {
                tvEventVenue.setText(R.string.not_available);
            }
            tvEventStart.setText(DateUtils.toDate(DateUtils.parse(events.getStart(), SERVER_DATE_TEMPLATE), CHAT_DATE_TEMPLATE));
            tvEventEnd.setText(DateUtils.toDate(DateUtils.parse(events.getEnd(), SERVER_DATE_TEMPLATE), CHAT_DATE_TEMPLATE));

        }
    }
}
