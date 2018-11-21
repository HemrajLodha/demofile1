package com.pws.pateast.activity.chat.add;

import com.pws.pateast.api.model.UserInfo;
import com.pws.pateast.base.AppView;
import com.pws.pateast.enums.UserType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by intel on 03-Aug-17.
 */

public interface ChatUserView extends AppView {
    int getAppTheme();

    UserType getChatUserType();

    void setTitle(int title);

    void getClasses();

    void setChatUserAdapter(List<UserInfo> userInfos);

    void updateChatUserAdapter(List<UserInfo> userInfos);
}
