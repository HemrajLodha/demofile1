package com.pws.pateast.fragment.profile;

import com.pws.pateast.api.model.User;
import com.pws.pateast.api.model.UserInfo;
import com.pws.pateast.base.AppView;

public interface ProfileView extends AppView {
    void setData(User user, UserInfo userInfo);
    boolean loadFromPreference();
}
