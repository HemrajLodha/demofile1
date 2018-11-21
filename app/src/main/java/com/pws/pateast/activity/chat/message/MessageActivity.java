package com.pws.pateast.activity.chat.message;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.view.BaseRecyclerView;
import com.pws.pateast.R;
import com.pws.pateast.activity.chat.message.observer.MessageAdapterObserver;
import com.pws.pateast.api.model.Message;
import com.pws.pateast.base.Extras;
import com.pws.pateast.base.ResponseAppActivity;
import com.pws.pateast.enums.MessageType;
import com.pws.pateast.events.MessageEvent;
import com.pws.pateast.events.SocketEvent;
import com.pws.pateast.listener.AdapterListener;
import com.pws.pateast.service.upload.UploadService;
import com.pws.pateast.utils.MediaHelper;
import com.pws.pateast.utils.NetworkUtil;
import com.pws.pateast.utils.Utils;
import com.pws.pateast.widget.chat.ChatMessageView;
import com.pws.pateast.widget.chat.ChatToolbarView;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.pws.pateast.events.SocketEvent.EVENT_BLOCK_USER;
import static com.pws.pateast.events.SocketEvent.EVENT_MESSAGE_READ;
import static com.pws.pateast.events.SocketEvent.EVENT_STOP_TYPING;
import static com.pws.pateast.events.SocketEvent.EVENT_TYPING;
import static com.pws.pateast.events.SocketEvent.EVENT_UNBLOCK_USER;

/**
 * Created by intel on 28-Jul-17.
 */
@RuntimePermissions
public class MessageActivity extends ResponseAppActivity implements MessageView, View.OnClickListener, AdapterListener<Integer>, BaseRecyclerAdapter.OnItemClickListener {
    private ChatMessageView messageView;
    private ChatToolbarView toolbarChat;
    private BaseRecyclerView rvMessage;
    private MessageAdapter messageAdapter;
    private MediaHelper mediaHelper;
    private MessagePresenter mPresenter;

