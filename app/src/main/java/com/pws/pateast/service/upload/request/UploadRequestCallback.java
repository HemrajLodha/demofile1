package com.pws.pateast.service.upload.request;

import io.reactivex.Observable;

/**
 * Created by intel on 10-Aug-17.
 */

public interface UploadRequestCallback<T>
{
    Observable<T> getUploadAbleItem(T t);

    void onUploadEnqueued(T uploadItem);

    void onUploadCompleted(T uploadItem);

    void onUploadComplete();

    void onError(Throwable e);
}
