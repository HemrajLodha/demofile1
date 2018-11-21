package com.pws.pateast.di.component;

import com.pws.pateast.di.ActivityScope;
import com.pws.pateast.di.module.ServiceModule;
import com.pws.pateast.service.socket.SocketPresenter;
import com.pws.pateast.service.sync.SyncPresenter;
import com.pws.pateast.service.upload.UploadPresenter;

import javax.inject.Singleton;

import dagger.Component;
import io.socket.client.Socket;

/**
 * Created by intel on 21-Jul-17.
 */
@ActivityScope
@Component(modules = {ServiceModule.class}, dependencies = ApplicationComponent.class)
public interface ServiceComponent {
    void inject(SocketPresenter socketPresenter);

    void inject(SyncPresenter socketPresenter);

    void inject(UploadPresenter socketPresenter);

}
