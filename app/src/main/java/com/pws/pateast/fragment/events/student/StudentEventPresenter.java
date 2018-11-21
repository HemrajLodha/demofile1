package com.pws.pateast.fragment.events.student;

import com.pws.pateast.fragment.events.EventPresenter;

import java.util.HashMap;

public class StudentEventPresenter extends EventPresenter {

    public void getStudentEvent() {
        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put("students", "1");
        queryParams.put("eventrecord__bcsMapId__eq", String.valueOf(user.getUserdetails().getBcsMapId()));
        getEvents(String.valueOf(user.getData().getMasterId()), String.valueOf(user.getUserdetails().getAcademicSessionId()), queryParams);
    }
}
