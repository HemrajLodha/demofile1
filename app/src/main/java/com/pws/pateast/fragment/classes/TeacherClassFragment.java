package com.pws.pateast.fragment.classes;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.view.BaseRecyclerView;
import com.pws.pateast.R;
import com.pws.pateast.activity.assignment.AssignmentStatusActivity;
import com.pws.pateast.activity.tasks.TeacherTaskActivity;
import com.pws.pateast.api.model.Schedule;
import com.pws.pateast.api.model.TeacherClass;
import com.pws.pateast.base.AppDialogFragment;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.base.Extras;
import com.pws.pateast.base.FragmentActivity;
import com.pws.pateast.enums.TaskType;
import com.pws.pateast.fragment.classes.report.TeacherReportFragment;
import com.pws.pateast.listener.OnDateChangeListener;
import com.pws.pateast.utils.DateUtils;
import com.pws.pateast.widget.SpaceItemDecoration;
import com.pws.pateast.widget.datepickerview.DatePickerEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by intel on 20-Apr-17.
 */

public class TeacherClassFragment extends AppFragment implements TeacherClassView, BaseRecyclerAdapter.OnItemClickListener, View.OnClickListener, OnDateChangeListener {
    private BaseRecyclerView rvClasses;

    private TeacherScheduleAdapter myClassesAdapter;
    private TeacherClassPresenter classesPresenter;

    private DatePickerEditText etDate;

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_my_classes;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        getAppListener().setTitle(isClassAttendance() ? R.string.menu_attendance : R.string.dashboard_menu_my_classes);

        rvClasses = (BaseRecyclerView) findViewById(R.id.rv_classes);
        rvClasses.setUpAsList();
        rvClasses.setLoadingMoreEnabled(false);
        rvClasses.setPullRefreshEnabled(false);
        rvClasses.addItemDecoration(new SpaceItemDecoration(getContext(), R.dimen.size_8, 1, true));

