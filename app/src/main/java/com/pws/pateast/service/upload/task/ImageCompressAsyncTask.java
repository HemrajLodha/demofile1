package com.pws.pateast.service.upload.task;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import com.iceteck.silicompressorr.SiliCompressor;
import com.pws.pateast.enums.MessageType;
import com.pws.pateast.utils.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static com.pws.pateast.utils.MediaHelper.TYPE_IMAGE;

/**
 * Created by intel on 10-Aug-17.
 */

public class ImageCompressAsyncTask extends AsyncTask<Uri, Void, String> {
    private Context mContext;
    private UploadView uploadView;

    public ImageCompressAsyncTask(Context context, UploadView uploadView) {
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
            InputStream inputUri = mContext.getContentResolver().openInputStream(params[0]);
            File outputFile = FileUtils.getOutputMediaDirectory(TYPE_IMAGE, false);
            filePath = SiliCompressor.with(mContext).compress(inputUri, outputFile);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return filePath;
    }

    @Override
    protected void onPostExecute(String filePath) {
        uploadView.onCompressComplete();
        uploadView.uploadFile(MessageType.IMAGE, filePath);
    }

}
