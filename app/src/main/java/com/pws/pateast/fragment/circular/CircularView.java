package com.pws.pateast.fragment.circular;

import com.pws.pateast.api.model.Circular;
import com.pws.pateast.api.model.Events;
import com.pws.pateast.base.AppView;

import java.util.List;

public interface CircularView extends AppView {

    CircularPresenter getCircularPresenter();

    void setCircularAdapter(List<Circular> circulars);
}
