package com.pws.pateast.fragment.home;

import com.pws.pateast.model.DashboardItem;
import com.pws.pateast.base.AppView;

import java.util.List;

/**
 * Created by intel on 13-May-17.
 */

public interface DashBoardView extends AppView
{
    void setDashboardAdapter(List<DashboardItem> items);
}
