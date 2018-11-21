package com.pws.pateast.fragment.classes;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.adapter.viewholder.BaseItemViewHolder;
import com.pws.pateast.R;
import com.pws.pateast.api.model.Schedule;
import com.pws.pateast.api.model.TeacherClass;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by intel on 25-May-17.
 */

public class TeacherScheduleAdapter extends BaseRecyclerAdapter<TeacherClass, BaseItemViewHolder> {
    private TeacherClassView classView;

    public TeacherScheduleAdapter(Context context) {
        super(context);
    }

    public void attach(TeacherClassView classView) {
        this.classView = classView;
    }

    @Override
    protected int getItemResourceLayout(int viewType) {
        if (classView.isClassAttendance())
            return R.layout.item_teacher_attendance;
        else
            return R.layout.item_teacher_classes;
    }

    @Override
    public BaseItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (classView.isClassAttendance())
            return new AttendanceHolder(getView(parent, viewType), mItemClickListener);
        else
            return new ClassHolder(getView(parent, viewType), mItemClickListener);
    }

    class ClassHolder extends BaseItemViewHolder<TeacherClass> {
        private TextView tvClass, tvClassTeacher, tvStudents;
        private TextView tvTime, tvSubject;
        private TextView tvAssignment, tvAttendance, tvDailyReport;
        private LinearLayout layoutOptions;
        private DateFormat format1, format2;

        public ClassHolder(View view, OnItemClickListener onItemClickListener) {
            super(view, onItemClickListener);
            tvClass = (TextView) findViewById(R.id.tv_class);
            tvStudents = (TextView) findViewById(R.id.tv_student);
            tvClassTeacher = (TextView) findViewById(R.id.tv_class_teacher);
            tvTime = (TextView) findViewById(R.id.tv_time);
            tvSubject = (TextView) findViewById(R.id.tv_subject);
            tvAssignment = (TextView) findViewById(R.id.tv_assignment);
            tvAttendance = (TextView) findViewById(R.id.tv_attendance);
            tvDailyReport = (TextView) findViewById(R.id.tv_daily_report);
            layoutOptions = (LinearLayout) findViewById(R.id.layout_options);

            format1 = new SimpleDateFormat("HH:mm:ss");
            format2 = new SimpleDateFormat("hh:mm a");


        }

        @Override
        public void bind(TeacherClass classes) {
            layoutOptions.setVisibility(classView.isFutureDay() ? View.GONE : View.VISIBLE);
            itemView.setOnClickListener(classView.isFutureDay() ? null : this);
            tvAttendance.setOnClickListener(classView.isFutureDay() ? null : this);
            tvDailyReport.setOnClickListener(classView.isFutureDay() ? null : this);
            tvAssignment.setOnClickListener(classView.isFutureDay() ? null : this);
            //layoutOptions.setVisibility(classes.isOpened() ? View.VISIBLE : View.GONE);

            tvClass.setText(getString(R.string.class_name, classes.getBcsmap().getBoard().getBoarddetails().get(0).getAlias(), classes.getBcsmap().getClasses().getClassesdetails().get(0).getName(), classes.getBcsmap().getSection().getSectiondetails().get(0).getName()));
            tvStudents.setText(String.valueOf(classes.getStudentrecord()));
            tvClassTeacher.setText(classes.getTeacher().getUser().getUserdetails().get(0).getFullname());
            bind(classes.getTimetable());
        }

        public void bind(Schedule schedule) {
            tvTime.setText(schedule.getTime(format1, format2));
            tvSubject.setText(schedule.getSubject().getSubjectdetails().get(0).getName());
        }
    }

    class AttendanceHolder extends BaseItemViewHolder<TeacherClass> {
        private TextView tvClass, tvStudents;
        private TextView tvTime, tvSubject;
        private TextView tvTakeAttendance, tvReport;
        private LinearLayout layoutOptions;
        private DateFormat format1, format2;

        public AttendanceHolder(View view, OnItemClickListener onItemClickListener) {
            super(view, onItemClickListener);
            tvClass = (TextView) findViewById(R.id.tv_class);
            tvStudents = (TextView) findViewById(R.id.tv_student);
            tvTime = (TextView) findViewById(R.id.tv_time);
            tvSubject = (TextView) findViewById(R.id.tv_subject);
            tvTakeAttendance = (TextView) findViewById(R.id.tv_take_attendance);
            tvReport = (TextView) findViewById(R.id.tv_report);
            layoutOptions = (LinearLayout) findViewById(R.id.layout_options);

            format1 = new SimpleDateFormat("HH:mm:ss");
            format2 = new SimpleDateFormat("hh:mm a");


        }

        @Override
        public void bind(TeacherClass classes) {
            layoutOptions.setVisibility(classView.isFutureDay() ? View.GONE : View.VISIBLE);
            itemView.setOnClickListener(classView.isFutureDay() ? null : this);
            tvTakeAttendance.setOnClickListener(classView.isFutureDay() ? null : this);
            tvReport.setOnClickListener(classView.isFutureDay() ? null : this);
            //layoutOptions.setVisibility(classes.isOpened() ? View.VISIBLE : View.GONE);
            tvClass.setText(getString(R.string.class_name, classes.getBcsmap().getBoard().getBoarddetails().get(0).getAlias(), classes.getBcsmap().getClasses().getClassesdetails().get(0).getName(), classes.getBcsmap().getSection().getSectiondetails().get(0).getName()));
            tvStudents.setText(String.valueOf(classes.getStudentrecord()));
            bind(classes.getTimetable());
        }

        public void bind(Schedule schedule) {
            tvTime.setText(schedule.getTime(format1, format2));
            tvSubject.setText(schedule.getSubject().getSubjectdetails().get(0).getName());
        }
    }

}
