package com.base.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.provider.BaseColumns;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.ui.adapter.viewholder.BaseItemViewHolder;

import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by intel on 01-Aug-17.
 */

public abstract class BaseCursorRecyclerAdapter<Holder extends BaseItemViewHolder> extends RecyclerView.Adapter<Holder> {
    public static final int NO_CURSOR_POSITION = -99; // used when mapping section list position to cursor position

    protected static final int VIEW_TYPE_SECTION = -1;

    private Context mContext;

    private Cursor mCursor;

    private boolean mDataValid;

    private int mRowIdColumn;

    private DataSetObserver mDataSetObserver;

    protected BaseRecyclerAdapter.OnItemClickListener mItemClickListener;
    protected BaseRecyclerAdapter.OnLongItemClickListener mLongItemClickListener;

    protected SortedMap<Integer, Object> mSections = new TreeMap<>(); // should not be null
    ArrayList<Integer> mSectionList = new ArrayList<Integer>();

    public BaseCursorRecyclerAdapter(Context context, Cursor cursor) {
        this(context, cursor, null);
    }

    public BaseCursorRecyclerAdapter(Context context, Cursor cursor, BaseRecyclerAdapter.OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.mItemClickListener = onItemClickListener;
        this.mCursor = cursor;
        mDataValid = cursor != null;
        mRowIdColumn = mDataValid ? mCursor.getColumnIndex(BaseColumns._ID) : -1;
        mDataSetObserver = new NotifyingDataSetObserver();
        if (mCursor != null) {
            mCursor.registerDataSetObserver(mDataSetObserver);
        }
        init(null);
    }

    private void init(SortedMap<Integer, Object> sections) {
        if (sections != null) {
            mSections = sections;
        } else {
            buildSections();
        }
    }

    public Cursor getCursor() {
        return mCursor;
    }

    @Override
    public int getItemCount() {
        if (isSectioned() && mDataValid && mCursor != null) {
            return mCursor.getCount() + mSections.size();
        } else if (mDataValid && mCursor != null) {
            return mCursor.getCount();
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        if (isSectioned() && isSection(position))
            return position;
        else if (mDataValid && mCursor != null && mCursor.moveToPosition(getCursorPositionWithoutSections(position))) {
            return mCursor.getLong(mRowIdColumn);
        }
        return 0;
    }

    /**
     * @param position
     * @return Get the type of View that will be created by getView(int, View, ViewGroup) for the specified item.
     */
    @Override
    public int getItemViewType(int position) {
        return isSection(position) ? VIEW_TYPE_SECTION : getViewType(position);
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }

    protected View getView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(mContext).inflate(getItemResourceLayout(viewType), parent, false);
    }

    protected abstract int getItemResourceLayout(int viewType);

    protected abstract Object getSectionFromCursor(Cursor cursor);

    protected abstract boolean isSectioned();

    protected abstract int getViewType(int position);

