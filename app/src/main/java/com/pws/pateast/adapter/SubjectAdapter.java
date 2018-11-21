package com.pws.pateast.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.base.ui.adapter.BaseSpinnerAdapter;
import com.base.ui.adapter.viewholder.BaseListViewHolder;
import com.pws.pateast.R;
import com.pws.pateast.api.model.Subject;

import java.util.List;

/**
 * Created by intel on 27-Apr-17.
 */

public class SubjectAdapter extends BaseSpinnerAdapter<Subject, SubjectAdapter.SubjectHolder, SubjectAdapter.SubjectHolder> {
    public SubjectAdapter(Context context) {
        super(context);
    }

    public SubjectAdapter(Context context, List<Subject> data) {
        super(context, data);
    }

    @Override
    public int getDropdownItemView() {
        return R.layout.item_spinner_dropdown;
    }

    @Override
    public SubjectHolder onCreateDropdownViewHolder(View itemView) {
        return new SubjectHolder(itemView);
    }

    @Override
    protected int getItemView() {
        return R.layout.item_spinner_view;
    }

    @Override
    public SubjectHolder onCreateViewHolder(View itemView) {
        return new SubjectHolder(itemView);
    }

    class SubjectHolder extends BaseListViewHolder<Subject> {
        private TextView tvSpinner;

        public SubjectHolder(View itemView) {
            super(itemView);
            tvSpinner = (TextView) findViewById(R.id.tv_spinner);
        }

        @Override
        public void bind(Subject subject) {
            tvSpinner.setText(getSubjectName(subject));
        }
    }

    public String getSubjectName(Subject subject) {
        if (subject.getSubject() != null)
            return subject.getSubject().getSubjectdetails().get(0).getName();
        return null;
    }

    public Subject getSubject(int position) {
        if (position > -1)
            return getDatas().get(position);
        return null;
    }

}
