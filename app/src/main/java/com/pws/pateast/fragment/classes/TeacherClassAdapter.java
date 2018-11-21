package com.pws.pateast.fragment.classes;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.adapter.viewholder.BaseItemViewHolder;
import com.pws.pateast.R;
import com.pws.pateast.api.model.TeacherClass;

/**
 * Created by intel on 21-Apr-17.
 */

public class TeacherClassAdapter extends BaseRecyclerAdapter<TeacherClass, TeacherClassAdapter.ViewHolder>
{

    public TeacherClassAdapter(Context context)
    {
        super(context);
    }

    @Override
    protected int getItemResourceLayout(int viewType) {
        return R.layout.item_teacher_classes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getView(parent, viewType),mItemClickListener);
    }

    class ViewHolder extends BaseItemViewHolder<TeacherClass>
    {
        private TextView tvClass,tvClassTeacher,tvStudents,tvOptions;

        public ViewHolder(View view,OnItemClickListener onItemClickListener) {
            super(view,onItemClickListener);
            tvClass = (TextView) findViewById(R.id.tv_class);
            tvStudents = (TextView) findViewById(R.id.tv_student);
            tvClassTeacher = (TextView) findViewById(R.id.tv_class_teacher);
            tvOptions = (TextView) findViewById(R.id.tv_options);
            tvOptions.setOnClickListener(this);
        }

        @Override
        public void bind(TeacherClass classes)
        {
            tvClass.setText(getString(R.string.class_name,classes.getBcsmap().getBoard().getBoarddetails().get(0).getAlias(),classes.getBcsmap().getClasses().getClassesdetails().get(0).getName(),classes.getBcsmap().getSection().getSectiondetails().get(0).getName()));
            tvStudents.setText(String.valueOf(classes.getStudentrecord()));
            tvClassTeacher.setText(classes.getTeacher().getUser().getUserdetails().get(0).getFullname());
        }
    }
}
