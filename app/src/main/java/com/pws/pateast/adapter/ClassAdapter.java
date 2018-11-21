package com.pws.pateast.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.base.ui.adapter.BaseSpinnerAdapter;
import com.base.ui.adapter.viewholder.BaseListViewHolder;
import com.pws.pateast.R;
import com.pws.pateast.api.model.TeacherClass;

/**
 * Created by intel on 25-Apr-17.
 */

public class ClassAdapter extends BaseSpinnerAdapter<TeacherClass, ClassAdapter.ClassHolder, ClassAdapter.ClassHolder> {

    public boolean showSection = true;

    public ClassAdapter(Context context) {
        super(context);
    }

    public ClassAdapter(Context context, boolean showSection) {
        super(context);
        this.showSection = showSection;
    }

    @Override
    public int getDropdownItemView() {
        return R.layout.item_spinner_dropdown;
    }

    @Override
    public ClassHolder onCreateDropdownViewHolder(View itemView) {
        return new ClassHolder(itemView);
    }

    @Override
    protected int getItemView() {
        return R.layout.item_spinner_view;
    }

    @Override
    public ClassHolder onCreateViewHolder(View itemView) {
        return new ClassHolder(itemView);
    }

    class ClassHolder extends BaseListViewHolder<TeacherClass> {
        private TextView tvSpinner;

        public ClassHolder(View itemView) {
            super(itemView);
            tvSpinner = (TextView) findViewById(R.id.tv_spinner);
        }

        @Override
        public void bind(TeacherClass classes) {
            tvSpinner.setText(getClassName(classes));
        }
    }

    public String getClassName(TeacherClass classes) {
        if (classes.getBcsMapId() == 0)
            return getString(R.string.title_all);
        if (classes.getBcsmap() != null) {
            if (showSection) {
                return getString(R.string.class_name,
                        classes.getBcsmap().getBoard().getBoarddetails().get(0).getAlias(),
                        classes.getBcsmap().getClasses().getClassesdetails().get(0).getName(),
                        classes.getBcsmap().getSection().getSectiondetails().get(0).getName());
            } else {
                return getString(R.string.class_section,
                        classes.getBcsmap().getBoard().getBoarddetails().get(0).getAlias(),
                        classes.getBcsmap().getClasses().getClassesdetails().get(0).getName());
            }
        }

        return null;
    }

    public TeacherClass getClass(int position) {
        if (position > -1)
            return getDatas().get(position);
        return new TeacherClass();
    }

}
