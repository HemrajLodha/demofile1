package com.pws.pateast.service.socket;

import android.content.ContentProviderResult;
import android.content.Intent;

import com.google.gson.Gson;
import com.pws.pateast.R;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.model.Message;
import com.pws.pateast.api.model.Response;
import com.pws.pateast.api.model.TripEvent;
import com.pws.pateast.api.model.TripLocation;
import com.pws.pateast.api.model.TripNextStop;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.observer.ServiceSingleObserver;
import com.pws.pateast.api.service.APIService;
import com.pws.pateast.base.ServicePresenter;
import com.pws.pateast.chat.ChatHelper;
import com.pws.pateast.chat.UserChatHelper;
import com.pws.pateast.enums.MessageStatusType;
import com.pws.pateast.enums.NotificationType;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.events.LocationEvent;
import com.pws.pateast.events.MessageEvent;
import com.pws.pateast.events.SocketEvent;
import com.pws.pateast.listener.SocketAdapter;
import com.pws.pateast.provider.table.UserChats;
import com.pws.pateast.utils.NetworkUtil;
import com.pws.pateast.utils.NotificationHelper;
import com.pws.pateast.utils.Preference;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.socket.client.Ack;
import io.socket.client.Socket;

import static com.pws.pateast.events.LocationEvent.EVENT_CANCEL_DROP_ON_BOARD;
import static com.pws.pateast.events.LocationEvent.EVENT_CANCEL_PICKUP_ON_BOARD;
import static com.pws.pateast.events.LocationEvent.EVENT_CONFIRM_PICKUP_ON_BOARD;
import static com.pws.pateast.events.LocationEvent.EVENT_JOIN_DRIVER;
import static com.pws.pateast.events.LocationEvent.EVENT_JOIN_PARENT;
import static com.pws.pateast.events.LocationEvent.EVENT_JOIN_ROUTE_MAP;
import static com.pws.pateast.events.LocationEvent.EVENT_JOIN_TRIP;
import static com.pws.pateast.events.LocationEvent.EVENT_JOIN_TRIP_ADMIN;
import static com.pws.pateast.events.LocationEvent.EVENT_JOIN_TRIP_RECORD;
import static com.pws.pateast.events.LocationEvent.EVENT_LEAVE_DRIVER;
import static com.pws.pateast.events.LocationEvent.EVENT_LEAVE_PARENT;
import static com.pws.pateast.events.LocationEvent.EVENT_LEAVE_ROUTE_MAP;
import static com.pws.pateast.events.LocationEvent.EVENT_LEAVE_TRIP;
import static com.pws.pateast.events.LocationEvent.EVENT_LEAVE_TRIP_ADMIN;
import static com.pws.pateast.events.LocationEvent.EVENT_LEAVE_TRIP_RECORD;
import static com.pws.pateast.events.LocationEvent.EVENT_NEXT_STOP_ARRIVING;
import static com.pws.pateast.events.LocationEvent.EVENT_START_DROP;
import static com.pws.pateast.events.LocationEvent.EVENT_START_DROP_ON_BOARD;
import static com.pws.pateast.events.LocationEvent.EVENT_START_PICKUP;
import static com.pws.pateast.events.LocationEvent.EVENT_START_PICKUP_ON_BOARD;
import static com.pws.pateast.events.LocationEvent.EVENT_STOP_DROP;
import static com.pws.pateast.events.LocationEvent.EVENT_STOP_DROP_OFF_BOARD;
import static com.pws.pateast.events.LocationEvent.EVENT_STOP_PICKUP;
import static com.pws.pateast.events.LocationEvent.EVENT_STOP_PICKUP_OFF_BOARD;
import static com.pws.pateast.events.LocationEvent.EVENT_TRACK_POSITION;
import static com.pws.pateast.events.LocationEvent.EVENT_TRIP_POSITION;
import static com.pws.pateast.events.LocationEvent.EVENT_TRIP_RECORD_STATUS_CHANGED;
import static com.pws.pateast.events.LocationEvent.EVENT_TRIP_STATUS_CHANGED;
import static com.pws.pateast.events.SocketEvent.EVENT_BLOCK_USER;
import static com.pws.pateast.events.SocketEvent.EVENT_FILE_MESSAGE;
import static com.pws.pateast.events.SocketEvent.EVENT_IS_BLOCKED;
import static com.pws.pateast.events.SocketEvent.EVENT_IS_CONNECTED;
import static com.pws.pateast.events.SocketEvent.EVENT_IS_ONLINE;
import static com.pws.pateast.events.SocketEvent.EVENT_MESSAGE_READ;
import static com.pws.pateast.events.SocketEvent.EVENT_MESSAGE_RECEIVED;
import static com.pws.pateast.events.SocketEvent.EVENT_MESSAGE_SEEN;
import static com.pws.pateast.events.SocketEvent.EVENT_MESSAGE_SENT;
import static com.pws.pateast.events.SocketEvent.EVENT_MY_MESSAGE;
import static com.pws.pateast.events.SocketEvent.EVENT_MY_MESSAGES;
import static com.pws.pateast.events.SocketEvent.EVENT_OFFLINE;
import static com.pws.pateast.events.SocketEvent.EVENT_ONLINE;
import static com.pws.pateast.events.SocketEvent.EVENT_ON_CONNECT;
import static com.pws.pateast.events.SocketEvent.EVENT_ON_DISCONNECT;
import static com.pws.pateast.events.SocketEvent.EVENT_SEND_MESSAGE;
import static com.pws.pateast.events.SocketEvent.EVENT_STOP_TYPING;
import static com.pws.pateast.events.SocketEvent.EVENT_TYPING;
import static com.pws.pateast.events.SocketEvent.EVENT_UNBLOCK_USER;
import static io.socket.client.Socket.EVENT_CONNECT;
import static io.socket.client.Socket.EVENT_CONNECT_ERROR;
import static io.socket.client.Socket.EVENT_CONNECT_TIMEOUT;
import static io.socket.client.Socket.EVENT_DISCONNECT;
import static io.socket.client.Socket.EVENT_ERROR;
import static io.socket.client.Socket.EVENT_MESSAGE;

