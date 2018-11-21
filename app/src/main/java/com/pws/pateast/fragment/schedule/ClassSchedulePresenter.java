package com.pws.pateast.fragment.schedule;

import com.pws.pateast.R;
import com.pws.pateast.api.ServiceAction;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.ServiceModel;
import com.pws.pateast.api.comparator.HolidayComparator;
import com.pws.pateast.api.comparator.ScheduleComparator;
import com.pws.pateast.api.exception.RetrofitException;
import com.pws.pateast.api.model.ClassSchedule;
import com.pws.pateast.api.model.Holiday;
import com.pws.pateast.api.model.Schedule;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.model.Ward;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.service.ParentService;
import com.pws.pateast.api.service.StudentService;
import com.pws.pateast.api.service.TeacherService;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.utils.DateUtils;
import com.pws.pateast.utils.Preference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;

/**
 * Created by intel on 24-Aug-17.
 */

public abstract class ClassSchedulePresenter<V extends ClassScheduleView> extends AppPresenter<V> {
    @Inject
    protected Preference preference;

    @Inject
    protected ServiceBuilder serviceBuilder;

    protected User user;

    protected Ward ward;

    protected StudentService studentService;

    protected TeacherService teacherService;

    protected ParentService parentService;

    private Calendar calendar;

    private V view;

    abstract public void getMySchedule(AppSingleObserver observer);

    abstract public void isHoliday();

    @Override
    public V getView() {
        return view;
    }

    @Override
    public void attachView(V view) {
        this.view = view;
        getComponent().inject((ClassSchedulePresenter<ClassScheduleView>) this);
        user = preference.getUser();
        ward = preference.getWard();
    }

    public void setCalendar() {
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(getView().getTimeInMillis());
    }

    private AppSingleObserver appSingleObserver = new AppSingleObserver<Holiday>() {
        @Override
        public void onResponse(Holiday response) {
            if (response.getData() == null || response.getData().isEmpty()) {
                onError(new RetrofitException(getString(R.string.no_holiday), RetrofitException.ERROR_TYPE_MESSAGE));
            } else {
                setHoliday(response.getData(), this);
            }
        }

        @Override
        public ClassSchedulePresenter getPresenter() {
            return ClassSchedulePresenter.this;
        }
    };

    protected void getHolidays(int masterId, int academicSessionId) {
        if (getView().getSchedules() != null) {
            ArrayList list = getView().getSchedules().get(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));
            if (list == null || list.isEmpty()) {
                if (appSingleObserver != null) {
                    appSingleObserver.onError(new RetrofitException(getString(R.string.no_holiday), RetrofitException.ERROR_TYPE_MESSAGE));
                }
                return;
            } else {
                if (appSingleObserver != null) {
                    appSingleObserver.hide();
                }
            }
            getView().setScheduleAdapter(list);
            return;
        }

        parentService = serviceBuilder.createService(ParentService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("masterId", String.valueOf(masterId));
        params.put("academicSessionId", String.valueOf(academicSessionId));

        disposable = parentService.getHolidays(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(appSingleObserver);
    }


    protected void isHoliday(int masterId, int academicSessionId) {
        parentService = serviceBuilder.createService(ParentService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("masterId", String.valueOf(masterId));
        params.put("academicSessionId", String.valueOf(academicSessionId));
        params.put("date", DateUtils.toDate(calendar.getTime(), "yyyy-MM-dd"));

        disposable = parentService.isHoliday(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Holiday>() {
                    @Override
                    public void onResponse(Holiday response) {
                        if (response.getData().isEmpty()) {
                            getMySchedule(this);
                        } else {
                            HashMap<String, ArrayList> schedules = new HashMap<>();
                            String weekDay = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH);
                            for (Holiday holiday : response.getData()) {
                                if (!schedules.containsKey(weekDay)) {
                                    ArrayList list = new ArrayList<>();
                                    list.add(weekDay);
                                    schedules.put(weekDay, list);
                                }
                                ArrayList list = schedules.get(weekDay);
                                list.add(holiday);
                                schedules.put(weekDay, list);
                            }
                            getView().setScheduleAdapter(schedules.get(weekDay));
                        }
                    }

                    @Override
                    public ClassSchedulePresenter getPresenter() {
                        return ClassSchedulePresenter.this;
                    }
                });

    }

    private void getClassSchedule(int userId, int masterId, int academicSessionId, int bcsMapId, String userType, AppSingleObserver observer) {
        if (getView().getSchedules() != null) {
            ArrayList list = getView().getSchedules().get(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH));
            if (list == null || list.isEmpty()) {
                observer.onError(new RetrofitException(getString(R.string.no_classes), RetrofitException.ERROR_TYPE_MESSAGE));
                return;
            }
            getView().setScheduleAdapter(list);
            return;
        }

        studentService = serviceBuilder.createService(StudentService.class);

        HashMap<String, String> params = serviceBuilder.getParams(ServiceModel.TIME_TABLE, ServiceAction.VIEW);
        params.put("userId", String.valueOf(userId));
        params.put("masterId", String.valueOf(masterId));
        params.put("academicSessionId", String.valueOf(academicSessionId));
        params.put("bcsMapId", String.valueOf(bcsMapId));
        params.put("userType", userType);

        disposable = studentService.getClassSchedule(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<ClassSchedule>() {
                    @Override
                    public void onResponse(ClassSchedule response) {
                        if (response.getData() != null) {
                            setSchedule(response.getData().getTimetableallocations(), this);
                        } else {
                            onError(new RetrofitException(getString(R.string.no_classes), RetrofitException.ERROR_TYPE_MESSAGE));
                        }
                    }

                    @Override
                    public ClassSchedulePresenter getPresenter() {
                        return ClassSchedulePresenter.this;
                    }

                });
    }

