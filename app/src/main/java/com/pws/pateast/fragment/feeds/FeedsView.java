package com.pws.pateast.fragment.feeds;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.pws.pateast.api.model.Feeds;
import com.pws.pateast.base.AppView;
import com.pws.pateast.fragment.feeds.model.FeedList;

import java.util.List;

public interface FeedsView extends AppView, BaseRecyclerAdapter.OnItemClickListener {

    boolean isVisible();

    int getFeedCategory();

    void updateAction(int feedID, boolean isLiked);

    void setFeedAdapter(FeedList feeds, int userID);
}
