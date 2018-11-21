package com.pws.pateast.fragment.schedule.student.exam;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.adapter.viewholder.BaseItemViewHolder;
import com.pws.pateast.R;
import com.pws.pateast.api.model.Schedule;
import com.pws.pateast.enums.TaskType;
import com.pws.pateast.utils.DateUtils;

import java.text.DecimalFormat;

/**
 * Created by intel on 16-May-17.
 */

public class ExamScheduleAdapter extends BaseRecyclerAdapter<Schedule, ExamScheduleAdapter.ScheduleHolder> {

    private final Context context;
    private final TaskType taskType;

    public ExamScheduleAdapter(Context context, TaskType taskType) {
        super(context);
        this.context = context;
        this.taskType = taskType;
    }


    @Override
    protected int getItemResourceLayout(int viewType) {
        switch (this.taskType) {
            case STUDENT_EXAM_SCHEDULE:
            case WARD_EXAM_SCHEDULE:
                return R.layout.item_student_exam_schedule;
            case TEACHER_EXAM_SCHEDULE:
            default:
                return R.layout.item_exam_schedule;
        }
    }

    @Override
    public ScheduleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ScheduleHolder(getView(parent, viewType), mItemClickListener);
    }

    class ScheduleHolder extends BaseItemViewHolder<Schedule> {
        private TextView tvSubject, tvExamDate, tvExamTime, tvMinMarks, tvMaxMarks;
        private LinearLayout layoutSyllabus;
        private DecimalFormat format;

        public ScheduleHolder(View view, OnItemClickListener onItemClickListener) {
            super(view, onItemClickListener);
            tvSubject = (TextView) findViewById(R.id.tv_subject);
            tvExamDate = (TextView) findViewById(R.id.tv_exam_date);
            tvExamTime = (TextView) findViewById(R.id.tv_exam_time);
            tvMinMarks = (TextView) findViewById(R.id.tv_min_marks);
            tvMaxMarks = (TextView) findViewById(R.id.tv_max_marks);
            layoutSyllabus = (LinearLayout) findViewById(R.id.layout_syllabus);
            format = new DecimalFormat("##.##");
            layoutSyllabus.setOnClickListener(this);
        }

        @Override
        public void bind(Schedule schedule) {
            String examType;
            if (schedule.getExam_type().equalsIgnoreCase("theory"))
                examType = getString(R.string.title_theory);
            else
                examType = getString(R.string.title_practical);

            tvSubject.setText(String.format("%s (%s)", schedule.getSubject().getSubjectdetails().get(0).getName(), examType));

            tvExamDate.setText(String.format("%s",
                    DateUtils.toTime(DateUtils.parse(schedule.getDate(), DateUtils.DATE_FORMAT_PATTERN), "dd-MM-yyyy")));

            tvExamTime.setText(String.format("%s to %s",
                    DateUtils.toTime(DateUtils.parse(schedule.getStart_time(), DateUtils.TIME_FORMAT_PATTERN), "hh:mm a"),
                    DateUtils.toTime(DateUtils.parse(schedule.getEnd_time(), DateUtils.TIME_FORMAT_PATTERN), "hh:mm a")));

            tvMinMarks.setText(format.format(schedule.getMin_passing_mark()));
            tvMaxMarks.setText(format.format(schedule.getMax_mark()));

            layoutSyllabus.setVisibility(schedule.getExamsyllabuses().isEmpty() ? View.GONE : View.VISIBLE);
        }
    }

}
