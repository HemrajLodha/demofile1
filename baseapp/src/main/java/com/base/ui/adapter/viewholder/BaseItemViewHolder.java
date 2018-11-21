package com.base.ui.adapter.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.base.ui.adapter.BaseRecyclerAdapter.OnItemClickListener;
import com.base.ui.adapter.BaseRecyclerAdapter.OnLongItemClickListener;


public abstract class BaseItemViewHolder<Data> extends RecyclerView.ViewHolder implements
        View.OnClickListener,
        View.OnLongClickListener {
    protected Context mContext;
    private OnItemClickListener mItemClickListener;
    private OnLongItemClickListener mLongItemClickListener;
    protected boolean mHasHeader = false;

    public BaseItemViewHolder(View itemView) {
        this(itemView, null, null);
    }

    public BaseItemViewHolder(View itemView, OnItemClickListener itemClickListener) {
        this(itemView, itemClickListener, null);
    }

    public BaseItemViewHolder(View itemView, OnItemClickListener itemClickListener, OnLongItemClickListener longItemClickListener) {
        super(itemView);
        this.mItemClickListener = itemClickListener;
        this.mLongItemClickListener = longItemClickListener;
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    public abstract void bind(Data data);

    public boolean isHasHeader() {
        return mHasHeader;
    }

    public void setHasHeader(boolean hasHeader) {
        this.mHasHeader = hasHeader;
    }

    public final View findViewById(int id) {
        return itemView.findViewById(id);
    }

    @Override
    public void onClick(View v) {
        if (mItemClickListener != null) {
            int position = (int) itemView.getTag();
            mItemClickListener.onItemClick(v, mHasHeader ? position - 1 : position);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mLongItemClickListener != null) {
            int position = (int) itemView.getTag();
            mLongItemClickListener.onLongItemClick(v, mHasHeader ? position - 1 : position);
            return true;
        }
        return false;
    }
}
