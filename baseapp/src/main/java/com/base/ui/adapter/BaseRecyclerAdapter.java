package com.base.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.ui.adapter.viewholder.BaseItemViewHolder;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseRecyclerAdapter<Data, Holder extends BaseItemViewHolder> extends
        RecyclerView.Adapter<Holder> {
    private LayoutInflater inflater;
    protected Context mContext;
    protected List<Data> mDatas;
    protected OnItemClickListener mItemClickListener;
    protected OnLongItemClickListener mLongItemClickListener;

    public BaseRecyclerAdapter(Context context) {
        this.mContext = context;
        mDatas = new ArrayList<>();

    }

    public BaseRecyclerAdapter(Context context, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.mItemClickListener = onItemClickListener;
        mDatas = new ArrayList<>();

    }

    public BaseRecyclerAdapter(Context context, List<Data> data) {
        this.mContext = context;
        this.mDatas = data;
    }

    public BaseRecyclerAdapter(Context context, List<Data> data, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.mItemClickListener = onItemClickListener;
        this.mDatas = data;
    }

    protected View getView(ViewGroup parent, int viewType) {
        if (inflater == null || inflater.getContext() != parent.getContext()) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        return inflater.inflate(getItemResourceLayout(viewType), parent, false);
    }

    protected abstract int getItemResourceLayout(int viewType);

    @Override
    public abstract Holder onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(Holder holder, int position)
    {
        holder.itemView.setTag(position);
        holder.bind(getItem(position));
    }

    @Override
    public int getItemCount() {
        try {
            return mDatas.size();
        } catch (Exception e) {
            return 0;
        }
    }


    public Data getItem(int position) {
        try {
            return mDatas.get(position);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public void setOnLongItemClickListener(OnLongItemClickListener longItemClickListener) {
        this.mLongItemClickListener = longItemClickListener;
    }

    public List<Data> getDatas() {
        return mDatas;
    }

    public void add(Data item) {
        mDatas.add(item);
        notifyItemInserted(mDatas.size());
    }


    public void add(Data item, int position) {
        mDatas.add(position, item);
        notifyItemInserted(position);
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
            notifyItemRemoved(position);
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

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnLongItemClickListener {
        void onLongItemClick(View view, int position);
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
