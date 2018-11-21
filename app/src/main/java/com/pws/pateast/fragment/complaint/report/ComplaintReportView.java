package com.pws.pateast.fragment.complaint.report;

import android.view.View;

import com.pws.pateast.api.model.Complaint;
import com.pws.pateast.base.AppView;

/**
 * Created by intel on 10-Feb-18.
 */

public interface ComplaintReportView extends AppView, View.OnClickListener {

    boolean isError(CharSequence message);

    void setReportDetail(Complaint complaint);

    Complaint getComplaint();

    void navigateTpDetail(String message);
}
