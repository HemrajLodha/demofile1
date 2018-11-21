package com.pws.pateast.fragment.feeds.add;

import android.net.Uri;

import com.pws.pateast.R;
import com.pws.pateast.api.exception.RetrofitException;
import com.pws.pateast.api.model.Feeds;
import com.pws.pateast.api.model.Response;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.model.UserInfo;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.APIService;
import com.pws.pateast.enums.MessageType;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.fragment.assignment.teacher.add.AddAssignmentResponse;
import com.pws.pateast.fragment.presenter.ClassPresenter;
import com.pws.pateast.service.upload.task.ImageCompressAsyncTask;
import com.pws.pateast.service.upload.task.VideoCompressAsyncTask;
import com.pws.pateast.utils.DialogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AddFeedsPresenter extends ClassPresenter<AddFeedsView> {
    private int MAX_FILE_COUNT = 5;

    private APIService apiService;

    public AddFeedsPresenter(boolean openDialog) {
        super(openDialog);

    }

    public UserType getUserType() {
        return UserType.getUserType(getUser().getUser_type());
    }

    public User getUser() {
        return user.getData();
    }

    @Override
    public void getMyClasses() {
        UserType userType = UserType.getUserType(user.getData().getUser_type());
        switch (userType) {
            case TEACHER:
                super.getMyClasses();
                getView().showClasses(true);
                break;
            default:
                getControlUsers(userType);
                getView().showClasses(false);
                break;
        }
    }


    private void getControlUsers(UserType userType) {
        if (isOpenDialog())
            getView().showProgressDialog(getString(R.string.wait));

        apiService = serviceBuilder.createService(APIService.class);
        HashMap<String, String> params = serviceBuilder.getParams();
        switch (userType) {
            case STUDENT:
                params.put("masterId", String.valueOf(user.getData().getMasterId()));
                params.put("academicSessionId", String.valueOf(user.getUserdetails().getAcademicSessionId()));
                params.put("bcsmapId", String.valueOf(user.getUserdetails().getBcsMapId()));
                break;
            case PARENT:
                params.put("masterId", String.valueOf(ward.getMasterId()));
                params.put("academicSessionId", String.valueOf(ward.getAcademicSessionId()));
                params.put("bcsmapId", String.valueOf(ward.getBcsMapId()));
                break;
        }

        disposable = apiService.getControlUsers(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Response<ArrayList<UserInfo>>>() {

                    @Override
                    public boolean openDialog() {
                        return isOpenDialog();
                    }

                    @Override
                    public void onResponse(Response<ArrayList<UserInfo>> response) {
                        if (response.getData().isEmpty()) {
                            onError(new RetrofitException(getString(R.string.no_teachers)));
                        } else {
                            getView().setTeacherAdapter(response.getData());
                        }
                    }

                    @Override
                    public AddFeedsPresenter getPresenter() {
                        return AddFeedsPresenter.this;
                    }
                });

    }

    public boolean postFeed() {
        boolean isError = getView().isError();
        if (isError) {
            return isError;
        }
        getView().showProgressDialog(getString(R.string.wait));
        apiService = serviceBuilder.createService(APIService.class);
        HashMap<String, RequestBody> params = serviceBuilder.getRequestBodyParams();
        UserType userType = UserType.getUserType(user.getData().getUser_type());
        int bcsMapId = getView().getBcsMapID(userType);
        switch (userType) {
            case PARENT:
                params.put("masterId", serviceBuilder.prepareStringPart(String.valueOf(ward.getMasterId())));
                params.put("academicSessionId", serviceBuilder.prepareStringPart(String.valueOf(ward.getAcademicSessionId())));
                params.put("controlUserId", serviceBuilder.prepareStringPart(String.valueOf(getView().getTeacher().getUser().getId())));
                if (bcsMapId != 0)
                    params.put("bcsmapId", serviceBuilder.prepareStringPart(String.valueOf(ward.getBcsMapId())));
                break;
            case STUDENT:
                params.put("masterId", serviceBuilder.prepareStringPart(String.valueOf(user.getData().getMasterId())));
                params.put("academicSessionId", serviceBuilder.prepareStringPart(String.valueOf(user.getUserdetails().getAcademicSessionId())));
                params.put("controlUserId", serviceBuilder.prepareStringPart(String.valueOf(String.valueOf(getView().getTeacher().getUser().getId()))));
                if (bcsMapId != 0)
                    params.put("bcsmapId", serviceBuilder.prepareStringPart(String.valueOf(user.getUserdetails().getBcsMapId())));
                break;
            case TEACHER:
                params.put("masterId", serviceBuilder.prepareStringPart(String.valueOf(user.getData().getMasterId())));
                params.put("academicSessionId", serviceBuilder.prepareStringPart(String.valueOf(user.getUserdetails().getAcademicSessionId())));
                if (bcsMapId != 0)
                    params.put("bcsmapId", serviceBuilder.prepareStringPart(String.valueOf(bcsMapId)));
                break;
        }

        params.put("description", serviceBuilder.prepareStringPart(getView().getDescription().toString()));

        List<MultipartBody.Part> fileParams = new ArrayList<>();
        int flag = 0;
        for (Feeds feeds : getView().getFiles()) {
            MultipartBody.Part fileBody = serviceBuilder.prepareFilePart(getContext(), String.format("files[%d]", flag), feeds.getFileUri());
            fileParams.add(fileBody);
            flag++;
        }

        disposable = apiService.postFeed(params, fileParams)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<AddAssignmentResponse>() {
                    @Override
                    public boolean openDialog() {
                        return isOpenDialog();
                    }

                    @Override
                    public void onResponse(AddAssignmentResponse response) {
                        getView().onFeedPosted(response.getMessage());
                    }

                    @Override
                    public AddFeedsPresenter getPresenter() {
                        return AddFeedsPresenter.this;
                    }
                });

        return isError;
    }

    public void chooseMediaFileDialog(int fileCount) {
        if (fileCount < MAX_FILE_COUNT) {
            String[] options = new String[]{getString(R.string.camera), getString(R.string.video), getString(R.string.pick_image), getString(R.string.pick_video)};

            DialogUtils.showSingleChoiceDialog(getContext(), getString(R.string.app_name), options, getView(), getString(R.string.ok), getString(R.string.cancel));
        } else {
            getView().showDialog(getString(R.string.app_name), getString(R.string.max_feed_file_count_exceed), null, R.string.ok);
        }
    }

    public void handleFiles(MessageType messageType, Uri uri) {
        switch (messageType) {
            case IMAGE:
                handleActionImage(uri);
                break;
            case VIDEO:
                handleActionVideo(uri);
                break;
            case NONE:
                getView().showMessage(R.string.validate_file_type, false, R.color.colorPrimary);
                break;
        }
    }

    private void handleActionImage(Uri uri) {
        new ImageCompressAsyncTask(getContext(), getView()).execute(uri);
    }

    private void handleActionVideo(Uri uri) {
        new VideoCompressAsyncTask(getContext(), getView()).execute(uri);
    }
}
