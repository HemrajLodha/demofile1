package com.base.ui.adapter.viewholder;

import android.content.Context;
import android.view.View;


public abstract class BaseListViewHolder<Data> {
    protected Context mContext;
    protected View itemView;

    public BaseListViewHolder(View itemView) {
        this.itemView = itemView;
    }

    public final View findViewById(int id) {
        return itemView.findViewById(id);
    }

    public abstract void bind(Data data);

    public Context getContext() {
        return mContext;
    }
}
