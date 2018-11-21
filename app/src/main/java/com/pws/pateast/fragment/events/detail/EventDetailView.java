package com.pws.pateast.fragment.events.detail;

import android.net.Uri;
import android.view.View;

import com.pws.pateast.api.model.Events;
import com.pws.pateast.base.AppView;

public interface EventDetailView extends AppView, View.OnClickListener {

    void viewFile(Uri uri, String type);

    int getEventID();

    void setEventDetail(Events events);
}
