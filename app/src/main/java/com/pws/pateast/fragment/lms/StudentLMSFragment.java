package com.pws.pateast.fragment.lms;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.pws.pateast.R;
import com.pws.pateast.activity.lms.content.ImageContentActivity;
import com.pws.pateast.activity.lms.content.WebContentActivity;
import com.pws.pateast.activity.lms.video.VideoActivity;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.model.Topic;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.base.Extras;
import com.pws.pateast.enums.LMSCategory;
import com.pws.pateast.utils.NetworkUtil;

import java.util.List;

import static com.pws.pateast.Constants.GOOGLE_DRIVE;
import static com.pws.pateast.enums.LMSCategory.LMSType.WORK_SHEET;

/**
 * Created by intel on 24-Jan-18.
 */

public class StudentLMSFragment extends AppFragment implements StudentLMSView {
    private RecyclerView rvStudyMaterial;
    private GridLayoutManager glm;
    private StudentLMSAdapter mAdapter;
    private StudentLMSPresenter mPresenter;

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_student_lms;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        rvStudyMaterial = (RecyclerView) findViewById(R.id.rv_study_material);
        rvStudyMaterial.setHasFixedSize(true);

        mPresenter = new StudentLMSPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void onActionClick() {
        super.onActionClick();
        mPresenter.loadStudyMaterial();
    }

    @Override
    public LMSCategory getLmsCategory() {
        return getArguments().getParcelable(Extras.LMS_CATEGORY);
    }

    @LMSCategory.LMSType
    @Override
    public int getLmsType() {
        return getLmsCategory().getLmsType();
    }

    @Override
    public Topic getTopic() {
        return getLmsCategory().getTopic();
    }

    @Override
    public void setStudyMaterialAdapter(int lmsType, List<Topic> studyMaterial) {
        if (rvStudyMaterial.getAdapter() == null) {
            glm = new GridLayoutManager(getContext(), lmsType == WORK_SHEET ? 1 : 2);
            mAdapter = new StudentLMSAdapter(getContext(), this, lmsType);
            rvStudyMaterial.setLayoutManager(glm);
            rvStudyMaterial.setAdapter(mAdapter);
        }

        mAdapter.update(studyMaterial);
    }

    @Override
    public void onItemClick(View view, int position) {
        if (mAdapter == null)
            return;
        if (!NetworkUtil.isOnline(getContext())) {
            showMessage(getString(R.string.no_network), true, R.attr.colorPrimary);
            return;
        }
        Topic topic = mAdapter.getItem(position);
        switch (view.getId()) {
            case R.id.layout_material:
                Bundle bundle = new Bundle();
                bundle.putString(Extras.TITLE, topic.getName());
                String url = ServiceBuilder.IMAGE_URL + topic.getPath();
                switch (getLmsType()) {
                    case LMSCategory.LMSType.VIDEO:
                        getAppListener().openActivity(VideoActivity.class, bundle, Uri.parse(url));
                        break;
                    case LMSCategory.LMSType.PPT:
                    case LMSCategory.LMSType.NOTES:
                        if (DOC_NOTES_MATERIAL.contains(topic.getType()) || PDF_NOTES_MATERIAL.contains(topic.getType()) || PPT_MATERIAL.contains(topic.getType())) {
                            url = GOOGLE_DRIVE + url;
                            getAppListener().openActivity(WebContentActivity.class, bundle, Uri.parse(url));
                        } else if (IMAGE_NOTES_MATERIAL.contains(topic.getType())) {
                            getAppListener().openActivity(ImageContentActivity.class, bundle, Uri.parse(url));
                        }
                        break;
                }
                break;
        }
    }
}
