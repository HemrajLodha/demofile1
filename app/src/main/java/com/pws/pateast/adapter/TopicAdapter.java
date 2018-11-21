package com.pws.pateast.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.base.ui.adapter.BaseSpinnerAdapter;
import com.base.ui.adapter.viewholder.BaseListViewHolder;
import com.pws.pateast.R;
import com.pws.pateast.api.model.Topic;

import java.util.List;

/**
 * Created by intel on 24-Jan-18.
 */

public class TopicAdapter extends BaseSpinnerAdapter<Topic, TopicAdapter.TopicHolder, TopicAdapter.TopicHolder> {

    public TopicAdapter(Context context) {
        super(context);
    }

    public TopicAdapter(Context context, List<Topic> data) {
        super(context, data);
    }

    @Override
    public int getDropdownItemView() {
        return R.layout.item_spinner_dropdown;
    }

    @Override
    public TopicHolder onCreateDropdownViewHolder(View itemView) {
        return new TopicHolder(itemView);
    }

    @Override
    protected int getItemView() {
        return R.layout.item_spinner_view;
    }

    @Override
    public TopicHolder onCreateViewHolder(View itemView) {
        return new TopicHolder(itemView);
    }

    class TopicHolder extends BaseListViewHolder<Topic> {
        private TextView tvSpinner;

        public TopicHolder(View itemView) {
            super(itemView);
            tvSpinner = (TextView) findViewById(R.id.tv_spinner);
        }

        @Override
        public void bind(Topic chapter) {
            tvSpinner.setText(getTopicName(chapter));
        }
    }

    public String getTopicName(Topic topic) {
        return topic.getLmstopicdetails().get(0).getName();
    }

    public Topic getTopic(int position) {
        if (position > -1)
            return getDatas().get(position);
        return null;
    }
}
