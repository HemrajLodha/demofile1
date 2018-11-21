package com.pws.pateast.download;

/**
 * Created by anshul on 18/2/17.
 */

public interface ItemDownloadCallback<T> {

  void onDownloadEnqueued(T downloadableItem);

  void onDownloadStarted(T downloadableItem);

  void onDownloadComplete();

  void onError();
}
