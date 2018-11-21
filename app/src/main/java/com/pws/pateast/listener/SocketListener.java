package com.pws.pateast.listener;

import io.socket.emitter.Emitter;

/**
 * Created by intel on 21-Jul-17.
 */

public interface SocketListener
{
    void call(String event, Object... args);
}
