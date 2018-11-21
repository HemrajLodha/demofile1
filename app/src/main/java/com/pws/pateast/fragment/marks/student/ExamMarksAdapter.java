package com.pws.pateast.fragment.marks.student;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.adapter.viewholder.BaseItemViewHolder;
import com.pws.pateast.R;
import com.pws.pateast.api.model.ExamMarks;

import java.text.DecimalFormat;

/**
 * Created by intel on 17-May-17.
 */

public class ExamMarksAdapter extends BaseRecyclerAdapter<ExamMarks, ExamMarksAdapter.MarksHolder> {
    private final int HEADER = 0;
    private final int ITEM = 1;

    public ExamMarksAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemResourceLayout(int viewType) {
        return R.layout.item_exam_marks;
    }

    @Override
    public MarksHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case HEADER:
                return new HeaderHolder(getView(parent, viewType));
            default:
                return new MarksHolder(getView(parent, viewType));
        }
    }

    class HeaderHolder extends MarksHolder {

        public HeaderHolder(View view) {
            super(view);
            cardMarks.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.md_grey_100));
            tvSubject.setTextColor(ContextCompat.getColor(getContext(), R.color.teacher_class_schedule_form_green));
            tvExamMarks.setTextColor(ContextCompat.getColor(getContext(), R.color.teacher_class_schedule_form_green));

            tvSubject.setText(R.string.title_subjects);
            tvExamMarks.setText(R.string.title_marks_grade);
        }

        @Override
        public void bind(ExamMarks marks) {

        }
    }

    class MarksHolder extends BaseItemViewHolder<ExamMarks> {
        protected CardView cardMarks;
        protected TextView tvSubject, tvExamMarks/*, tvResult*/;
        private DecimalFormat format;

        public MarksHolder(View view) {
            super(view);
            cardMarks = (CardView) findViewById(R.id.card_marks);
            tvSubject = (TextView) findViewById(R.id.tv_subject);
            tvExamMarks = (TextView) findViewById(R.id.tv_exam_marks);
            format = new DecimalFormat("##.##");

            cardMarks.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
            tvSubject.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor));
            tvExamMarks.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor));
        }

        @Override
        public void bind(ExamMarks marks) {
            tvSubject.setText(String.format("%s (%s)", marks.getSubject().getSubjectdetails().get(0).getName(), marks.getExam_type().toUpperCase()));
            double obtainMarks = marks.getMarkrecords().get(0).getObtained_mark();
            double maxMarks = marks.getMax_mark();
            if (obtainMarks != -1)
                tvExamMarks.setText(String.format("%s/%s", format.format(obtainMarks), format.format(maxMarks)));
            else
                tvExamMarks.setText(R.string.leave_status_absent);
        }
    }

    @Override
    public ExamMarks getItem(int position) {
        return super.getItem(position - 1);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return HEADER;
        return ITEM;
    }
}
