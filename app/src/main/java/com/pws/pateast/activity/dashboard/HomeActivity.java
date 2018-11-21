package com.pws.pateast.activity.dashboard;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.ui.adapter.BaseExpandableRecyclerAdapter;
import com.pws.pateast.R;
import com.pws.pateast.api.model.Assignment;
import com.pws.pateast.api.model.DashboardEvent;
import com.pws.pateast.api.model.ExamMarks;
import com.pws.pateast.api.model.Response;
import com.pws.pateast.api.model.Schedule;
import com.pws.pateast.base.ResponseAppActivity;
import com.pws.pateast.enums.ExpandableViewType;
import com.pws.pateast.enums.MessageStatusType;
import com.pws.pateast.fragment.assignment.AssignmentView;
import com.pws.pateast.listener.AppListener;
import com.pws.pateast.provider.table.Chat;
import com.pws.pateast.widget.BadgeView;
import com.pws.pateast.widget.ProfileImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class HomeActivity extends ResponseAppActivity implements HomeView,
        NavigationView.OnNavigationItemSelectedListener,
        BaseExpandableRecyclerAdapter.OnParentClickListener<DashboardEvent<Response>>,
        DashboardEventAdapter.OnChildItemClickListener,
        View.OnClickListener,
        LoaderManager.LoaderCallbacks<Cursor>,
        AssignmentView {
    private int COUNT_LOADER_ID = 101;

    private ActionBarDrawerToggle mDrawerToggle;
    protected DrawerLayout drawer;
    protected NavigationView navigationView;
    protected HomePresenter homePresenter;
    protected RecyclerView rvUpcomingEvent;
    private DashboardEventAdapter dashboardEventAdapter;

    protected ProfileImageView studentImage;
    protected TextView tvStudentName;
    protected TextView tvDueDate;
    protected TextView tvDueFee;
    protected TextView tvStudentClass;
    protected RelativeLayout layoutFees;

    private RelativeLayout layoutMessage;
    private BadgeView badgeMessage;


    //private TextView tvDueDateIcon;
    private List<DashboardEvent<Response>> eventList;

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);
        setDrawer();
        studentImage = findViewById(R.id.ivAvatar);
        tvStudentName = findViewById(R.id.tvName);
        tvStudentClass = findViewById(R.id.tvClass);
        tvDueDate = findViewById(R.id.tv_due_date);
        tvDueFee = findViewById(R.id.tv_due_fee);
        /*tvChapter = findViewById(R.id.tv_chapter);
        tvDueDate = findViewById(R.id.tv_dueDate);
        tvDueDateIcon = findViewById(R.id.ic_dueDate);*/
        //tvMessageCount = findViewById(R.id.message_count);
        layoutFees = findViewById(R.id.fees_view);
        layoutMessage = findViewById(R.id.count_view);
        badgeMessage = new BadgeView(getContext(), layoutMessage);
        badgeMessage.setBadgeBackgroundColor(ContextCompat.getColor(getContext(), R.color.trans));
        badgeMessage.setTextSize(16);
        badgeMessage.setBadgeMargin(0, 5);

        rvUpcomingEvent = findViewById(R.id.recycler_view);
        rvUpcomingEvent.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        homePresenter = new HomePresenter();
        homePresenter.attachView(this);

        findViewById(R.id.profile_view).setOnClickListener(this);
        findViewById(R.id.assignment_view).setOnClickListener(this);
        findViewById(R.id.bus_track_view).setOnClickListener(this);
        findViewById(R.id.message_view).setOnClickListener(this);
        findViewById(R.id.feeds_view).setOnClickListener(this);

        setDashboardEventAdapter(null);
        getLoaderManager().initLoader(COUNT_LOADER_ID, null, this);
        homePresenter.getDashbordDetails();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (homePresenter != null) {
            homePresenter.getDashbordDetails();
        }
    }


    private void setDrawer() {
        drawer = findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(mDrawerToggle);
        drawer.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
        navigationView.setNavigationItemSelectedListener(this);
    }

    private DashboardEvent getDashboardItem(ExpandableViewType viewType) {
        switch (viewType) {
            case SCHEDULE:
                ChildFooterItem footerItem = new ChildFooterItem(getString(R.string.title_view_more));
                return new DashboardEvent(getString(R.string.title_todays_schedule),
                        R.drawable.dashboard_today_schedule_250, ExpandableViewType.SCHEDULE, footerItem, 0);
            case ATTENDANCE:
                footerItem = new ChildFooterItem(getString(R.string.title_view_more));
                return new DashboardEvent(getString(R.string.title_attendance),
                        R.drawable.dashboard_attendance_250, ExpandableViewType.ATTENDANCE, footerItem, 0);
            case ASSIGNMENT:
                footerItem = new ChildFooterItem(getString(R.string.title_view_more));
                return new DashboardEvent(getString(R.string.title_my_assignment),
                        R.drawable.dashboard_my_assignments_250, ExpandableViewType.ASSIGNMENT, footerItem, R.color.teacher_class_schedule_form_green);
            case EXAM_SCHEDULE:
                footerItem = new ChildFooterItem(getString(R.string.title_view_more));
                return new DashboardEvent(getString(R.string.title_exam_schedule),
                        R.drawable.dashboard_exam_schedule_250, ExpandableViewType.EXAM_SCHEDULE, footerItem, R.color.teacher_form_color_purple);
            case EXAM_MARKS:
                footerItem = new ChildFooterItem(getString(R.string.title_view_more));
                return new DashboardEvent(getString(R.string.title_exam_marks),
                        R.drawable.dashboard_marks_exams_250, ExpandableViewType.EXAM_MARKS, footerItem, R.color.student_form_color_dark_red);
        }
        return null;
    }

    private List<DashboardEvent<Response>> getDashboardEvents(DashboardEvent item) {
        if (eventList != null) {
            if (item == null) {
                eventList.get(0).setSchedule(new Schedule());
            } else {
                if (item.getSchedule() == null)
                    item.setSchedule(new Schedule(new ArrayList<Schedule>()));

                if (item.getSchedule().getTimetableallocations() == null)
                    item.getSchedule().setTimetableallocations(new ArrayList<Schedule>());
                //run in each condition
                eventList.get(0).setSchedule(item.getSchedule());
            }
            return eventList;
        }

        eventList = new ArrayList<>();
        DashboardEvent itemSchedule = getDashboardItem(ExpandableViewType.SCHEDULE);
        if (item == null) {
            itemSchedule.setSchedule(new Schedule());
        } else {
            itemSchedule.setSchedule(item.getSchedule());
        }
        eventList.add(itemSchedule);

        eventList.add(getDashboardItem(ExpandableViewType.ATTENDANCE));

        eventList.add(getDashboardItem(ExpandableViewType.ASSIGNMENT));

        eventList.add(getDashboardItem(ExpandableViewType.EXAM_SCHEDULE));

        eventList.add(getDashboardItem(ExpandableViewType.EXAM_MARKS));
        return eventList;
    }

    @Override
    public void setDashboardEventAdapter(DashboardEvent dashboardEvent) {

        List<DashboardEvent<Response>> eventList = getDashboardEvents(dashboardEvent);
        if (rvUpcomingEvent.getAdapter() == null) {
            dashboardEventAdapter = new DashboardEventAdapter(getContext());
            rvUpcomingEvent.setAdapter(dashboardEventAdapter);
            dashboardEventAdapter.setOnParentClickListener(this);
            dashboardEventAdapter.setOnChildClickListener(this);
        }
        dashboardEventAdapter.setParentList(eventList, true);
    }

    @Override
    public void setDashboardAttendance(ArrayList<Schedule> data) {
        List<DashboardEvent<Response>> dashboardEvent = dashboardEventAdapter.getParentList();
        dashboardEvent.get(1).getAttendance().setData(data);
        dashboardEventAdapter.setParentList(dashboardEvent, true);
    }

    @Override
    public void setAssignmentAdapter(List<Assignment> assignments) {
        List<DashboardEvent<Response>> dashboardEvent = dashboardEventAdapter.getParentList();
        dashboardEvent.get(2).getAssignments().setData((ArrayList<Assignment>) assignments);
        dashboardEventAdapter.setParentList(dashboardEvent, true);
    }

    @Override
    public void setExamSchedule(ArrayList<Schedule> schedule) {
        List<DashboardEvent<Response>> dashboardEvent = dashboardEventAdapter.getParentList();
        dashboardEvent.get(3).getExam_schedules().setData(schedule);
        dashboardEventAdapter.setParentList(dashboardEvent, true);
    }

    @Override
    public void setExamMarks(ArrayList<ExamMarks> marks) {
        List<DashboardEvent<Response>> dashboardEvent = dashboardEventAdapter.getParentList();
        dashboardEvent.get(4).getExam_marks().setData(marks);
        dashboardEventAdapter.setParentList(dashboardEvent, true);
    }


    @Override
    public void onParentClick(View view, DashboardEvent<Response> parent, int position) {
        dashboardEventAdapter.collapseAllParents();
        dashboardEventAdapter.expandParent(parent);
        dashboardEventAdapter.notifyDataSetChanged();

        switch (dashboardEventAdapter.getParentItem(position).getExpandableViewType()) {
            case SCHEDULE:
                if (dashboardEventAdapter.getParentItem(position).getSchedule().getTimetableallocations() == null) {
                    homePresenter.getDashbordDetails();
                }
                break;
            case ATTENDANCE:
                if (dashboardEventAdapter.getParentItem(position).getAttendance().getData() == null) {
                    homePresenter.getAttendance();
                }
                break;
            case ASSIGNMENT:
                if (dashboardEventAdapter.getParentItem(position).getAssignments().getData() == null) {
                    homePresenter.getStudentAssignment(this, 0, 0, null);
                }
                break;
            case EXAM_SCHEDULE:
                if (dashboardEventAdapter.getParentItem(position).getAttendance().getData() == null) {
                    homePresenter.getExamSchedule();
                }
                break;
            case EXAM_MARKS:
                if (dashboardEventAdapter.getParentItem(position).getAttendance().getData() == null) {
                    homePresenter.getExamMarks();
                }
                break;
        }
    }


    @Override
    public void onChildClick(View view, DashboardEvent<Response> parent, Response child) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public boolean isToolbarSetupEnabled() {
        return true;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void inflateMenu(int resId) {
        navigationView.inflateMenu(resId);
    }


    @Override
    public AppListener getAppListener() {
        return this;
    }

    @Override
    public int getPage() {
        return 1;
    }

    @Override
    public void addPage() {

    }

    @Override
    public void setPageCount(int pageCount) {

    }

    @Override
    public void viewAssignmentFile(File file, String type) {

    }


    private class ItemDecoration extends DividerItemDecoration {

        ItemDecoration(Context context, int orientation) {
            super(context, orientation);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            if (position < parent.getAdapter().getItemCount() &&
                    parent.getAdapter().getItemViewType(position) == BaseExpandableRecyclerAdapter.TYPE_PARENT) {
                return;
            }
            super.getItemOffsets(outRect, view, parent, state);
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            for (int i = 0; i < parent.getChildCount() - 1; i++) {
                if (parent.getAdapter().getItemViewType(i) != BaseExpandableRecyclerAdapter.TYPE_PARENT) {
                    super.onDraw(c, parent, state);
                }
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this, Chat.CONTENT_URI, Chat.PROJECTION_COUNT,
                Chat.COLUMN_SENDER_ID + "!=? AND (" +
                        Chat.COLUMN_MESSAGE_STATUS + "=? OR " + Chat.COLUMN_MESSAGE_STATUS + "=?)",
                new String[]{String.valueOf(homePresenter.getReceiverId()), String.valueOf(MessageStatusType.STATUS_RECEIVED.getValue()), String.valueOf(MessageStatusType.STATUS_SENT.getValue())},
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        int unreadCount = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                unreadCount = cursor.getInt(0);
            }
        }
        if (unreadCount > 0) {
            badgeMessage.setText(unreadCount < 100 ? ((unreadCount < 10 ? "0" : "") + String.valueOf(unreadCount)) : "99+");
        } else {
            badgeMessage.setText(getString(R.string.text_zero));
        }
        badgeMessage.show();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getLoaderManager().destroyLoader(COUNT_LOADER_ID);
        if (homePresenter != null)
            homePresenter.detachView();
    }
}
