package com.pws.pateast.fragment.leave.parent.apply;

import com.pws.pateast.api.model.Tag;
import com.pws.pateast.api.model.Ward;
import com.pws.pateast.base.AppView;
import com.pws.pateast.fragment.leave.LeaveApplyView;

import java.util.ArrayList;

/**
 * Created by intel on 19-Aug-17.
 */

public interface WardLeaveApplyView extends LeaveApplyView
{
    void setWardData(Ward ward);
}
