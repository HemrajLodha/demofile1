package com.pws.pateast.fragment.events;

import com.pws.pateast.api.model.Events;
import com.pws.pateast.base.AppView;

import java.util.List;

public interface EventView extends AppView {

    EventPresenter getEventPresenter();

    void setEventsAdapter(List<Events> events);
}
