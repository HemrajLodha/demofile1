package com.pws.pateast.listener;

import android.text.TextUtils;

import com.pws.pateast.service.socket.SocketView;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import io.socket.emitter.Emitter;

/**
 * Created by intel on 21-Jul-17.
 */

public class SocketAdapter implements Emitter.Listener {
    private static ConcurrentMap<String, SocketAdapter> callbacks = new ConcurrentHashMap<>();

    private String event;
    private SocketListener socketListener;

    private SocketAdapter(String event, SocketView socketListener) {
        this(event, (SocketListener) socketListener);
    }

    private SocketAdapter(String event, SocketListener socketListener) {
        this.event = event;
        this.socketListener = socketListener;
    }

    @Override
    public void call(Object... args) {
        if (socketListener != null && !TextUtils.isEmpty(event)) {
            socketListener.call(event, args);
        }
    }

    public static SocketAdapter get(String event, SocketView socketListener) {
        return get(event, (SocketListener) socketListener);
    }

    public static SocketAdapter get(String event, SocketListener socketListener) {
        SocketAdapter socketAdapter = callbacks.get(event);
        if (socketAdapter == null) {
            socketAdapter = new SocketAdapter(event, socketListener);
            SocketAdapter tempSocketAdapter = callbacks.putIfAbsent(event, socketAdapter);
            if (tempSocketAdapter != null) {
                socketAdapter = tempSocketAdapter;
            }
        }
        return socketAdapter;
    }

    public static void clear() {
        callbacks.clear();
    }
}
