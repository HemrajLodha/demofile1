package com.pws.pateast.fragment.home;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.pws.pateast.R;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.base.Extras;

/**
 * Created by intel on 18-Apr-17.
 */

public class DummyFragment extends AppFragment {

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_dummy;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState)
    {
        if(getTitle() == null)
            getAppListener().setTitle(R.string.app_name);
        else
            getAppListener().setTitle(getTitle());
    }

    String getTitle()
    {
        return getArguments().getString(Extras.TITLE);
    }

}
