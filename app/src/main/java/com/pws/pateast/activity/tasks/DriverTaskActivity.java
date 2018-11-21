package com.pws.pateast.activity.tasks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.pws.pateast.R;
import com.pws.pateast.base.Extras;
import com.pws.pateast.fragment.track.driver.StudentRouteView;

/**
 * Created by planet on 4/20/2017.
 */

public class DriverTaskActivity extends TaskActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.DriverTheme);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);
        if (isToolbarSetupEnabled()) {
            getToolbar().setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDriver));
        }
    }

    @Override
    public void onBackPressed() {
        if (appFragment instanceof StudentRouteView) {
            StudentRouteView routeView = ((StudentRouteView) appFragment);
            Intent intent = new Intent();
            intent.putExtra(Extras.TRIP, routeView.getTrip());
            setResult(RESULT_OK, intent);
            finish();
            return;
        }
        super.onBackPressed();
    }
}