/**
 * Created by intel on 21-Jul-17.
 */

public class SocketPresenter extends ServicePresenter<SocketView> {

    @Inject
    protected Socket mSocket;

    @Inject
    protected EventBus eventBus;

    @Inject
    protected Preference preference;

    @Inject
    ServiceBuilder serviceBuilder;

    private APIService apiService;

    protected User user;
    protected UserType userType;

    @Override
    public void attachView(SocketView view) {
        super.attachView(view);
        getServiceComponent().inject(this);
        user = preference.getUser();
        userType = UserType.getUserType(user.getData().getUser_type());
        if (!eventBus.isRegistered(this))
            eventBus.register(this);
    }

    public synchronized void connect() {
        if (NetworkUtil.isOnline(getContext()))
            connectSocket();
        else
            disconnectSocket();
    }

    public void sendBroadcast(String action) {
        if (preference.getUser() == null) {
            return;
        }
        Intent broadcastIntent = new Intent(action);
        getContext().sendBroadcast(broadcastIntent);
    }

    public boolean isConnected() {
        return mSocket != null && mSocket.connected();
    }

    private synchronized void connectSocket() {
        if (!isConnected()) {
            mSocket.on(EVENT_CONNECT, SocketAdapter.get(EVENT_CONNECT, getView()));
            mSocket.on(EVENT_DISCONNECT, SocketAdapter.get(EVENT_DISCONNECT, getView()));
            mSocket.on(EVENT_CONNECT_ERROR, SocketAdapter.get(EVENT_CONNECT_ERROR, getView()));
            mSocket.on(EVENT_CONNECT_TIMEOUT, SocketAdapter.get(EVENT_CONNECT_TIMEOUT, getView()));
            mSocket.on(EVENT_ERROR, SocketAdapter.get(EVENT_ERROR, getView()));
            mSocket.connect();
        }
    }

    private synchronized void disconnectSocket() {
        mSocket.disconnect();
        mSocket.off(EVENT_CONNECT, SocketAdapter.get(EVENT_CONNECT, getView()));
        mSocket.off(EVENT_DISCONNECT, SocketAdapter.get(EVENT_DISCONNECT, getView()));
        mSocket.off(EVENT_CONNECT_ERROR, SocketAdapter.get(EVENT_CONNECT_ERROR, getView()));
        mSocket.off(EVENT_CONNECT_TIMEOUT, SocketAdapter.get(EVENT_CONNECT_TIMEOUT, getView()));
        mSocket.off(EVENT_ERROR, SocketAdapter.get(EVENT_ERROR, getView()));
    }

