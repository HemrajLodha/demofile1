package com.pws.pateast.fragment.assignment.teacher.review;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.view.BaseRecyclerView;
import com.pws.pateast.R;
import com.pws.pateast.api.model.Assignment;
import com.pws.pateast.api.model.Student;
import com.pws.pateast.api.model.Tag;
import com.pws.pateast.base.AppDialogFragment;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.base.Extras;
import com.pws.pateast.fragment.assignment.teacher.review.remark.RemarkAssignmentFragment;
import com.pws.pateast.utils.Utils;
import com.pws.pateast.widget.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by intel on 07-Sep-17.
 */

public class ReviewAssignmentFragment extends AppFragment implements ReviewAssignmentView, BaseRecyclerAdapter.OnItemClickListener, View.OnClickListener, SearchView.OnQueryTextListener {
    public static final int REVIEW_REQUEST = 2001;
    public static final int REVIEW_RESPONSE = 2002;

    private Button btnSubmit;
    private BaseRecyclerView rvStudentAssignment;
    private ReviewAssignmentAdapter reviewAssignmentAdapter;


    private ReviewAssignmentPresenter mPresenter;

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_assignment_review;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        getAppListener().setTitle(R.string.menu_review);

        btnSubmit = (Button) findViewById(R.id.btn_submit);
        rvStudentAssignment = (BaseRecyclerView) findViewById(R.id.rv_student_assignment);
        rvStudentAssignment.setUpAsList();
        rvStudentAssignment.setPullRefreshEnabled(false);
        rvStudentAssignment.setLoadingMoreEnabled(false);
        rvStudentAssignment.addItemDecoration(new SpaceItemDecoration(getContext(), R.dimen.size_8, 1, true));

        btnSubmit.setOnClickListener(this);


        mPresenter = new ReviewAssignmentPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void onActionClick() {
        super.onActionClick();
        mPresenter.getStudentsAssignment();
    }

    @Override
    protected boolean hasOptionMenu() {
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.filter_search, menu);
        MenuItem itemSearch = menu.findItem(R.id.menu_filter_advance);
        itemSearch.setVisible(showSearch());
        SearchManager searchManager =
                (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) itemSearch.getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public int getClassId() {
        return getAssignment().getBcsMapId();
    }

    @Override
    public int getAssignmentId() {
        return getAssignment().getId();
    }

    @Override
    public Assignment getAssignment() {
        return getArguments().getParcelable(AppFragment.EXTRA_DATA);
    }

    @Override
    public boolean showSearch() {
        return reviewAssignmentAdapter != null && !reviewAssignmentAdapter.getDatas().isEmpty();
    }

    @Override
    public void assignmentReviewed() {
        getActivity().setResult(REVIEW_RESPONSE);
        getActivity().finish();
    }

    @Override
    public void setAssignmentAdapter(List<Student> students) {
        if (students == null || students.isEmpty()) {
            btnSubmit.setVisibility(View.GONE);
            return;
        }

        btnSubmit.setVisibility(View.VISIBLE);
        if (rvStudentAssignment.getAdapter() == null) {
            reviewAssignmentAdapter = new ReviewAssignmentAdapter(getContext(),this);
            reviewAssignmentAdapter.setTags(getAssignmentTags());
            rvStudentAssignment.setAdapter(reviewAssignmentAdapter);
        }
        reviewAssignmentAdapter.update(students);
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void setAssignmentAdapter(Student student) {
        if (reviewAssignmentAdapter != null)
            reviewAssignmentAdapter.addOrUpdate(student);
    }

    @Override
    public void updateAssignmentAdapter(List<Student> students) {
        if (reviewAssignmentAdapter != null)
            reviewAssignmentAdapter.updateSearch(students);
    }

    @Override
    public void setAssignmentTags(List<Tag> tags) {
        getArguments().putParcelableArrayList(Extras.ASSIGNMENT_TAGS, (ArrayList<Tag>) tags);
    }

    @Override
    public List<Tag> getAssignmentTags() {
        return getArguments().getParcelableArrayList(Extras.ASSIGNMENT_TAGS);
    }

    @Override
    public List<Student> getStudents() {
        return reviewAssignmentAdapter != null ? reviewAssignmentAdapter.getDatas() : new ArrayList<Student>();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                mPresenter.submitStudentRemark();
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Student item = reviewAssignmentAdapter.getItem(position);
        switch (view.getId()) {
            case R.id.tv_review:
                if (getAssignmentTags() != null && !getAssignmentTags().isEmpty()) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(Extras.STUDENT, item);
                    bundle.putParcelableArrayList(Extras.ASSIGNMENT_TAGS, (ArrayList<? extends Parcelable>) getAssignmentTags());
                    RemarkAssignmentFragment dialog = AppDialogFragment.newInstance(RemarkAssignmentFragment.class, getAppListener(), bundle);
                    dialog.attachView(this);
                    dialog.show(getFragmentManager(), RemarkAssignmentFragment.class.getSimpleName());
                } else {
                    Utils.showToast(getContext(), R.string.no_tag_found);
                }
                break;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (mPresenter != null && reviewAssignmentAdapter != null) {
            mPresenter.getFilter(reviewAssignmentAdapter.getDatas()).filter(newText);
        }

        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.detachView();
    }
}
