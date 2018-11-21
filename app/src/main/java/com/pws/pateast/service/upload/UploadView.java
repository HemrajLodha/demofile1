package com.pws.pateast.service.upload;

import com.pws.pateast.api.model.Message;
import com.pws.pateast.base.ServiceView;
import com.pws.pateast.enums.MessageType;

/**
 * Created by intel on 09-Aug-17.
 */

public interface UploadView extends ServiceView
{

    void uploadStart(Message message);


    void uploadNext();


    void uploading(long bytesWritten, long contentLength);


    void uploadSuccess(Message message);


    void uploadFail(String error);


    void uploadComplete();
}
