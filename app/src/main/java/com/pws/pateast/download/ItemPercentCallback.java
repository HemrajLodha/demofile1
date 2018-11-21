package com.pws.pateast.download;

/**
 * Created by anshul on 18/2/17.
 */

public interface ItemPercentCallback<T> {
  void updateDownloadableItem(T downloadableItem);
}
