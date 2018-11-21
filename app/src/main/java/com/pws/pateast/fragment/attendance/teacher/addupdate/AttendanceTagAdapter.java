package com.pws.pateast.fragment.attendance.teacher.addupdate;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.adapter.viewholder.BaseItemViewHolder;
import com.pws.pateast.api.model.Tag;
import com.pws.pateast.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by intel on 20-Jun-17.
 */

public class AttendanceTagAdapter extends BaseRecyclerAdapter<Tag,AttendanceTagAdapter.TagHolder>
{
    private List<String> tags;

    public AttendanceTagAdapter(Context context)
    {
        super(context);
        tags = new ArrayList<>();
    }


    @Override
    protected int getItemResourceLayout(int viewType) {
        return R.layout.item_attendance_tag;
    }

    @Override
    public TagHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TagHolder(getView(parent,viewType),mItemClickListener);
    }

    public void setTags(List<String> tags) {
        this.tags = new ArrayList<>(tags);
        notifyDataSetChanged();
    }

    public List<String> getTags() {
        return tags;
    }

    class TagHolder extends BaseItemViewHolder<Tag>
    {
        TextView tvTag;
        public TagHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView,onItemClickListener);
            tvTag = (TextView) findViewById(R.id.tv_tag);
            tvTag.setOnClickListener(this);
        }

        @Override
        public void bind(Tag tag) {
            tvTag.setText(tag.getTagdetails().get(0).getTitle());
            tvTag.setSelected(tags.contains(String.valueOf(tag.getId())));
        }
    }

}
