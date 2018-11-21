package com.pws.pateast.fragment.assignment.parent;

import com.pws.pateast.api.model.Assignment;
import com.pws.pateast.fragment.assignment.AssignmentDownloadView;
import com.pws.pateast.fragment.assignment.AssignmentView;

import java.util.List;

/**
 * Created by intel on 15-May-17.
 */

public interface ParentAssignmentView extends AssignmentView {
    void getSubjects(int bcsMapId, int userId, int masterId, int sessionId);
    void getTags(int masterId, int type);
}
