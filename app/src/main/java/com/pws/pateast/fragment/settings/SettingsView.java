package com.pws.pateast.fragment.settings;

import com.pws.pateast.api.model.User;
import com.pws.pateast.base.AppView;
import com.pws.pateast.enums.UserType;

/**
 * Created by planet on 4/21/2017.
 */

public interface SettingsView extends AppView {

    void setNotification(boolean notification);

    void setSettingsVisibility(UserType userType);

    void onNotificationStatusChange(int status);

}
