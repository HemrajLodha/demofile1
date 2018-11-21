package com.pws.pateast.fragment.assignment.teacher.review.remark;

import com.pws.pateast.api.model.Student;
import com.pws.pateast.api.model.Tag;
import com.pws.pateast.base.AppView;

import java.util.List;

/**
 * Created by intel on 07-Sep-17.
 */

public interface RemarkAssignmentView extends AppView {
    void setData(Student student);
    void setTagAdapter(List<Tag> tags);
    void setActionButtonText(int textRes);

    Student getStudent();
    List<Tag> getTags();
}
