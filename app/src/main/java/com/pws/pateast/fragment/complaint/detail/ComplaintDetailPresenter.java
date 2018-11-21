package com.pws.pateast.fragment.complaint.detail;

import android.text.TextUtils;

import com.pws.pateast.Constants;
import com.pws.pateast.R;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.model.Tag;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.model.Ward;
import com.pws.pateast.api.service.StudentService;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.download.RxDownloadManagerHelper;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.listener.AdapterListener;
import com.pws.pateast.utils.FileUtils;
import com.pws.pateast.utils.Preference;

import java.util.List;

import javax.inject.Inject;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

/**
 * Created by intel on 10-Feb-18.
 */

public class ComplaintDetailPresenter extends AppPresenter<ComplaintDetailView> {
    @Inject
    Preference preference;

    @Inject
    ServiceBuilder serviceBuilder;
    private User user;
    private Ward ward;
    private StudentService apiService;
    private ComplaintDetailView mView;

    @Override
    public ComplaintDetailView getView() {
        return mView;
    }

    @Override
    public void attachView(ComplaintDetailView view) {
        mView = view;
        getComponent().inject(this);
        user = preference.getUser();
        ward = preference.getWard();
        getView().setComplaintDetail(getUserType(), getView().getComplaint());
    }

    public UserType getUserType() {
        return UserType.getUserType(user.getData().getUser_type());
    }

    public void downloadProof(final String proofUrl) {
        getView().showDialog(getString(R.string.app_name), getString(R.string.download_prompt_message), new AdapterListener<String>() {
            @Override
            public void onClick(int id, String value) {
                switch (id) {
                    case 0:
                        startDownload(proofUrl);
                        break;
                    case 1:
                        break;
                }
            }
        }, R.string.yes, R.string.no);
    }

    private void startDownload(String proofUrl) {
        String downloadUrl = ServiceBuilder.IMAGE_URL + proofUrl;
        String destFileName = FileUtils.getFileName(downloadUrl);
        RxDownloadManagerHelper.enqueueDownload(getView().getDownloadManager(), downloadUrl, Constants.COMPLAINT_FILES_DIR, destFileName);
    }

    public String getFollowUp(String id) {
        List<Tag> tags = getView().getTags();
        tags = StreamSupport
                .stream(tags)
                .filter(trip -> Integer.parseInt(id) == trip.getId())
                .collect(Collectors.toList());
        List<String> tagData = StreamSupport
                .stream(tags)
                .map(trip -> trip.getTagdetails().get(0).getTitle())
                .collect(Collectors.toList());
        return TextUtils.join(",", tagData);
    }
}
