package com.pws.pateast.activity.home;

import com.pws.pateast.base.AppView;
import com.pws.pateast.fragment.home.DashBoardFragment;
import com.pws.pateast.listener.AppListener;

/**
 * Created by intel on 20-Apr-17.
 */

public interface HomeView extends AppView
{
    void setHomeFragment(DashBoardFragment homeFragment);

    void inflateMenu(int resId);


    AppListener getAppListener();
}
