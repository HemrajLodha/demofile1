package com.pws.pateast.fragment.circular.teacher;

import com.pws.pateast.fragment.circular.CircularPresenter;

import java.util.HashMap;

public class TeacherCircularPresenter extends CircularPresenter {

    public void getTeacherCircular() {
        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put("teachers", "1");
        getCircular(String.valueOf(user.getData().getMasterId()), String.valueOf(user.getUserdetails().getAcademicSessionId()), queryParams);
    }
}
