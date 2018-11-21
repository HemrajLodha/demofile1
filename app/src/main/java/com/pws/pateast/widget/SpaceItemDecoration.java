package com.pws.pateast.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by intel on 06-Mar-17.
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int spacing;
    private int spanCount;
    private boolean includeEdge;
    public SpaceItemDecoration(int spacing, int spanCount, boolean includeEdge)
    {
        this.spacing = spacing;
        this.spanCount = spanCount;
        this.includeEdge = includeEdge;
    }

    public SpaceItemDecoration(@NonNull Context context, @DimenRes int itemOffsetId, int spanCount, boolean includeEdge)
    {
        this(context.getResources().getDimensionPixelSize(itemOffsetId),spanCount,includeEdge);

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column

        if (includeEdge) {
            outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

            if (position < spanCount) { // top edge
                outRect.top = spacing;
            }
            outRect.bottom = spacing; // item bottom
        } else {
            outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
            outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            //don't want to include top edge un-comment
            /*if (position >= spanCount ) {
                outRect.top = spacing; // item top
            }*/
            outRect.top = spacing;
        }

    }

}
