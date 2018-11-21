package com.pws.pateast.fragment.schedule.parent.holiday;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.base.ui.adapter.BaseExpandableRecyclerAdapter;
import com.base.ui.view.BaseRecyclerView;
import com.pws.pateast.R;
import com.pws.pateast.activity.schedule.ScheduleView;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.base.Extras;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.fragment.schedule.ClassScheduleAdapter;
import com.pws.pateast.widget.AuthorView;
import com.pws.pateast.widget.DividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by intel on 25-Aug-17.
 */

public class HolidayFragment extends AppFragment implements HolidayView {
    private BaseRecyclerView rvLeave;
    private HolidayAdapter holidayAdapter;
    private HolidayPresenter mPresenter;
    private ScheduleView scheduleView;
    private AuthorView authorView;

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_recycler_view;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        authorView = new AuthorView(getContext());
        rvLeave = (BaseRecyclerView) findViewById(R.id.rv_leave);
        rvLeave.addItemDecoration(new CustomDecoration());
        rvLeave.setUpAsList();
        rvLeave.setPullRefreshEnabled(false);
        rvLeave.setLoadingMoreEnabled(true);
        authorView.setMessage(R.string.class_schedule_message);
        authorView.setAuthor(R.string.class_schedule_author);
        authorView.setMessageColor(ContextCompat.getColor(getContext(), R.color.teacher_class_schedule_form_green));
        authorView.setAuthorColor(ContextCompat.getColor(getContext(), R.color.student_form_icon_blue));
        rvLeave.setFootView(authorView);
        mPresenter = new HolidayPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void onActionClick() {
        if (getSchedules() == null)
            super.onActionClick();
        mPresenter.setCalendar();
        mPresenter.getHolidays();
    }

    @Override
    public UserType getUserType() {
        return scheduleView != null ? scheduleView.getUserType() : UserType.NONE;
    }

    @Override
    public void attachView(ScheduleView scheduleView) {
        this.scheduleView = scheduleView;
    }

    @Override
    public long getTimeInMillis() {
        return getArguments().getLong(Extras.TIME_IN_MILLIS);
    }

    @Override
    public void setSchedules(HashMap<String, ArrayList> schedules) {
        if (scheduleView != null)
            scheduleView.setScheduleData(schedules);
    }

    @Override
    public HashMap<String, ArrayList> getSchedules() {
        return scheduleView != null ? (HashMap<String, ArrayList>) scheduleView.getScheduleData() : null;
    }

    @Override
    public void setScheduleAdapter(List<Object> schedules) {
        if (rvLeave.getAdapter() == null) {
            holidayAdapter = new HolidayAdapter(getContext());
            holidayAdapter.setUserType(getUserType());
            rvLeave.setAdapter(holidayAdapter);
        }
        holidayAdapter.update(schedules);
    }

    @Override
    public void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }

    class CustomDecoration extends RecyclerView.ItemDecoration {
        private int spacing;

        public CustomDecoration() {
            spacing = getContext().getResources().getDimensionPixelSize(R.dimen.size_5);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            outRect.left = spacing;
            outRect.right = spacing;
            if (parent.getAdapter().getItemViewType(position) == BaseExpandableRecyclerAdapter.TYPE_PARENT)
                outRect.top = spacing;
            if (position == parent.getAdapter().getItemCount() - 1) {
                view.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.dashboard_card_bottom_bg));
                outRect.bottom = spacing;
            }
        }

    }
}
