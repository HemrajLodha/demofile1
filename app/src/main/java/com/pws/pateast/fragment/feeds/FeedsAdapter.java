package com.pws.pateast.fragment.feeds;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.pws.pateast.R;
import com.pws.pateast.api.model.Feeds;
import com.pws.pateast.fragment.feeds.holder.FeedsHolder;

import im.ene.toro.CacheManager;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

public class FeedsAdapter extends BaseRecyclerAdapter<Feeds, FeedsHolder> implements CacheManager {

    private int userID;

    public FeedsAdapter(Context context, OnItemClickListener onItemClickListener) {
        super(context, onItemClickListener);
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    @Override
    protected int getItemResourceLayout(int viewType) {
        return R.layout.item_feeds;
    }

    public void updateAction(int feedID, boolean isLiked) {
        final Feeds[] feeds = {null};

        mDatas = StreamSupport
                .stream(mDatas)
                .map((feed) -> {
                    if (feed.getId() == feedID) {
                        feed.setLiked(isLiked ? 1 : 0);
                        feed.setLikes(isLiked ? feed.getLikes() + 1 : feed.getLikes() - 1);
                        feeds[0] = feed;
                    }
                    return feed;
                })
                .collect(Collectors.toList());
        if (feeds[0] != null) {
            int position = mDatas.indexOf(feeds[0]);
            notifyItemChanged(position);
        }
    }

    @Override
    public FeedsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FeedsHolder(getView(parent, viewType), mItemClickListener, userID);
    }

    @Override
    public void onViewRecycled(@NonNull FeedsHolder holder) {
        super.onViewRecycled(holder);
        holder.onRecycled();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull FeedsHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.onDetached();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull FeedsHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.onAttached();
    }

    @NonNull
    @Override
    public Object getKeyForOrder(int order) {
        return order;
    }

    @Nullable
    @Override
    public Integer getOrderForKey(@NonNull Object key) {
        return key instanceof Feeds ? getDatas().indexOf(key) : null;
    }
}
