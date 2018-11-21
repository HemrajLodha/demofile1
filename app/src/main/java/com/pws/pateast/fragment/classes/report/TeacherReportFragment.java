package com.pws.pateast.fragment.classes.report;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pws.pateast.R;
import com.pws.pateast.api.model.Schedule;
import com.pws.pateast.api.model.TeacherClass;
import com.pws.pateast.api.model.TeacherReport;
import com.pws.pateast.base.AppDialogFragment;
import com.pws.pateast.base.Extras;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by intel on 04-Jul-17.
 */

public class TeacherReportFragment extends AppDialogFragment implements TeacherReportView, View.OnClickListener {
    private Button btnSave;
    private TextView tvClass, tvTime, tvSubject;
    private EditText etReport;
    private TextInputLayout tilReport;
    private DateFormat format1, format2;

    private TeacherReportPresenter reportPresenter;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_teacher_report;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);
        setTitle(R.string.dialog_menu_daily_report);

        btnSave = (Button) findViewById(R.id.btn_save);
        etReport = (EditText) findViewById(R.id.et_report);
        tilReport = (TextInputLayout) findViewById(R.id.til_report);
        tvClass = (TextView) findViewById(R.id.tv_class);

        tvTime = (TextView) findViewById(R.id.tv_time);
        tvSubject = (TextView) findViewById(R.id.tv_subject);

        format1 = new SimpleDateFormat("HH:mm:ss");
        format2 = new SimpleDateFormat("hh:mm a");

        btnSave.setOnClickListener(this);

        reportPresenter = new TeacherReportPresenter();
        reportPresenter.attachView(this);
        reportPresenter.bind();
        reportPresenter.getClassReport();
    }

    @Override
    public TeacherClass getTeacherClass() {
        return getArguments().getParcelable(Extras.TEACHER_CLASS);
    }

    @Override
    public Schedule getSchedule() {
        return getArguments().getParcelable(Extras.SCHEDULE);
    }

    @Override
    public String getDate() {
        return getArguments().getString(Extras.DATE);
    }

    @Override
    public String getTime() {
        return tvTime.getText().toString();
    }

    @Override
    public void setReportId(int reportId) {
        getArguments().putInt(Extras.TEACHER_REPORT_ID, reportId);
    }

    @Override
    public int getReportId() {
        return getArguments().getInt(Extras.TEACHER_REPORT_ID, 0);
    }

    @Override
    public int getOrder() {
        return getSchedule().getOrder();
    }

    @Override
    public void setError(int id, String error) {
        switch (id) {
            case R.id.til_report:
                tilReport.setError(error);
                tilReport.setErrorEnabled(error != null);
                break;
        }
    }

    @Override
    public String getContent() {
        return etReport.getText().toString().trim();
    }

    @Override
    public void bind(TeacherClass classes, Schedule schedule) {
        tvClass.setText(getString(R.string.class_name, classes.getBcsmap().getBoard().getBoarddetails().get(0).getAlias(), classes.getBcsmap().getClasses().getClassesdetails().get(0).getName(), classes.getBcsmap().getSection().getSectiondetails().get(0).getName()));
        tvTime.setText(schedule.getTime(format1, format2));
        tvSubject.setText(schedule.getSubject().getSubjectdetails().get(0).getName());
    }

    @Override
    public void setTeacherReport(TeacherReport report) {
        if (report != null) {
            setReportId(report.getId());
            etReport.setText(Html.fromHtml(report.getContent()));
            etReport.setEnabled(report.getIs_locked() == 0);
            btnSave.setEnabled(report.getIs_locked() == 0);
            if (!etReport.isEnabled())
                setError(R.id.til_report, getString(R.string.error_teacher_report_locked));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                if (reportPresenter != null)
                    reportPresenter.saveClassReport();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (reportPresenter != null)
            reportPresenter.detachView();
    }


}
