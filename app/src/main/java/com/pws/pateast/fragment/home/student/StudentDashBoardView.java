package com.pws.pateast.fragment.home.student;

import com.pws.pateast.api.model.UserInfo;
import com.pws.pateast.fragment.home.DashBoardView;

/**
 * Created by intel on 13-May-17.
 */

public interface StudentDashBoardView extends DashBoardView
{
    void setStudentData(UserInfo userInfo);

}
