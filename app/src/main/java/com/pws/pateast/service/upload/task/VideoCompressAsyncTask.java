package com.pws.pateast.service.upload.task;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import com.iceteck.silicompressorr.SiliCompressor;
import com.pws.pateast.enums.MessageType;
import com.pws.pateast.utils.FileUtils;

import java.io.File;

import static com.pws.pateast.utils.MediaHelper.TYPE_VIDEO;

/**
 * Created by intel on 09-Aug-17.
 */

public class VideoCompressAsyncTask extends AsyncTask<Uri, Void, String> {
    private Context mContext;
    private UploadView uploadView;

    public VideoCompressAsyncTask(Context context, UploadView uploadView) {
        mContext = context;
        this.uploadView = uploadView;
    }

    @Override
    protected void onPreExecute() {
        uploadView.onCompressStart();
    }

    @Override
    protected String doInBackground(Uri... params) {
        String filePath = null;
        try {
            Uri inputUri = params[0];
            File outputFile = FileUtils.getOutputMediaDirectory(TYPE_VIDEO, false);
            File inputFile = new File(FileUtils.getPath(mContext, inputUri));
            filePath = SiliCompressor.with(mContext).compressVideo(inputFile.getPath(), outputFile.getPath());
            return filePath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String filePath) {
        uploadView.onCompressComplete();
        uploadView.uploadFile(MessageType.VIDEO, filePath);
    }
}