    public synchronized void onConnect() {
        if (mSocket != null) {
            eventBus.post(new SocketEvent(EVENT_ON_CONNECT));
            switch (userType) {
                case DRIVER:
                    mSocket.on(EVENT_START_PICKUP_ON_BOARD, SocketAdapter.get(EVENT_START_PICKUP_ON_BOARD, getView()));
                    break;
                case PARENT:
                    mSocket.on(EVENT_TRIP_POSITION, SocketAdapter.get(EVENT_TRIP_POSITION, getView()));
                    mSocket.on(EVENT_START_PICKUP, SocketAdapter.get(EVENT_START_PICKUP, getView()));
                    mSocket.on(EVENT_STOP_PICKUP, SocketAdapter.get(EVENT_STOP_PICKUP, getView()));
                    mSocket.on(EVENT_START_DROP, SocketAdapter.get(EVENT_START_DROP, getView()));
                    mSocket.on(EVENT_STOP_DROP, SocketAdapter.get(EVENT_STOP_DROP, getView()));

                    mSocket.on(EVENT_CONFIRM_PICKUP_ON_BOARD, SocketAdapter.get(EVENT_CONFIRM_PICKUP_ON_BOARD, getView()));
                    mSocket.on(EVENT_CANCEL_PICKUP_ON_BOARD, SocketAdapter.get(EVENT_CANCEL_PICKUP_ON_BOARD, getView()));
                    mSocket.on(EVENT_STOP_PICKUP_OFF_BOARD, SocketAdapter.get(EVENT_STOP_PICKUP_OFF_BOARD, getView()));
                    mSocket.on(EVENT_START_DROP_ON_BOARD, SocketAdapter.get(EVENT_START_DROP_ON_BOARD, getView()));
                    mSocket.on(EVENT_CANCEL_DROP_ON_BOARD, SocketAdapter.get(EVENT_CANCEL_DROP_ON_BOARD, getView()));
                    mSocket.on(EVENT_STOP_DROP_OFF_BOARD, SocketAdapter.get(EVENT_STOP_DROP_OFF_BOARD, getView()));
                default:
                    mSocket.on(EVENT_MESSAGE, SocketAdapter.get(EVENT_MESSAGE, getView()));
                    mSocket.on(EVENT_MESSAGE_SENT, SocketAdapter.get(EVENT_MESSAGE_SENT, getView()));
                    mSocket.on(EVENT_MESSAGE_RECEIVED, SocketAdapter.get(EVENT_MESSAGE_RECEIVED, getView()));
                    mSocket.on(EVENT_MESSAGE_SEEN, SocketAdapter.get(EVENT_MESSAGE_SEEN, getView()));
                    mSocket.on(EVENT_MY_MESSAGE, SocketAdapter.get(EVENT_MY_MESSAGE, getView()));
                    mSocket.on(EVENT_TYPING, SocketAdapter.get(EVENT_TYPING, getView()));
                    mSocket.on(EVENT_STOP_TYPING, SocketAdapter.get(EVENT_STOP_TYPING, getView()));
                    mSocket.on(EVENT_ONLINE, SocketAdapter.get(EVENT_ONLINE, getView()));
                    mSocket.on(EVENT_OFFLINE, SocketAdapter.get(EVENT_OFFLINE, getView()));
                    getView().getInbox();
                    break;
            }
        }

    }

