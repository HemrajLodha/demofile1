package com.pws.pateast.fragment.leave.teacher.apply;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.base.ui.adapter.BaseSpinnerAdapter;
import com.base.ui.adapter.viewholder.BaseListViewHolder;
import com.pws.pateast.api.model.LeaveType;
import com.pws.pateast.api.model.Tag;
import com.pws.pateast.R;

/**
 * Created by intel on 28-Jun-17.
 */

public class LeaveTypeAdapter extends BaseSpinnerAdapter<LeaveType,LeaveTypeAdapter.TagHolder,LeaveTypeAdapter.TagHolder>
{
    public LeaveTypeAdapter(Context context)
    {
        super(context);
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

    class TagHolder extends BaseListViewHolder<LeaveType>
    {
        private TextView tvSpinner;
        public TagHolder(View itemView)
        {
            super(itemView);
            tvSpinner = (TextView) findViewById(R.id.tv_spinner);
        }

        @Override
        public void bind(LeaveType type)
        {
            tvSpinner.setText(type.getEmpleavetypedetails().get(0).getName());
        }
    }
}
