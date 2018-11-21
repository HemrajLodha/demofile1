package com.pws.pateast.fragment.circular.parent;

import com.pws.pateast.fragment.circular.CircularPresenter;

import java.util.HashMap;

public class ParentCircularPresenter extends CircularPresenter {

    public void getParentCircular() {
        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put("parents", "1");
        queryParams.put("eventrecord__bcsMapId__eq", String.valueOf(ward.getBcsMapId()));
        getCircular(String.valueOf(ward.getMasterId()), String.valueOf(ward.getAcademicSessionId()), queryParams);
    }
}
