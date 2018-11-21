package com.pws.pateast.service.sync;

import com.pws.pateast.api.model.Message;
import com.pws.pateast.api.model.UserInfo;
import com.pws.pateast.base.ServiceView;

import java.util.ArrayList;

/**
 * Created by intel on 26-Jul-17.
 */

public interface SyncView extends ServiceView
{
    void updateUser(ArrayList<UserInfo> userInfos);
}
