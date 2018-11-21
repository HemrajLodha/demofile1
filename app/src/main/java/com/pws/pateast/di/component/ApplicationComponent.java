package com.pws.pateast.di.component;


import com.google.maps.GeoApiContext;
import com.pws.pateast.MyApplication;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.interceptor.AuthorisedInterceptor;
import com.pws.pateast.api.interceptor.LoginInterceptor;
import com.pws.pateast.broadcast.SocketBroadcastReceiver;
import com.pws.pateast.di.module.ApplicationModule;
import com.pws.pateast.firebase.MyFirebaseInstanceIDService;
import com.pws.pateast.fragment.assignment.AssignmentDownloadService;
import com.pws.pateast.utils.Preference;
import com.pws.pateast.utils.ShortcutUtils;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    void inject(MyApplication baseApplication);

    void inject(ServiceBuilder serviceBuilder);

    void inject(AuthorisedInterceptor authorisedInterceptor);

    void inject(LoginInterceptor authorisedInterceptor);

    void inject(AssignmentDownloadService service);

    void inject(MyFirebaseInstanceIDService service);

    void inject(SocketBroadcastReceiver service);

    EventBus eventBus();

    OkHttpClient.Builder getHTTPBuilder();

    Retrofit.Builder getRetrofitBuilder();

    ServiceBuilder getServiceBuilder();

    Preference providePreference();

    ShortcutUtils getShortcutUtilInstance();

    GeoApiContext provideGeoContext();
}