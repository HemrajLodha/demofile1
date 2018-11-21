package com.pws.pateast.service.socket;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.pws.pateast.MyApplication;
import com.pws.pateast.api.model.Message;
import com.pws.pateast.api.model.TripEvent;
import com.pws.pateast.api.model.TripLocation;
import com.pws.pateast.di.component.ApplicationComponent;
import com.pws.pateast.enums.MessageStatusType;
import com.pws.pateast.events.LocationEvent;
import com.pws.pateast.events.MessageEvent;
import com.pws.pateast.events.SocketEvent;
import com.pws.pateast.listener.SocketListener;
import com.pws.pateast.service.sync.SyncService;

import org.json.JSONObject;

import static com.pws.pateast.base.Extras.CHAT_SYNC_TYPE;
import static com.pws.pateast.events.LocationEvent.EVENT_CANCEL_DROP_ON_BOARD;
import static com.pws.pateast.events.LocationEvent.EVENT_CANCEL_PICKUP_ON_BOARD;
import static com.pws.pateast.events.LocationEvent.EVENT_CONFIRM_PICKUP_ON_BOARD;
import static com.pws.pateast.events.LocationEvent.EVENT_START_DROP;
import static com.pws.pateast.events.LocationEvent.EVENT_START_DROP_ON_BOARD;
import static com.pws.pateast.events.LocationEvent.EVENT_START_PICKUP;
import static com.pws.pateast.events.LocationEvent.EVENT_START_PICKUP_ON_BOARD;
import static com.pws.pateast.events.LocationEvent.EVENT_STOP_DROP;
import static com.pws.pateast.events.LocationEvent.EVENT_STOP_DROP_OFF_BOARD;
import static com.pws.pateast.events.LocationEvent.EVENT_STOP_PICKUP;
import static com.pws.pateast.events.LocationEvent.EVENT_STOP_PICKUP_OFF_BOARD;
import static com.pws.pateast.events.LocationEvent.EVENT_TRIP_POSITION;
import static com.pws.pateast.events.SocketEvent.EVENT_MESSAGE_RECEIVED;
import static com.pws.pateast.events.SocketEvent.EVENT_MESSAGE_SEEN;
import static com.pws.pateast.events.SocketEvent.EVENT_MESSAGE_SENT;
import static com.pws.pateast.events.SocketEvent.EVENT_MY_MESSAGE;
import static com.pws.pateast.events.SocketEvent.EVENT_OFFLINE;
import static com.pws.pateast.events.SocketEvent.EVENT_ONLINE;
import static com.pws.pateast.events.SocketEvent.EVENT_STOP_TYPING;
import static com.pws.pateast.events.SocketEvent.EVENT_TYPING;
import static com.pws.pateast.service.sync.SyncService.CHAT_LIST;
import static io.socket.client.Socket.EVENT_CONNECT;
import static io.socket.client.Socket.EVENT_CONNECT_ERROR;
import static io.socket.client.Socket.EVENT_CONNECT_TIMEOUT;
import static io.socket.client.Socket.EVENT_DISCONNECT;
import static io.socket.client.Socket.EVENT_ERROR;
import static io.socket.client.Socket.EVENT_MESSAGE;

/**
 * Created by intel on 21-Jul-17.
 */

public class SocketService extends Service implements SocketView, SocketListener {

    private SocketPresenter mPresenter;

