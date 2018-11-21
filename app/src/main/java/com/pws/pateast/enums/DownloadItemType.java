package com.pws.pateast.enums;

/**
 * Created by anshul on 6/2/17.
 */

public enum DownloadItemType {
  ASSIGNMENT("assignment");

  private String itemType;

  DownloadItemType(String downloadStatus) {
    this.itemType = downloadStatus;
  }

  public String getType() {
    return itemType;
  }

  public static DownloadItemType getValue(String downloadType) {
    for (DownloadItemType type : DownloadItemType.values()) {
      if (type.getType().equalsIgnoreCase(downloadType)) {
        return type;
      }
    }
    return null;
  }

}
