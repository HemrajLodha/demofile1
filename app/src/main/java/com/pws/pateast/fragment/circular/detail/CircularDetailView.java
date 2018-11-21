package com.pws.pateast.fragment.circular.detail;

import android.net.Uri;
import android.view.View;

import com.pws.pateast.api.model.Circular;
import com.pws.pateast.base.AppView;

public interface CircularDetailView extends AppView, View.OnClickListener {

    void viewFile(Uri uri, String type);

    int getCircularID();

    void setCircularDetail(Circular circular);
}
