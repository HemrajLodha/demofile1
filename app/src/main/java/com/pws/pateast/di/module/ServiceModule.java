package com.pws.pateast.di.module;

import android.content.Context;

import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.utils.Preference;

import java.net.URISyntaxException;

import dagger.Module;
import dagger.Provides;
import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by intel on 21-Jul-17.
 */
@Module
public class ServiceModule {
    Context mContext;
    Preference preference;

    public ServiceModule(Context context) {
        mContext = context;
        preference = Preference.get(context);
    }

    @Provides
    public Context getContext() {
        return mContext;
    }

    @Provides
    public Socket getSocket() {
        try {
            return IO.socket(ServiceBuilder.SOCKET_BASE_URL, getOptions());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public IO.Options getOptions() {
        IO.Options options = new IO.Options();
        options.forceNew = true;
        options.query = "token=" + preference.getAccessToken().getAccessToken();
        return options;
    }

}
