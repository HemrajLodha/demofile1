package com.pws.pateast.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.pws.pateast.enums.MessageType;

import java.util.List;

import static com.pws.pateast.Constants.FILE_AUTHORITY;

/**
 * Created by intel on 09-Aug-17.
 */

public class MediaHelper {
    public static final int TYPE_IMAGE = 1;
    public static final int TYPE_VIDEO = 2;

    public static final int REQUEST_TAKE_PHOTO = 0x101;
    public static final int REQUEST_TAKE_VIDEO = 0x102;
    public static final int REQUEST_PICK_PHOTO = 0x103;
    public static final int REQUEST_PICK_VIDEO = 0x104;
    public static final int REQUEST_PICK_AUDIO = 0x105;
    public static final int REQUEST_PICK_OTHER = 0x106;

    private static final int EXTRA_DURATION_LIMIT = 60;
    private static final int EXTRA_VIDEO_QUALITY = 1;

    private Uri mUri;
    private List<String> allowedMimeTypes;

    public MediaHelper(Context context) {
        allowedMimeTypes = Utils.getAllowedFileMimeTypes(context);
    }

    public Uri getUri() {
        return mUri;
    }

    private void setUri(Uri mUri) {
        this.mUri = mUri;
    }

    public Intent dispatchTakePictureIntent(Context context) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N)
                setUri(Uri.fromFile(FileUtils.getOutputMediaFile(TYPE_IMAGE, true)));
            else
                setUri(FileProvider.getUriForFile(context, FILE_AUTHORITY, FileUtils.getOutputMediaFile(TYPE_IMAGE, true)));
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, getUri());
            return takePictureIntent;
        }

        return null;
    }

    public Intent dispatchTakeVideoIntent(Context context) {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        takeVideoIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (takeVideoIntent.resolveActivity(context.getPackageManager()) != null) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N)
                setUri(Uri.fromFile(FileUtils.getOutputMediaFile(TYPE_VIDEO, true)));
            else
                setUri(FileProvider.getUriForFile(context, FILE_AUTHORITY, FileUtils.getOutputMediaFile(TYPE_VIDEO, true)));
            takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, EXTRA_DURATION_LIMIT);
            takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, getUri());
            takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, EXTRA_VIDEO_QUALITY);
            return takeVideoIntent;
        }
        return null;
    }

    public Intent dispatchPickPictureIntent(Context context) {
        Intent pickPictureIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (pickPictureIntent.resolveActivity(context.getPackageManager()) != null) {
            return pickPictureIntent;
        }
        return null;
    }

    public Intent dispatchPickVideoIntent(Context context) {
        Intent pickPictureIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        if (pickPictureIntent.resolveActivity(context.getPackageManager()) != null) {
            return pickPictureIntent;
        }
        return null;
    }

    public Intent dispatchPickAudioIntent(Context context) {
        Intent pickAudioIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        if (pickAudioIntent.resolveActivity(context.getPackageManager()) != null) {
            return pickAudioIntent;
        }
        return null;
    }

    public Intent dispatchPickFileIntent(Context context) {
        Intent pickFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        pickFileIntent.setType("*/*");
        pickFileIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        // Only return URIs that can be opened with ContentResolver
        pickFileIntent.addCategory(Intent.CATEGORY_OPENABLE);
        if (pickFileIntent.resolveActivity(context.getPackageManager()) != null) {
            return pickFileIntent;
        }
        return null;
    }

    public static Intent dispatchOpenFileIntent(Context context, Uri uri, String type) {
        Intent openFileIntent = new Intent(Intent.ACTION_VIEW);
        openFileIntent.setDataAndType(uri, type);
        openFileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (openFileIntent.resolveActivity(context.getPackageManager()) != null) {
            return openFileIntent;
        }
        return null;
    }

    public MessageType getMessageType(Context context, Uri uri) {
        setUri(uri);
        return getMessageTypeFromMime(FileUtils.getMimeType(context, getUri()));
    }

    public MessageType getMessageType(String filepath) {
        return getMessageTypeFromMime(FileUtils.getMimeType(filepath));
    }

    public MessageType getMessageTypeFromMime(String mimeType) {
        if (isImageFile(mimeType))
            return MessageType.IMAGE;
        else if (isVideoFile(mimeType))
            return MessageType.VIDEO;
        else if (isAudioFile(mimeType))
            return MessageType.AUDIO;
        else if (isOtherFile(mimeType))
            return MessageType.OTHER;
        else
            return MessageType.NONE;
    }

    public boolean isImageFile(String mimeType) {
        return mimeType != null && allowedMimeTypes.contains(mimeType) && mimeType.startsWith("image");
    }

    public boolean isVideoFile(String mimeType) {
        return mimeType != null && allowedMimeTypes.contains(mimeType) && mimeType.startsWith("video");
    }

    public boolean isAudioFile(String mimeType) {
        return mimeType != null && allowedMimeTypes.contains(mimeType) && mimeType.startsWith("audio");
    }

    public boolean isOtherFile(String mimeType) {
        return mimeType != null && allowedMimeTypes.contains(mimeType);
    }
}
