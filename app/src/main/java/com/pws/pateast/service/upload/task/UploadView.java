package com.pws.pateast.service.upload.task;

import com.pws.pateast.base.AppView;
import com.pws.pateast.enums.MessageType;

public interface UploadView extends AppView {
    void onCompressStart();

    void onCompressComplete();

    void uploadFile(MessageType messageType, String filePath);
}
