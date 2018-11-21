package com.base.ui.adapter.model;

import java.util.List;

/**
 * Simple implementation of the ParentListItem interface,
 * by default all items are not initially expanded.
 *
 * @param <C> Type of the Child Items held by the Parent.
 */
public class SimpleParent<C, CF> implements Parent<C, CF> {

    private List<C> mChildList;

    private CF mChildFooter;
    private int mCount;

    protected SimpleParent(List<C> childItemList) {
        mChildList = childItemList;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    @Override
    public CF getChildListFooter() {
        return mChildFooter;
    }

    @Override
    public boolean expandableViewFooterEnable() {
        return false;
    }

    @Override
    public List<C> getChildList() {
        return mChildList;
    }

    public void setChildList(List<C> childList) {
        mChildList = childList;
    }

    public void setChildFooter(CF mChildFooter) {
        this.mChildFooter = mChildFooter;
    }

    @Override
    public int getMaxChildCount() {
        return mCount;
    }

    public void setMaxChildCount(int mCount) {
        this.mCount = mCount;
    }
}
