package com.pws.pateast.download;

/**
 * Created by anshul on 7/2/17.
 */

public class DownloadableResult {

  private long id;
  private int percent;
  private int status;
  private String fileName;
  private String fileUri;
  private String fileUrl;

  public DownloadableResult() {
  }

  private String downloadStatus;

  public DownloadableResult(int percent, String downloadStatus) {
    this.percent = percent;
    this.downloadStatus = downloadStatus;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public int getPercent() {
    return percent;
  }

  public void setPercent(int percent) {
    this.percent = percent;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getFileUri() {
    return fileUri;
  }

  public void setFileUri(String fileUri) {
    this.fileUri = fileUri;
  }

  public String getFileUrl() {
    return fileUrl;
  }

  public void setFileUrl(String fileUrl) {
    this.fileUrl = fileUrl;
  }

  public String getDownloadStatus() {
    return downloadStatus;
  }

  public void setDownloadStatus(String downloadStatus) {
    this.downloadStatus = downloadStatus;
  }
}
