package com.base.ui.adapter.viewholder;

import android.content.Context;
import android.os.Bundle;
import android.view.View;


public abstract class BaseHeaderViewHolder<Data> extends BaseItemViewHolder<Data> {
    protected Context mContext;
    protected Bundle mBundle;

    public BaseHeaderViewHolder(View itemView, Bundle bundle) {
        super(itemView, null, null);
        this.mBundle = bundle;
    }


    public abstract void show();

    public void saveState(Bundle bundle) {
        this.mBundle = bundle;
    }
}
