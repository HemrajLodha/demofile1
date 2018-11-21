package com.pws.pateast.fragment.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;

import com.pws.pateast.base.AppFragment;
import com.pws.pateast.R;

/**
 * Created by intel on 13-May-17.
 */

public abstract class DashBoardFragment extends AppFragment implements NavigationView.OnNavigationItemSelectedListener
{
    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_dummy;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState)
    {
        getAppListener().setTitle(R.string.title_dashboard);
    }


}
