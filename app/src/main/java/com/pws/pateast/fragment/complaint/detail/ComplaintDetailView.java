package com.pws.pateast.fragment.complaint.detail;

import android.app.DownloadManager;
import android.view.View;

import com.pws.pateast.api.model.Complaint;
import com.pws.pateast.api.model.Tag;
import com.pws.pateast.base.AppView;
import com.pws.pateast.enums.UserType;

import java.util.List;

/**
 * Created by intel on 10-Feb-18.
 */

public interface ComplaintDetailView extends AppView, View.OnClickListener {

    void setComplaintDetail(UserType userType, Complaint complaint);

    Complaint getComplaint();

    List<Tag> getTags();

    DownloadManager getDownloadManager();

    void downloadProof(String proofUrl);

    void doCheckout(String paymentMode);
}
