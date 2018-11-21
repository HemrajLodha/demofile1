package com.base.ui.adapter.model;

import java.util.List;

/**
 * Interface for implementing required methods in a parent.
 */
public interface Parent<C, CF> {

    /**
     * Getter for the list of this parent's child items.
     * <p>
     * If list is empty, the parent has no children.
     *
     * @return A {@link List} of the children of this {@link Parent}
     */
    List<C> getChildList();

    /**
     * Getter used to determine if this {@link Parent}'s
     * {@link android.view.View} should show up initially as expanded.
     *
     * @return true if expanded, false if not
     */
    boolean isInitiallyExpanded();

    /**
     * get child list footer
     *
     * @return
     */
    CF getChildListFooter();

    /**
     * getermine for add footer to child list
     *
     * @return
     */
    boolean expandableViewFooterEnable();

    /**
     * get max child data limit <1 for unlimited
     * @return
     */
    int getMaxChildCount();
}