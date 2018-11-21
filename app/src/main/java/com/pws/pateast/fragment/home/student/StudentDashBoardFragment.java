package com.pws.pateast.fragment.home.student;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.view.BaseRecyclerView;
import com.pws.pateast.activity.chat.inbox.InboxActivity;
import com.pws.pateast.activity.leave.StudentLeaveStatusActivity;
import com.pws.pateast.activity.schedule.activity.StudentScheduleActivity;
import com.pws.pateast.activity.tasks.TaskActivity;
import com.pws.pateast.base.FragmentActivity;
import com.pws.pateast.model.DashboardItem;
import com.pws.pateast.R;
import com.pws.pateast.api.model.UserInfo;
import com.pws.pateast.base.Extras;
import com.pws.pateast.enums.TaskType;
import com.pws.pateast.fragment.home.DashBoardFragment;
import com.pws.pateast.fragment.home.DashboardAdapter;
import com.pws.pateast.utils.ImageUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by intel on 18-Apr-17.
 */

public class StudentDashBoardFragment extends DashBoardFragment implements StudentDashBoardView,
        BaseRecyclerAdapter.OnItemClickListener {
    private TextView tvName;
    private CircleImageView imgProfile;

    private BaseRecyclerView rvMenu;

    private DashboardAdapter dashboardAdapter;

    private StudentDashBoardPresenter studentPresenter;

    @Override
    protected int getResourceLayout()
    {
        return R.layout.fragment_student;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState)
    {
        getAppListener().setTitle(R.string.title_dashboard);

        tvName = (TextView) findViewById(R.id.tv_name);

        imgProfile = (CircleImageView) findViewById(R.id.img_profile);

        rvMenu = (BaseRecyclerView) findViewById(R.id.rv_menu);
        rvMenu.setUpAsGrid(3);
        rvMenu.setPullRefreshEnabled(false);
        rvMenu.setLoadingMoreEnabled(false);


        studentPresenter = new StudentDashBoardPresenter();
        studentPresenter.attachView(this);

        studentPresenter.setStudentData();
        studentPresenter.setDashboardAdapter();
    }

    @Override
    protected boolean hasOptionMenu() {
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_teacher_options, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_session:
                studentPresenter.showSessionDialog(true);
                break;
            case R.id.action_language:
                studentPresenter.showLanguageDialog(true);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setDashboardAdapter(List<DashboardItem> items) {
        if (rvMenu.getAdapter() == null) {
            dashboardAdapter = new DashboardAdapter(getContext(), this);
            rvMenu.setAdapter(dashboardAdapter);
        }

        dashboardAdapter.add(items);
    }

    @Override
    public void setStudentData(UserInfo userInfo) {
        tvName.setText(userInfo.getUserdetails().get(0).getFullname());
        ImageUtils.setImageUrl(getContext(), imgProfile, userInfo.getUser_image(), R.drawable.user_placeholder);
    }

    @Override
    public void onItemClick(View view, int position)
    {
        DashboardItem dashboardItem = dashboardAdapter.getItem(position);
        if (dashboardItem != null)
        {
            switch (dashboardItem.getTaskType())
            {
                case STUDENT_LEAVE:
                    getAppListener().openActivity(StudentLeaveStatusActivity.class, TaskType.STUDENT_LEAVE_APPLY);
                    break;
                case STUDENT_SCHEDULE:
                    getAppListener().openActivity(StudentScheduleActivity.class, TaskType.STUDENT_SCHEDULE);
                    break;
                case STUDENT_EXAM_SCHEDULE:
                    getAppListener().openActivity(StudentScheduleActivity.class, TaskType.STUDENT_EXAM_SCHEDULE);
                    break;
                case MY_MESSAGE:
                    getAppListener().openActivity(InboxActivity.class);
                    break;
                default:
                    Bundle bundle = new Bundle();
                    bundle.putInt(FragmentActivity.EXTRA_TASK_TYPE,dashboardItem.getTaskType().getValue());
                    bundle.putString(Extras.TITLE,getString(dashboardItem.getTitle()));
                    getAppListener().openActivity(TaskActivity.class, bundle);
                    break;
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                getAppListener().closeDrawer();
                break;
            case R.id.nav_assignments:
                getAppListener().openActivity(TaskActivity.class, TaskType.STUDENT_ASSIGNMENT);
                break;
            case R.id.nav_attendance:
                getAppListener().openActivity(TaskActivity.class, TaskType.STUDENT_ATTENDANCE);
                break;

            case R.id.nav_my_schedule:
                getAppListener().openActivity(StudentScheduleActivity.class, TaskType.STUDENT_SCHEDULE);
                break;
            case R.id.nav_settings:
                getAppListener().openActivity(TaskActivity.class, TaskType.SETTINGS);
                break;
            case R.id.nav_messages:
                getAppListener().openActivity(InboxActivity.class);
                break;
            default:
                Bundle bundle = new Bundle();
                bundle.putInt(FragmentActivity.EXTRA_TASK_TYPE, TaskType.DEFAULT.getValue());
                bundle.putString(Extras.TITLE, item.getTitle().toString());
                getAppListener().openActivity(TaskActivity.class, bundle);
                break;
        }
        getAppListener().closeDrawer();
        return true;
    }


}
