package com.pws.pateast.fragment.lms;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.adapter.viewholder.BaseItemViewHolder;
import com.pws.pateast.R;
import com.pws.pateast.api.model.Topic;
import com.pws.pateast.enums.LMSCategory;

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
 * Created by intel on 25-Jan-18.
 */

public class StudentLMSAdapter extends BaseRecyclerAdapter<Topic, StudentLMSAdapter.TopicHolder> {
    @LMSCategory.LMSType
    int lmsType;


    public StudentLMSAdapter(Context context, OnItemClickListener onItemClickListener, @LMSCategory.LMSType int lmsType) {
        super(context, onItemClickListener);
        this.lmsType = lmsType;
    }

    @Override
    protected int getItemResourceLayout(int viewType) {
        switch (viewType) {
            case VIDEO:
                return R.layout.item_lms_video;
            case PPT:
                return R.layout.item_lms_video;
            case NOTES:
                return R.layout.item_lms_video;
            case WORK_SHEET:
                return R.layout.item_lms_work_sheet;
        }
        return R.layout.item_lms_video;
    }

    @Override
    public TopicHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIDEO:
                return new VideoHolder(getView(parent, viewType), mItemClickListener);
            case PPT:
                return new PresentationHolder(getView(parent, viewType), mItemClickListener);
            case NOTES:
                return new NotesHolder(getView(parent, viewType), mItemClickListener);
            case WORK_SHEET:
                return new WorkSheetHolder(getView(parent, viewType), mItemClickListener);
        }
        return new TopicHolder(getView(parent, viewType), mItemClickListener);
    }


    class VideoHolder extends TopicHolder {

        public VideoHolder(View view, OnItemClickListener onItemClickListener) {
            super(view, onItemClickListener);
        }

        @Override
        public void bind(Topic topic) {
            super.bind(topic);
            tvMaterial.setText(topic.getName());
            layoutMaterial.setOnClickListener(this);
            setMaterialType(topic.getType());
        }
    }

    class PresentationHolder extends TopicHolder {

        public PresentationHolder(View view, OnItemClickListener onItemClickListener) {
            super(view, onItemClickListener);
        }

        @Override
        public void bind(Topic topic) {
            super.bind(topic);
            tvMaterial.setText(topic.getName());
            layoutMaterial.setOnClickListener(this);
            setMaterialType(topic.getType());
        }
    }

    class NotesHolder extends TopicHolder {

        public NotesHolder(View view, OnItemClickListener onItemClickListener) {
            super(view, onItemClickListener);
        }

        @Override
        public void bind(Topic topic) {
            super.bind(topic);
            tvMaterial.setText(topic.getName());
            layoutMaterial.setOnClickListener(this);
            setMaterialType(topic.getType());
        }
    }

    class WorkSheetHolder extends TopicHolder {
        public WorkSheetHolder(View view, OnItemClickListener onItemClickListener) {
            super(view, onItemClickListener);
        }

        @Override
        public void bind(Topic topic) {
            super.bind(topic);
            tvMaterial.setText(Html.fromHtml(topic.getContent()));
        }
    }

    class TopicHolder extends BaseItemViewHolder<Topic> {
        protected ImageView imgMaterial;
        protected TextView tvMaterial;
        protected LinearLayout layoutMaterial;

        public TopicHolder(View view, OnItemClickListener onItemClickListener) {
            super(view, onItemClickListener);
            imgMaterial = (ImageView) findViewById(R.id.img_material);
            tvMaterial = (TextView) findViewById(R.id.tv_material);
            layoutMaterial = (LinearLayout) findViewById(R.id.layout_material);
        }

        @Override
        public void bind(Topic topic) {

        }

        protected void setMaterialType(String type) {
            if (VIDEO_MATERIAL.contains(type)) {
                imgMaterial.setImageResource(R.drawable.ic_video_message);
            } else if (PPT_MATERIAL.contains(type)) {
                imgMaterial.setImageResource(R.drawable.ic_ppt_message);
            } else if (IMAGE_NOTES_MATERIAL.contains(type)) {
                imgMaterial.setImageResource(R.drawable.ic_image_message);
            } else if (PDF_NOTES_MATERIAL.contains(type)) {
                imgMaterial.setImageResource(R.drawable.ic_pdf_message);
            } else if (DOC_NOTES_MATERIAL.contains(type)) {
                imgMaterial.setImageResource(R.drawable.ic_file_message);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return lmsType;
    }
}