    @Override
    protected int getResourceLayout() {
        return R.layout.activity_message;
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

        toolbarChat = findViewById(R.id.toolbar_chat);
        messageView = findViewById(R.id.chat_message_view);
        messageView.setRootView(findViewById(R.id.layout_content));
        messageView.attachView(this);
        rvMessage = findViewById(R.id.rv_message);
        rvMessage.setLoadingMoreEnabled(false);
        rvMessage.setPullRefreshEnabled(false);
        rvMessage.setUpAsChatList();


        mPresenter = new MessagePresenter();
        mPresenter.attachView(this);
        messageView.setSendClickListener(this);

        toolbarChat.setTitle(getReceiverName());
        toolbarChat.setIcon(getReceiverImage());
        mediaHelper = new MediaHelper(getContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter_chat, menu);
        MenuItem itemBlock = menu.findItem(R.id.menu_block);
        MenuItem itemUnBlock = menu.findItem(R.id.menu_unblock);
        if (mPresenter != null) {
            itemBlock.setVisible(!mPresenter.isBlocked());
            itemUnBlock.setVisible(mPresenter.isBlocked());
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_block:
                mPresenter.blockUser(new SocketEvent(EVENT_BLOCK_USER, getReceiverId()), true);
                break;
            case R.id.menu_unblock:
                mPresenter.blockUser(new SocketEvent(EVENT_UNBLOCK_USER, getReceiverId()), false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public int getSenderId() {
        return mPresenter.getUser().getData().getId();
    }

    @Override
    public String getReceiverName() {
        return getIntent().getStringExtra(Extras.SENDER_NAME);
    }

    @Override
    public String getReceiverImage() {
        return getIntent().getStringExtra(Extras.SENDER_IMAGE);
    }

    @Override
    public int getReceiverId() {
        return getIntent().getIntExtra(Extras.SENDER_ID, 0);
    }

    @Override
    public void emitMessageSeen(Message message) {
        mPresenter.onEvent(new MessageEvent(EVENT_MESSAGE_READ, message));
    }

    @Override
    public void emitStartTyping() {
        if (!mPresenter.isBlocked())
            mPresenter.emitSocketEvent(new SocketEvent(EVENT_TYPING, getReceiverId()));
    }

    @Override
    public void emitStopTyping() {
        if (!mPresenter.isBlocked())
            mPresenter.emitSocketEvent(new SocketEvent(EVENT_STOP_TYPING, getReceiverId()));
    }

    @Override
    public void setMessageAdapter(Cursor cursor) {
        if (rvMessage.getAdapter() == null) {
            messageAdapter = new MessageAdapter(getContext(), cursor, this);
            messageAdapter.setMessageView(this);
            messageAdapter.registerAdapterDataObserver(new MessageAdapterObserver(rvMessage, messageAdapter));
            rvMessage.setAdapter(messageAdapter);
        } else {
            messageAdapter.swapCursor(cursor);
        }
    }

    @Override
    public void setSubTitle(final String subTitle) {
        toolbarChat.setSubtitle(subTitle);
    }

    @Override
    public void onCompressStart() {
        showProgressDialog(getString(R.string.preparing));
    }

    @Override
    public void onCompressComplete() {
        hideProgressDialog();
    }

    @Override
    public void uploadFile(MessageType messageType, String filePath) {
        mPresenter.saveFileMessage(messageType, filePath);
    }

    @Override
    public void userBlocked(boolean isBlocked) {
        if (messageView != null)
            messageView.setVisibility(isBlocked ? View.GONE : View.VISIBLE);
        invalidateOptionsMenu();
    }

    @Override
    public boolean isUploading() {
        return mPresenter.isUploading();
    }


    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    @Override
    public void openCamera(int media) {
        Intent intent = null;
        if (media == 0) {
            intent = mediaHelper.dispatchTakePictureIntent(getContext());
            if (intent != null)
                startActivityForResult(intent, MediaHelper.REQUEST_TAKE_PHOTO);
            else
                Utils.showToast(getContext(), R.string.no_camera_exists);
        } else if (media == 1) {
            intent = mediaHelper.dispatchTakeVideoIntent(getContext());
            if (intent != null)
                startActivityForResult(intent, MediaHelper.REQUEST_TAKE_VIDEO);
            else
                Utils.showToast(getContext(), R.string.no_camera_exists);
        }
    }

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    @Override
    public void openGallery(int media) {
        Intent intent = null;
        if (media == 2) {
            intent = mediaHelper.dispatchPickPictureIntent(getContext());
            if (intent != null)
                startActivityForResult(intent, MediaHelper.REQUEST_PICK_PHOTO);
            else
                Utils.showToast(getContext(), R.string.no_gallery_exists);
        } else if (media == 3) {
            intent = mediaHelper.dispatchPickVideoIntent(getContext());
            if (intent != null)
                startActivityForResult(intent, MediaHelper.REQUEST_PICK_VIDEO);
            else
                Utils.showToast(getContext(), R.string.no_gallery_exists);
        } else if (media == 4) {
            intent = mediaHelper.dispatchPickAudioIntent(getContext());
            if (intent != null)
                startActivityForResult(intent, MediaHelper.REQUEST_PICK_AUDIO);
            else
                Utils.showToast(getContext(), R.string.no_gallery_exists);
        } else if (media == 5) {
            intent = mediaHelper.dispatchPickFileIntent(getContext());
            if (intent != null)
                startActivityForResult(intent, MediaHelper.REQUEST_PICK_OTHER);
            else
                Utils.showToast(getContext(), R.string.no_gallery_exists);
        }
    }

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    @Override
    public void viewFile(Uri uri, String type) {
        Intent intent = MediaHelper.dispatchOpenFileIntent(getContext(), uri, type);
        if (intent != null)
            startActivity(intent);
        else
            Utils.showToast(getContext(), R.string.no_app_exists);
    }

    @Override
    public void onClick(int id, Integer value) {
        switch (value) {
            case 0:
            case 1:
                MessageActivityPermissionsDispatcher.openCameraWithCheck(this, value);
                break;
            case 2:
            case 3:
            case 4:
            case 5:
                MessageActivityPermissionsDispatcher.openGalleryWithCheck(this, value);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                if (NetworkUtil.isOnline(getContext()))
                    mPresenter.sendTextMessage(messageView.getMessageText());
                else
                    showMessage(R.string.no_network, true, R.color.colorPrimary);
                break;
            case R.id.tv_attachment:
                mPresenter.chooseMediaFileDialog();
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if (messageAdapter.isSection(position))
            return;
        Message message = Message.getChat((Cursor) messageAdapter.getItem(position));
        switch (view.getId()) {
            case R.id.upload:
                UploadService.startUploadService(getContext(), message);
                break;
            case R.id.container_file:
                mPresenter.viewFile(message);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case MediaHelper.REQUEST_TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    mPresenter.handleFiles(mediaHelper.getMessageType(getContext(), mediaHelper.getUri()), mediaHelper.getUri());
                }
                break;
            case MediaHelper.REQUEST_TAKE_VIDEO:
                if (resultCode == Activity.RESULT_OK) {
                    mPresenter.handleFiles(mediaHelper.getMessageType(getContext(), mediaHelper.getUri()), mediaHelper.getUri());
                }
                break;
            case MediaHelper.REQUEST_PICK_PHOTO:
                if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
                    mPresenter.handleFiles(mediaHelper.getMessageType(getContext(), data.getData()), mediaHelper.getUri());
                }
                break;
            case MediaHelper.REQUEST_PICK_VIDEO:
                if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
                    mPresenter.handleFiles(mediaHelper.getMessageType(getContext(), data.getData()), mediaHelper.getUri());
                }
                break;
            case MediaHelper.REQUEST_PICK_AUDIO:
                if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
                    mPresenter.handleFiles(mediaHelper.getMessageType(getContext(), data.getData()), mediaHelper.getUri());
                }
                break;
            case MediaHelper.REQUEST_PICK_OTHER:
                if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
                    mPresenter.handleFiles(mediaHelper.getMessageType(getContext(), data.getData()), mediaHelper.getUri());
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MessageActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.detachView();
    }


}
