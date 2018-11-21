package com.pws.pateast.enums;

/**
 * Created by intel on 14-Aug-17.
 */

public enum UploadingStatus
{
    NOT_UPLOADED("notUploaded"),
    WAITING("waiting"),
    IN_PROGRESS("inProgress"),
    UPLOADED("uploaded");

    private final String uploadStatus;

    UploadingStatus(String uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

    public String getUploadStatus() {
        return uploadStatus;
    }

    public static UploadingStatus getUploadingStatus(String uploadStatus)
    {
        for (UploadingStatus uploadingStatus : values())
        {
            if(uploadingStatus.getUploadStatus().equalsIgnoreCase(uploadStatus))
                return uploadingStatus;
        }
        return NOT_UPLOADED;
    }

}
