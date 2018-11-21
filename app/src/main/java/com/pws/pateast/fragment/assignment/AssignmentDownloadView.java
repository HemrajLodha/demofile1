package com.pws.pateast.fragment.assignment;

import android.net.Uri;

import com.pws.pateast.base.AppView;

import java.io.File;

/**
 * Created by planet on 5/15/2017.
 */

public interface AssignmentDownloadView extends AppView {
   // void updateAssignmentAdapter(Assignment assignment);
    int getPage();
    void addPage();
    void setPageCount(int pageCount);
    void viewAssignmentFile(File file, String type);
}
