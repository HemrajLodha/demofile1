package com.pws.pateast;

/**
 * Created by anshul on 7/2/17.
 */

public interface Constants {
    String CURRENCY_TYPE = "INR";

    String EXTRA_PUBLIC_KEY = "1234-6666-6789-56";

    String MERCHANT_IDENTIFIER = "T143668";

    String TXN = "TXN";

    String OTP_DELIMITER = ":";

    String DOWNLOAD_BROADCAST_ACTION = "broadcast_download_complete";

    String APPLICATION_DIR_NAME = "Patest";

    String ASSIGNMENT_FILES_DIR = "/Assignment/";

    String INVOICE_FILES_DIR = "/Invoice/";

    String CHALLAN_FILES_DIR = "/Challan/";

    String COMPLAINT_FILES_DIR = "/Complaints/";

    String IMAGES_DIR = "/Images/";

    String VIDEOS_DIR = "/Videos/";

    String TEMP_DIR = "/Temp/";

    String ALLOWED_MIME_TYPE_FILE_NAME = "allowed_mime_types.csv";

    String FILE_AUTHORITY = "com.pws.pateast.fileprovider";

    String ALLOWED_FILE_MIME_TYPE[] = {"audio/x-wav",
            "audio/mpeg",
            "audio/webm",
            "video/3gpp",
            "video/mp4",
            "application/pdf",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.template",
            "application/vnd.ms-word.document.macroEnabled.12",
            "application/vnd.ms-word.template.macroEnabled.12",
            "application/msword",
            "text/plain",
            "application/ogg",
            "image/png",
            "image/jpeg",
            "image/pjpeg",
            "image/gif",
            "application/excel",
            "application/vnd.ms-excel",
            "application/x-excel",
            "application/x-msexcel",
            "application/excel",
            "video/x-ms-wmv",
            "text/csv",
            "application/csv",
            "text/comma-separated-values"};

    String GOOGLE_DRIVE = "http://drive.google.com/viewerng/viewer?embedded=true&url=";
}
