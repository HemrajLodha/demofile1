package com.base.ui.adapter.viewholder;

import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.base.ui.adapter.BaseExpandableRecyclerAdapter;
import com.base.ui.adapter.model.Parent;

/**
 * ViewHolder for a child list item.
 * <p>
 * The user should extend this class and implement as they wish for their
 * child list item.
 */
public abstract class ChildViewHolder<C, CF> extends RecyclerView.ViewHolder implements View.OnClickListener {
    public C mChild;
    public CF mChildFooter;
    public Parent<C, CF> mParent;
    public BaseExpandableRecyclerAdapter mExpandableAdapter;
    private BaseExpandableRecyclerAdapter.OnChildClickListener onChildClickListener;

    /**
     * Default constructor.
     *
     * @param itemView The {@link View} being hosted in this ViewHolder
     */
    public ChildViewHolder(@NonNull View itemView) {
        this(itemView, null);
    }

    public ChildViewHolder(View itemView, BaseExpandableRecyclerAdapter.OnChildClickListener onChildClickListener) {
        super(itemView);
        this.onChildClickListener = onChildClickListener;
        itemView.setOnClickListener(this);
    }


    public final View findViewById(int id) {
        return itemView.findViewById(id);
    }

    public abstract void bind(C child);

    /**
     * @return the childListItem associated with this view holder
     */
    @UiThread
    public C getChild() {
        return mChild;
    }

    public Parent<C, CF> getParent() {
        return mParent;
    }

    /**
     * @return the childListItem footer associated with this view holder
     */
    public CF getChildFooter() {
        return mChildFooter;
    }

    /**
     * Returns the adapter position of the Parent associated with this ChildViewHolder
     *
     * @return The adapter position of the Parent if it still exists in the adapter.
     * RecyclerView.NO_POSITION if item has been removed from the adapter,
     * RecyclerView.Adapter.notifyDataSetChanged() has been called after the last
     * layout pass or the ViewHolder has already been recycled.
     */
    @UiThread
    public int getParentAdapterPosition() {
        int flatPosition = getAdapterPosition();
        if (mExpandableAdapter == null || flatPosition == RecyclerView.NO_POSITION) {
            return RecyclerView.NO_POSITION;
        }

        return mExpandableAdapter.getNearestParentPosition(flatPosition);
    }

    /**
     * Returns the adapter position of the Child associated with this ChildViewHolder
     *
     * @return The adapter position of the Child if it still exists in the adapter.
     * RecyclerView.NO_POSITION if item has been removed from the adapter,
     * RecyclerView.Adapter.notifyDataSetChanged() has been called after the last
     * layout pass or the ViewHolder has already been recycled.
     */
    @UiThread
    public int getChildAdapterPosition() {
        int flatPosition = getAdapterPosition();
        if (mExpandableAdapter == null || flatPosition == RecyclerView.NO_POSITION) {
            return RecyclerView.NO_POSITION;
        }
        return mExpandableAdapter.getChildPosition(flatPosition);
    }

    @Override
    public void onClick(View v) {
        if (onChildClickListener != null) {
            onChildClickListener.onChildClick(v, mParent, mChild);
        }
    }
}