package com.pws.pateast.fragment.feeds;

import android.content.Context;
import android.view.ViewGroup;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.adapter.viewholder.BaseItemViewHolder;
import com.pws.pateast.R;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.model.Feeds;
import com.pws.pateast.fragment.feeds.holder.FeedImageHolder;
import com.pws.pateast.fragment.feeds.holder.FeedVideoHolder;
import com.pws.pateast.utils.Utils;

public class FeedMediaAdapter extends BaseRecyclerAdapter<Feeds, BaseItemViewHolder<Feeds>> {
    static final int TYPE_VIDEO = 1;
    static final int TYPE_IMAGE = 2;

    public FeedMediaAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemResourceLayout(int viewType) {
        switch (viewType) {
            case TYPE_IMAGE:
                return R.layout.item_feed_image;
            case TYPE_VIDEO:
                return R.layout.item_feed_video;
        }
        return 0;
    }

    @Override
    public BaseItemViewHolder<Feeds> onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_IMAGE:
                return new FeedImageHolder(getView(parent, viewType), mItemClickListener);
            case TYPE_VIDEO:
                return new FeedVideoHolder(getView(parent, viewType), mItemClickListener);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        Feeds feeds = getItem(position);
        if (Utils.isVideoFile(ServiceBuilder.IMAGE_URL + feeds.getFile())) {
            return TYPE_VIDEO;
        } else {
            return TYPE_IMAGE;
        }
    }
}