    public synchronized void onDisconnect() {
        if (mSocket != null) {
            eventBus.post(new SocketEvent(EVENT_ON_DISCONNECT));
            switch (userType) {
                case DRIVER:
                    mSocket.off(EVENT_START_PICKUP_ON_BOARD, SocketAdapter.get(EVENT_START_PICKUP_ON_BOARD, getView()));
                    break;
                case PARENT:
                    mSocket.off(EVENT_TRIP_POSITION, SocketAdapter.get(EVENT_TRIP_POSITION, getView()));
                    mSocket.off(EVENT_START_PICKUP, SocketAdapter.get(EVENT_START_PICKUP, getView()));
                    mSocket.off(EVENT_STOP_PICKUP, SocketAdapter.get(EVENT_STOP_PICKUP, getView()));
                    mSocket.off(EVENT_START_DROP, SocketAdapter.get(EVENT_START_DROP, getView()));
                    mSocket.off(EVENT_STOP_DROP, SocketAdapter.get(EVENT_STOP_DROP, getView()));

                    mSocket.off(EVENT_CONFIRM_PICKUP_ON_BOARD, SocketAdapter.get(EVENT_CONFIRM_PICKUP_ON_BOARD, getView()));
                    mSocket.off(EVENT_CANCEL_PICKUP_ON_BOARD, SocketAdapter.get(EVENT_CANCEL_PICKUP_ON_BOARD, getView()));
                    mSocket.off(EVENT_STOP_PICKUP_OFF_BOARD, SocketAdapter.get(EVENT_STOP_PICKUP_OFF_BOARD, getView()));
                    mSocket.off(EVENT_START_DROP_ON_BOARD, SocketAdapter.get(EVENT_START_DROP_ON_BOARD, getView()));
                    mSocket.off(EVENT_CANCEL_DROP_ON_BOARD, SocketAdapter.get(EVENT_CANCEL_DROP_ON_BOARD, getView()));
                    mSocket.off(EVENT_STOP_DROP_OFF_BOARD, SocketAdapter.get(EVENT_STOP_DROP_OFF_BOARD, getView()));
                default:
                    mSocket.off(EVENT_MESSAGE, SocketAdapter.get(EVENT_MESSAGE, getView()));
                    mSocket.off(EVENT_MESSAGE_SENT, SocketAdapter.get(EVENT_MESSAGE_SENT, getView()));
                    mSocket.off(EVENT_MESSAGE_RECEIVED, SocketAdapter.get(EVENT_MESSAGE_RECEIVED, getView()));
                    mSocket.off(EVENT_MESSAGE_SEEN, SocketAdapter.get(EVENT_MESSAGE_SEEN, getView()));
                    mSocket.off(EVENT_MY_MESSAGE, SocketAdapter.get(EVENT_MY_MESSAGE, getView()));
                    mSocket.off(EVENT_TYPING, SocketAdapter.get(EVENT_TYPING, getView()));
                    mSocket.off(EVENT_STOP_TYPING, SocketAdapter.get(EVENT_STOP_TYPING, getView()));
                    mSocket.off(EVENT_ONLINE, SocketAdapter.get(EVENT_ONLINE, getView()));
                    mSocket.off(EVENT_OFFLINE, SocketAdapter.get(EVENT_OFFLINE, getView()));
                    break;
            }
        }
    }

    public void sendMessageEvent(MessageEvent event) {
        eventBus.post(event);
    }

    @Subscribe
    public void onEvent(final SocketEvent event) {
        Message message = event.getMessage();
        ContentProviderResult[] results;
        switch (event.getEventType()) {
            case SocketEvent.EVENT_CONNECT:
                connect();
                break;
            case EVENT_IS_CONNECTED:
                if (isConnected())
                    eventBus.post(new SocketEvent(EVENT_ON_CONNECT));
                else
                    eventBus.post(new SocketEvent(EVENT_ON_DISCONNECT));
                break;
            case EVENT_SEND_MESSAGE:
                if (isConnected()) {
                    results = ChatHelper.insertChat(getContext(), message, null);
                    UserChatHelper.insertUsersChat(getContext(), user.getData().getId(), results);
                    if (results != null && results.length >= 1) {
                        if (results[0].uri != null) {
                            List<String> list = results[0].uri.getPathSegments();
                            message.setChatId(Integer.parseInt(list.get(1)));
                            mSocket.emit(EVENT_SEND_MESSAGE, message.getMessageJson());
                        }
                    }
                }
                break;
            case EVENT_FILE_MESSAGE:
                if (isConnected() && message != null) {
                    mSocket.emit(EVENT_SEND_MESSAGE, message.getMessageJson());
                }
                break;
            case EVENT_MESSAGE_SENT:
                ChatHelper.updateChat(getContext(), message, true);
                break;
            case EVENT_MESSAGE:
                message.setMsg_status(MessageStatusType.STATUS_RECEIVED.getValue());
                results = ChatHelper.insertChat(getContext(), message, mSocket);
                UserChatHelper.insertUsersChat(getContext(), user.getData().getId(), results);
                if (mSocket != null)
                    mSocket.emit(EVENT_MESSAGE_RECEIVED, message.getId());
                if (!eventBus.hasSubscriberForEvent(MessageEvent.class)) {
                    NotificationHelper.showNotificationWithIntent(getContext(), getString(R.string.app_name), message.getData(), NotificationType.MESSAGE.getValue());
                } else {
                    eventBus.post(new MessageEvent(EVENT_MESSAGE, message));
                }
                break;
            case EVENT_MY_MESSAGES:
                results = ChatHelper.insertChats(getContext(), event.getMessages(), mSocket);
                UserChatHelper.insertUsersChat(getContext(), user.getData().getId(), results);
                break;
            case EVENT_MESSAGE_RECEIVED:
                ChatHelper.updateChat(getContext(), message, false);
                break;
            case EVENT_MESSAGE_SEEN:
                ChatHelper.updateChat(getContext(), message, false);
                break;
            case EVENT_MESSAGE_READ:
                mSocket.emit(EVENT_MESSAGE_SEEN, message.getId());
                ChatHelper.updateChat(getContext(), message, false);
                getContext().getContentResolver().notifyChange(UserChats.CONTENT_URI, null);
                break;
            case EVENT_TYPING:
                mSocket.emit(EVENT_TYPING, event.getReceiverId());
                break;
            case EVENT_STOP_TYPING:
                mSocket.emit(EVENT_STOP_TYPING, event.getReceiverId());
                break;
            case EVENT_IS_ONLINE:
                mSocket.emit(EVENT_IS_ONLINE, event.getReceiverIds(), new Ack() {
                    @Override
                    public void call(Object... args) {
                        if (args != null && args.length >= 1)
                            sendMessageEvent(new MessageEvent(EVENT_IS_ONLINE, (JSONArray) args[0]));
                    }
                });
                break;
            case EVENT_BLOCK_USER:
            case EVENT_UNBLOCK_USER:
            case EVENT_IS_BLOCKED:
                mSocket.emit(event.getEventType(), event.getReceiverId(), new Ack() {
                    @Override
                    public void call(Object... args) {
                        if (args != null && args.length >= 1)
                            sendMessageEvent(new MessageEvent(event.getEventType(), new Gson().fromJson(args[0].toString(), Message.class)));
                    }
                });
                break;
        }
    }

