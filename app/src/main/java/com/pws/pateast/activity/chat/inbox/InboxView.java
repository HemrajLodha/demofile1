package com.pws.pateast.activity.chat.inbox;

import android.database.Cursor;

import com.hlab.fabrevealmenu.model.FABMenuItem;
import com.pws.pateast.api.model.Message;
import com.pws.pateast.base.AppView;

import java.util.ArrayList;

/**
 * Created by intel on 25-Jul-17.
 */

public interface InboxView extends AppView
{
    int getAppTheme();

    void setChatUser(ArrayList<FABMenuItem> fabMenuItems);
    void setInboxAdapter(Cursor cursor);
}
