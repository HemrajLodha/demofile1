package com.pws.pateast.fragment.assignment.teacher.add;

import android.net.Uri;

import com.pws.pateast.R;
import com.pws.pateast.api.ServiceAction;
import com.pws.pateast.api.ServiceModel;
import com.pws.pateast.api.model.Assignment;
import com.pws.pateast.api.model.Session;
import com.pws.pateast.api.model.Subject;
import com.pws.pateast.api.model.TeacherClass;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.TeacherService;
import com.pws.pateast.download.DownloadItemHelper;
import com.pws.pateast.fragment.presenter.SubjectPresenter;
import com.pws.pateast.utils.DateUtils;
import com.pws.pateast.utils.FileUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by planet on 5/3/2017.
 */

public class AddAssignmentPresenter extends SubjectPresenter<AddAssignmentView> {

    private Assignment assignment;
    private Uri mSelectedFileUri;
    private TeacherClass classes;
    private Subject subject;

    private Uri imageUri;

    public AddAssignmentPresenter(boolean openDialog) {
        super(openDialog);
    }


    @Override
    public void attachView(AddAssignmentView view) {
        super.attachView(view);
        setDateFormat();
        setCalender();
        if (getAssignment() != null) {
            getView().setData();
        }
    }

    public void setCalender() {
        Session session = Session.getSelectedSession(user.getUserdetails().getAcademicSessions(), user.getUserdetails().getAcademicSessionId());
        if (session != null) {
            getView().setCalenderDate(DateUtils.parse(session.getStart_date(), DateUtils.DATE_FORMAT_PATTERN), DateUtils.parse(session.getEnd_date(), DateUtils.DATE_FORMAT_PATTERN));
        }
    }

    public void setDateFormat() {
        getView().setDateFormat(preference.getDateFormat());
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public TeacherClass getClasses() {
        return classes;
    }

    public void setClasses(TeacherClass classes) {
        this.classes = classes;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Uri getSelectedFileUri() {
        return mSelectedFileUri;
    }


    public void setSelectedFileUri(Uri mSelectedFileUri) {
        this.mSelectedFileUri = mSelectedFileUri;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }


    public void setDate(int id, String dateString) {
        dateString = DateUtils.toTime(DateUtils.parse(dateString, DateUtils.DATE_FORMAT_PATTERN), preference.getDateFormat(), Locale.getDefault());
        getView().setDate(id, dateString);
    }

    public void setFileData(Uri fileUri) {
        setSelectedFileUri(fileUri);
        getView().setFileData();
    }

    public String getUploadedFileName() {
        try {
            File file = FileUtils.getFile(getContext(), getSelectedFileUri());
            return file.getName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getUploadedFileSizeInMb() {
        try {
            File file = FileUtils.getFile(getContext(), getSelectedFileUri());
            return (int) ((file.length() / 1024) / 1024); // in mb
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    public void validateFormData(int id, String error) {
        getView().setError(id, error);
    }

    public void onSubmit() {
        getView().onSubmit(assignment != null);
    }


    public void saveAssignment(boolean isUpdate,
                               String startDate, String endDate,
                               String title, String comment) {
        getView().showProgressDialog(getString(R.string.wait));
        teacherApiService = serviceBuilder.createService(TeacherService.class);

        HashMap<String, String> params = serviceBuilder.getParams(ServiceModel.ASSIGNMENT, isUpdate ? ServiceAction.EDIT : ServiceAction.ADD);
        params.put("langId", "1");
        params.put("academicSessionId", String.valueOf(user.getUserdetails().getAcademicSessionId()));
        params.put("masterId", String.valueOf(user.getData().getMasterId()));
        params.put("userId", String.valueOf(user.getData().getId()));
        params.put("bcsMapId", String.valueOf(getClasses().getBcsMapId()));
        params.put("subjectId", String.valueOf(getSubject().getSubjectId()));
        params.put("start_date", startDate);
        params.put("end_date", endDate);

        if (isUpdate) {
            params.put("id", String.valueOf(getAssignment().getId()));
        }
        JSONObject data = new JSONObject(params);
        JSONObject assignmentDetails = new JSONObject();
        try {
            if (isUpdate) {
                assignmentDetails.put("id", getAssignment().getAssignmentdetails().get(0).getId());
            }
            assignmentDetails.put("title", title);
            assignmentDetails.put("comment", comment);
            data.put("assignmentdetails", assignmentDetails);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = serviceBuilder.prepareStringPart(data.toString());

        MultipartBody.Part fileBody = null;
        if (getSelectedFileUri() != null) {
            String path = FileUtils.getPath(getContext(), getSelectedFileUri());
            Uri destUri = FileUtils.saveFile(path);
            setSelectedFileUri(destUri);
            if (getSelectedFileUri() != null) {
                fileBody = serviceBuilder.prepareFilePart(getContext(), "assignment_file", getSelectedFileUri());
            }
        }

        disposable = teacherApiService.saveAssignment(requestBody, fileBody)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<AddAssignmentResponse>() {
                    @Override
                    public void onResponse(AddAssignmentResponse response) {
                        DownloadItemHelper.saveDownloadItem(getContext(), user.getUserdetails().getUserId(), response.getData(), getSelectedFileUri());
                        AddAssignmentPresenter.this.getView().openDialog(response.getMessage());
                    }

                    @Override
                    public AddAssignmentPresenter getPresenter() {
                        return AddAssignmentPresenter.this;
                    }

                    @Override
                    public boolean openDialog() {
                        return true;
                    }
                });
    }

}
