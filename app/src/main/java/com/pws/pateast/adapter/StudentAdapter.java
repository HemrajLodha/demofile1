package com.pws.pateast.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.base.ui.adapter.BaseSpinnerAdapter;
import com.base.ui.adapter.viewholder.BaseListViewHolder;
import com.pws.pateast.api.model.Student;
import com.pws.pateast.R;

/**
 * Created by intel on 10-May-17.
 */

public class StudentAdapter extends BaseSpinnerAdapter<Student,StudentAdapter.StudentHolder,StudentAdapter.StudentHolder>
{
    public StudentAdapter(Context context)
    {
        super(context);
    }

    @Override
    public int getDropdownItemView() {
        return R.layout.item_spinner_dropdown;
    }

    @Override
    public StudentHolder onCreateDropdownViewHolder(View itemView) {
        return new StudentHolder(itemView);
    }

    @Override
    protected int getItemView() {
        return R.layout.item_spinner_view;
    }

    @Override
    public StudentHolder onCreateViewHolder(View itemView) {
        return new StudentHolder(itemView);
    }

    class StudentHolder extends BaseListViewHolder<Student>
    {
        private TextView tvSpinner;
        public StudentHolder(View itemView)
        {
            super(itemView);
            tvSpinner = (TextView) findViewById(R.id.tv_spinner);
        }

        @Override
        public void bind(Student student)
        {
            tvSpinner.setText(getStudentName(student));
        }
    }

    public String getStudentName(Student student)
    {
        return student.getStudent().getUser().getUserdetails().get(0).getFullname();
    }
}
