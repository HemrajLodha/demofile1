package com.pws.pateast.fragment.assignment;

import com.pws.pateast.api.model.Assignment;

import java.util.List;

/**
 * Created by intel on 15-May-17.
 */

public interface AssignmentView extends AssignmentDownloadView {
    void setAssignmentAdapter(List<Assignment> assignments);

    void onError(String message);
}
