package com.pws.pateast.fragment.assignment.parent.detail;

import android.net.Uri;

import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.model.User;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.download.DownloadItemHelper;
import com.pws.pateast.fragment.assignment.AssignmentDownloadPresenter;
import com.pws.pateast.utils.FileUtils;
import com.pws.pateast.utils.Preference;

import javax.inject.Inject;

/**
 * Created by intel on 15-May-17.
 */

public class AssignmentDetailPresenter extends AppPresenter<AssignmentDetailView> {
    @Inject
    protected ServiceBuilder serviceBuilder;

    @Inject
    protected Preference preference;

    protected User user;
    private static final String TAG = AssignmentDownloadPresenter.class.getSimpleName();
    private AssignmentDetailView mAssignmentDownloadView;

    @Override
    public AssignmentDetailView getView() {
        return mAssignmentDownloadView;
    }


    @Override
    public void attachView(AssignmentDetailView view) {
        mAssignmentDownloadView = view;
        getComponent().inject(this);
        user = preference.getUser();
    }

    public String getDateFormat() {
        return preference.getDateFormat();
    }

    public void openAssignmentFile(int itemId) {
        Uri fileUri = DownloadItemHelper.getDownloadedUri(getContext(), itemId);
        if (fileUri != null) {
            mAssignmentDownloadView.viewAssignmentFile(fileUri,
                    FileUtils.getMimeType(getContext(), fileUri));
        }
    }
}
