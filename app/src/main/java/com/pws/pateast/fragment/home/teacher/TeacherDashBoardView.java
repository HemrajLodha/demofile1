package com.pws.pateast.fragment.home.teacher;

import com.pws.pateast.api.model.UserInfo;
import com.pws.pateast.fragment.home.DashBoardView;

/**
 * Created by intel on 20-Apr-17.
 */

public interface TeacherDashBoardView extends DashBoardView {
    void setTeacherData(UserInfo userInfo);
}
