package com.pws.pateast.activity.lms.video;

import android.net.Uri;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.pws.pateast.R;
import com.pws.pateast.base.AppPresenter;

/**
 * Created by intel on 29-Jan-18.
 */

public class VideoPresenter extends AppPresenter<VideoView> {
    private VideoView mView;

    @Override
    public VideoView getView() {
        return mView;
    }

    @Override
    public void attachView(VideoView view) {
        mView = view;
    }

    public SimpleExoPlayer buildPlayer(Uri uri) {
        SimpleExoPlayer exoPlayer = buildSimpleExoPlayer();
        MediaSource videoSource = buildVideoSource(uri);
        exoPlayer.prepare(videoSource);

        return exoPlayer;
    }

    private SimpleExoPlayer buildSimpleExoPlayer() {
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        return ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
    }

    private MediaSource buildVideoSource(Uri uri) {
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        String userAgent = Util.getUserAgent(getContext(), getString(R.string.app_name));
        DataSource.Factory dataSourceFactory = new DefaultHttpDataSourceFactory(userAgent, bandwidthMeter);

        return new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);
    }
}
