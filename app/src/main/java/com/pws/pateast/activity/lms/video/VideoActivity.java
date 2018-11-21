package com.pws.pateast.activity.lms.video;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.pws.pateast.R;
import com.pws.pateast.base.Extras;
import com.pws.pateast.base.ResponseAppActivity;

/**
 * Created by intel on 17-Jan-18.
 */

public class VideoActivity extends ResponseAppActivity implements VideoView {
    private static final String LOG_TAG = VideoActivity.class.getSimpleName();

    private SimpleExoPlayerView playerView;
    private SimpleExoPlayer player;
    private SeekBar progressBar;
    private ImageButton btnClose;
    private TextView tvTitle;
    private Intent intent;
    private Uri uri;
    private String title;
    private final VideoProgressUpdater progressUpdater = new VideoProgressUpdater();
    private VideoPresenter mPresenter;

    @Override
    protected int getResourceLayout() {
        return R.layout.activity_video;
    }

    @Override
    public boolean isToolbarSetupEnabled() {
        return false;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);
        intent = getIntent();
        tvTitle = findViewById(R.id.tv_title);
        btnClose = findViewById(R.id.btn_close);
        playerView = findViewById(R.id.player_view);
        progressBar = playerView.findViewById(R.id.progress);

        progressBar.setOnSeekBarChangeListener(this);
        btnClose.setOnClickListener(this);
        title = intent.getStringExtra(Extras.TITLE);
        uri = intent.getData();
        tvTitle.setText(title != null ? title : getString(R.string.app_name));

        mPresenter = new VideoPresenter();
        mPresenter.attachView(this);

        player = mPresenter.buildPlayer(uri);
        playerView.setPlayer(player);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null)
            player.setPlayWhenReady(true);
        progressUpdater.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null)
            player.setPlayWhenReady(false);
        progressUpdater.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
        mPresenter.detachView();
    }

    private void updateVideoProgress() {
        if (player != null) {
            long videoProgress = player.getCurrentPosition() * 100 / player.getDuration();
            progressBar.setProgress((int) videoProgress);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        long videoPosition = player.getDuration() * progressBar.getProgress() / 100;
        player.seekTo(videoPosition);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close:
                finish();
                break;
        }
    }

    private final class VideoProgressUpdater extends Handler {

        public void start() {
            sendEmptyMessage(0);
        }

        public void stop() {
            removeMessages(0);
        }

        @Override
        public void handleMessage(Message msg) {
            updateVideoProgress();
            sendEmptyMessageDelayed(0, 500);
        }
    }
}
