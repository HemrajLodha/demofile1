package com.pws.pateast.fragment.assignment.parent.detail;

import android.net.Uri;

import com.pws.pateast.api.model.Assignment;
import com.pws.pateast.base.AppView;
import com.pws.pateast.fragment.assignment.AssignmentDownloadView;

import java.util.List;

/**
 * Created by intel on 15-May-17.
 */

public interface AssignmentDetailView extends AppView
{
    void setData(Assignment assignment);
    void  viewAssignmentFile(Uri uri, String type);
}
