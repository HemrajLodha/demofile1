package com.pws.pateast.fragment.attendance.teacher.report;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.view.BaseRecyclerView;
import com.pws.pateast.R;
import com.pws.pateast.api.model.Student;
import com.pws.pateast.base.AppDialogFragment;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.base.Extras;
import com.pws.pateast.fragment.attendance.teacher.report.calender.CalenderReportFragment;
import com.pws.pateast.widget.SpaceItemDecoration;

import java.util.List;

/**
 * Created by intel on 10-May-17.
 */

public class AttendanceReportFragment extends AppFragment implements AttendanceReportView, BaseRecyclerAdapter.OnItemClickListener, SearchView.OnQueryTextListener {

    private BaseRecyclerView rvAttendanceReport;
    private AttendanceReportAdapter reportAdapter;

    private AttendanceReportPresenter reportPresenter;

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_attendance_report;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        getAppListener().setTitle(R.string.menu_attendance_report);

        rvAttendanceReport = (BaseRecyclerView) findViewById(R.id.rv_attendance_report);
        rvAttendanceReport.setUpAsList();
        rvAttendanceReport.setPullRefreshEnabled(false);
        rvAttendanceReport.setLoadingMoreEnabled(false);
        rvAttendanceReport.addItemDecoration(new SpaceItemDecoration(getContext(), R.dimen.size_5, 1, true));


        reportPresenter = new AttendanceReportPresenter();
        reportPresenter.attachView(this);
        onActionClick();
    }

    @Override
    protected boolean hasOptionMenu() {
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.filter_search, menu);
        SearchManager searchManager =
                (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        MenuItem itemSearch = menu.findItem(R.id.menu_filter_advance);
        itemSearch.setVisible(isSearch());
        SearchView searchView =
                (SearchView) itemSearch.getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public void onActionClick() {
        super.onActionClick();
        reportPresenter.getAttendanceReport(getClassId(), getSubjectId(), getStudentId(), getStartDate(), getEndDate());
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
    public int getStudentId() {
        return getArguments().getInt(Extras.STUDENT_ID);
    }

    @Override
    public String getStartDate() {
        return getArguments().getString(Extras.START_DATE);
    }

    @Override
    public String getEndDate() {
        return getArguments().getString(Extras.END_DATE);
    }

    @Override
    public boolean isSearch() {
        return reportAdapter != null && !reportAdapter.getDatas().isEmpty();
    }

    @Override
    public void updateReportAdapter(List<Student> students) {
        if (reportAdapter != null) {
            reportAdapter.updateSearch(students);
        }
    }


    @Override
    public void setReportAdapter(List<Student> students) {
        if (rvAttendanceReport.getAdapter() == null) {
            reportAdapter = new AttendanceReportAdapter(getContext(), this);
            rvAttendanceReport.setAdapter(reportAdapter);
        }
        reportAdapter.update(students);
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        reportPresenter.detachView();
    }


    @Override
    public void onItemClick(View view, int position) {
        Student student = reportAdapter.getItem(position);
        switch (view.getId()) {
            case R.id.tv_view_report:
                Bundle bundle = new Bundle();
                bundle.putInt(Extras.CLASS_ID, getClassId());
                bundle.putInt(Extras.SUBJECT_ID, getSubjectId());
                bundle.putParcelable(Extras.STUDENT, student);
                CalenderReportFragment filterFragment = AppDialogFragment.newInstance(CalenderReportFragment.class, getAppListener(), bundle);
                filterFragment.show(getFragmentManager(), CalenderReportFragment.class.getSimpleName());
                break;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (reportPresenter != null && reportAdapter != null) {
            reportPresenter.getFilter(reportAdapter.getDatas()).filter(newText);
        }

        return true;
    }
}
