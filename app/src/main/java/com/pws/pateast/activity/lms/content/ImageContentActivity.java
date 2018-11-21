package com.pws.pateast.activity.lms.content;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.pws.pateast.R;
import com.pws.pateast.base.Extras;
import com.pws.pateast.base.ResponseAppActivity;
import com.pws.pateast.utils.ImageUtils;

/**
 * Created by intel on 29-Jan-18.
 */

public class ImageContentActivity extends ResponseAppActivity {
    private ImageView imgContent;
    private Intent intent;
    private Uri uri;
    private String title;

    @Override
    public boolean isToolbarSetupEnabled() {
        return true;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.activity_image_content;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);
        intent = getIntent();
        imgContent = findViewById(R.id.image_content);
        title = intent.getStringExtra(Extras.TITLE);
        uri = intent.getData();
        setTitle(title);
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(imgContent);
        ImageUtils.setGifUrl(this, imageViewTarget, uri.toString());
    }

}