    @Override
    public abstract Holder onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        if (!mDataValid) {
            throw new IllegalStateException("this should only be called when the cursor is valid");
        }
        holder.itemView.setTag(position);
        holder.bind(getItem(position));
    }

    public Object getItem(int position) {
        if (isSectioned() && isSection(position))
            return mSections.get(position);
        else if (!mCursor.moveToPosition(getCursorPositionWithoutSections(position))) {
            throw new IllegalStateException("couldn't move cursor to position " + position);
        }
        return mCursor;
    }

    /**
     * Change the underlying cursor to a new cursor. If there is an existing cursor it will be
     * closed.
     */
    public void changeCursor(Cursor cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null) {
            old.close();
        }
    }

    /**
     * Swap in a new Cursor, returning the old Cursor.  Unlike
     * {@link #changeCursor(Cursor)}, the returned old Cursor is <em>not</em>
     * closed.
     */
    public Cursor swapCursor(Cursor newCursor) {
        if (isSectioned() && hasOpenCursor()) {
            buildSections();
            mSectionList.clear();
        }

        if (newCursor == mCursor) {
            return null;
        }
        final Cursor oldCursor = mCursor;
        if (oldCursor != null && mDataSetObserver != null) {
            oldCursor.unregisterDataSetObserver(mDataSetObserver);
        }
        mCursor = newCursor;
        if (mCursor != null) {
            if (mDataSetObserver != null) {
                mCursor.registerDataSetObserver(mDataSetObserver);
            }
            mRowIdColumn = newCursor.getColumnIndexOrThrow("_id");
            mDataValid = true;
            notifyDataSetChanged();
        } else {
            mRowIdColumn = -1;
            mDataValid = false;
            notifyDataSetChanged();
            //There is no notifyDataSetInvalidated() method in RecyclerView.Adapter
        }
        return oldCursor;
    }

    private class NotifyingDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            mDataValid = true;
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            mDataValid = false;
            notifyDataSetChanged();
            //There is no notifyDataSetInvalidated() method in RecyclerView.Adapter
        }
    }

    /**
     * If the adapter's cursor is not null then this method will call buildSections(Cursor cursor).
     */
    private void buildSections() {
        if (hasOpenCursor()) {
            Cursor cursor = getCursor();
            cursor.moveToPosition(-1);
            mSections = buildSections(cursor);
            if (mSections == null) {
                mSections = new TreeMap<>();
            }
        }
    }

    /**
     * @param cursor a non-null cursor at position -1.
     * @return A map whose keys are the position at which a section is and values are an object
     * which will be passed to newSectionView and bindSectionView
     */
    protected SortedMap<Integer, Object> buildSections(Cursor cursor) {
        TreeMap<Integer, Object> sections = new TreeMap<>();
        int cursorPosition = 0;
        while (hasOpenCursor() && cursor.moveToNext()) {
            Object section = getSectionFromCursor(cursor);
            if (cursor.getPosition() != cursorPosition)
                throw new IllegalStateException("Do no move the cursor's position in getSectionFromCursor.");
            if (!sections.containsValue(section))
                sections.put(cursorPosition + sections.size(), section);
            cursorPosition++;
        }
        return sections;
    }


    /**
     * @return True if cursor is not null and open.
     * If the cursor is closed a null cursor will be swapped out.
     */
    protected boolean hasOpenCursor() {
        Cursor cursor = getCursor();
        if (cursor == null || cursor.isClosed()) {
            swapCursor(null);
            return false;
        }
        return true;
    }


    /**
     * @param listPosition the position of the current item in the list with mSections included
     * @return Whether or not the listPosition points to a section.
     */
    public boolean isSection(int listPosition) {
        return mSections.containsKey(listPosition);
    }

    /**
     * This will map a position in the list adapter (which includes mSections) to a position in
     * the cursor (which does not contain mSections).
     *
     * @param listPosition the position of the current item in the list with mSections included
     * @return the correct position to use with the cursor
     */
    public int getCursorPositionWithoutSections(int listPosition) {
        if (!isSectioned() || mSections.size() == 0) {
            return listPosition;
        } else if (!isSection(listPosition)) {
            int sectionIndex = getIndexWithinSections(listPosition);
            if (isListPositionBeforeFirstSection(listPosition, sectionIndex)) {
                return listPosition;
            } else {
                return listPosition - (sectionIndex + 1);
            }
        } else {
            return NO_CURSOR_POSITION;
        }
    }

    /**
     * Finds the section index for a given list position.
     *
     * @param listPosition the position of the current item in the list with mSections included
     * @return an index in an ordered list of section names
     */
    public int getIndexWithinSections(int listPosition) {
        boolean isSection = false;
        int numPrecedingSections = 0;
        for (Integer sectionPosition : mSections.keySet()) {
            if (listPosition > sectionPosition)
                numPrecedingSections++;
            else if (listPosition == sectionPosition)
                isSection = true;
            else
                break;
        }
        return isSection ? numPrecedingSections : Math.max(numPrecedingSections - 1, 0);
    }

    private boolean isListPositionBeforeFirstSection(int listPosition, int sectionIndex) {
        boolean hasSections = mSections != null && mSections.size() > 0;
        return sectionIndex == 0 && hasSections && listPosition < mSections.firstKey();
    }

    public Context getContext() {
        return mContext;
    }

    public String getString(int resID) {
        return getContext().getString(resID);
    }

    public String getString(int resID, Object... args) {
        return getContext().getString(resID, args);
    }


}
