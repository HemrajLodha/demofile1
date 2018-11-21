package com.pws.pateast.activity.chat.inbox;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.view.BaseRecyclerView;
import com.hlab.fabrevealmenu.listeners.OnFABMenuSelectedListener;
import com.hlab.fabrevealmenu.model.FABMenuItem;
import com.hlab.fabrevealmenu.view.FABRevealMenu;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pws.pateast.R;
import com.pws.pateast.activity.chat.add.ChatUserActivity;
import com.pws.pateast.activity.chat.message.MessageActivity;
import com.pws.pateast.activity.teacher.student.StudentFilterActivity;
import com.pws.pateast.api.model.Message;
import com.pws.pateast.api.model.UserInfo;
import com.pws.pateast.base.Extras;
import com.pws.pateast.base.ResponseAppActivity;
import com.pws.pateast.chat.UserChatHelper;
import com.pws.pateast.chat.UserHelper;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.widget.SpaceItemDecoration;

import java.util.ArrayList;

import static com.pws.pateast.activity.chat.add.ChatUserActivity.ADD_CHAT_USER;

/**
 * Created by intel on 25-Jul-17.
 */

public class InboxActivity extends ResponseAppActivity implements InboxView, XRecyclerView.LoadingListener, BaseRecyclerAdapter.OnItemClickListener, OnFABMenuSelectedListener {
    private FloatingActionButton fabAddChat;
    private FABRevealMenu fabMenuChat;

    private BaseRecyclerView rvInbox;
    private InboxAdapter inboxAdapter;

    private InboxPresenter mPresenter;

    @Override
    protected int getResourceLayout() {
        return R.layout.activity_inbox;
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
        setTitle(R.string.menu_messages);
        fabAddChat = (FloatingActionButton) findViewById(R.id.fab_add_chat);
        fabMenuChat = (FABRevealMenu) findViewById(R.id.fab_menu_chat);
        rvInbox = (BaseRecyclerView) findViewById(R.id.rv_inbox);
        rvInbox.setUpAsList();
        rvInbox.addItemDecoration(new SpaceItemDecoration(getContext(), R.dimen.size_5, 1, true));
        rvInbox.setLoadingMoreEnabled(false);
        rvInbox.setPullRefreshEnabled(false);
        rvInbox.setLoadingListener(this);

        mPresenter = new InboxPresenter();
        mPresenter.attachView(this);
        mPresenter.getChatPermissions();
    }

    @Override
    public void onActionClick() {
        super.onActionClick();
        onRefresh();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.detachView();
    }

    @Override
    public void onRefresh() {
        mPresenter.getUsersChat();
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void setChatUser(ArrayList<FABMenuItem> fabMenuItems) {
        if (fabAddChat != null && fabMenuChat != null) {
            if (!fabMenuItems.isEmpty()) {
                fabMenuChat.bindAncherView(fabAddChat);
                fabMenuChat.setMenuItems(fabMenuItems);
                fabMenuChat.setOnFABMenuSelectedListener(this);
                fabMenuChat.setVisibility(View.VISIBLE);
                fabAddChat.setVisibility(View.VISIBLE);
            } else {
                fabMenuChat.setVisibility(View.GONE);
                fabAddChat.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void setInboxAdapter(Cursor cursor) {
        if (rvInbox.getAdapter() == null) {
            inboxAdapter = new InboxAdapter(getContext(), cursor, this);
            rvInbox.setAdapter(inboxAdapter);
        } else {
            inboxAdapter.swapCursor(cursor);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Message message = Message.getUserChat((Cursor) inboxAdapter.getItem(position));
        switch (view.getId()) {
            default:
                Bundle bundle = new Bundle();
                bundle.putInt(Extras.SENDER_ID, message.getSenderId());
                bundle.putString(Extras.SENDER_NAME, message.getSender().getFullname());
                bundle.putString(Extras.SENDER_IMAGE, message.getSender().getUser_image());
                bundle.putInt(Extras.APP_THEME, getAppTheme());
                openActivity(MessageActivity.class, bundle);
                break;
        }
    }

    @Override
    public void onMenuItemSelected(View view, int id) {
        Bundle bundle = new Bundle();
        Class aClass = null;
        switch (id) {
            case R.id.menu_student:
                if (mPresenter.getUserType() == UserType.TEACHER) {
                    bundle.putBoolean(Extras.ADD_USER, true);
                    aClass = StudentFilterActivity.class;
                } else if (mPresenter.getUserType() == UserType.STUDENT) {
                    bundle.putString(Extras.USER_TYPE, UserType.STUDENT.getValue());
                    aClass = ChatUserActivity.class;
                }
                break;
            case R.id.menu_teacher:
                bundle.putString(Extras.USER_TYPE, UserType.TEACHER.getValue());
                aClass = ChatUserActivity.class;
                break;
            case R.id.menu_admin:
                bundle.putString(Extras.USER_TYPE, UserType.ADMIN.getValue());
                aClass = ChatUserActivity.class;
                break;
            case R.id.menu_institute:
                bundle.putString(Extras.USER_TYPE, UserType.INSTITUTE.getValue());
                aClass = ChatUserActivity.class;
                break;
            case R.id.menu_parent:
                bundle.putString(Extras.USER_TYPE, UserType.PARENT.getValue());
                aClass = ChatUserActivity.class;
                break;

        }
        if (aClass != null) {
            bundle.putInt(Extras.APP_THEME, getAppTheme());
            openActivityForResult(aClass, bundle, ADD_CHAT_USER);
        }
    }

    @Override
    public void onBackPressed() {
        if (fabMenuChat != null) {
            if (fabMenuChat.isShowing()) {
                fabMenuChat.closeMenu();
                return;
            }
        }
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ADD_CHAT_USER:
                if (resultCode == RESULT_OK) {
                    UserInfo userInfo = data.getParcelableExtra(Extras.ADD_USER);
                    if (userInfo.getId() != mPresenter.getUser().getId()) {
                        UserHelper.insertUser(getContext(), userInfo);
                        UserChatHelper.insertUsersChat(getContext(), userInfo.getId(), mPresenter.getUser().getId());
                        Bundle bundle = new Bundle();
                        bundle.putInt(Extras.SENDER_ID, userInfo.getId());
                        bundle.putString(Extras.SENDER_NAME, userInfo.getUserdetails().get(0).getFullname());
                        bundle.putString(Extras.SENDER_IMAGE, userInfo.getUser_image());
                        bundle.putInt(Extras.APP_THEME, getAppTheme());
                        openActivity(MessageActivity.class, bundle);
                    }
                }
                break;
        }
    }
}
