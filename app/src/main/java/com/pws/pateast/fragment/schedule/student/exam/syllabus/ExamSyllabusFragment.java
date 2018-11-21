package com.pws.pateast.fragment.schedule.student.exam.syllabus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pws.pateast.R;
import com.pws.pateast.api.model.Schedule;
import com.pws.pateast.base.AppDialogFragment;
import com.pws.pateast.base.Extras;
import com.pws.pateast.utils.DateUtils;

import java.text.DecimalFormat;
import java.util.Locale;

/**
 * Created by intel on 02-Sep-17.
 */

public class ExamSyllabusFragment extends AppDialogFragment implements ExamSyllabusView {

    /*private TextView tvSubject, tvExamTime, tvExamDuration, tvMinMarks, tvMaxMarks;
    private LinearLayout layoutSyllabus;
    private DecimalFormat format;*/
    private WebView webSyllabus;
    private WebSettings webSettings;
    private ExamSyllabusPresenter mPresenter;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_exam_syllabus;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);
        setTitle(R.string.title_exam_syllabus);

        /*tvSubject = (TextView) findViewById(R.id.tv_subject);
        tvExamTime = (TextView) findViewById(R.id.tv_exam_time);
        tvExamDuration = (TextView) findViewById(R.id.tv_exam_duration);
        tvMinMarks = (TextView) findViewById(R.id.tv_min_marks);
        tvMaxMarks = (TextView) findViewById(R.id.tv_max_marks);
        layoutSyllabus = (LinearLayout) findViewById(R.id.layout_syllabus);
        format = new DecimalFormat("##.##");*/

        webSyllabus = (WebView) findViewById(R.id.web_syllabus);
        webSettings = webSyllabus.getSettings();
        webSyllabus.getSettings().setLoadWithOverviewMode(true);
        webSyllabus.getSettings().setUseWideViewPort(true);
        webSettings.setDefaultFontSize(getResources().getDimensionPixelSize(R.dimen.text_size_large));

        mPresenter = new ExamSyllabusPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public Schedule getSchedule() {
        return getArguments().getParcelable(Extras.SCHEDULE);
    }

    @Override
    public void bindData(Schedule schedule) {
        /*tvSubject.setText(String.format("%s (%s)", schedule.getSubject().getSubjectdetails().get(0).getName(), schedule.getExam_type().toUpperCase(Locale.getDefault())));

        tvExamTime.setText(String.format("%s - %s to %s",
                DateUtils.toTime(DateUtils.parse(schedule.getDate(), DateUtils.DATE_FORMAT_PATTERN), "dd-MM-yyyy"),
                DateUtils.toTime(DateUtils.parse(schedule.getStart_time(), DateUtils.TIME_FORMAT_PATTERN), "hh:mm a"),
                DateUtils.toTime(DateUtils.parse(schedule.getEnd_time(), DateUtils.TIME_FORMAT_PATTERN), "hh:mm a")));
        tvExamDuration.setText(DateUtils.duration(schedule.getStart_time(), schedule.getEnd_time()));

        tvMinMarks.setText(format.format(schedule.getMin_passing_mark()));
        tvMaxMarks.setText(format.format(schedule.getMax_mark()));*/

        String head1 = "<head><style>@font-face {font-family: 'arial';src: url('file:///android_asset/Lato_Reg.ttf');}body {font-family: 'verdana';}</style></head>";
        String text = "<html>" + head1
                + "<body style=\"font-family: arial\">" + schedule.getExamsyllabuses().get(0).getSyllabus()
                + "</body></html>";

        webSyllabus.loadData(text, "text/html; charset=utf-8", "UTF-8");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
