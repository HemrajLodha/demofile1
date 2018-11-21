package com.pws.pateast.fragment.events.parent;

import com.pws.pateast.fragment.events.EventPresenter;

import java.util.HashMap;

public class ParentEventPresenter extends EventPresenter {

    public void getParentEvent() {
        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put("parents", "1");
        queryParams.put("eventrecord__bcsMapId__eq", String.valueOf(ward.getBcsMapId()));
        getEvents(String.valueOf(ward.getMasterId()), String.valueOf(ward.getAcademicSessionId()), queryParams);
    }
}
