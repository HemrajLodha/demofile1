package com.pws.pateast.service.socket;

import com.pws.pateast.base.ServiceView;
import com.pws.pateast.service.sync.SyncView;

/**
 * Created by intel on 21-Jul-17.
 */

public interface SocketView extends ServiceView
{
    void getInbox();
}