    @Override
    public void onCreate() {
        super.onCreate();
        mPresenter = new SocketPresenter();
        mPresenter.attachView(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        getPresenter().connect();
        return new SocketBinder();
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        getPresenter().connect();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public MyApplication getApp() {
        return (MyApplication) getApplication();
    }

    @Override
    public ApplicationComponent getApplicationComponent() {
        return getApp().getApplicationComponent();
    }

    @Override
    public void call(String event, Object... args) {
        Log.d("Socket Event", event);
        switch (event) {
            case EVENT_CONNECT:
                getPresenter().onConnect();
                break;
            case EVENT_DISCONNECT:
                getPresenter().onDisconnect();
                break;
            case EVENT_CONNECT_ERROR:
                break;
            case EVENT_CONNECT_TIMEOUT:
                break;
            case EVENT_ERROR:
                Object error = args[0];
                if (error instanceof String && ((String) error).equalsIgnoreCase("INVALID_ACCESS_TOKEN")) {
                    getPresenter().updateAccessToken();
                }
                break;
            case EVENT_MESSAGE:
                if (args != null && args.length >= 1)
                    getPresenter().onEvent(new SocketEvent(EVENT_MESSAGE, new Gson().fromJson(args[0].toString(), Message.class)));
                break;
            case EVENT_MY_MESSAGE:
                if (args != null && args.length >= 1)
                    getPresenter().onEvent(new SocketEvent(EVENT_MESSAGE, new Gson().fromJson(args[0].toString(), Message.class)));
                break;
            case EVENT_MESSAGE_SENT:
                if (args != null && args.length >= 1)
                    getPresenter().onEvent(new SocketEvent(EVENT_MESSAGE_SENT, Message.getMessageObject((JSONObject) args[0])));
                break;
            case EVENT_MESSAGE_RECEIVED:
                if (args != null && args.length >= 1)
                    getPresenter().onEvent(new SocketEvent(EVENT_MESSAGE_RECEIVED, Message.updateMessageStatus(Integer.parseInt(String.valueOf(args[0])), MessageStatusType.STATUS_RECEIVED.getValue())));
                break;
            case EVENT_MESSAGE_SEEN:
                if (args != null && args.length >= 1)
                    getPresenter().onEvent(new SocketEvent(EVENT_MESSAGE_SEEN, Message.updateMessageStatus(Integer.parseInt(String.valueOf(args[0])), MessageStatusType.STATUS_SEEN.getValue())));
                break;
            case EVENT_TYPING:
                if (args != null && args.length >= 1)
                    getPresenter().sendMessageEvent(new MessageEvent(EVENT_TYPING, Integer.parseInt(String.valueOf(args[0]))));
                break;
            case EVENT_STOP_TYPING:
                if (args != null && args.length >= 1)
                    getPresenter().sendMessageEvent(new MessageEvent(EVENT_STOP_TYPING, Integer.parseInt(String.valueOf(args[0]))));
                break;
            case EVENT_ONLINE:
                if (args != null && args.length >= 1)
                    getPresenter().sendMessageEvent(new MessageEvent(EVENT_ONLINE, Integer.parseInt(String.valueOf(args[0]))));
                break;
            case EVENT_OFFLINE:
                if (args != null && args.length >= 1)
                    getPresenter().sendMessageEvent(new MessageEvent(EVENT_OFFLINE, Integer.parseInt(String.valueOf(args[0]))));
                break;
            case EVENT_TRIP_POSITION:
                if (args != null && args.length >= 1) {
                    getPresenter().onEvent(new LocationEvent(EVENT_TRIP_POSITION, new Gson().fromJson(args[0].toString(), TripLocation.class)));
                }
                break;
            case EVENT_START_PICKUP:
            case EVENT_STOP_PICKUP:
            case EVENT_START_DROP:
            case EVENT_STOP_DROP:
                if (args != null && args.length >= 1) {
                    getPresenter().onEvent(new LocationEvent(event, new TripEvent.Builder()
                            .setTripId(Integer.parseInt(String.valueOf(args[0])))
                            .build()));
                }
                break;
            case EVENT_START_PICKUP_ON_BOARD:
                if (args != null && args.length >= 1) {
                    getPresenter().onEvent(new LocationEvent(event, new TripEvent.Builder()
                            .setTripRecordId(Integer.parseInt(String.valueOf(args[0])))
                            .setTripEvent(event)
                            .build()));
                }
                break;
            case EVENT_CONFIRM_PICKUP_ON_BOARD:
            case EVENT_CANCEL_PICKUP_ON_BOARD:
            case EVENT_STOP_PICKUP_OFF_BOARD:
            case EVENT_START_DROP_ON_BOARD:
            case EVENT_CANCEL_DROP_ON_BOARD:
            case EVENT_STOP_DROP_OFF_BOARD:
                if (args != null && args.length >= 1) {
                    getPresenter().onEvent(new LocationEvent(event, new TripEvent.Builder()
                            .setTripRecordId(Integer.parseInt(String.valueOf(args[0])))
                            .setTripEvent(event)
                            .build()));
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        getPresenter().detachView();
        super.onDestroy();
    }

    private SocketPresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public void getInbox() {
        Bundle bundle = new Bundle();
        bundle.putInt(CHAT_SYNC_TYPE, CHAT_LIST);
        SyncService.startSyncService(getContext(), bundle);
    }

    public class SocketBinder extends Binder {
        public SocketService getService() {
            return SocketService.this;
        }
    }
}
