package com.pws.pateast.base;

import com.base.view.BaseView;
import com.pws.pateast.MyApplication;
import com.pws.pateast.di.component.ApplicationComponent;

/**
 * Created by intel on 21-Jul-17.
 */

public interface ServiceView extends BaseView
{
    MyApplication getApp();

    ApplicationComponent getApplicationComponent();
}
