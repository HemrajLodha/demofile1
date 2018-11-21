package com.pws.pateast.fragment.assignment;

import android.net.Uri;

import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.model.Ward;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.utils.Preference;
import com.pws.pateast.download.DownloadItemHelper;
import com.pws.pateast.utils.FileUtils;

import javax.inject.Inject;

/**
 * Created by planet on 5/15/2017.
 */

public class AssignmentDownloadPresenter<V extends AssignmentDownloadView> extends AppPresenter<V> {
    @Inject
    protected ServiceBuilder serviceBuilder;

    @Inject
    protected Preference preference;

    protected User user;
    private static final String TAG = AssignmentDownloadPresenter.class.getSimpleName();
    private V mAssignmentDownloadView;

    @Override
    public V getView() {
        return mAssignmentDownloadView;
    }


    @Override
    public void attachView(V view) {
        mAssignmentDownloadView = view;
        getComponent().inject((AssignmentDownloadPresenter<AssignmentDownloadView>) this);
        user = preference.getUser();
    }

    public void openAssignmentFile(int itemId) {
        Uri fileUri = DownloadItemHelper.getDownloadedUri(getContext(), itemId);
        if (fileUri != null) {
            getView().viewAssignmentFile(FileUtils.getFile(getContext(), fileUri),
                    FileUtils.getMimeType(getContext(), fileUri));
        }
    }

}

