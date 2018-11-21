package com.pws.pateast.di.module;

import com.google.maps.GeoApiContext;
import com.pws.pateast.MyApplication;
import com.pws.pateast.R;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.utils.Preference;
import com.pws.pateast.utils.ShortcutUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApplicationModule {

    private final MyApplication mBaseApplication;

    public ApplicationModule(MyApplication baseApplication) {
        this.mBaseApplication = baseApplication;
    }

    @Provides
    @Singleton
    public MyApplication provideApplication() {
        return mBaseApplication;
    }

    @Provides
    @Singleton
    public EventBus eventBus() {
        return new EventBus();
    }

    @Provides
    @Singleton
    public ServiceBuilder getServiceBuilder() {
        return new ServiceBuilder.Builder(mBaseApplication)
                .create();
    }

    @Provides
    @Singleton
    public OkHttpClient.Builder getHTTPBuilder() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(60 * 5, TimeUnit.SECONDS); // 60*5 = 5 minutes
        builder.connectTimeout(60 * 5, TimeUnit.SECONDS);
        builder.writeTimeout(60 * 5, TimeUnit.SECONDS);
        return builder;
    }

    @Provides
    @Singleton
    public Retrofit.Builder getRetrofitBuilder() {
        return new Retrofit.Builder()
                .baseUrl(ServiceBuilder.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
    }

    @Provides
    @Singleton
    public Preference providePreference() {
        return Preference.get(mBaseApplication.getApplicationContext());
    }

    @Provides
    @Singleton
    public ShortcutUtils getShortcutUtilInstance() {
        return ShortcutUtils.getInstance(mBaseApplication.getApplicationContext());
    }

    @Provides
    @Singleton
    public GeoApiContext provideGeoContext() {
        GeoApiContext geoApiContext = new GeoApiContext();
        geoApiContext = geoApiContext
                .setQueryRateLimit(3)
                .setApiKey(mBaseApplication.getString(R.string.map_api_key))
                .setConnectTimeout(1, TimeUnit.SECONDS)
                .setReadTimeout(1, TimeUnit.SECONDS)
                .setWriteTimeout(1, TimeUnit.SECONDS);
        return geoApiContext;
    }

}