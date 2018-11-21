package com.pws.pateast.widget.chat;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.pws.pateast.R;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.model.Message;
import com.pws.pateast.enums.MessageStatusType;
import com.pws.pateast.enums.MessageType;
import com.pws.pateast.enums.UploadingStatus;
import com.pws.pateast.widget.trasformation.BlurTransformation;

import java.io.File;

/**
 * Created by intel on 11-Aug-17.
 */

public class ChatImageView extends FrameLayout {
    private ImageView imgImage;
    private RelativeLayout containerFile;
    private RelativeLayout containerDownload, containerUpload;
    private RelativeLayout containerDownloadProgress, containerUploadProgress;
    private ImageView imgDownloadCancel, imgUploadCancel;
    private View download, upload;
    private ProgressBar progressDownload, progressUpload;

    private Message message;
    private int resId = R.drawable.user_placeholder;

    private static final int radius = 25;

    private OnClickListener onClickListener;

    public ChatImageView(@NonNull Context context) {
        this(context, null);
    }

    public ChatImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChatImageView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ChatImageView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize();
    }

    private void initialize() {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.chat_image_view, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        imgImage = (ImageView) findViewById(R.id.img_image);
        containerFile = (RelativeLayout) findViewById(R.id.container_file);
        containerDownload = (RelativeLayout) findViewById(R.id.container_download);
        containerUpload = (RelativeLayout) findViewById(R.id.container_upload);

        containerDownloadProgress = (RelativeLayout) findViewById(R.id.container_download_progress);
        containerUploadProgress = (RelativeLayout) findViewById(R.id.container_upload_progress);

        imgDownloadCancel = (ImageView) findViewById(R.id.img_download_cancel);
        imgUploadCancel = (ImageView) findViewById(R.id.img_upload_cancel);

        download = findViewById(R.id.download);
        upload = findViewById(R.id.upload);

        progressDownload = (ProgressBar) findViewById(R.id.progress_download);
        progressUpload = (ProgressBar) findViewById(R.id.progress_upload);
    }

    public void setMessage(Message message) {
        this.message = message;
    }


    public void setClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void apply() {
        switch (MessageType.getMessageType(message.getType())) {
            case IMAGE:
                resId = R.drawable.ic_image_message;
                break;
            case VIDEO:
                resId = R.drawable.ic_video_message;
                break;
            case AUDIO:
                resId = R.drawable.ic_audio_message;
                break;
            case OTHER:
                resId = R.drawable.ic_file_message;
                break;
        }


        if (message.isIncomingMessage())
            isIncoming();
        else
            isOutgoing();
    }

    private void isIncoming() {
        setIncomingVisibility();

        setImageUrl(message.getUrl(), false, resId);
    }

    private void setIncomingVisibility() {
        containerDownload.setVisibility(GONE);
        containerUpload.setVisibility(GONE);
        containerFile.setOnClickListener(onClickListener);
    }

    private void isOutgoing() {

        setOutgoingVisibility();

        if (message.getUri() != null) {
            File file = new File(message.getUri());
            if (file.exists())
                setImageUri(Uri.fromFile(file), message.getMsg_status() == MessageStatusType.STATUS_SAVE.getValue(), resId);
            else
                setImageUrl(message.getUrl(), true, resId);
        } else
            setImageUrl(message.getUrl(), true, resId);
    }

    private void setOutgoingVisibility() {
        containerDownload.setVisibility(GONE);
        containerUpload.setVisibility(GONE);
        containerDownloadProgress.setVisibility(GONE);
        upload.setVisibility(GONE);
        imgUploadCancel.setVisibility(GONE);
        if (message.getMsg_status() != MessageStatusType.STATUS_SAVE.getValue()) {
            containerFile.setOnClickListener(onClickListener);
            return;
        }
        if (message.getUploadStatus() != null) {
            switch (UploadingStatus.getUploadingStatus(message.getUploadStatus())) {
                case NOT_UPLOADED:
                    containerUpload.setVisibility(VISIBLE);
                    upload.setVisibility(VISIBLE);
                    upload.setOnClickListener(onClickListener);
                    break;
                case WAITING:
                case IN_PROGRESS:
                    containerUpload.setVisibility(VISIBLE);
                    containerDownloadProgress.setVisibility(VISIBLE);
                    break;
                case UPLOADED:
                    containerUpload.setVisibility(VISIBLE);
                    upload.setVisibility(VISIBLE);
                    upload.setOnClickListener(onClickListener);
                    break;
            }
        }

    }

    private void setImageUri(Uri imageUri, boolean isBlur, int resId) {
        if (isBlur) {
            Glide.with(getContext())
                    .load(imageUri)
                    .placeholder(resId)
                    .error(resId)
                    .dontAnimate()
                    .bitmapTransform(new BlurTransformation(getContext(), radius))
                    .into(imgImage);
        } else {
            Glide.with(getContext())
                    .load(imageUri)
                    .placeholder(resId)
                    .error(resId)
                    .dontAnimate()
                    .into(imgImage);
        }
    }

    private void setImageUrl(String imageUrl, boolean isBlur, int resId) {
        imageUrl = ServiceBuilder.IMAGE_URL + imageUrl;
        if (isBlur) {
            Glide.with(getContext())
                    .load(imageUrl)
                    .placeholder(resId)
                    .error(resId)
                    .dontAnimate()
                    .bitmapTransform(new BlurTransformation(getContext(), radius))
                    .into(imgImage);
        } else {
            Glide.with(getContext())
                    .load(imageUrl)
                    .placeholder(resId)
                    .error(resId)
                    .dontAnimate()
                    .into(imgImage);
        }

    }

}
