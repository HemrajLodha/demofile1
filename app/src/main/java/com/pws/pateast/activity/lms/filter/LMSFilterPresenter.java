package com.pws.pateast.activity.lms.filter;

import com.pws.pateast.R;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.exception.RetrofitException;
import com.pws.pateast.api.model.Chapter;
import com.pws.pateast.api.model.Subject;
import com.pws.pateast.api.model.Topic;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.StudentService;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.utils.Preference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by intel on 24-Jan-18.
 */

public class LMSFilterPresenter extends AppPresenter<LMSFilterView> {
    @Inject
    Preference preference;

    @Inject
    ServiceBuilder serviceBuilder;
    private User user;
    private StudentService studentApiService;
    private boolean openDialog;
    private LMSFilterView filterView;
    private HashMap<Integer, List<Subject>> subjectData;
    private HashMap<Integer, List<Chapter>> chapterData;
    private HashMap<Integer, List<Topic>> topicData;

    public LMSFilterPresenter(boolean openDialog) {
        this.openDialog = openDialog;
    }

    public boolean isOpenDialog() {
        return openDialog;
    }

    @Override
    public LMSFilterView getView() {
        return filterView;
    }

    @Override
    public void attachView(LMSFilterView view) {
        filterView = view;
        getComponent().inject(this);
        user = preference.getUser();
        subjectData = getView().getSubjectData();
        chapterData = getView().getChapterData();
        topicData = getView().getTopicData();
    }


    public void getSubject() {
        final int bcsmapId = user.getUserdetails().getBcsMapId();
        if (subjectData == null)
            subjectData = new HashMap<>();
        if (subjectData.containsKey(bcsmapId)) {
            getView().setSubjectAdapter(subjectData.get(bcsmapId));
            return;
        } else if (bcsmapId == -1) {
            getView().setSubjectAdapter(new ArrayList<Subject>());
            return;
        }

        if (isOpenDialog())
            getView().showProgressDialog(getString(R.string.wait));
        studentApiService = serviceBuilder.createService(StudentService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("academicSessionId", String.valueOf(user.getUserdetails().getAcademicSessionId()));
        params.put("masterId", String.valueOf(user.getData().getMasterId()));
        params.put("bcsmapId", String.valueOf(bcsmapId));

        disposable = studentApiService.getSubject(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Subject>() {

                    @Override
                    public boolean openDialog() {
                        return isOpenDialog();
                    }

                    @Override
                    public void onResponse(Subject response) {
                        if (response.getData().isEmpty()) {
                            onError(new RetrofitException(getString(R.string.no_subject)));
                            getView().setSubjectAdapter(new ArrayList<Subject>());
                        } else {
                            subjectData.put(bcsmapId, response.getData());
                            getView().setSubjectData(subjectData);
                            getView().setSubjectAdapter(subjectData.get(bcsmapId));
                        }
                    }

                    @Override
                    public LMSFilterPresenter getPresenter() {
                        return LMSFilterPresenter.this;
                    }

                });
    }


    public void getChapters(final int subjectId) {
        if (chapterData == null)
            chapterData = new HashMap<>();
        if (chapterData.containsKey(subjectId)) {
            getView().setChapterAdapter(chapterData.get(subjectId));
            return;
        } else if (subjectId == -1) {
            getView().setChapterAdapter(new ArrayList<Chapter>());
            return;
        }

        if (isOpenDialog())
            getView().showProgressDialog(getString(R.string.wait));
        studentApiService = serviceBuilder.createService(StudentService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("academicSessionId", String.valueOf(user.getUserdetails().getAcademicSessionId()));
        params.put("masterId", String.valueOf(user.getData().getMasterId()));
        params.put("bcsmapId", String.valueOf(user.getUserdetails().getBcsMapId()));
        params.put("subjectId", String.valueOf(subjectId));

        disposable = studentApiService.getChapters(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Chapter>() {

                    @Override
                    public boolean openDialog() {
                        return isOpenDialog();
                    }

                    @Override
                    public boolean isDismiss() {
                        return false;
                    }

                    @Override
                    public void onResponse(Chapter response) {
                        if (response.getData().isEmpty()) {
                            onError(new RetrofitException(getString(R.string.no_chapter)));
                            getView().setChapterAdapter(new ArrayList<Chapter>());
                        } else {
                            chapterData.put(subjectId, response.getData());
                            getView().setChapterData(chapterData);
                            getView().setChapterAdapter(chapterData.get(subjectId));
                        }
                    }

                    @Override
                    public LMSFilterPresenter getPresenter() {
                        return LMSFilterPresenter.this;
                    }

                });
    }

    public void getTopics(final int chapterId) {
        if (topicData == null)
            topicData = new HashMap<>();
        if (topicData.containsKey(chapterId)) {
            getView().setTopicAdapter(topicData.get(chapterId));
            return;
        } else if (chapterId == -1) {
            getView().setTopicAdapter(new ArrayList<Topic>());
            return;
        }
        if (isOpenDialog())
            getView().showProgressDialog(getString(R.string.wait));
        studentApiService = serviceBuilder.createService(StudentService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("academicSessionId", String.valueOf(user.getUserdetails().getAcademicSessionId()));
        params.put("masterId", String.valueOf(user.getData().getMasterId()));
        params.put("lmschapterId", String.valueOf(chapterId));

        disposable = studentApiService.getTopics(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Topic>() {

                    @Override
                    public boolean openDialog() {
                        return isOpenDialog();
                    }

                    @Override
                    public boolean isDismiss() {
                        return false;
                    }

                    @Override
                    public void onResponse(Topic response) {
                        if (response.getData().isEmpty()) {
                            onError(new RetrofitException(getString(R.string.no_topic)));
                            getView().setTopicAdapter(new ArrayList<Topic>());
                        } else {
                            topicData.put(chapterId, response.getData());
                            getView().setTopicData(topicData);
                            getView().setTopicAdapter(topicData.get(chapterId));
                        }
                    }

                    @Override
                    public LMSFilterPresenter getPresenter() {
                        return LMSFilterPresenter.this;
                    }

                });
    }
}
