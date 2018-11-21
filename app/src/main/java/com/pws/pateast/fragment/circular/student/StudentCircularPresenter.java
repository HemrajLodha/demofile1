package com.pws.pateast.fragment.circular.student;

import com.pws.pateast.fragment.circular.CircularPresenter;

import java.util.HashMap;

public class StudentCircularPresenter extends CircularPresenter {

    public void getStudentCircular() {
        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put("students", "1");
        queryParams.put("eventrecord__bcsMapId__eq", String.valueOf(user.getUserdetails().getBcsMapId()));
        getCircular(String.valueOf(user.getData().getMasterId()), String.valueOf(user.getUserdetails().getAcademicSessionId()), queryParams);
    }
}
