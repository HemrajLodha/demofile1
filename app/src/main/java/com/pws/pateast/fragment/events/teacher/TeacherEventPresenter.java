package com.pws.pateast.fragment.events.teacher;

import com.pws.pateast.fragment.events.EventPresenter;

import java.util.HashMap;

public class TeacherEventPresenter extends EventPresenter {

    public void getTeacherEvent() {
        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put("teachers", "1");
        getEvents(String.valueOf(user.getData().getMasterId()), String.valueOf(user.getUserdetails().getAcademicSessionId()), queryParams);
    }
}
