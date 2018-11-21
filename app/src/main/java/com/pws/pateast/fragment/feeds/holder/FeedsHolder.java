package com.pws.pateast.fragment.feeds.holder;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.SnapHelper;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.adapter.viewholder.BaseItemViewHolder;
import com.pws.pateast.R;
import com.pws.pateast.api.model.Feeds;
import com.pws.pateast.fragment.feeds.FeedMediaAdapter;
import com.pws.pateast.fragment.feeds.indicator.OverflowPagerIndicator;
import com.pws.pateast.fragment.feeds.indicator.SimpleSnapHelper;
import com.pws.pateast.fragment.feeds.model.FeedList;
import com.pws.pateast.fragment.feeds.utils.ExtraPlaybackInfo;
import com.pws.pateast.utils.DateUtils;
import com.pws.pateast.utils.ImageUtils;
import com.pws.pateast.utils.TimeUtils;

import java.util.Calendar;
import java.util.List;

import im.ene.toro.CacheManager;
import im.ene.toro.PlayerSelector;
import im.ene.toro.ToroPlayer;
import im.ene.toro.ToroUtil;
import im.ene.toro.media.PlaybackInfo;
import im.ene.toro.widget.Container;

import static com.pws.pateast.utils.DateUtils.DATE_FORMAT_PATTERN;

public class FeedsHolder extends BaseItemViewHolder<Feeds> implements ToroPlayer {

    private ImageView imgProfile;
    private TextView tvUserName, tvFeedTime, tvDescription, imgLikes, tvLikes, imgOptions, tvRejectReason;
    private Container containerFeedMedia;
    private SnapHelper snapHelper;
    private OverflowPagerIndicator indicator;
    private Calendar mCalendar;
    private int userID;
    private int initPosition = -1;


    public FeedsHolder(View itemView, BaseRecyclerAdapter.OnItemClickListener itemClickListener, int userID) {
        super(itemView, itemClickListener);
        this.userID = userID;
        imgProfile = (ImageView) findViewById(R.id.img_profile);
        tvUserName = (TextView) findViewById(R.id.tv_user_name);
        tvFeedTime = (TextView) findViewById(R.id.tv_feed_time);
        tvDescription = (TextView) findViewById(R.id.tv_description);
        imgLikes = (TextView) findViewById(R.id.img_likes);
        imgOptions = (TextView) findViewById(R.id.img_options);
        tvLikes = (TextView) findViewById(R.id.tv_likes);
        tvRejectReason = (TextView) findViewById(R.id.tv_reject_reason);
        containerFeedMedia = (Container) findViewById(R.id.container_feed_media);
        indicator = (OverflowPagerIndicator) findViewById(R.id.indicator);
        snapHelper = new SimpleSnapHelper(indicator);
    }

    @Override
    public void bind(Feeds feeds) {
        mCalendar = Calendar.getInstance();
        imgLikes.setEnabled(true);
        ImageUtils.setImageUrl(itemView.getContext(), imgProfile, feeds.getUser().getUser_image(), R.drawable.avatar1);
        tvUserName.setText(feeds.getUser().getUserdetails().get(0).getFullname());
        tvFeedTime.setText(TimeUtils.toRelative(DateUtils.parse(feeds.getCreatedAt(), DATE_FORMAT_PATTERN), mCalendar.getTime(), 1));
        imgLikes.setText(feeds.getLiked() == 1 ? R.string.fa_heart : R.string.fa_heart_o);
        tvLikes.setText(itemView.getContext().getString(R.string.title_likes, feeds.getLikes()));
        tvDescription.setText(feeds.getDescription());
        tvDescription.setVisibility(TextUtils.isEmpty(feeds.getDescription()) ? View.GONE : View.VISIBLE);
        tvRejectReason.setText(feeds.getReject_reason());
        ((LinearLayout) tvRejectReason.getParent()).setVisibility(TextUtils.isEmpty(feeds.getReject_reason()) ? View.GONE : View.VISIBLE);
        setOptions(feeds);
        setFeedMediaAdapter(feeds.getFeedrecords());
        imgLikes.setOnClickListener(this);
        imgOptions.setOnClickListener(this);
    }

