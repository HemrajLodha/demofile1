package com.pws.pateast.activity.leave;

import android.os.Bundle;

import com.pws.pateast.api.model.User;
import com.pws.pateast.api.model.Ward;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.enums.TaskType;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.fragment.leave.parent.ParentLeaveFragment;
import com.pws.pateast.fragment.leave.student.StudentLeaveFragment;
import com.pws.pateast.fragment.leave.teacher.TeacherLeaveFragment;
import com.pws.pateast.fragment.leave.teacher.approve.ClassLeaveFragment;
import com.pws.pateast.utils.Preference;
import com.pws.pateast.R;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.base.Extras;
import com.pws.pateast.enums.LeaveType;
import com.pws.pateast.model.LeaveCategory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by intel on 28-Jun-17.
 */

public class LeaveStatusPresenter extends AppPresenter<LeaveStatusView> {
    @Inject
    Preference preference;

    private User user;
    private UserType userType;
    private Ward ward;
    private LeaveStatusView leaveView;
    private TaskType taskType;


    @Override
    public LeaveStatusView getView() {
        return leaveView;
    }

    @Override
    public void attachView(LeaveStatusView view) {
        leaveView = view;
        getComponent().inject(this);
        user = preference.getUser();
        ward = preference.getWard();
        userType = UserType.getUserType(user.getData().getUser_type());
    }

    public UserType getUserType() {
        return userType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public void setTitle() {
        if (taskType == TaskType.EMPLOYEE_LEAVE_APPLY) {
            getView().setTitle(R.string.menu_class_my_leaves);
        } else {
            if (userType == UserType.STUDENT)
                getView().setTitle(R.string.menu_my_leave);
            else if (userType == UserType.TEACHER)
                getView().setTitle(R.string.menu_class_leave);
            else if (userType == UserType.PARENT)
                getView().setTitle(getString(R.string.menu_ward_leave , ward.getFullname()));
        }

    }

    public List<AppFragment> getLeaveFragments(List<LeaveCategory> categories, Bundle extras) {
        List<AppFragment> leaveFragments = new ArrayList<>();

        for (LeaveCategory category : categories) {
            Bundle bundle = new Bundle();
            bundle.putString(Extras.STATUS, category.getLeaveType().getValue());
            bundle.putString(Extras.TITLE, getString(category.getTitle()));
            if (extras != null) {
                bundle.putAll(extras);
            }
            if (taskType == TaskType.EMPLOYEE_LEAVE_APPLY) {
                leaveFragments.add(AppFragment.newInstance(TeacherLeaveFragment.class, null, bundle));
            } else {
                if (userType == UserType.STUDENT)
                    leaveFragments.add(AppFragment.newInstance(StudentLeaveFragment.class, null, bundle));
                else if (userType == UserType.TEACHER)
                    leaveFragments.add(AppFragment.newInstance(ClassLeaveFragment.class, null, bundle));
                else if (userType == UserType.PARENT)
                    leaveFragments.add(AppFragment.newInstance(ParentLeaveFragment.class, null, bundle));
            }
        }

        return leaveFragments;
    }

    public void setStatusAdapter() {
        getView().setStatusAdapter(getLeaveCategory());
    }

    public List<LeaveCategory> getLeaveCategory() {
        ArrayList<LeaveCategory> categories = new ArrayList<>();
        categories.add(new LeaveCategory(R.string.leave_pending, R.string.fa_ellipsi, LeaveType.PENDING));
        categories.add(new LeaveCategory(R.string.leave_approved, R.string.fa_check, LeaveType.APPROVED));
        categories.add(new LeaveCategory(R.string.leave_rejected, R.string.fa_ban, LeaveType.REJECTED));
        categories.add(new LeaveCategory(R.string.leave_canceled, R.string.fa_close, LeaveType.CANCELED));
        return categories;
    }
}
