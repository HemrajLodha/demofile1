package com.pws.pateast.fragment.leave;

import com.pws.pateast.api.model.Leave;
import com.pws.pateast.base.AppView;

import java.util.List;

/**
 * Created by intel on 23-Aug-17.
 */

public interface LeaveView extends AppView
{
    int getPage();
    void addPage();
    void setPageCount(int pageCount);

    String getLeaveStatus();
    void leaveApply();
    void setApplyVisible(boolean visible);

    void setLeaveAdapter(List<Leave> leaves);
}
