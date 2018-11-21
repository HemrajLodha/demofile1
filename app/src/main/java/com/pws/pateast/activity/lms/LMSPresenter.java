package com.pws.pateast.activity.lms;

import android.os.Bundle;

import com.pws.pateast.R;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.exception.RetrofitException;
import com.pws.pateast.api.model.StudyMaterial;
import com.pws.pateast.api.model.Topic;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.StudentService;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.base.Extras;
import com.pws.pateast.enums.LMSCategory;
import com.pws.pateast.fragment.lms.StudentLMSFragment;
import com.pws.pateast.utils.Preference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.pws.pateast.enums.LMSCategory.LMSType.NOTES;
import static com.pws.pateast.enums.LMSCategory.LMSType.PPT;
import static com.pws.pateast.enums.LMSCategory.LMSType.VIDEO;
import static com.pws.pateast.enums.LMSCategory.LMSType.WORK_SHEET;

/**
 * Created by intel on 24-Jan-18.
 */

public class LMSPresenter extends AppPresenter<LMSView> {
    @Inject
    Preference preference;

    @Inject
    ServiceBuilder serviceBuilder;
    private User user;
    private StudentService studentApiService;
    private LMSView lmsView;

    @Override
    public LMSView getView() {
        return lmsView;
    }

    @Override
    public void attachView(LMSView view) {
        lmsView = view;
        getComponent().inject(this);
        user = preference.getUser();
        getView().showFilterFragment();
    }

    private List<StudentLMSFragment> getStudentLMSFragments(Topic topic) {
        List<StudentLMSFragment> lmsFragments = new ArrayList<>();
        lmsFragments.add(AppFragment.newInstance(StudentLMSFragment.class, null, getBundle(R.string.video_animation, VIDEO, topic)));
        lmsFragments.add(AppFragment.newInstance(StudentLMSFragment.class, null, getBundle(R.string.presentation, PPT, topic)));
        lmsFragments.add(AppFragment.newInstance(StudentLMSFragment.class, null, getBundle(R.string.notes, NOTES, topic)));
        lmsFragments.add(AppFragment.newInstance(StudentLMSFragment.class, null, getBundle(R.string.work_sheet, WORK_SHEET, topic)));

        return lmsFragments;
    }

    private Bundle getBundle(int title, @LMSCategory.LMSType int lmsType, Topic topic) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Extras.LMS_CATEGORY, new LMSCategory(getString(title), lmsType, topic));
        return bundle;
    }

    public void loadStudyMaterial(int topicId) {
        studentApiService = serviceBuilder.createService(StudentService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("academicSessionId", String.valueOf(user.getUserdetails().getAcademicSessionId()));
        params.put("masterId", String.valueOf(user.getData().getMasterId()));
        params.put("lmstopicId", String.valueOf(topicId));

        disposable = studentApiService.loadStudyMaterial(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<StudyMaterial>() {


                    @Override
                    public void onResponse(StudyMaterial response) {
                        if (response.getData() == null) {
                            onError(new RetrofitException(getString(R.string.no_result_found)));
                        } else {
                            getView().setLMSAdapter(getStudentLMSFragments(response.getData()));
                        }
                    }

                    @Override
                    public LMSPresenter getPresenter() {
                        return LMSPresenter.this;
                    }

                });
    }
}
