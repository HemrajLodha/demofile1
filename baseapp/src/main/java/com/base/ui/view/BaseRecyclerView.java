package com.base.ui.view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.jcodecraeer.xrecyclerview.XRecyclerView;


public class BaseRecyclerView extends XRecyclerView {

    public BaseRecyclerView(Context context) {
        super(context);
    }

    public BaseRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setUpAsList() {
        setHasFixedSize(true);
        setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void setUpAsGrid(int spanCount) {
        setHasFixedSize(true);
        setLayoutManager(new GridLayoutManager(getContext(), spanCount));
    }

    public void setUpAsChatList() {
        setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setStackFromEnd(true);
        /*manager.setReverseLayout(true);*/
        setLayoutManager(manager);
    }

    /***
     * set footer view by resource
     * @param res
     */
    public void setFootView(int res) {
        setFootView(LayoutInflater.from(getContext()).inflate(res, null, false));
    }


}
