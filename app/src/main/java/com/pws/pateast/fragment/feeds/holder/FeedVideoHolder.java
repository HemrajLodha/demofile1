package com.pws.pateast.fragment.feeds.holder;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.adapter.viewholder.BaseItemViewHolder;
import com.google.android.exoplayer2.ui.PlayerView;
import com.pws.pateast.R;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.model.Feeds;

import im.ene.toro.ToroPlayer;
import im.ene.toro.ToroUtil;
import im.ene.toro.exoplayer.ExoPlayerViewHelper;
import im.ene.toro.media.PlaybackInfo;
import im.ene.toro.widget.Container;

public class FeedVideoHolder extends BaseItemViewHolder<Feeds> implements ToroPlayer {

    private PlayerView playerFeedMedia;
    private ExoPlayerViewHelper helper;
    private Uri mediaUri;

    public FeedVideoHolder(View itemView, BaseRecyclerAdapter.OnItemClickListener itemClickListener) {
        super(itemView, itemClickListener);
        playerFeedMedia = (PlayerView) findViewById(R.id.player_feed_media);
    }

    @Override
    public void bind(Feeds feeds) {
        this.mediaUri = Uri.parse(ServiceBuilder.IMAGE_URL + feeds.getFile());
        //this.mediaUri = Uri.parse("file:///android_asset/bbb.mp4");
    }

    @NonNull
    @Override
    public View getPlayerView() {
        return playerFeedMedia;
    }

    @NonNull
    @Override
    public PlaybackInfo getCurrentPlaybackInfo() {
        return helper != null ? helper.getLatestPlaybackInfo() : new PlaybackInfo();
    }

    @Override
    public void initialize(@NonNull Container container, @NonNull PlaybackInfo playbackInfo) {
        if (helper == null) {
            helper = new ExoPlayerViewHelper(this, mediaUri);
        }
        helper.initialize(container, playbackInfo);
    }


    @Override
    public void play() {
        if (helper != null) helper.play();
    }

    @Override
    public void pause() {
        if (helper != null) helper.pause();
    }

    @Override
    public boolean isPlaying() {
        return helper != null && helper.isPlaying();
    }

    @Override
    public void release() {
        if (helper != null) {
            helper.release();
            helper = null;
        }
    }

    @Override
    public boolean wantsToPlay() {
        return ToroUtil.visibleAreaOffset(this, itemView.getParent()) >= 0.65;
    }

    @Override
    public int getPlayerOrder() {
        return getAdapterPosition();
    }
}
