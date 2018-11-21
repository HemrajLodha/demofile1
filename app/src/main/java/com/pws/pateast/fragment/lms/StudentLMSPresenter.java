package com.pws.pateast.fragment.lms;

import com.pws.pateast.R;
import com.pws.pateast.api.exception.RetrofitException;
import com.pws.pateast.api.model.Topic;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.base.AppPresenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.pws.pateast.api.exception.RetrofitException.ERROR_TYPE_MESSAGE;
import static com.pws.pateast.enums.LMSCategory.LMSType.NOTES;
import static com.pws.pateast.enums.LMSCategory.LMSType.PPT;
import static com.pws.pateast.enums.LMSCategory.LMSType.VIDEO;
import static com.pws.pateast.enums.LMSCategory.LMSType.WORK_SHEET;
import static com.pws.pateast.fragment.lms.StudentLMSView.DOC_NOTES_MATERIAL;
import static com.pws.pateast.fragment.lms.StudentLMSView.IMAGE_NOTES_MATERIAL;
import static com.pws.pateast.fragment.lms.StudentLMSView.PDF_NOTES_MATERIAL;
import static com.pws.pateast.fragment.lms.StudentLMSView.PPT_MATERIAL;
import static com.pws.pateast.fragment.lms.StudentLMSView.VIDEO_MATERIAL;

/**
 * Created by intel on 24-Jan-18.
 */

public class StudentLMSPresenter extends AppPresenter<StudentLMSView> {
    private StudentLMSView lmsView;

    @Override
    public StudentLMSView getView() {
        return lmsView;
    }

    @Override
    public void attachView(StudentLMSView view) {
        lmsView = view;
    }

    public void loadStudyMaterial() {
        List<Topic> studyMaterial = new ArrayList<>();
        int lmsType = getView().getLmsType();
        List<Topic> topics = getView().getTopic().getLmstopicdocuments();
        switch (lmsType) {
            case VIDEO:
                for (Topic topic : topics) {
                    if (VIDEO_MATERIAL.contains(topic.getType())) {
                        studyMaterial.add(topic);
                    }
                }
                break;
            case PPT:
                for (Topic topic : topics) {
                    if (PPT_MATERIAL.contains(topic.getType())) {
                        studyMaterial.add(topic);
                    }
                }
                break;
            case NOTES:
                for (Topic topic : topics) {
                    if (IMAGE_NOTES_MATERIAL.contains(topic.getType()) || PDF_NOTES_MATERIAL.contains(topic.getType()) || DOC_NOTES_MATERIAL.contains(topic.getType())) {
                        studyMaterial.add(topic);
                    }
                }
                break;
            case WORK_SHEET:
                for (Topic topic : getView().getTopic().getLmstopicdetails()) {
                    studyMaterial.add(topic);
                }
                break;
        }
        Topic topic = new Topic(lmsType, studyMaterial);
        topic.setStatus(!studyMaterial.isEmpty());
        topic.setMessage(getString(R.string.no_result_found));
        observer.onResponse(topic);
    }

    private AppSingleObserver<Topic> observer = new AppSingleObserver<Topic>() {

        @Override
        public void onResponse(Topic response) {
            if (response.isStatus()) {
                hide();
                getView().setStudyMaterialAdapter(response.getLmsType(), response.getLmstopicdocuments());
            } else {
                onError(new RetrofitException(response.getMessage(), ERROR_TYPE_MESSAGE));
            }
        }

        @Override
        public StudentLMSPresenter getPresenter() {
            return StudentLMSPresenter.this;
        }
    };

}
