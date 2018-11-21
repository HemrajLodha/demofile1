package com.pws.pateast.fragment.leave.teacher.info;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.adapter.viewholder.BaseItemViewHolder;
import com.pws.pateast.R;

/**
 * Created by intel on 28-Jun-17.
 */

public class LeaveInfoAdapter extends BaseRecyclerAdapter<LeaveInfoAdapter.InfoData, LeaveInfoAdapter.LeaveHolder> {
    public LeaveInfoAdapter(Context context, OnItemClickListener onItemClickListener) {
        super(context, onItemClickListener);
    }

    @Override
    protected int getItemResourceLayout(int viewType) {
        return R.layout.item_teacher_leave_info;
    }

    @Override
    public LeaveHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LeaveHolder(getView(parent, viewType), mItemClickListener);
    }

    class LeaveHolder extends BaseItemViewHolder<LeaveInfoAdapter.InfoData> {
        private TextView tvLabel, tvValue;

        public LeaveHolder(View itemView, OnItemClickListener itemClickListener) {
            super(itemView, itemClickListener);
            tvLabel = (TextView) findViewById(R.id.tv_label);
            tvValue = (TextView) findViewById(R.id.tv_value);
        }

        @Override
        public void bind(InfoData leave) {
            tvLabel.setText(leave.getKey());
            tvValue.setText(leave.getValue());
        }
    }

    public static class InfoData {
        private String key, value;

        public InfoData(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }
}
