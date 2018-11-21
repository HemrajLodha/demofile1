package com.pws.pateast.fragment.attendance.teacher.addupdate;

import com.pws.pateast.api.model.Student;
import com.pws.pateast.api.model.Tag;
import com.pws.pateast.base.AppView;

import java.util.List;

/**
 * Created by planet on 5/1/2017.
 */

public interface AddUpdateAttendanceView extends AppView {
    void setData(Student student);
    void setTagAdapter(List<Tag> tags);
    void setActionButtonText(int textRes);
}