    private void setHoliday(ArrayList<Holiday> holidayList, DisposableSingleObserver observer) {
        HashMap<String, ArrayList> holidays = new HashMap<>();
        if (holidayList != null) {
            List<String> months = Arrays.asList(getContext().getResources().getStringArray(R.array.month));
            Collections.sort(holidayList, new HolidayComparator(getContext(), Calendar.getInstance()));
            for (Holiday holiday : holidayList) {
                for (String month : months) {
                    if (!holidays.containsKey(month)) {
                        ArrayList list = new ArrayList<>();
                        holidays.put(month, list);
                    }
                    if (holiday.getMonth(Calendar.getInstance()).equalsIgnoreCase(month)) {
                        ArrayList list = holidays.get(month);
                        list.add(holiday);
                        holidays.put(month, list);
                    }
                }
            }
        }
        ArrayList list = holidays.get(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));
        if (list != null)
            getView().setScheduleAdapter(list);
        getView().setSchedules(holidays);

        if (holidayList == null || holidays.isEmpty() || list == null || list.isEmpty())
            observer.onError(new RetrofitException(getString(R.string.no_holiday), RetrofitException.ERROR_TYPE_MESSAGE));
    }

    private void setSchedule(ArrayList<Schedule> schedulesList, DisposableSingleObserver observer) {
        HashMap<String, ArrayList> schedules = new HashMap<>();
        if (schedulesList != null) {
            List<String> week = Arrays.asList(getContext().getResources().getStringArray(R.array.week));
            Collections.sort(schedulesList, new ScheduleComparator(getContext()));

            for (Schedule schedule : schedulesList) {
                for (String weekDay : week) {
                    if (!schedules.containsKey(weekDay)) {
                        ArrayList list = new ArrayList<>();
                        //list.add(weekDay); // TODO Fuck off later this line
                        schedules.put(weekDay, list);
                    }

                    if (schedule.getWeekday().equalsIgnoreCase(weekDay)) {
                        ArrayList list = schedules.get(weekDay);
                        list.add(schedule);
                        schedules.put(weekDay, list);
                    }
                }
            }
        }
        ArrayList list = schedules.get(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH));
        if (list != null)
            getView().setScheduleAdapter(list);
        getView().setSchedules(schedules);
        if (schedulesList == null || schedules.isEmpty() || list == null || list.isEmpty())
            observer.onError(new RetrofitException(getString(R.string.no_classes), RetrofitException.ERROR_TYPE_MESSAGE));
    }

    protected void getTeacherSchedule(DisposableSingleObserver observer) {
        if (getView().getSchedules() != null) {
            ArrayList list = getView().getSchedules().get(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH));
            if (list == null || list.isEmpty()) {
                observer.onError(new RetrofitException(getString(R.string.no_classes), RetrofitException.ERROR_TYPE_MESSAGE));
                return;
            }
            getView().setScheduleAdapter(list);
            return;
        }

        teacherService = serviceBuilder.createService(TeacherService.class);
        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("userId", String.valueOf(user.getUserdetails().getUserId()));
        params.put("masterId", String.valueOf(user.getData().getMasterId()));
        params.put("academicSessionId", String.valueOf(user.getUserdetails().getAcademicSessionId()));


        disposable = teacherService.getMySchedule(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<Schedule>() {
                    @Override
                    public void onResponse(Schedule response) {
                        setSchedule(response.getData(), this);
                    }

                    @Override
                    public ClassSchedulePresenter getPresenter() {
                        return ClassSchedulePresenter.this;
                    }
                });
    }


    protected void getStudentSchedule(AppSingleObserver observer) {

        getClassSchedule(user.getUserdetails().getUserId(),
                user.getData().getMasterId(),
                user.getUserdetails().getAcademicSessionId(),
                user.getUserdetails().getBcsMapId(),
                user.getData().getUser_type(), observer);

    }

    protected void getWardSchedule(AppSingleObserver observer) {
        getClassSchedule(ward.getStudentId(),
                ward.getMasterId(),
                ward.getAcademicSessionId(),
                ward.getBcsMapId(),
                UserType.STUDENT.getValue(), observer);
    }

}