    private void setOptions(Feeds feeds) {
        imgOptions.setVisibility(View.GONE);
        if (feeds.getUserId() == userID) {
            imgOptions.setText(R.string.fa_trash);
            imgOptions.setVisibility(View.VISIBLE);
        }

        else if (feeds.getControlUserId() == userID) {
            imgOptions.setText(R.string.fa_ellipsis_v);
            imgOptions.setVisibility(View.VISIBLE);
        }
    }


    private void setFeedMediaAdapter(FeedList feeds) {
        FeedMediaAdapter adapter = new FeedMediaAdapter(itemView.getContext());
        adapter.add(feeds);
        getPlayerView().setAdapter(adapter);
        indicator.attachToRecyclerView(getPlayerView());
        if (getPlayerView().getCacheManager() == null) {
            getPlayerView().setCacheManager(new StateManager(feeds));
        }
        containerFeedMedia.setVisibility(feeds.isEmpty() ? View.GONE : View.VISIBLE);
        indicator.setVisibility(feeds.isEmpty() ? View.GONE : View.VISIBLE);
    }

    public void onRecycled() {

    }

    public void onDetached() {
        snapHelper.attachToRecyclerView(null);
    }

    public void onAttached() {
        snapHelper.attachToRecyclerView(getPlayerView());
    }

    @NonNull
    @Override
    public Container getPlayerView() {
        return containerFeedMedia;
    }

    @NonNull
    @Override
    public PlaybackInfo getCurrentPlaybackInfo() {
        SparseArray<PlaybackInfo> actualInfos = containerFeedMedia.getLatestPlaybackInfos();
        ExtraPlaybackInfo resultInfo = new ExtraPlaybackInfo(actualInfos);

        List<ToroPlayer> activePlayers = getPlayerView().filterBy(Container.Filter.PLAYING);
        if (activePlayers.size() >= 1) {
            resultInfo.setResumeWindow(activePlayers.get(0).getPlayerOrder());
        }

        return resultInfo;
    }

    @Override
    public void initialize(@NonNull Container container, @NonNull PlaybackInfo playbackInfo) {
        initPosition = -1;
        if (playbackInfo instanceof ExtraPlaybackInfo) {
            //noinspection unchecked
            SparseArray<PlaybackInfo> cache = ((ExtraPlaybackInfo) playbackInfo).actualInfo;
            if (cache != null && cache.size() > 0) {
                for (int i = 0; i < cache.size(); i++) {
                    int key = cache.keyAt(i);
                    getPlayerView().savePlaybackInfo(key, cache.get(key));
                }
            }
            initPosition = playbackInfo.getResumeWindow();
        }
        getPlayerView().setPlayerSelector(PlayerSelector.NONE);
    }

    @Override
    public void play() {
        if (initPosition >= 0)
            getPlayerView().scrollToPosition(initPosition);
        initPosition = -1;
        getPlayerView().setPlayerSelector(PlayerSelector.DEFAULT);
    }

    @Override
    public void pause() {
        getPlayerView().setPlayerSelector(PlayerSelector.NONE);
    }

    @Override
    public boolean isPlaying() {
        return getPlayerView().filterBy(Container.Filter.PLAYING).size() > 0;
    }

    @Override
    public void release() {
        List<ToroPlayer> managed = getPlayerView().filterBy(Container.Filter.MANAGING);
        for (ToroPlayer player : managed) {
            if (player.isPlaying()) {
                getPlayerView().savePlaybackInfo(player.getPlayerOrder(), player.getCurrentPlaybackInfo());
                player.pause();
            }
            player.release();
        }
        getPlayerView().setPlayerSelector(PlayerSelector.NONE);
    }

    @Override
    public boolean wantsToPlay() {
        return ToroUtil.visibleAreaOffset(this, itemView.getParent()) >= 0.65;
    }

    @Override
    public int getPlayerOrder() {
        return getAdapterPosition();
    }

    static class StateManager implements CacheManager {

        final FeedList feedList;

        StateManager(FeedList mediaList) {
            this.feedList = mediaList;
        }

        @NonNull
        @Override
        public Object getKeyForOrder(int order) {
            return this.feedList.get(order);
        }

        @Nullable
        @Override
        public Integer getOrderForKey(@NonNull Object key) {
            return key instanceof Feeds ? this.feedList.indexOf(key) : null;
        }
    }
}