    @Subscribe
    public void onEvent(final LocationEvent event) {
        TripLocation location = event.getLocation();
        final TripEvent tripEvent = event.getTripEvent();
        switch (event.getEventType()) {
            case EVENT_TRIP_POSITION:
                if (location != null && location.getUser_type() == UserType.DRIVER) {
                    try {
                        mSocket.emit(EVENT_TRIP_POSITION, location.getLocation());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (location != null) {
                    eventBus.post(new LocationEvent(EVENT_TRACK_POSITION, location));
                }
                break;
            case EVENT_JOIN_PARENT:
                if (tripEvent != null) {
                    for (int routeId : tripEvent.getRouteIds()) {
                        mSocket.emit(EVENT_JOIN_ROUTE_MAP, routeId);
                    }
                    for (int tripId : tripEvent.getTripIds()) {
                        mSocket.emit(EVENT_JOIN_TRIP, tripId);
                    }
                    for (int tripRecordId : tripEvent.getTripRecordIds()) {
                        mSocket.emit(EVENT_JOIN_TRIP_RECORD, tripRecordId);
                    }
                }
                break;
            case EVENT_LEAVE_PARENT:
                if (tripEvent != null) {
                    for (int routeId : tripEvent.getRouteIds()) {
                        mSocket.emit(EVENT_LEAVE_ROUTE_MAP, routeId);
                    }
                    for (int tripId : tripEvent.getTripIds()) {
                        mSocket.emit(EVENT_LEAVE_TRIP, tripId);
                    }
                    for (int tripRecordId : tripEvent.getTripRecordIds()) {
                        mSocket.emit(EVENT_LEAVE_TRIP_RECORD, tripRecordId);
                    }
                }
                break;
            case EVENT_JOIN_DRIVER:
                if (tripEvent != null) {
                    for (int routeId : tripEvent.getRouteIds()) {
                        mSocket.emit(EVENT_JOIN_ROUTE_MAP, routeId);
                    }
                    for (int tripId : tripEvent.getTripIds()) {
                        mSocket.emit(EVENT_JOIN_TRIP, tripId);
                        mSocket.emit(EVENT_JOIN_TRIP_ADMIN, tripId);
                    }
                }
                break;
            case EVENT_LEAVE_DRIVER:
                if (tripEvent != null) {
                    for (int routeId : tripEvent.getRouteIds()) {
                        mSocket.emit(EVENT_LEAVE_ROUTE_MAP, routeId);
                    }
                    for (int tripId : tripEvent.getTripIds()) {
                        mSocket.emit(EVENT_LEAVE_TRIP, tripId);
                        mSocket.emit(EVENT_LEAVE_TRIP_ADMIN, tripId);
                    }
                }
                break;
            case EVENT_START_PICKUP:
            case EVENT_STOP_PICKUP:
            case EVENT_START_DROP:
            case EVENT_STOP_DROP:
                if (tripEvent != null) {
                    for (final int tripId : tripEvent.getTripIds()) {
                        if (tripEvent.getUser_type() == UserType.DRIVER)
                            mSocket.emit(event.getEventType(), tripId, new Ack() {
                                @Override
                                public void call(Object... args) {
                                    if (args != null && args.length >= 1) {
                                        try {
                                            tripEvent.setTripId(tripId);
                                            tripEvent.setStatus(((JSONObject) args[0]).getBoolean("status"));
                                            eventBus.post(new LocationEvent(EVENT_TRIP_STATUS_CHANGED, tripEvent));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });
                        else
                            eventBus.post(new LocationEvent(EVENT_TRIP_STATUS_CHANGED, tripEvent));
                    }
                }
                break;
            case EVENT_NEXT_STOP_ARRIVING:
                final TripNextStop nextStop = event.getNextStopArrivalEvent();
                mSocket.emit(EVENT_NEXT_STOP_ARRIVING, nextStop.getTripId(), nextStop.getRouteId());
                break;
            case EVENT_START_PICKUP_ON_BOARD:
                if (tripEvent != null) {
                    for (final int tripRecordId : tripEvent.getTripRecordIds()) {
                        if (tripEvent.getUser_type() == UserType.PARENT)
                            mSocket.emit(event.getEventType(), tripRecordId, new Ack() {
                                @Override
                                public void call(Object... args) {
                                    if (args != null && args.length >= 1) {
                                        try {
                                            tripEvent.setTripRecordId(tripRecordId);
                                            tripEvent.setStatus(((JSONObject) args[0]).getBoolean("status"));
                                            eventBus.post(new LocationEvent(EVENT_TRIP_RECORD_STATUS_CHANGED, tripEvent));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });
                        else
                            eventBus.post(new LocationEvent(EVENT_TRIP_RECORD_STATUS_CHANGED, tripEvent));
                    }
                }
                break;
            case EVENT_CONFIRM_PICKUP_ON_BOARD:
            case EVENT_CANCEL_PICKUP_ON_BOARD:
            case EVENT_STOP_PICKUP_OFF_BOARD:
            case EVENT_START_DROP_ON_BOARD:
            case EVENT_CANCEL_DROP_ON_BOARD:
            case EVENT_STOP_DROP_OFF_BOARD:
                if (tripEvent != null) {
                    for (final int tripRecordId : tripEvent.getTripRecordIds()) {
                        if (tripEvent.getUser_type() == UserType.DRIVER)
                            mSocket.emit(event.getEventType(), tripRecordId, new Ack() {
                                @Override
                                public void call(Object... args) {
                                    if (args != null && args.length >= 1) {
                                        try {
                                            tripEvent.setTripRecordId(tripRecordId);
                                            tripEvent.setStatus(((JSONObject) args[0]).getBoolean("status"));
                                            tripEvent.setTripEvent(event.getEventType());
                                            eventBus.post(new LocationEvent(EVENT_TRIP_RECORD_STATUS_CHANGED, tripEvent));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });
                        else
                            eventBus.post(new LocationEvent(EVENT_TRIP_RECORD_STATUS_CHANGED, tripEvent));
                    }
                }
                break;
        }
    }

    public synchronized void updateAccessToken() {
        apiService = serviceBuilder.createService(APIService.class);
        HashMap<String, String> params = serviceBuilder.getParams();
        disposable = apiService.nothing(params)
                .subscribeWith(new ServiceSingleObserver<Response>() {
                    @Override
                    public void onResponse(Response response) {
                        if (response.isStatus() && !isConnected()) {
                            disconnectSocket();
                            SocketAdapter.clear();
                            getServiceComponent().inject(SocketPresenter.this);
                            connect();
                        }
                    }

                    @Override
                    public SocketPresenter getPresenter() {
                        return SocketPresenter.this;
                    }
                });
    }


    @Override
    public void detachView() {
        if (eventBus.isRegistered(this))
            eventBus.unregister(this);
        disconnectSocket();
        //sendBroadcast(RESTART_SOCKET_RECEIVER);
        super.detachView();
        SocketAdapter.clear();
    }
}
