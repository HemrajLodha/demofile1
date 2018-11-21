package com.base.ui.adapter.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper used to link metadata with a list item.
 *
 * @param <P> Parent list item
 * @param <C> Child list item
 */
public class ExpandableWrapper<P extends Parent<C, CF>, C, CF> {
    public static final int MAX_CHILD_COUNT_ALL = 0;
    private P mParent;
    private C mChild;
    private CF mChildFooter;

    private boolean childFooterWrap;
    private boolean mWrappedParent;
    private boolean mExpanded;


    private ExpandableWrapper<P, C, CF> mWrappedChildFooter;
    private List<ExpandableWrapper<P, C, CF>> mWrappedChildList;

    /**
     * Constructor to wrap a parent object of type {@link P}.
     *
     * @param parent The parent object to wrap
     */
    public ExpandableWrapper(@NonNull P parent) {
        mParent = parent;
        childFooterWrap = false;
        mWrappedParent = true;
        mExpanded = false;
        mWrappedChildList = generateChildItemList(parent);
        mWrappedChildFooter = generateChildFooterItem(parent);
    }

    /**
     * Constructor to wrap a child object of type {@link C}
     *
     * @param child The child object to wrap
     */
    public ExpandableWrapper(@NonNull C child) {
        mChild = child;
        childFooterWrap = false;
        mWrappedParent = false;
        mExpanded = false;
    }

    /***
     * Constructor to wrap a child footer object of type {@link C}
     * @param parent
     * @param footerView
     */
    public ExpandableWrapper(@NonNull P parent, @NonNull CF footerView) {
        mParent = parent;
        mChildFooter = footerView;
        childFooterWrap = true;
        mWrappedParent = false;
        mExpanded = false;
    }

    public P getParent() {
        return mParent;
    }

    public void setParent(@NonNull P parent) {
        mParent = parent;
        mWrappedChildList = generateChildItemList(parent);
    }

    public C getChild() {
        return mChild;
    }

    public boolean isExpanded() {
        return mExpanded;
    }

    public void setExpanded(boolean expanded) {
        mExpanded = expanded;
    }

    public boolean isParent() {
        return mWrappedParent;
    }

    public boolean isChildFooter() {
        return childFooterWrap;
    }

    /**
     * @return The initial expanded state of a parent
     * @throws IllegalStateException If a parent isn't being wrapped
     */
    public boolean isParentInitiallyExpanded() {
        if (!mWrappedParent) {
            throw new IllegalStateException("Parent not wrapped");
        }

        return mParent.isInitiallyExpanded();
    }

    /**
     * @return The list of children of a parent
     * @throws IllegalStateException If a parent isn't being wrapped
     */
    public List<ExpandableWrapper<P, C, CF>> getWrappedChildList() {
        if (!mWrappedParent) {
            throw new IllegalStateException("Parent not wrapped");
        }

        return mWrappedChildList;
    }


    /***
     * get wrapped footer
     * @return
     */
    public ExpandableWrapper<P, C, CF> getWrappedChildFooter() {
        if (!mWrappedParent) {
            throw new IllegalStateException("Parent not wrapped");
        }

        return mWrappedChildFooter;
    }


    private List<ExpandableWrapper<P, C, CF>> generateChildItemList(P parentListItem) {

        // return empty list if null
        if (parentListItem.getChildList() == null) {
            return new ArrayList<>();
        }

        List<ExpandableWrapper<P, C, CF>> childItemList = new ArrayList<>();

        int count = 0;
        for (C child : parentListItem.getChildList()) {
            count++;
            childItemList.add(new ExpandableWrapper<P, C, CF>(child));
            if (parentListItem.getMaxChildCount() > MAX_CHILD_COUNT_ALL && count >= parentListItem.getMaxChildCount()) {
                break;
            }
        }

        return childItemList;
    }

    private ExpandableWrapper<P, C, CF> generateChildFooterItem(P parentListItem) {
        return new ExpandableWrapper<>(parentListItem, parentListItem.getChildListFooter());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ExpandableWrapper<?, ?, ?> that = (ExpandableWrapper<?, ?, ?>) o;

        if (mParent != null ? !mParent.equals(that.mParent) : that.mParent != null)
            return false;
        return mChild != null ? mChild.equals(that.mChild) : that.mChild == null;

    }

    @Override
    public int hashCode() {
        int result = mParent != null ? mParent.hashCode() : 0;
        result = 31 * result + (mChild != null ? mChild.hashCode() : 0);
        return result;
    }
}
