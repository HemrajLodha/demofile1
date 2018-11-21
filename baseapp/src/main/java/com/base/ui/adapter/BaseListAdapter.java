package com.base.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import com.base.ui.adapter.viewholder.BaseListViewHolder;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseListAdapter<Data, Holder extends BaseListViewHolder> extends
        BaseAdapter {
    public static String PAGE = "BaseListAdapter.Page";
    protected Context mContext;
    protected LayoutInflater mInflater;
    protected List<Data> mDatas;

    public BaseListAdapter(Context context) {
        this.mContext = context;
        mDatas = new ArrayList<>();

    }

    public BaseListAdapter(Context context, List<Data> data) {
        this.mContext = context;
        this.mDatas = data;
        this.mInflater = LayoutInflater.from(mContext);

    }

    @Override
    public int getCount() {
        try {
            return mDatas.size();
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public Data getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View itemView, ViewGroup parent) {
        Holder holder;
        if (itemView == null) {
            itemView = LayoutInflater.from(mContext).inflate(getItemView(), parent, false);
            holder = onCreateViewHolder(itemView);
            itemView.setTag(holder);
        } else {
            holder = (Holder) itemView.getTag();
        }

        holder.bind(mDatas.get(position));

        return itemView;
    }

    protected abstract int getItemView();

    public abstract Holder onCreateViewHolder(View itemView);

    public List<Data> getDatas() {
        return mDatas;
    }

    public void add(Data item) {
        mDatas.add(item);
        notifyDataSetChanged();
    }

    public void add(Data item, int position) {
        mDatas.add(position, item);
        notifyDataSetChanged();
    }

    public void add(final List<Data> items) {
        mDatas.addAll(items);
        notifyDataSetChanged();
    }

    public void addOrUpdate(Data item) {
        int i = mDatas.indexOf(item);
        if (i >= 0) {
            mDatas.set(i, item);
            notifyDataSetChanged();
        } else {
            add(item);
        }
    }

    public void addOrUpdate(final List<Data> items) {
        final int size = items.size();
        for (int i = 0; i < size; i++) {
            Data item = items.get(i);
            int x = mDatas.indexOf(item);
            if (x >= 0) {
                mDatas.set(x, item);
            } else {
                add(item);
            }
        }
        notifyDataSetChanged();
    }

    public void update(final List<Data> items)
    {
        clear();
        add(items);
    }


    public void remove(int position) {
        if (position >= 0 && position < mDatas.size()) {
            mDatas.remove(position);
            notifyDataSetChanged();
        }
    }

    public void remove(Data item) {
        int position = mDatas.indexOf(item);
        remove(position);
    }

    public void clear() {
        mDatas.clear();
        notifyDataSetChanged();
    }

    public Context getContext()
    {
        return mContext;
    }

    public String getString(int resID)
    {
        return getContext().getString(resID);
    }

    public String getString(int resID,Object... args)
    {
        return getContext().getString(resID,args);
    }
}
