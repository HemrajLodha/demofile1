package com.pws.pateast.fragment.schedule.student.exam;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.ListPopupWindow;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.view.BaseRecyclerView;
import com.pws.pateast.R;
import com.pws.pateast.activity.tasks.TaskActivity;
import com.pws.pateast.activity.tasks.TeacherTaskActivity;
import com.pws.pateast.adapter.ClassAdapter;
import com.pws.pateast.adapter.ExamAdapter;
import com.pws.pateast.api.model.Schedule;
import com.pws.pateast.api.model.TeacherClass;
import com.pws.pateast.base.AppDialogFragment;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.base.Extras;
import com.pws.pateast.enums.TaskType;
import com.pws.pateast.fragment.presenter.ClassPresenter;
import com.pws.pateast.fragment.presenter.ClassView;
import com.pws.pateast.fragment.schedule.student.exam.syllabus.ExamSyllabusFragment;
import com.pws.pateast.widget.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by intel on 16-May-17.
 */

public class ExamScheduleFragment extends AppFragment implements ExamScheduleView, ClassView, View.OnClickListener, BaseRecyclerAdapter.OnItemClickListener {
    private CardView layoutFilter;
    private BaseRecyclerView rvExamSchedule;
    private ListPopupWindow popUpClass, popUpExam;
    private TextView tvSelectClass, tvSelectExam;
    private ExamScheduleAdapter scheduleAdapter;
    private ClassAdapter classAdapter;
    private ExamAdapter examAdapter;

    private ExamSchedulePresenter schedulePresenter;
    private ClassPresenter classPresenter;
    private TeacherClass mTeacherClass;
    private Schedule mExamHead;

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_exam_schedule;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        getAppListener().setTitle(R.string.menu_exam_schedule);

        layoutFilter = (CardView) findViewById(R.id.layout_filter);
        tvSelectClass = (TextView) findViewById(R.id.tv_select_class);
        tvSelectExam = (TextView) findViewById(R.id.tv_select_exam);
        rvExamSchedule = (BaseRecyclerView) findViewById(R.id.rv_exam_schedule);
        rvExamSchedule.setUpAsList();
        rvExamSchedule.addItemDecoration(new SpaceItemDecoration(getContext(), R.dimen.size_5, 1, true));
        rvExamSchedule.setLoadingMoreEnabled(false);
        rvExamSchedule.setPullRefreshEnabled(false);

        tvSelectClass.setOnClickListener(this);
        tvSelectExam.setOnClickListener(this);

        if (getActivity() instanceof TeacherTaskActivity) {
            schedulePresenter = new ExamSchedulePresenter(true);
            schedulePresenter.attachView(this);

            classPresenter = new ClassPresenter(true);
            classPresenter.attachView(this);
            classPresenter.getMyClasses();
        } else {
            schedulePresenter = new ExamSchedulePresenter(false);
            schedulePresenter.attachView(this);
        }
    }

    @Override
    public void onActionClick() {
        super.onActionClick();
        schedulePresenter.getMyExamSchedule();
    }


    @Override
    public int getExamHeadID() {
        if (mExamHead != null)
            return mExamHead.getExamheadId();
        return getArguments().getInt(Extras.EXAM_HEAD_ID);
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.getTaskType(getArguments().getInt(TaskActivity.EXTRA_TASK_TYPE));
    }


    @Override
    public void setExamScheduleAdapter(ArrayList<Schedule> schedules) {
        if (rvExamSchedule.getAdapter() == null) {
            scheduleAdapter = new ExamScheduleAdapter(getContext(), getTaskType());
            scheduleAdapter.setOnItemClickListener(this);
            rvExamSchedule.setAdapter(scheduleAdapter);
        }
        scheduleAdapter.update(schedules);
    }

    @Override
    public void onError(String message) {
        if (rvExamSchedule.getAdapter() != null) {
            scheduleAdapter.update(new ArrayList<Schedule>());
        }
    }

    @Override
    public void setClass(TeacherClass classes) {
        mTeacherClass = classes;
        if (mTeacherClass != null) {
            tvSelectClass.setText(classAdapter.getClassName(mTeacherClass));
            schedulePresenter.getTeacherExamHead(mTeacherClass, false);
        } else {
            tvSelectClass.setText(null);
        }
    }

    @Override
    public void setClassAdapter(List<TeacherClass> classes) {
        Map<String, TeacherClass> myClasses = new HashMap<>();

        for (TeacherClass item : classes) {
            myClasses.put(String.valueOf(item.getBcsmap().getBoardId() + item.getBcsmap().getClassId()), item);
        }

        List<TeacherClass> refinedList = new ArrayList<>();
        for (Map.Entry<String, TeacherClass> item : myClasses.entrySet()) {
            refinedList.add(item.getValue());
        }

        if (popUpClass == null) {
            popUpClass = new ListPopupWindow(getContext());
            popUpClass.setAnchorView(tvSelectClass);
            popUpClass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (classAdapter != null) {
                        setClass(classAdapter.getItem(position));
                        setExamHead(null);
                    } else {
                        setClass(null);
                    }
                    popUpClass.dismiss();
                }
            });
        }
        if (classAdapter == null) {
            classAdapter = new ClassAdapter(getContext(), false);
        }
        classAdapter.update(refinedList);
        popUpClass.setAdapter(classAdapter);
        layoutFilter.setVisibility(View.VISIBLE);
    }

    @Override
    public void setExamHeads(List<Schedule> examHeads, boolean isSchedule) {
        if (popUpExam == null) {
            popUpExam = new ListPopupWindow(getContext());
            popUpExam.setAnchorView(tvSelectExam);
            popUpExam.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (examAdapter != null) {
                        setExamHead(examAdapter.getItem(position));
                    } else {
                        setExamHead(null);
                    }
                    popUpExam.dismiss();
                }
            });
        }
        if (examAdapter == null) {
            examAdapter = new ExamAdapter(getContext());

        }
        examAdapter.update(examHeads);
        popUpExam.setAdapter(examAdapter);
    }

    @Override
    public void setExamHead(Schedule examHead) {
        mExamHead = examHead;
        if (mExamHead != null) {
            tvSelectExam.setText(examAdapter.getExamName(mExamHead));
            onActionClick();
        } else {
            if (scheduleAdapter != null)
                scheduleAdapter.clear();
            tvSelectExam.setText(null);
        }
    }

    @Override
    public TeacherClass getTeacherClass() {
        return mTeacherClass;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_select_class:
                if (popUpClass != null)
                    popUpClass.show();
                break;
            case R.id.tv_select_exam:
                if (popUpExam != null)
                    popUpExam.show();
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Schedule schedule = scheduleAdapter.getItem(position);
        switch (view.getId()) {
            case R.id.layout_syllabus:
                Bundle bundle = new Bundle();
                bundle.putParcelable(Extras.SCHEDULE, schedule);
                ExamSyllabusFragment syllabusFragment = AppDialogFragment.newInstance(ExamSyllabusFragment.class, getAppListener(), bundle);
                syllabusFragment.show(getChildFragmentManager(), ExamSyllabusFragment.class.getSimpleName());
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        schedulePresenter.detachView();
    }


}
