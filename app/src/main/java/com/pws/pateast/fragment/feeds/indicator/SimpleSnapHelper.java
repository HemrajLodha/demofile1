package com.pws.pateast.fragment.feeds.indicator;

import android.support.annotation.NonNull;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;

/**
 * SnapHelper which allows snapping of pages, customized with notifying of {@link OverflowPagerIndicator}
 * when position is snapped
 *
 * @author Petr Introvic <introvic.petr@gmail.com>
 * created 07.10.2017.
 */
public class SimpleSnapHelper extends PagerSnapHelper {
    private int position;
    private OverflowPagerIndicator mOverflowPagerIndicator;

    public SimpleSnapHelper(@NonNull OverflowPagerIndicator overflowPagerIndicator) {
        mOverflowPagerIndicator = overflowPagerIndicator;
    }

    @Override
    public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
        position = super.findTargetSnapPosition(layoutManager, velocityX, velocityY);

        // Notify OverflowPagerIndicator about changed page
        mOverflowPagerIndicator.onPageSelected(position);

        return position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
        mOverflowPagerIndicator.onPageSelected(this.position);
    }
}
