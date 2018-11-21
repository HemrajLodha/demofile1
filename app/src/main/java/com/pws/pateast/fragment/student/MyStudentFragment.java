package com.pws.pateast.fragment.student;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.view.BaseRecyclerView;
import com.pws.pateast.R;
import com.pws.pateast.activity.attendance.TeacherAttendanceReportActivity;
import com.pws.pateast.activity.tasks.TaskActivity;
import com.pws.pateast.api.model.Student;
import com.pws.pateast.api.model.TeacherClass;
import com.pws.pateast.api.model.UserInfo;
import com.pws.pateast.base.AppDialogFragment;
import com.pws.pateast.base.AppFragment;
import com.pws.pateast.base.Extras;
import com.pws.pateast.base.FragmentActivity;
import com.pws.pateast.enums.TaskType;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.fragment.student.message.MessageToStudentFragment;
import com.pws.pateast.widget.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by intel on 25-Apr-17.
 */

public class MyStudentFragment extends AppFragment implements MyStudentView,
        BaseRecyclerAdapter.OnItemClickListener,
        SearchView.OnQueryTextListener {
    private BaseRecyclerView rvStudent;
    private MyStudentAdapter studentAdapter;

    private MyStudentsPresenter studentsPresenter;

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_my_student;
    }

    @Override
    protected void onViewReady(@Nullable Bundle savedInstanceState) {
        rvStudent = (BaseRecyclerView) findViewById(R.id.rv_student);
        rvStudent.setUpAsList();
        rvStudent.setLoadingMoreEnabled(false);
        rvStudent.setPullRefreshEnabled(false);
        rvStudent.addItemDecoration(new SpaceItemDecoration(getContext(), R.dimen.size_5, 1, true));

        studentsPresenter = new MyStudentsPresenter();
        studentsPresenter.attachView(this);
    }

    @Override
    protected boolean hasOptionMenu() {
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.filtert_teacher_student, menu);
        MenuItem itemSearch = menu.findItem(R.id.menu_filter_advance);
        itemSearch.setVisible(studentAdapter != null && !studentAdapter.getDatas().isEmpty());
        SearchManager searchManager =
                (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) itemSearch.getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public void setFilter(int bcsMapId, String enrollNumber, String studentName) {
        getArguments().putInt(Extras.CLASS_ID, bcsMapId);
        getArguments().putString(Extras.ENROLL_NUMBER, enrollNumber);
        getArguments().putString(Extras.STUDENT_NAME, studentName);
        onActionClick();
    }

    @Override
    public void setMyClasses(List<TeacherClass> myClasses) {
        getArguments().putParcelableArrayList(Extras.CLASSES, (ArrayList<? extends Parcelable>) myClasses);
    }

    @Override
    public boolean isAddUser() {
        return getArguments().getBoolean(Extras.ADD_USER);
    }

    @Override
    public void clearFilter() {
        getArguments().putInt(Extras.CLASS_ID, 0);
        getArguments().putString(Extras.ENROLL_NUMBER, null);
        getArguments().putString(Extras.STUDENT_NAME, null);
        onActionClick();
    }

    @Override
    public List<TeacherClass> getMyClasses() {
        return getArguments().getParcelableArrayList(Extras.CLASSES);
    }

    @Override
    public int getClassId() {
        return getArguments().getInt(Extras.CLASS_ID);
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
    public void setMyStudentAdapter(List<Student> students) {
        if (rvStudent.getAdapter() == null) {
            studentAdapter = new MyStudentAdapter(getContext());
            studentAdapter.setAddUser(isAddUser());
            studentAdapter.setOnItemClickListener(this);
            rvStudent.setAdapter(studentAdapter);

        }
        studentAdapter.update(students);
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void updateStudentAdapter(List<Student> students) {
        if (studentAdapter != null) {
            studentAdapter.updateSearch(students);
        }
    }

    @Override
    public void onActionClick() {
        super.onActionClick();
        studentsPresenter.getMyStudents(getClassId(), getEnrollNumber(), getStudentName());
    }

    @Override
    public void onItemClick(View view, int position) {
        Student student = studentAdapter.getItem(position);
        switch (view.getId()) {
            case R.id.tv_send_message:
                Bundle bundle = new Bundle();
                bundle.putInt(Extras.STUDENT_ID, student.getStudentId());
                bundle.putString(Extras.STUDENT_NAME, student.getStudent().getUser().getUserdetails().get(0).getFullname());
                MessageToStudentFragment messageFragment = AppDialogFragment.newInstance(MessageToStudentFragment.class, getAppListener(), bundle);
                messageFragment.attach(MyStudentFragment.this);
                messageFragment.show(getChildFragmentManager(), MessageToStudentFragment.class.getSimpleName());
                break;
            case R.id.tv_attendance_report:
                bundle = new Bundle();
                bundle.putParcelable(Extras.STUDENT, student);
                bundle.putInt(Extras.CLASS_ID, getClassId());
                getAppListener().openActivity(TeacherAttendanceReportActivity.class, bundle);
                break;
            default:
                if (isAddUser()) {
                    UserInfo userInfo = student.getStudent().getUser();
                    userInfo.setUser_type(UserType.STUDENT.getValue());
                    Intent intent = new Intent();
                    intent.putExtra(Extras.ADD_USER, userInfo);
                    getActivity().setResult(Activity.RESULT_OK, intent);
                    getActivity().finish();
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
        if (studentsPresenter != null && studentAdapter != null) {
            studentsPresenter.getFilter(studentAdapter.getDatas()).filter(newText);
        }

        return true;
    }

    class OnMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        private int position;

        public OnMenuItemClickListener(int position) {
            this.position = position;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            Student student = studentAdapter.getItem(position);
            Bundle bundle;
            switch (item.getItemId()) {

                case R.id.menu_attendance_report:
                    bundle = new Bundle();
                    bundle.putSerializable(FragmentActivity.EXTRA_TASK_TYPE, TaskType.ATTENDANCE_REPORT);
                    bundle.putInt(Extras.STUDENT_ID, student.getStudentId());
                    bundle.putInt(Extras.CLASS_ID, getClassId());
                    bundle.putParcelableArrayList(Extras.CLASSES, (ArrayList<? extends Parcelable>) getMyClasses());
                    getAppListener().openActivity(TaskActivity.class, bundle);
                    break;
                case R.id.menu_message:

                    break;
            }
            return true;
        }
    }

    @Override
    public void onDestroy() {
        studentsPresenter.detachView();
        super.onDestroy();
    }
}
