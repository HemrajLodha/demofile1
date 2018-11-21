package com.pws.pateast.fragment.feeds.holder;

import android.view.View;
import android.widget.ImageView;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.adapter.viewholder.BaseItemViewHolder;
import com.pws.pateast.R;
import com.pws.pateast.api.model.Feeds;
import com.pws.pateast.utils.ImageUtils;

public class FeedImageHolder extends BaseItemViewHolder<Feeds> {
    private ImageView imgFeedMedia;

    public FeedImageHolder(View itemView, BaseRecyclerAdapter.OnItemClickListener itemClickListener) {
        super(itemView, itemClickListener);
        imgFeedMedia = (ImageView) findViewById(R.id.img_feed_media);
    }

    @Override
    public void bind(Feeds feeds) {
        ImageUtils.setImageUrl(itemView.getContext(), imgFeedMedia, feeds.getFile(), R.drawable.ic_image_placeholder);
    }
}
