package com.pws.pateast.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.base.ui.adapter.BaseSpinnerAdapter;
import com.base.ui.adapter.viewholder.BaseListViewHolder;
import com.pws.pateast.api.model.Tag;
import com.pws.pateast.R;

import java.util.List;

/**
 * Created by intel on 28-Jun-17.
 */

public class SpinnerAdapter extends BaseSpinnerAdapter<Tag,SpinnerAdapter.TagHolder,SpinnerAdapter.TagHolder>
{

    public SpinnerAdapter(Context context, List<Tag> data) {
        super(context, data);
    }

    @Override
    public int getDropdownItemView() {
        return R.layout.item_spinner_dropdown;
    }

    @Override
    public TagHolder onCreateDropdownViewHolder(View itemView) {
        return new TagHolder(itemView);
    }

    @Override
    protected int getItemView() {
        return R.layout.item_spinner_view;
    }

    @Override
    public TagHolder onCreateViewHolder(View itemView) {
        return new TagHolder(itemView);
    }

    class TagHolder extends BaseListViewHolder<Tag>
    {
        private TextView tvSpinner;
        public TagHolder(View itemView)
        {
            super(itemView);
            tvSpinner = (TextView) findViewById(R.id.tv_spinner);
        }

        @Override
        public void bind(Tag tag)
        {
            tvSpinner.setText(tag.getTagdetails().get(0).getTitle());
        }
    }
}
