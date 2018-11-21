package com.pws.pateast.fragment.lms;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.pws.pateast.api.model.Topic;
import com.pws.pateast.base.AppView;
import com.pws.pateast.enums.LMSCategory;

import java.util.Arrays;
import java.util.List;

/**
 * Created by intel on 24-Jan-18.
 */

public interface StudentLMSView extends AppView, BaseRecyclerAdapter.OnItemClickListener {
    List<String> VIDEO_MATERIAL = Arrays.asList(".mp4", ".avi", ".wmv", ".mov", ".webm", ".pps");
    List<String> PPT_MATERIAL = Arrays.asList(".ppt", ".pptx");
    List<String> IMAGE_NOTES_MATERIAL = Arrays.asList(".png", ".jpg", ".jpeg", ".gif");
    List<String> PDF_NOTES_MATERIAL = Arrays.asList(".pdf");
    List<String> DOC_NOTES_MATERIAL = Arrays.asList(".xls", ".xlsx", ".doc", ".docx", ".txt");

    boolean isVisible();

    LMSCategory getLmsCategory();

    @LMSCategory.LMSType
    int getLmsType();

    Topic getTopic();

    void setStudyMaterialAdapter(@LMSCategory.LMSType int lmsType, List<Topic> studyMaterial);
}
