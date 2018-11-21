package com.pws.pateast.fragment.feeds.media;

import com.pws.pateast.api.model.Feeds;
import com.pws.pateast.base.AppView;

public interface FeedMediaView extends AppView {

    Feeds getFeeds();

    void bindImage(Feeds feed);

    void bindVideo(Feeds feed);

    void pauseVideo();

    void resumeVideo();

   // void onPageChanged();
}
