package com.pws.pateast.activity.chat.inbox;

import android.app.Activity;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.hlab.fabrevealmenu.model.FABMenuItem;
import com.pws.pateast.R;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.exception.RetrofitException;
import com.pws.pateast.api.model.ChatPermission;
import com.pws.pateast.api.model.Message;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.model.Ward;
import com.pws.pateast.api.observer.AppSingleObserver;
import com.pws.pateast.api.observer.ChatMessageObserver;
import com.pws.pateast.api.service.APIService;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.chat.UserChatHelper;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.events.InboxEvent;
import com.pws.pateast.service.sync.SyncService;
import com.pws.pateast.utils.Preference;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.pws.pateast.base.Extras.CHAT_SYNC_DATA;
import static com.pws.pateast.base.Extras.CHAT_SYNC_TYPE;
import static com.pws.pateast.service.sync.SyncService.CHAT_USERS;

/**
 * Created by intel on 25-Jul-17.
 */

public class InboxPresenter extends AppPresenter<InboxView> implements LoaderManager.LoaderCallbacks<Cursor> {
    private APIService apiService;
    @Inject
    ServiceBuilder serviceBuilder;
    @Inject
    Preference preference;

    private User user;
    private Ward ward;
    private InboxView mInboxView;

    private int INBOX_LOADER_ID = 1;
    private InboxObserver inboxObserver;

    @Override
    public InboxView getView() {
        return mInboxView;
    }

    @Override
    public void attachView(InboxView view) {
        mInboxView = view;
        getComponent().inject(this);
        user = preference.getUser();
        ward = preference.getWard();
        ((Activity) getView()).getLoaderManager().initLoader(INBOX_LOADER_ID, null, this);
        inboxObserver = new InboxObserver();
    }

    @Override
    public void detachView() {
        ((Activity) getView()).getLoaderManager().destroyLoader(INBOX_LOADER_ID);
        super.detachView();
    }

    public UserType getUserType() {
        return UserType.getUserType(getUser().getUser_type());
    }

    public User getUser() {
        return user.getData();
    }

    public void getChatPermissions() {
        apiService = serviceBuilder.createService(APIService.class);

        HashMap<String, String> params = serviceBuilder.getParams();
        params.put("id", String.valueOf(user.getData().getId()));
        switch (UserType.getUserType(user.getData().getUser_type())) {
            case PARENT:
                params.put("masterId", String.valueOf(ward.getMasterId()));
                break;
            default:
                params.put("masterId", String.valueOf(user.getData().getMasterId()));
                break;
        }
        disposable = apiService.getPermissions(params)
                .subscribeOn(getApp().getSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppSingleObserver<ChatPermission>() {

                    @Override
                    public void onResponse(ChatPermission response) {
                        preference.setChatPermission(new Gson().toJson(response.getPermissions()));
                        getView().setChatUser(getChatUser(preference.getChatPermission()));
                        //TODO will handle for Non Permission User
                        if (response.getPermissions().isEmpty()) {
                            getView().setInboxAdapter(null);
                            onError(new RetrofitException(inboxObserver.getMessage(), RetrofitException.ERROR_TYPE_MESSAGE));
                        }
                    }

                    @Override
                    public InboxPresenter getPresenter() {
                        return InboxPresenter.this;
                    }
                });
    }

    private ArrayList<FABMenuItem> getChatUser(ArrayList<String> chatPermissions) {
        ArrayList<FABMenuItem> fabMenuItems = new ArrayList<>();

        for (String permission : chatPermissions) {
            switch (UserType.getUserType(permission)) {
                case STUDENT:
                    fabMenuItems.add(new FABMenuItem(R.id.menu_student, getString(R.string.menu_students), ContextCompat.getDrawable(getContext(), R.drawable.ic_student)));
                    break;
                case TEACHER:
                    fabMenuItems.add(new FABMenuItem(R.id.menu_teacher, getString(R.string.menu_teachers), ContextCompat.getDrawable(getContext(), R.drawable.ic_teacher)));
                    break;
                case ADMIN:
                    fabMenuItems.add(new FABMenuItem(R.id.menu_admin, getString(R.string.menu_admins), ContextCompat.getDrawable(getContext(), R.drawable.ic_admin)));
                    break;
                case INSTITUTE:
                    fabMenuItems.add(new FABMenuItem(R.id.menu_institute, getString(R.string.menu_institutes), ContextCompat.getDrawable(getContext(), R.drawable.ic_institute)));
                    break;
                case PARENT:
                    fabMenuItems.add(new FABMenuItem(R.id.menu_parent, getString(R.string.menu_parents), ContextCompat.getDrawable(getContext(), R.drawable.ic_parent)));
                    break;
            }
        }
        return fabMenuItems;
    }

    @Subscribe
    public void OnEvent(InboxEvent event) {
        getUsersChat();
    }

    public void getUsersChat() {
        ((Activity) getView()).getLoaderManager().restartLoader(INBOX_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return UserChatHelper.getUserChats(getContext());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        inboxObserver.onSuccess(cursor);

        Bundle bundle = new Bundle();
        bundle.putInt(CHAT_SYNC_TYPE, CHAT_USERS);
        bundle.putString(CHAT_SYNC_DATA, getSenderIds(cursor));
        SyncService.startSyncService(getContext(), bundle);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private String getSenderIds(Cursor cursor) {
        List<Integer> senderIds = new ArrayList();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    int senderId = Message.getSenderId(cursor);
                    if (senderId != 0)
                        senderIds.add(senderId);
                } while (cursor.moveToNext());
            }
        }

        return TextUtils.join(",", senderIds);
    }

    class InboxObserver extends ChatMessageObserver<Cursor> {

        @Override
        public void onResponse(Cursor response) {
            getView().setInboxAdapter(response);
        }

        @Override
        public InboxPresenter getPresenter() {
            return InboxPresenter.this;
        }

        @Override
        public String getMessage() {
            return getString(R.string.no_result_found);
        }
    }
}
