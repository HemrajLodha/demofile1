package com.pws.pateast.activity.chat.add;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.view.BaseRecyclerView;
import com.pws.pateast.R;
import com.pws.pateast.adapter.ClassAdapter;
import com.pws.pateast.adapter.StudentAdapter;
import com.pws.pateast.api.model.Student;
import com.pws.pateast.api.model.TeacherClass;
import com.pws.pateast.api.model.UserInfo;
import com.pws.pateast.base.Extras;
import com.pws.pateast.base.ResponseAppActivity;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.fragment.presenter.StudentPresenter;
import com.pws.pateast.fragment.presenter.StudentView;
import com.pws.pateast.widget.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by intel on 03-Aug-17.
 */

public class ChatUserActivity extends ResponseAppActivity implements ChatUserView, StudentView, View.OnClickListener, BaseRecyclerAdapter.OnItemClickListener, SearchView.OnQueryTextListener {
    public static final int ADD_CHAT_USER = 100;
    private CardView cardFilter;

    private ListPopupWindow popUpClass, popUpStudent;
    private TextView tvSelectClass, tvSelectStudent;

    private BaseRecyclerView rvChatUser;

    private ChatUserPresenter mPresenter;
    private ChatUserAdapter mAdapter;
    private StudentPresenter studentPresenter;

    private ClassAdapter classAdapter;
    private StudentAdapter studentAdapter;

    private TeacherClass mTeacherClass;
    private Student mStudent;

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.activity_chat_user;
    }

    @Override
    public boolean isToolbarSetupEnabled() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(getAppTheme());
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getAppTheme() {
        int theme = getIntent().getIntExtra(Extras.APP_THEME, 0);
        if (theme == 0)
            return R.style.AppTheme;
        return theme;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);
        cardFilter = (CardView) findViewById(R.id.card_filter);
        tvSelectClass = (TextView) findViewById(R.id.tv_select_class);
        tvSelectStudent = (TextView) findViewById(R.id.tv_select_student);

        rvChatUser = findViewById(R.id.rv_chat_user);
        rvChatUser.setUpAsList();
        rvChatUser.addItemDecoration(new SpaceItemDecoration(getContext(), R.dimen.size_5, 1, true));
        rvChatUser.setPullRefreshEnabled(false);
        rvChatUser.setLoadingMoreEnabled(false);

        tvSelectClass.setOnClickListener(this);
        tvSelectStudent.setOnClickListener(this);

        mPresenter = new ChatUserPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter_search, menu);
        MenuItem itemSearch = menu.findItem(R.id.menu_filter_advance);
        itemSearch.setVisible(mAdapter != null && !mAdapter.getDatas().isEmpty());
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) itemSearch.getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public UserType getChatUserType() {
        return UserType.getUserType(getIntent().getStringExtra(Extras.USER_TYPE));
    }

    @Override
    public void getClasses() {
        studentPresenter = new StudentPresenter(true);
        studentPresenter.attachView(this);
        studentPresenter.getMyClasses();
    }

    @Override
    public void setStudent(Student student) {
        mStudent = student;
        if (mStudent != null) {
            tvSelectStudent.setText(studentAdapter.getStudentName(mStudent));
            onActionClick();
            mPresenter.getParents(mStudent);
        } else if (mStudent == null) {
            if (mAdapter != null)
                mAdapter.clear();
            if (studentAdapter != null)
                studentAdapter.clear();
            tvSelectStudent.setText(null);
        }
    }

    @Override
    public void setStudentAdapter(List<Student> students) {
        if (popUpStudent == null) {
            popUpStudent = new ListPopupWindow(getContext());
            popUpStudent.setAnchorView(tvSelectStudent);
            popUpStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (studentAdapter != null) {
                        setStudent(studentAdapter.getItem(position));
                    } else {
                        setStudent(null);
                    }
                    popUpStudent.dismiss();
                }
            });
        }
        if (studentAdapter == null) {
            studentAdapter = new StudentAdapter(getContext());
        }
        studentAdapter.update(students);
        popUpStudent.setAdapter(studentAdapter);
    }

    @Override
    public void setClass(TeacherClass classes) {
        setStudent(null);
        mTeacherClass = classes;
        if (mTeacherClass != null) {
            tvSelectClass.setText(classAdapter.getClassName(mTeacherClass));
            studentPresenter.getStudents(mTeacherClass);
        } else {
            tvSelectClass.setText(null);
        }
    }

    @Override
    public void setClassAdapter(List<TeacherClass> classes) {
        if (popUpClass == null) {
            popUpClass = new ListPopupWindow(getContext());
            popUpClass.setAnchorView(tvSelectClass);
            popUpClass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (classAdapter != null) {
                        setClass(classAdapter.getItem(position));
                    } else {
                        setClass(null);
                    }
                    popUpClass.dismiss();
                }
            });
        }
        if (classAdapter == null) {
            classAdapter = new ClassAdapter(getContext());
        }
        classAdapter.update(classes);
        popUpClass.setAdapter(classAdapter);
        cardFilter.setVisibility(View.VISIBLE);
    }

    @Override
    public void setChatUserAdapter(List<UserInfo> userInfos) {
        if (rvChatUser.getAdapter() == null) {
            mAdapter = new ChatUserAdapter(getContext(), this);
            mAdapter.setOnItemClickListener(this);
            mAdapter.setUserType(getChatUserType());
            rvChatUser.setAdapter(mAdapter);
        }
        mAdapter.update(userInfos);
        invalidateOptionsMenu();
    }

    @Override
    public void updateChatUserAdapter(List<UserInfo> userInfos) {
        if (mAdapter != null) {
            mAdapter.updateSearch(userInfos);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (mPresenter != null && mAdapter != null) {
            mPresenter.getFilter(mAdapter.getDatas()).filter(newText);
        }

        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_select_class:
                if (popUpClass != null)
                    popUpClass.show();
                break;
            case R.id.tv_select_student:
                if (popUpStudent != null)
                    popUpStudent.show();
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if (mAdapter != null) {
            UserInfo userInfo = mAdapter.getItem(position);
            switch (getChatUserType()) {
                case STUDENT:
                    userInfo = userInfo.getStudent().getUser();
                    userInfo.setUser_type(getChatUserType().getValue());
                    Intent intent = new Intent();
                    intent.putExtra(Extras.ADD_USER, userInfo);
                    setResult(RESULT_OK, intent);
                    break;
                case TEACHER:
                    userInfo.setUser_type(getChatUserType().getValue());
                    intent = new Intent();
                    intent.putExtra(Extras.ADD_USER, userInfo);
                    setResult(RESULT_OK, intent);
                    break;
                case ADMIN:
                    userInfo.setUser_type(getChatUserType().getValue());
                    intent = new Intent();
                    intent.putExtra(Extras.ADD_USER, userInfo);
                    setResult(RESULT_OK, intent);
                    break;
                case INSTITUTE:
                    userInfo.setUser_type(getChatUserType().getValue());
                    intent = new Intent();
                    intent.putExtra(Extras.ADD_USER, userInfo);
                    setResult(RESULT_OK, intent);
                    break;
                case PARENT:
                    userInfo.setUser_type(getChatUserType().getValue());
                    intent = new Intent();
                    intent.putExtra(Extras.ADD_USER, userInfo);
                    setResult(RESULT_OK, intent);
                    break;
            }
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.detachView();
    }

}
