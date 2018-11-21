package com.pws.pateast.service.upload;

import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.model.Message;
import com.pws.pateast.api.service.APIService;
import com.pws.pateast.base.ServicePresenter;
import com.pws.pateast.chat.ChatHelper;
import com.pws.pateast.events.SocketEvent;
import com.pws.pateast.events.UploadEvent;
import com.pws.pateast.service.upload.request.UploadRequestCallback;
import com.pws.pateast.service.upload.request.UploadRequestSubscriber;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.pws.pateast.events.SocketEvent.EVENT_FILE_MESSAGE;

/**
 * Created by intel on 09-Aug-17.
 */

public class UploadPresenter extends ServicePresenter<UploadView> implements UploadRequestCallback<Message>
{
    @Inject
    EventBus eventBus;

    @Inject
    ServiceBuilder serviceBuilder;

    APIService apiService;

    UploadRequestSubscriber<Message> uploadRequestSubscriber;



    @Override
    public void attachView(UploadView view) {
        super.attachView(view);
        getServiceComponent().inject(this);
        eventBus.register(this);
        uploadRequestSubscriber = UploadRequestSubscriber.getInstance(this);
    }

    @Override
    public void detachView() {
        super.detachView();
        eventBus.unregister(this);
        uploadRequestSubscriber.performCleanUp();
    }


    @Override
    public Observable<Message> getUploadAbleItem(Message message)  {
        apiService = serviceBuilder.createService(APIService.class);


        RequestBody requestBody = serviceBuilder.prepareStringPart(String.valueOf(message.getChatId()));
        MultipartBody.Part fileBody = serviceBuilder.prepareFilePart("file", new File(message.getUri()));

        return apiService.uploadFile(requestBody, fileBody);
    }

    @Override
    public void onUploadEnqueued(Message uploadItem) {
         uploadRequestSubscriber.emitNextItem(uploadItem);
    }

    @Override
    public void onUploadCompleted(Message uploadItem)
    {
        getView().uploadSuccess(uploadItem);
    }

    @Override
    public void onUploadComplete() {
        getView().uploadComplete();
    }

    @Override
    public void onError(Throwable e) {
        getView().uploadFail(e.getMessage());
    }

    @Subscribe
    public void onEvent(UploadEvent uploadEvent)
    {
        Message uploadItem = uploadEvent.getMessage();
        switch (uploadEvent.getEventType())
        {
            case EVENT_FILE_MESSAGE:
                eventBus.post(new SocketEvent(EVENT_FILE_MESSAGE, ChatHelper.getChat(getContext(), uploadItem, true)));
                break;
        }


    }
}
