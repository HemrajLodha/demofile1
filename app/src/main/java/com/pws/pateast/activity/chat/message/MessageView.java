package com.pws.pateast.activity.chat.message;

import android.database.Cursor;
import android.net.Uri;

import com.pws.pateast.api.model.Message;
import com.pws.pateast.service.upload.task.UploadView;

/**
 * Created by intel on 28-Jul-17.
 */

public interface MessageView extends UploadView {
    int getAppTheme();

    int getSenderId();

    String getReceiverName();

    String getReceiverImage();

    int getReceiverId();

    void emitMessageSeen(Message message);

    void emitStartTyping();

    void emitStopTyping();

    void setMessageAdapter(Cursor cursor);

    void setSubTitle(String subTitle);

    void openCamera(int media);

    void openGallery(int media);

    void viewFile(Uri uri, String type);


    void userBlocked(boolean isBlocked);

    boolean isUploading();
}
