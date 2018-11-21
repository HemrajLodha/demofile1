package com.pws.pateast.di.module;

import android.content.Context;

import com.pws.pateast.base.AppActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {
    Context mContext;

    public ActivityModule(Context context) {
        mContext = context;
    }

    @Provides
    public Context activityContext() {
        return mContext;
    }

}