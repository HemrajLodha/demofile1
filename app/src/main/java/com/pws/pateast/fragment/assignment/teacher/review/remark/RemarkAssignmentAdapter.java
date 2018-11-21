package com.pws.pateast.fragment.assignment.teacher.review.remark;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.adapter.viewholder.BaseItemViewHolder;
import com.pws.pateast.R;
import com.pws.pateast.api.model.Tag;

/**
 * Created by intel on 07-Sep-17.
 */

public class RemarkAssignmentAdapter extends BaseRecyclerAdapter<Tag, RemarkAssignmentAdapter.TagHolder> {
    private Tag tag;
    private View tagView;

    public RemarkAssignmentAdapter(Context context) {
        super(context);
        //setTag(new Tag(0));
    }


    @Override
    protected int getItemResourceLayout(int viewType) {
        return R.layout.item_attendance_tag;
    }

    @Override
    public TagHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TagHolder(getView(parent, viewType), mItemClickListener);
    }

    public void setTag(Tag tag) {
        this.tag = tag;
        notifyDataSetChanged();
    }

    public Tag getTag() {
        return tag;
    }

    class TagHolder extends BaseItemViewHolder<Tag> {
        TextView tvTag;

        public TagHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView, onItemClickListener);
            tvTag = (TextView) findViewById(R.id.tv_tag);
            tvTag.setOnClickListener(this);
        }

        @Override
        public void bind(Tag tag) {
            tvTag.setText(tag.getTagdetails().get(0).getTitle());
            tvTag.setSelected(getTag() != null && getTag().getId() == tag.getId());
        }
    }

}
