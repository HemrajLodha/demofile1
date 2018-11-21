package com.pws.pateast.fragment.presenter;

import com.pws.pateast.api.model.Schedule;
import com.pws.pateast.base.AppView;

import java.util.List;

/**
 * Created by intel on 02-Sep-17.
 */

public interface ExamHeadView extends AppView
{
    void setExamHeads(List<Schedule> examHeads, boolean isSchedule);

    void setExamHead(Schedule examHead);
}
