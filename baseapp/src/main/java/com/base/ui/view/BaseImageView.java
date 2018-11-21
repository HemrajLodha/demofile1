package com.base.ui.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;


public class BaseImageView extends AppCompatImageView {
    private String mImageUrl;

    public BaseImageView(Context context) {
        super(context);
    }

    public BaseImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setImageUrl(String url, int errorResourceId) {
        mImageUrl = url;
        Glide.with(getContext())
                .load(url)
                .error(errorResourceId)
                .into(this);
    }

    public void setImageUrl(String url, int placeHolderResourceId, int errorResourceId) {
        mImageUrl = url;
        Glide.with(getContext())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(placeHolderResourceId)
                .error(errorResourceId)
                .into(this);
    }

    public void setImageUrl(String url, int placeHolderDrawable, Drawable errorDrawable) {
        mImageUrl = url;
        Glide.with(getContext())
                .load(url)
                .placeholder(placeHolderDrawable)
                .error(errorDrawable)
                .into(this);
    }

    public void setImageUrl(String url, final ProgressBar progressBar) {
        mImageUrl = url;
        progressBar.setVisibility(VISIBLE);
        Glide.with(getContext())
                .load(url)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(GONE);
                        return false;
                    }
                })
                .into(this);
    }

    public void setImageUrl(String url, final ProgressBar progressBar, int errorResourceId) {
        mImageUrl = url;
        progressBar.setVisibility(VISIBLE);
        Glide.with(getContext())
                .load(url)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(GONE);
                        return false;
                    }
                })
                .error(errorResourceId)
                .into(this);
    }

    public void setImageUrl(String url, final ProgressBar progressBar, Drawable errorDrawable) {
        mImageUrl = url;
        progressBar.setVisibility(VISIBLE);
        Glide.with(getContext())
                .load(url)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(GONE);
                        return false;
                    }
                })
                .error(errorDrawable)
                .into(this);
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String url) {
        mImageUrl = url;
        Glide.with(getContext())
                .load(url)
                .into(this);
    }
}
