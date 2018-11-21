package com.pws.pateast.scheduler;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

/**
 * Created by planet on 5/31/2017.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class DownloadJobSchedulerService extends JobService {

    private static final String TAG = DownloadJobSchedulerService.class.getSimpleName();

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.w(TAG, "onStartJob");
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.w(TAG, "onStopJob");
        return false;
    }
}
