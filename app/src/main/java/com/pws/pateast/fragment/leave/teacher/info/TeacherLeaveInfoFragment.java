package com.pws.pateast.fragment.leave.teacher.info;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.view.BaseRecyclerView;
import com.pws.pateast.R;
import com.pws.pateast.api.model.Leave;
import com.pws.pateast.api.model.LeaveInfo;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.utils.DateUtils;

import java.util.ArrayList;

import static com.pws.pateast.fragment.assignment.teacher.add.AddAssignmentFragment.ADD_REQUEST;
import static com.pws.pateast.fragment.assignment.teacher.add.AddAssignmentFragment.ADD_RESPONSE;

/**
 * Created by planet on 8/3/2017.
 */

public class TeacherLeaveInfoFragment extends AppFragment implements TeacherLeaveInfoView, BaseRecyclerAdapter.OnItemClickListener {
    public static final String ARGS_DATA_ID = "args_data_id";
    private BaseRecyclerView rvLeaveInfo;
    private TeacherLeaveInfoPresenter leaveInfoPresenter;

    private LeaveInfoAdapter leaveAdapter;
    private int leaveId;

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_recycler_view;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            leaveId = getArguments().getInt(ARGS_DATA_ID);
        }

        getAppListener().setTitle(R.string.menu_leave_info);

        rvLeaveInfo = (BaseRecyclerView) findViewById(R.id.rv_leave);
        rvLeaveInfo.setUpAsList();
        leaveInfoPresenter = new TeacherLeaveInfoPresenter();
        leaveInfoPresenter.attachView(this);
        leaveInfoPresenter.getTeacherLeaveInfo(leaveId);
    }

    @Override
    public void onActionClick() {
        super.onActionClick();
    }


    @Override
    public void setLeaveInfoAdapter(LeaveInfo leaveInfo) {
        ArrayList<LeaveInfoAdapter.InfoData> datas = new ArrayList<>();

        if (leaveInfo.getUser() != null) {
            String fullName = leaveInfo.getUser().getUserdetails().get(0).getFullname();
            if (!TextUtils.isEmpty(fullName)) {
                datas.add(new LeaveInfoAdapter.InfoData(getString(R.string.label_leave_info_name), fullName));
            }
        }

        if (leaveInfo.getEmpleavetype() != null) {
            String leaveType = leaveInfo.getEmpleavetype().getEmpleavetypedetails().get(0).getName();
            if (!TextUtils.isEmpty(leaveType)) {
                datas.add(new LeaveInfoAdapter.InfoData(getString(R.string.label_leave_info_type), leaveType));
            }
        }
        if (leaveInfo.getTag() != null) {
            String leaveReason = "";

            if (!TextUtils.isEmpty(leaveInfo.getTag().getTagdetails().get(0).getTitle())) {
                leaveReason = leaveInfo.getTag().getTagdetails().get(0).getTitle();
            } else {
                leaveReason = leaveInfo.getTag().getTagdetails().get(0).getDescription();
            }
            if (!TextUtils.isEmpty(leaveReason)) {
                datas.add(new LeaveInfoAdapter.InfoData(getString(R.string.label_leave_info_leave_reason), leaveReason));
            }
        }

        datas.add(new LeaveInfoAdapter.InfoData(getString(R.string.label_leave_info_start_date),
                DateUtils.toTime(DateUtils.parse(leaveInfo.getStart_date(), DateUtils.DATE_FORMAT_PATTERN), DateUtils.DATE_TEMPLATE)));

        datas.add(new LeaveInfoAdapter.InfoData(getString(R.string.label_leave_info_end_date),
                DateUtils.toTime(DateUtils.parse(leaveInfo.getEnd_date(), DateUtils.DATE_FORMAT_PATTERN), DateUtils.DATE_TEMPLATE)));

        datas.add(new LeaveInfoAdapter.InfoData(getString(R.string.label_leave_info_duration), String.valueOf(leaveInfo.getDuration())));

        if (!TextUtils.isEmpty(leaveInfo.getHalfday())) {
            datas.add(new LeaveInfoAdapter.InfoData(getString(R.string.label_leave_info_halfday), leaveInfo.getHalfday()));
        }

        if (!TextUtils.isEmpty(leaveInfo.getComment())) {
            datas.add(new LeaveInfoAdapter.InfoData(getString(R.string.label_leave_info_comment), leaveInfo.getComment()));
        }

        if (!TextUtils.isEmpty(leaveInfo.getReject_reason())) {
            datas.add(new LeaveInfoAdapter.InfoData(getString(R.string.label_leave_info_reject_reason), leaveInfo.getReject_reason()));
        }

        if (leaveInfo.getLeavestatus() != 0) {
            String leaveStatus = "";
            switch (leaveInfo.getLeavestatus()) {
                case Leave.PENDING:
                    leaveStatus = getString(R.string.leave_pending);
                    break;
                case Leave.APPROVED:
                    leaveStatus = getString(R.string.leave_approved);
                    break;
                case Leave.CANCELED:
                    leaveStatus = getString(R.string.leave_canceled);
                    break;
                case Leave.REJECTED:
                    leaveStatus = getString(R.string.leave_rejected);
                    break;
            }
            datas.add(new LeaveInfoAdapter.InfoData(getString(R.string.label_leave_info_status), leaveStatus));
        }

        if (!TextUtils.isEmpty(leaveInfo.getStatus_updatedbytype())) {
            datas.add(new LeaveInfoAdapter.InfoData(getString(R.string.label_leave_info_status_updated_by_type),
                    leaveInfo.getStatus_updatedbytype()));
        }

        if (rvLeaveInfo.getAdapter() == null) {
            leaveAdapter = new LeaveInfoAdapter(getContext(), this);
            rvLeaveInfo.setAdapter(leaveAdapter);
        }

        leaveAdapter.addOrUpdate(datas);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ADD_REQUEST:
                if (resultCode == ADD_RESPONSE) {
                    onActionClick();
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (leaveInfoPresenter != null)
            leaveInfoPresenter.detachView();
    }

    @Override
    public void onItemClick(View view, int position) {

    }

}
