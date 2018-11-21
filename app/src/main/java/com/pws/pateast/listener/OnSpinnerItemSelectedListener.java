package com.pws.pateast.listener;

import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Spinner;


/**
 * Created by planet on 8/3/2017.
 */

public interface OnSpinnerItemSelectedListener {
    void onItemSelected(Spinner spinner, AdapterView<?> parent, View view, int position, long id);

    void onNothingSelected(AdapterView<?> parent);
}
