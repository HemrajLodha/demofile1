package com.pws.pateast.service.sync;

import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.model.ChatUser;
import com.pws.pateast.api.model.Message;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.model.UserInfo;
import com.pws.pateast.api.model.Ward;
import com.pws.pateast.api.observer.ServiceSingleObserver;
import com.pws.pateast.api.service.APIService;
import com.pws.pateast.base.ServicePresenter;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.events.SocketEvent;
import com.pws.pateast.provider.table.UserChats;
import com.pws.pateast.utils.Preference;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.inject.Inject;

/**
 * Created by intel on 26-Jul-17.
 */

public class SyncPresenter extends ServicePresenter<SyncView> {
    @Inject
    protected EventBus eventBus;

    @Inject
    ServiceBuilder serviceBuilder;

    @Inject
    protected Preference preference;

    private APIService apiService;
    protected User user;
    protected Ward ward;

    @Override
    public void attachView(SyncView view) {
        super.attachView(view);
        getServiceComponent().inject(this);
        user = preference.getUser();
        ward = preference.getWard();
    }

    public void getChatUsers(String senderIds) {
        apiService = serviceBuilder.createService(APIService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("ids", senderIds);


        disposable = apiService.getChatUsers(params)
                .subscribeWith(new ServiceSingleObserver<ChatUser>() {
                    @Override
                    public void onResponse(ChatUser response) {
                        if (response.getData() != null && !response.getData().isEmpty())
                            getView().updateUser(response.getData());
                    }

                    @Override
                    public SyncPresenter getPresenter() {
                        return SyncPresenter.this;
                    }
                });
    }

    public void getInbox() {
        apiService = serviceBuilder.createService(APIService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("id", String.valueOf(user.getData().getId()));
        switch (UserType.getUserType(user.getData().getUser_type())) {
            case PARENT:
                params.put("masterId", String.valueOf(ward.getMasterId()));
                break;
            default:
                params.put("masterId", String.valueOf(user.getData().getMasterId()));
                break;
        }


        disposable = apiService.getInbox(params)
                .subscribeWith(new ServiceSingleObserver<Message>() {
                    @Override
                    public void onResponse(Message response) {
                        ArrayList<UserInfo> userInfos = new ArrayList<>();
                        for (Message message : response.getConversations()) {
                            if (user.getData().getId() == message.getSenderId())
                                userInfos.add(message.getReceiver());
                            else if (user.getData().getId() == message.getReceiverId())
                                userInfos.add(message.getSender());
                        }
                        getView().updateUser(userInfos);
                    }

                    @Override
                    public SyncPresenter getPresenter() {
                        return SyncPresenter.this;
                    }
                });
    }

    public void getMessagesFromServer() {
        apiService = serviceBuilder.createService(APIService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("userId", String.valueOf(user.getData().getId()));
        params.put("createdAt", String.valueOf(preference.getTimeStamp()));

        disposable = apiService.getMessages(params)
                .subscribeWith(new ServiceSingleObserver<Message>() {

                    @Override
                    public void onResponse(Message response) {
                        if (response.getMessages() != null && !response.getMessages().isEmpty()) {
                            Collections.reverse(response.getMessages());
                            eventBus.post(new SocketEvent(SocketEvent.EVENT_MY_MESSAGES, response.getMessages()));
                            getContext().getContentResolver().notifyChange(UserChats.CONTENT_URI, null);
                            preference.setTimeStamp(System.currentTimeMillis());
                        }
                    }

                    @Override
                    public SyncPresenter getPresenter() {
                        return SyncPresenter.this;
                    }
                });
    }
}
