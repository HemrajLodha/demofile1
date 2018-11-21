package com.pws.pateast.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.pws.pateast.MyApplication;
import com.pws.pateast.events.SocketEvent;
import com.pws.pateast.service.socket.SocketService;

import org.greenrobot.eventbus.EventBus;

import java.net.Socket;

import javax.inject.Inject;

import static com.pws.pateast.events.SocketEvent.EVENT_CONNECT;

/**
 * Created by intel on 21-Jul-17.
 */

public class SocketBroadcastReceiver extends BroadcastReceiver
{
    @Inject
    EventBus eventBus;

    public final static String RESTART_SOCKET_RECEIVER = "com.pws.pateast.RESTART_SOCKET_RECEIVER";

    @Override
    public void onReceive(Context context, Intent intent) {
        MyApplication.get(context).getApplicationComponent().inject(this);
        switch (intent.getAction())
        {
            case RESTART_SOCKET_RECEIVER:
                eventBus.post(new SocketEvent(EVENT_CONNECT));
                break;
        }
    }
}
