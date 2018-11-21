package com.pws.pateast.fragment.feeds.media;

import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.model.Feeds;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.utils.Utils;

public class FeedMediaPresenter extends AppPresenter<FeedMediaView> {

    private FeedMediaView mView;

    @Override
    public FeedMediaView getView() {
        return mView;
    }

    @Override
    public void attachView(FeedMediaView view) {
        mView = view;
    }

    public void bind() {
        Feeds feeds = getView().getFeeds();
        if (Utils.isVideoFile(ServiceBuilder.IMAGE_URL + feeds.getFile())) {
            getView().bindVideo(feeds);
        } else {
            getView().bindImage(feeds);
        }
    }
}
