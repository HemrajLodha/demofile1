package com.pws.pateast.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.pws.pateast.R;

/**
 * Created by intel on 18-May-17.
 */

public class DividerItemDecoration extends android.support.v7.widget.DividerItemDecoration
{

    /**
     * Creates a divider {@link RecyclerView.ItemDecoration} that can be used with a
     * {@link LinearLayoutManager}.
     *
     * @param context     Current context, it will be used to access resources.
     * @param orientation Divider orientation. Should be {@link #HORIZONTAL} or {@link #VERTICAL}.
     */
    public DividerItemDecoration(Context context, int orientation) {
        this(context, orientation, R.drawable.divider_recycle_view);
    }

    public DividerItemDecoration(Context context, int orientation,int resId) {
        super(context, orientation);
        setDrawable(ContextCompat.getDrawable(context, resId));
    }

}
