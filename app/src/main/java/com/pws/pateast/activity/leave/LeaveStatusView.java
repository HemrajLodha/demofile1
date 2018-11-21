package com.pws.pateast.activity.leave;

import com.pws.pateast.base.AppView;
import com.pws.pateast.model.LeaveCategory;

import java.util.List;

/**
 * Created by intel on 28-Jun-17.
 */

public interface LeaveStatusView extends AppView
{
    int tabIndicatorColor();
    int tabSelectedTextColor();
    int tabTextColor();
    int tabBackground();

    void setTitle(int title);
    void setTitle(String title);
    void leaveApply();
    void setApplyVisible(boolean visible);

    void setStatusAdapter(List<LeaveCategory> categories);
}
