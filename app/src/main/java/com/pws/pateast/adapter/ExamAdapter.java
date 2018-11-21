package com.pws.pateast.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.base.ui.adapter.BaseSpinnerAdapter;
import com.base.ui.adapter.viewholder.BaseListViewHolder;
import com.pws.pateast.R;
import com.pws.pateast.api.model.Schedule;
import com.pws.pateast.api.model.TeacherClass;

/**
 * Created by intel on 01-Sep-17.
 */

public class ExamAdapter extends BaseSpinnerAdapter<Schedule,ExamAdapter.ExamHolder,ExamAdapter.ExamHolder>
{

    public ExamAdapter(Context context)
    {
        super(context);
    }

    @Override
    public int getDropdownItemView() {
        return R.layout.item_spinner_dropdown;
    }

    @Override
    public ExamHolder onCreateDropdownViewHolder(View itemView) {
        return new ExamHolder(itemView);
    }

    @Override
    protected int getItemView() {
        return R.layout.item_spinner_view;
    }

    @Override
    public ExamHolder onCreateViewHolder(View itemView) {
        return new ExamHolder(itemView);
    }

    class ExamHolder extends BaseListViewHolder<Schedule>
    {
        private TextView tvSpinner;
        public ExamHolder(View itemView)
        {
            super(itemView);
            tvSpinner = (TextView) findViewById(R.id.tv_spinner);
        }

        @Override
        public void bind(Schedule schedule)
        {
            tvSpinner.setText(getExamName(schedule));
        }
    }

    public String getExamName(Schedule schedule)
    {
        return schedule.getExamhead().getExamheaddetails().get(0).getName();
    }

}
