package com.pws.pateast.activity.schedule;

import android.content.Intent;
import android.os.Bundle;

import com.pws.pateast.R;
import com.pws.pateast.activity.tasks.TaskActivity;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.exception.RetrofitException;
import com.pws.pateast.api.model.Schedule;
import com.pws.pateast.api.model.Session;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.model.Ward;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.StudentService;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.base.Extras;
import com.pws.pateast.enums.TaskType;
import com.pws.pateast.fragment.marks.student.ExamMarksFragment;
import com.pws.pateast.fragment.presenter.ExamHeadPresenter;
import com.pws.pateast.fragment.schedule.student.exam.ExamScheduleFragment;
import com.pws.pateast.utils.Preference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.pws.pateast.base.FragmentActivity.EXTRA_TASK_TYPE;

/**
 * Created by intel on 21-Aug-17.
 */

public class SchedulePresenter extends ExamHeadPresenter<ScheduleView> {

    private TaskType mTaskType;

    private HashMap<TaskType, Object> scheduleData;

    public SchedulePresenter(boolean openDialog) {
        super(openDialog);
    }


    @Override
    public void attachView(ScheduleView view) {
        super.attachView(view);
        scheduleData = new HashMap<>();
    }

    public void setTaskType(Intent intent) {
        if (intent != null) {
            try {
                int type = intent.getIntExtra(EXTRA_TASK_TYPE, TaskType.DEFAULT.getValue());
                mTaskType = TaskType.getTaskType(type);
                if (mTaskType == TaskType.DEFAULT) {
                    mTaskType = (TaskType) intent.getSerializableExtra(EXTRA_TASK_TYPE);
                }
                if (mTaskType == null) {
                    mTaskType = TaskType.DEFAULT;
                }


            } catch (ClassCastException e) {
                //
            }
            setTitle();
        }
    }

    private void setTitle() {
        switch (mTaskType) {
            case STUDENT_SCHEDULE:
                getView().setTitle(R.string.class_schedule);
                getView().setScheduleAdapter(mTaskType, getCurrentSession());
                break;
            case TEACHER_SCHEDULE:
                getView().setTitle(R.string.class_schedule);
                getView().setScheduleAdapter(mTaskType, getCurrentSession());
                break;
            case WARD_SCHEDULE:
                getView().setTitle(R.string.class_schedule);
                getView().setScheduleAdapter(mTaskType, getCurrentSession());
                break;
            case HOLIDAY:
                Session session = getCurrentSession();
                if (session != null) {
                    getView().setTitle(getString(R.string.holiday_schedule, session.getAcademicsessiondetails().get(0).getName()));
                    getView().setScheduleAdapter(mTaskType, session);
                }
                break;
            case STUDENT_EXAM_SCHEDULE:
                getView().setTitle(R.string.title_exam_schedule);
                getView().onActionClick();
                getStudentExamHead(true);
                break;
            case WARD_EXAM_SCHEDULE:
                getView().setTitle(R.string.title_exam_schedule);
                getView().onActionClick();
                getWardExamHead(true);
                break;
            case STUDENT_MARKS:
                getView().setTitle(R.string.menu_exam_marks);
                getView().onActionClick();
                getStudentExamHead(false);
                break;
            case WARD_MARKS:
                getView().setTitle(R.string.menu_exam_marks);
                getView().onActionClick();
                getWardExamHead(false);
                break;
        }
    }

    private Session getCurrentSession() {
        switch (getView().getUserType()) {
            case STUDENT:
            case TEACHER:
            case DRIVER:
                return Session.getSelectedSession(user.getUserdetails().getAcademicSessions(), user.getUserdetails().getAcademicSessionId());
            case PARENT:
                return Session.getSelectedSession(ward.getUserInfo().getAcademicSessions(), ward.getAcademicSessionId());
            default:
                return null;
        }
    }

    public void setScheduleData(Object scheduleData) {
        this.scheduleData.put(mTaskType, scheduleData);
    }

    public Object getScheduleData() {
        return scheduleData.get(mTaskType);
    }


    public List<AppFragment> getExamScheduleFragments(List<Schedule> schedules) {
        List<AppFragment> scheduleFragments = new ArrayList<>();

        for (Schedule schedule : schedules) {
            Bundle bundle = new Bundle();
            bundle.putInt(TaskActivity.EXTRA_TASK_TYPE, mTaskType.getValue());
            bundle.putInt(Extras.EXAM_HEAD_ID, schedule.getExamheadId());
            bundle.putString(Extras.TITLE, schedule.getExamhead().getExamheaddetails().get(0).getName());
            scheduleFragments.add(AppFragment.newInstance(ExamScheduleFragment.class, bundle));
        }

        return scheduleFragments;
    }

    public List<AppFragment> getExamMarksFragments(List<Schedule> schedules) {
        List<AppFragment> scheduleFragments = new ArrayList<>();

        for (Schedule schedule : schedules) {
            Bundle bundle = new Bundle();
            bundle.putInt(TaskActivity.EXTRA_TASK_TYPE, mTaskType.getValue());
            bundle.putInt(Extras.EXAM_SCHEDULE_ID, schedule.getId());
            bundle.putString(Extras.TITLE, schedule.getExamhead().getExamheaddetails().get(0).getName());
            scheduleFragments.add(AppFragment.newInstance(ExamMarksFragment.class, bundle));
        }


        return scheduleFragments;
    }
}
