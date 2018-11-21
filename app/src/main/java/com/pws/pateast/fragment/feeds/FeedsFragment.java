package com.pws.pateast.fragment.feeds;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;

import com.pws.pateast.R;
import com.pws.pateast.api.model.Feeds;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.base.Extras;
import com.pws.pateast.fragment.feeds.model.FeedList;
import com.pws.pateast.widget.SpaceItemDecoration;

import im.ene.toro.PlayerSelector;
import im.ene.toro.widget.Container;

public class FeedsFragment extends AppFragment implements FeedsView {
    private Container rvFeeds;
    private LinearLayoutManager llm;
    private FeedsAdapter mAdapter;

    private FeedsPresenter mPresenter;
    final Handler handler = new Handler();
    private PlayerSelector selector = PlayerSelector.DEFAULT;

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_feeds;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        rvFeeds = (Container) findViewById(R.id.rv_feeds);
        llm = new LinearLayoutManager(getContext());
        rvFeeds.setLayoutManager(llm);
        llm.setItemPrefetchEnabled(true);
        rvFeeds.addItemDecoration(new SpaceItemDecoration(getContext(), R.dimen.size_5, 1, true));

        mPresenter = new FeedsPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void onActionClick() {
        super.onActionClick();
        mPresenter.getFeeds();
    }

    @Override
    public int getFeedCategory() {
        return getArguments().getInt(Extras.EXTRA_TYPE, 0);
    }

    @Override
    public void updateAction(int feedID, boolean isLiked) {
        if (mAdapter != null) {
            mAdapter.updateAction(feedID, isLiked);
        }
    }

    @Override
    public void setFeedAdapter(FeedList feeds, int userID) {
        if (rvFeeds.getAdapter() == null) {
            mAdapter = new FeedsAdapter(getContext(), this);
            mAdapter.setUserID(userID);
            rvFeeds.setAdapter(mAdapter);
            rvFeeds.setCacheManager(mAdapter);
            rvFeeds.setPlayerSelector(null);
            // Using TabLayout has a downside: once we click to a tab to change page, there will be no animation,
            // which will cause our setup doesn't work well. We need a delay to make things work.
            handler.postDelayed(() -> {
                if (rvFeeds != null)
                    rvFeeds.setPlayerSelector(selector);
            }, 500);
        }
        mAdapter.update(feeds);
    }


    @Override
    public void onItemClick(View view, int position) {
        Feeds feed = mAdapter.getItem(position);
        switch (view.getId()) {
            case R.id.img_options:
                if (feed.getUserId() == mPresenter.getUserId()) {
                    mPresenter.showDeleteFeedDialog(feed.getId());
                }
                else if (feed.getControlUserId() == mPresenter.getUserId()) {
                    PopupMenu popup = new PopupMenu(getContext(), view);
                    popup.inflate(R.menu.menu_feeds);
                    popup.getMenu().findItem(R.id.menu_approve_class).setVisible(feed.getApproved() == 0);
                    popup.getMenu().findItem(R.id.menu_approve_all).setVisible(feed.getApproved() == 0);
                    popup.getMenu().findItem(R.id.menu_disapprove).setVisible(feed.getApproved() != 0);
                    popup.setOnMenuItemClickListener(new OnMenuItemClickListener(feed));
                    popup.show();
                }
                break;
            case R.id.img_likes:
                if (view.isEnabled()) {
                    view.setEnabled(false);
                    boolean isLike = feed.getLiked() == 1;
                    mPresenter.likeUnlikeFeed(feed.getId(), !isLike);
                }
                break;
            default:
                break;
        }
    }

    class OnMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        Feeds feed;

        public OnMenuItemClickListener(Feeds feed) {
            this.feed = feed;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_reject:
                    mPresenter.showRejectFeedDialog(feed.getId());
                    return true;
                case R.id.menu_disapprove:
                    mPresenter.showApproveFeedDialog(feed.getId(), 0);
                    return true;
                case R.id.menu_approve_class:
                    mPresenter.showApproveFeedDialog(feed.getId(), 1);
                    return true;
                case R.id.menu_approve_all:
                    mPresenter.showApproveFeedDialog(feed.getId(), 2);
                    return true;
            }
            return false;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            selector = PlayerSelector.DEFAULT;
        } else {
            selector = PlayerSelector.NONE;
        }

        handler.postDelayed(() -> {
            if (rvFeeds != null) rvFeeds.setPlayerSelector(selector);
        }, 500);
    }

    @Override
    public void onDestroyView() {
        handler.removeCallbacksAndMessages(null);
        llm = null;
        mAdapter = null;
        selector = null;
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        super.onDestroyView();
    }
}
