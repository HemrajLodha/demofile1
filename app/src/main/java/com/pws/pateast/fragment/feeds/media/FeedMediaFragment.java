package com.pws.pateast.fragment.feeds.media;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.pws.pateast.R;
import com.pws.pateast.api.model.Feeds;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.base.Extras;

public class FeedMediaFragment extends AppFragment implements FeedMediaView {


    private FeedMediaPresenter mPresenter;

    @Override
    protected int getResourceLayout() {
        return R.layout.item_feed_video;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {

        mPresenter = new FeedMediaPresenter();
        mPresenter.attachView(this);
        mPresenter.bind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    @Override
    public Feeds getFeeds() {
        return getArguments().getParcelable(Extras.FEED);
    }

    @Override
    public void bindImage(Feeds feed) {

    }

    @Override
    public void bindVideo(Feeds feed) {

    }

    @Override
    public void pauseVideo() {

    }

    @Override
    public void resumeVideo() {

    }

    @Override
    public void onResume() {
        super.onResume();
        resumeVideo();
    }

    @Override
    public void onPause() {
        super.onPause();
        pauseVideo();
    }
}
