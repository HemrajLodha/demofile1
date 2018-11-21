package com.pws.pateast.fragment.home;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.adapter.viewholder.BaseItemViewHolder;
import com.pws.pateast.model.DashboardItem;
import com.pws.pateast.R;

import java.util.List;

/**
 * Created by planet on 4/14/2017.
 */

public class DashboardAdapter extends BaseRecyclerAdapter<DashboardItem, DashboardAdapter.ViewHolder> {

    public DashboardAdapter(Context context) {
        super(context);
    }

    public DashboardAdapter(Context context,OnItemClickListener onItemClickListener) {
        super(context,onItemClickListener);
    }

    public DashboardAdapter(Context context, List data, OnItemClickListener onItemClickListener)
    {
        super(context, data, onItemClickListener);
    }

    @Override
    protected int getItemResourceLayout(int viewType) {
        return R.layout.dashboard_item;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new ViewHolder(getView(parent, viewType), mItemClickListener);
    }

    class ViewHolder extends BaseItemViewHolder<DashboardItem>
    {
        private TextView tvTitle;
        private ImageView imgIcon;

        public ViewHolder(View itemView, OnItemClickListener onItemClickListener)
        {
            super(itemView, onItemClickListener);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            imgIcon = (ImageView) itemView.findViewById(R.id.img_icon);
        }

        @Override
        public void bind(DashboardItem dashboardItem) {
            tvTitle.setText(dashboardItem.getTitle());
            imgIcon.setImageResource(dashboardItem.getIcon());
        }
    }

}
