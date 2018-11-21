package com.pws.pateast.fragment.attendance.teacher;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.pws.pateast.R;
import com.pws.pateast.api.model.Student;
import com.pws.pateast.api.model.Tag;
import com.pws.pateast.base.AppDialogFragment;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.base.Extras;
import com.pws.pateast.fragment.attendance.teacher.addupdate.AttendanceTagAdapter;
import com.pws.pateast.fragment.attendance.teacher.addupdate.AttendanceTakerView;
import com.pws.pateast.fragment.attendance.teacher.report.calender.CalenderReportFragment;
import com.pws.pateast.widget.PointerPopupWindow;
import com.pws.pateast.widget.SpaceItemDecoration;
import com.pws.pateast.widget.chromepopup.TagHelpPopup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by intel on 27-Apr-17.
 */

public class AttendanceFragment extends AppFragment implements AttendanceView,
        AttendanceAdapter.OnItemClickListener,
        View.OnClickListener,
        SearchView.OnQueryTextListener {

    private Button btnSubmit;
    private RecyclerView rvStudentAttendance;
    private AttendanceAdapter attendanceAdapter;

    private AttendancePresenter attendancePresenter;

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_attendance;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {

        btnSubmit = (Button) findViewById(R.id.btn_submit);
        rvStudentAttendance = (RecyclerView) findViewById(R.id.rv_student_attendance);
        rvStudentAttendance.setLayoutManager(new LinearLayoutManager(getContext()));
        rvStudentAttendance.addItemDecoration(new SpaceItemDecoration(getContext(), R.dimen.size_5, 1, true));
        btnSubmit.setOnClickListener(this);

        attendancePresenter = new AttendancePresenter();
        attendancePresenter.attachView(this);
        onActionClick();
        getAppListener().setTitle(getString(R.string.menu_attendance_for) + " " + getClassName());
    }

    @Override
    protected boolean hasOptionMenu() {
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.filter_search, menu);
        MenuItem itemSearch = menu.findItem(R.id.menu_filter_advance);
        itemSearch.setVisible(isUpdate());
        SearchManager searchManager =
                (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) itemSearch.getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public void onDestroy() {
        attendancePresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void onActionClick() {
        super.onActionClick();
        attendancePresenter.getStudentsAttendance(getClassId(), getSubjectId(), getSubjectOrder(), getDate(), getEnrollNumber(), getStudentName());
    }

    @Override
    public void setAttendanceAdapter(List<Student> students) {
        if (rvStudentAttendance.getAdapter() == null) {
            attendanceAdapter = new AttendanceAdapter(getContext());
            attendanceAdapter.setOnItemClickListener(this);
            rvStudentAttendance.setAdapter(attendanceAdapter);
        }
        attendanceAdapter.update(students);
    }

    @Override
    public void updateAttendanceAdapter(List<Student> students) {
        if (attendanceAdapter != null) {
            attendanceAdapter.updateSearch(students);
        }
    }

    @Override
    public void setAttendanceAdapter(Student student) {
        attendanceAdapter.addOrUpdate(student);
    }


    @Override
    public void setSubmitVisible(boolean visible) {
        if (btnSubmit != null) {
            btnSubmit.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void setAttendanceId(int attendanceId) {
        getArguments().putInt(Extras.ATTENDANCE_ID, attendanceId);
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public int getAttendanceId() {
        return getArguments().getInt(Extras.ATTENDANCE_ID);
    }

    @Override
    public boolean isUpdate() {
        return getAttendanceId() != 0 && attendanceAdapter != null && !attendanceAdapter.getDatas().isEmpty();
    }

    @Override
    public int getClassId() {
        return getArguments().getInt(Extras.CLASS_ID);
    }

    @Override
    public int getSubjectId() {
        return getArguments().getInt(Extras.SUBJECT_ID);
    }

    @Override
    public int getSubjectOrder() {
        return getArguments().getInt(Extras.SUBJECT_ORDER);
    }

    @Override
    public String getDate() {
        return getArguments().getString(Extras.DATE);
    }

    @Override
    public String getEnrollNumber() {
        return getArguments().getString(Extras.ENROLL_NUMBER);
    }

    @Override
    public String getStudentName() {
        return getArguments().getString(Extras.STUDENT_NAME);
    }

    @Override
    public String getClassName() {
        return getArguments().getString(Extras.CLASS_NAME);
    }

    @Override
    public void setAttendanceTags(List<Tag> tags) {
        getArguments().putParcelableArrayList(Extras.ATTENDANCE_TAGS, (ArrayList<Tag>) tags);
    }

    @Override
    public List<Tag> getAttendanceTags() {
        return getArguments().getParcelableArrayList(Extras.ATTENDANCE_TAGS);
    }

    @Override
    public void onItemClick(View view, int position) {
        Student item = attendanceAdapter.getItem(position);
        switch (view.getId()) {
            case R.id.vw_view_report:
                Bundle bundle = new Bundle();
                bundle.putInt(Extras.CLASS_ID, getClassId());
                bundle.putInt(Extras.SUBJECT_ID, getSubjectId());
                bundle.putParcelable(Extras.STUDENT, item);
                CalenderReportFragment filterFragment = AppDialogFragment.newInstance(CalenderReportFragment.class, getAppListener(), bundle);
                filterFragment.show(getFragmentManager(), CalenderReportFragment.class.getSimpleName());
                break;
            case R.id.vw_present:
                updateStudentAttendance(AttendanceTakerView.SELECTED_PRESENT, item, view);
                break;
            case R.id.vw_absent:
                updateStudentAttendance(AttendanceTakerView.SELECTED_ABSENT, item, view);
                break;
            case R.id.vw_leave:
                updateStudentAttendance(AttendanceTakerView.SELECTED_LEAVE, item, view);
                break;
            default:
                item.setOpened(!item.isOpened());
                attendanceAdapter.addOrUpdate(item);
                break;
        }
    }

    private void updateStudentAttendance(int type, Student item, View anchor) {
        List<Tag> tags = getAttendanceTags();
        if (item.getStudent().getAttendancerecord() == null) {
            item.getStudent().setAttendancerecord(new Student());
        }
        item.getStudent().getAttendancerecord().setIs_present(type);
        if (type == AttendanceTakerView.SELECTED_PRESENT) {
            setAttendanceAdapter(item);
        } else if (type == AttendanceTakerView.SELECTED_LEAVE || type == AttendanceTakerView.SELECTED_ABSENT) {
            if (tags.isEmpty()) {
                setAttendanceAdapter(item);
                return;
            } else {
                openTagsDropdown(anchor, item, tags);
            }
        }
    }

    private void openTagsDropdown(final View anchor, final Student item, List<Tag> tags) {
        final Student attendanceRecord = item.getStudent().getAttendancerecord();
        TagHelpPopup tagHelpPopup = new TagHelpPopup(getContext(), tags, TextUtils.isEmpty(attendanceRecord.getTags()) ? null : attendanceRecord.getTags().split(","));
        tagHelpPopup.show(anchor);
        tagHelpPopup.setOnItemClickListener(new TagHelpPopup.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position, List<String> tags) {
                attendanceRecord.setTags(TextUtils.join(",", tags));
                setAttendanceAdapter(item);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                attendancePresenter.submitStudentsAttendance(attendanceAdapter.getDatas());
                break;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (attendancePresenter != null && attendanceAdapter != null) {
            attendancePresenter.getFilter(attendanceAdapter.getDatas()).filter(newText);
        }
        return true;
    }


}