        classesPresenter = new TeacherClassPresenter();
        classesPresenter.attachView(this);

    }

    @Override
    protected boolean hasOptionMenu() {
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.filter_teacher_class, menu);
        View rootView = menu.findItem(R.id.menu_calender).getActionView();
        etDate = rootView.findViewById(R.id.et_date);
        etDate.setManager(getFragmentManager(), this);

        if (classesPresenter != null) {
            classesPresenter.setCalender();
        }

        rootView.setOnClickListener(this);
        onActionClick();
    }

    @Override
    public void setDate(Date date) {
        etDate.setDate(date);
        etDate.setText(DateUtils.toDate(date, DateUtils.DAY_FORMAT_PATTERN, Locale.getDefault()));
    }

    @Override
    public void setCalenderDate(Date minDate, Date maxDate) {
        Date currentTime = Calendar.getInstance().getTime();
        if (maxDate.after(currentTime) && minDate.before(currentTime)) {
            if (isClassAttendance())
                etDate.setMaxDate(currentTime);
            else
                etDate.setMaxDate(maxDate);

            setDate(currentTime);
            etDate.setMinDate(minDate);
        } else if (maxDate.after(currentTime) && minDate.after(currentTime)) {
            if (isClassAttendance()) {
                etDate.setMaxDate(minDate);
                etDate.setMinDate(minDate);
            } else {
                etDate.setMaxDate(maxDate);
                etDate.setMinDate(minDate);
            }
            setDate(minDate);
        } else if (maxDate.before(currentTime) && minDate.before(currentTime)) {
            setDate(maxDate);
            etDate.setMaxDate(maxDate);
            etDate.setMinDate(minDate);
        }
    }

    @Override
    public void setClassAdapter(List<TeacherClass> classes) {
        if (rvClasses.getAdapter() == null) {
            myClassesAdapter = new TeacherScheduleAdapter(getContext());
            myClassesAdapter.setOnItemClickListener(this);
            myClassesAdapter.attach(this);
            rvClasses.setAdapter(myClassesAdapter);
        }
        myClassesAdapter.update(classes);
    }

    @Override
    public boolean isClassAttendance() {
        return getArguments().getBoolean(Extras.CLASS_ATTENDANCE);
    }

    @Override
    public boolean isFutureDay() {
        return Calendar.getInstance().getTime().before(etDate.getDate().getTime());
    }

    @Override
    public String getWeekday() {
        String[] week = getResources().getStringArray(R.array.week);

        return week[etDate.getDate().get(Calendar.DAY_OF_WEEK) - 1];
    }

    @Override
    public String getDate() {
        return DateUtils.toDate(etDate.getDate().getTime(), etDate.getDateFormat());
    }

    @Override
    public void onActionClick() {
        super.onActionClick();
        classesPresenter.getTeacherClasses();
    }

    @Override
    public void onDateChange(View view, Calendar time, String timeString) {
        etDate.setText(DateUtils.toDate(etDate.getDate().getTime(), DateUtils.DAY_FORMAT_PATTERN, Locale.getDefault()));
        onActionClick();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_date:
                if (etDate != null) {
                    etDate.show();
                }
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        TeacherClass teacherClass = myClassesAdapter.getItem(position);
        Schedule schedule = teacherClass.getTimetable();
        Bundle bundle;
        switch (view.getId()) {
            case R.id.tv_assignment:
                bundle = new Bundle();
                bundle.putInt(Extras.CLASS_ID, teacherClass.getBcsMapId());
                bundle.putString(Extras.SUBJECT, schedule.getSubject().getSubjectdetails().get(0).getName());
                getAppListener().openActivity(AssignmentStatusActivity.class, bundle);
                break;
            case R.id.tv_attendance:
                takeAttendance(teacherClass, schedule);
                break;
            case R.id.tv_daily_report:
                bundle = new Bundle();
                bundle.putParcelable(Extras.TEACHER_CLASS, teacherClass);
                bundle.putParcelable(Extras.SCHEDULE, schedule);
                bundle.putString(Extras.DATE, getDate());
                TeacherReportFragment reportFragment = AppDialogFragment.newInstance(TeacherReportFragment.class, getAppListener(), bundle);
                reportFragment.show(getChildFragmentManager(), TeacherReportFragment.class.getSimpleName());
                break;
            case R.id.tv_take_attendance:
                takeAttendance(teacherClass, schedule);
                break;
            case R.id.tv_report:
                showAttendanceReport(teacherClass, schedule);
                break;
        }
    }

    private void takeAttendance(TeacherClass teacherClass, Schedule schedule) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(FragmentActivity.EXTRA_TASK_TYPE, TaskType.CLASS_ATTENDANCE);
        bundle.putInt(Extras.CLASS_ID, teacherClass.getBcsMapId());
        bundle.putInt(Extras.SUBJECT_ID, schedule.getSubjectId());
        bundle.putString(Extras.CLASS_NAME, teacherClass.getBcsmap().getClasses().getClassesdetails().get(0).getName() + "-" +
                teacherClass.getBcsmap().getSection().getSectiondetails().get(0).getName());
        bundle.putInt(Extras.SUBJECT_ORDER, schedule.getOrder());
        bundle.putString(Extras.DATE, getDate());
        bundle.putParcelableArrayList(Extras.CLASSES, (ArrayList<? extends Parcelable>) myClassesAdapter.getDatas());
        getAppListener().openActivity(TeacherTaskActivity.class, bundle);
    }

    private void showAttendanceReport(TeacherClass teacherClass, Schedule schedule) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(FragmentActivity.EXTRA_TASK_TYPE, TaskType.ATTENDANCE_REPORT);
        bundle.putInt(Extras.CLASS_ID, teacherClass.getBcsMapId());
        bundle.putInt(Extras.SUBJECT_ID, schedule.getSubjectId());
        bundle.putInt(Extras.SUBJECT_ORDER, schedule.getOrder());
        bundle.putString(Extras.DATE, getDate());
        getAppListener().openActivity(TeacherTaskActivity.class, bundle);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (classesPresenter != null)
            classesPresenter.detachView();
    }

}
