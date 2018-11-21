package com.base.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.ui.adapter.viewholder.BaseListViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by intel on 22-May-17.
 */

public abstract class BaseSpinnerAdapter<Data, ViewHolder extends BaseListViewHolder,DropdownViewHolder
        extends BaseListViewHolder> extends BaseListAdapter<Data,ViewHolder>
{
    public BaseSpinnerAdapter(Context context) {
        super(context);
    }

    public BaseSpinnerAdapter(Context context, List<Data> data) {
        super(context,data);
    }

    @Override
    public View getDropDownView(int position, View itemView, ViewGroup parent)
    {
        DropdownViewHolder holder;
        if (itemView == null) {
            itemView = LayoutInflater.from(mContext).inflate(getDropdownItemView(), parent, false);
            holder = onCreateDropdownViewHolder(itemView);
            itemView.setTag(holder);
        } else {
            holder = (DropdownViewHolder) itemView.getTag();
        }

        holder.bind(mDatas.get(position));

        return itemView;
    }

    public abstract int getDropdownItemView();

    public abstract DropdownViewHolder onCreateDropdownViewHolder(View itemView);
}
