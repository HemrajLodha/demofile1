package com.pws.pateast.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.pws.pateast.R;
import com.pws.pateast.api.ServiceBuilder;

/**
 * Created by intel on 28-Apr-17.
 */

public class ImageUtils {

    public static void setImageUri(Context context, ImageView imageView, Uri uri, int errorResourceId) {
        Glide.with(context)
                .load(uri)
                .error(errorResourceId)
                .placeholder(errorResourceId)
                .dontAnimate()
                .into(imageView);
    }

    public static void setImageUrl(Context context, ImageView imageView, String url, int errorResourceId) {
        String imageUrl = ServiceBuilder.IMAGE_URL + url;
        Glide.with(context)
                .load(imageUrl)
                .error(errorResourceId)
                .placeholder(errorResourceId)
                .dontAnimate()
                .into(imageView);
    }


    public static void setImageUrl(Context context, ImageView imageView, String url, int placeHolderDrawable, Drawable errorDrawable) {
        String imageUrl = ServiceBuilder.IMAGE_URL + url;
        Glide.with(context)
                .load(imageUrl)
                .placeholder(placeHolderDrawable)
                .error(errorDrawable)
                .into(imageView);
    }


    public static void setImageUrl(Context context, ImageView imageView, String url) {
        Glide.with(context)
                .load(url)
                .into(imageView);
    }

    public static void setImageUri(Context context, ImageView imageView, Uri uri) {
        Glide.with(context)
                .load(uri)
                .into(imageView);
    }

    public static void setGifUrl(Context context, GlideDrawableImageViewTarget imageView, String url) {
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.ic_image_placeholder)
                .into(imageView);
    }
}
