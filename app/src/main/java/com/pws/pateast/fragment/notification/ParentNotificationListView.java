package com.pws.pateast.fragment.notification;

import com.pws.pateast.api.model.Leave;
import com.pws.pateast.api.model.Notification;
import com.pws.pateast.api.model.NotificationData;
import com.pws.pateast.base.AppView;
import com.pws.pateast.fragment.leave.LeaveView;

import java.util.List;

/**
 * Created by intel on 18-Aug-17.
 */

public interface ParentNotificationListView extends AppView
{
    int getPage();
    void addPage();
    void setPageCount(int pageCount);
    void setListAdapter(List<NotificationData> notifications);
    void onStatusChange(int position);
}
