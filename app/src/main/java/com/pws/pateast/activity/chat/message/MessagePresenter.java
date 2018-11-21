package com.pws.pateast.activity.chat.message;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentProviderResult;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.StringRes;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.pws.pateast.Constants;
import com.pws.pateast.R;
import com.pws.pateast.api.ServiceBuilder;
import com.pws.pateast.api.model.Message;
import com.pws.pateast.api.model.User;
import com.pws.pateast.base.AppPresenter;
import com.pws.pateast.chat.ChatHelper;
import com.pws.pateast.chat.UserChatHelper;
import com.pws.pateast.enums.MessageStatusType;
import com.pws.pateast.enums.MessageType;
import com.pws.pateast.enums.NotificationType;
import com.pws.pateast.enums.UploadingStatus;
import com.pws.pateast.events.MessageEvent;
import com.pws.pateast.events.SocketEvent;
import com.pws.pateast.events.UploadEvent;
import com.pws.pateast.listener.AdapterListener;
import com.pws.pateast.service.upload.UploadService;
import com.pws.pateast.service.upload.task.ImageCompressAsyncTask;
import com.pws.pateast.service.upload.task.VideoCompressAsyncTask;
import com.pws.pateast.utils.DialogUtils;
import com.pws.pateast.utils.FileUtils;
import com.pws.pateast.utils.Preference;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import static com.pws.pateast.events.SocketEvent.EVENT_BLOCK_USER;
import static com.pws.pateast.events.SocketEvent.EVENT_CONNECT;
import static com.pws.pateast.events.SocketEvent.EVENT_IS_BLOCKED;
import static com.pws.pateast.events.SocketEvent.EVENT_IS_CONNECTED;
import static com.pws.pateast.events.SocketEvent.EVENT_IS_ONLINE;
import static com.pws.pateast.events.SocketEvent.EVENT_MESSAGE_READ;
import static com.pws.pateast.events.SocketEvent.EVENT_OFFLINE;
import static com.pws.pateast.events.SocketEvent.EVENT_ONLINE;
import static com.pws.pateast.events.SocketEvent.EVENT_ON_CONNECT;
import static com.pws.pateast.events.SocketEvent.EVENT_ON_DISCONNECT;
import static com.pws.pateast.events.SocketEvent.EVENT_SEND_MESSAGE;
import static com.pws.pateast.events.SocketEvent.EVENT_STOP_TYPING;
import static com.pws.pateast.events.SocketEvent.EVENT_TYPING;
import static com.pws.pateast.events.SocketEvent.EVENT_UNBLOCK_USER;
import static com.pws.pateast.utils.NotificationHelper.showNotificationWithIntent;
import static io.socket.client.Socket.EVENT_MESSAGE;


/**
 * Created by intel on 28-Jul-17.
 */

public class MessagePresenter extends AppPresenter<MessageView> implements LoaderManager.LoaderCallbacks<Cursor> {

    @Inject
    EventBus eventBus;

    @Inject
    Preference preference;

    private User user;

    private MessageView mMessageView;


    private int MESSAGE_LOADER_ID = 2;
    private boolean isOnline;
    private boolean isBlocked;
    protected boolean connected;

    @Override
    public MessageView getView() {
        return mMessageView;
    }

    @Override
    public void attachView(MessageView view) {
        mMessageView = view;
        getComponent().inject(this);
        eventBus.register(this);
        user = preference.getUser();
        eventBus.post(new SocketEvent(EVENT_IS_CONNECTED));
        ((Activity) getView()).getLoaderManager().initLoader(MESSAGE_LOADER_ID, null, this);
    }

    @Override
    public void detachView() {
        eventBus.unregister(this);
        ((Activity) getView()).getLoaderManager().destroyLoader(MESSAGE_LOADER_ID);
        super.detachView();
    }


    public User getUser() {
        return user;
    }

    public boolean isUploading() {
        return eventBus.hasSubscriberForEvent(UploadEvent.class);
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void sendTextMessage(String data) {
        if (data != null && !TextUtils.isEmpty(data.trim())) {
            Message message = new Message();
            message.setSenderId(getView().getSenderId());
            message.setReceiverId(getView().getReceiverId());
            message.setData(data.trim());
            message.setType(MessageType.TEXT.getValue());
            message.setMsg_status(MessageStatusType.STATUS_SAVE.getValue());
            message.setCreatedAt(System.currentTimeMillis());
            message.setUpdatedAt(message.getCreatedAt());
            eventBus.post(new SocketEvent(EVENT_SEND_MESSAGE, message));
        }
    }

    public void saveFileMessage(MessageType messageType, String filePath) {
        if (filePath != null && !TextUtils.isEmpty(filePath.trim())) {
            Message message = new Message();
            message.setSenderId(getView().getSenderId());
            message.setReceiverId(getView().getReceiverId());
            message.setUri(filePath.trim());
            message.setType(messageType.getValue());
            message.setMsg_status(MessageStatusType.STATUS_SAVE.getValue());
            message.setUploadStatus(UploadingStatus.WAITING.getUploadStatus());
            message.setUploadPercentage(0);
            message.setCreatedAt(System.currentTimeMillis());
            message.setUpdatedAt(message.getCreatedAt());
            ContentProviderResult[] results = ChatHelper.insertChat(getContext(), message, null);
            UserChatHelper.insertUsersChat(getContext(), user.getData().getId(), results);
            if (results != null && results.length >= 1) {
                if (results[0].uri != null) {
                    List<String> list = results[0].uri.getPathSegments();
                    message.setChatId(Integer.parseInt(list.get(1)));
                    UploadService.startUploadService(getContext(), message);
                }
            }
        } else {
            getView().showMessage(R.string.validate_file_type, false, R.color.colorPrimary);
        }
    }

    public void viewFile(Message message) {
        if (message.getUri() != null) {
            File file = new File(message.getUri());
            if (file.exists()) {
                MessageActivityPermissionsDispatcher.viewFileWithCheck((MessageActivity) getView(), FileProvider.getUriForFile(getContext(), Constants.FILE_AUTHORITY, file), FileUtils.getMimeType(file));
            } else if (message.getUrl() != null) {
                MessageActivityPermissionsDispatcher.viewFileWithCheck((MessageActivity) getView(), Uri.parse(message.getUrl()), FileUtils.getMimeType(message.getUrl()));
            }
        } else if (message.getUrl() != null) {
            MessageActivityPermissionsDispatcher.viewFileWithCheck((MessageActivity) getView(), Uri.parse(ServiceBuilder.IMAGE_URL + message.getUrl()), FileUtils.getMimeType(message.getUrl()));
        }
    }


    public void handleFiles(MessageType messageType, Uri uri) {
        switch (messageType) {
            case IMAGE:
                handleActionImage(uri);
                break;
            case VIDEO:
                handleActionVideo(uri);
                break;
            case AUDIO:
                handleActionAudio(uri);
                break;
            case OTHER:
                handleActionFile(uri);
                break;
            case NONE:
                getView().showMessage(R.string.validate_file_type, false, R.color.colorPrimary);
                break;
        }
    }

    private void handleActionImage(Uri uri) {
        new ImageCompressAsyncTask(getContext(), getView()).execute(uri);
    }

    private void handleActionVideo(Uri uri) {
        new VideoCompressAsyncTask(getContext(), getView()).execute(uri);
    }

    private void handleActionAudio(Uri uri) {
        getView().uploadFile(MessageType.AUDIO, FileUtils.getPath(getContext(), uri));
    }

    private void handleActionFile(Uri uri) {
        getView().uploadFile(MessageType.OTHER, FileUtils.getPath(getContext(), uri));
    }


    public void chooseMediaFileDialog() {
        String[] options = new String[]{getString(R.string.camera), getString(R.string.video), getString(R.string.pick_image), getString(R.string.pick_video), getString(R.string.pick_audio), getString(R.string.pick_file)};

        DialogUtils.showSingleChoiceDialog(getContext(), getString(R.string.app_name), options, (MessageActivity) getView(), getString(R.string.ok), getString(R.string.cancel));
    }

    public void blockUser(final SocketEvent event, boolean block) {
        DialogUtils.showDialog(getContext(), R.string.app_name, block ? R.string.block_user : R.string.unblock_user, new AdapterListener<String>() {
            @Override
            public void onClick(int id, String value) {
                switch (id) {
                    case 0:
                        emitSocketEvent(event);
                        break;
                }
            }
        }, R.string.yes, R.string.no);
    }

    public void emitSocketEvent(SocketEvent event) {
        eventBus.post(event);
    }

    private void setReceiverAction(String receiverAction, int receiverId) {
        if (getView().getReceiverId() == receiverId)
            setReceiverAction(receiverAction);
    }

    private void setReceiverAction(String receiverAction) {
        getView().setSubTitle(receiverAction);
    }

    protected void showMessage(@StringRes int message) {
        getView().showDialog(getString(R.string.app_name),
                getString(message),
                null,
                R.string.ok);
    }


    @Subscribe
    public void onEvent(final SocketEvent event) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                switch (event.getEventType()) {
                    case EVENT_ON_CONNECT:
                        connected = true;
                        eventBus.post(new SocketEvent(SocketEvent.EVENT_IS_ONLINE, getView().getReceiverId()));
                        eventBus.post(new SocketEvent(SocketEvent.EVENT_IS_BLOCKED, getView().getReceiverId()));
                        getView().hideDialog();
                        break;
                    case EVENT_ON_DISCONNECT:
                        connected = false;
                        eventBus.post(new SocketEvent(EVENT_CONNECT));
                        getView().userBlocked(true);
                        showMessage(R.string.unable_to_connect);
                        break;
                }
            }
        });

    }

    @Subscribe
    public void onEvent(final MessageEvent event) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Message message = event.getMessage();
                switch (event.getEventType()) {
                    case EVENT_MESSAGE:
                        if (getView().getReceiverId() != message.getSenderId()) {
                            showNotificationWithIntent(getContext(), getString(R.string.app_name), message.getData(), NotificationType.MESSAGE.getValue());
                        }
                        break;
                    case EVENT_MESSAGE_READ:
                        eventBus.post(new SocketEvent(EVENT_MESSAGE_READ, message));
                        break;
                    case EVENT_TYPING:
                        setReceiverAction("Typing...", event.getReceiverId());
                        break;
                    case EVENT_STOP_TYPING:
                        setReceiverAction(isOnline ? "Online" : null, event.getReceiverId());
                        break;
                    case EVENT_ONLINE:
                        isOnline = true;
                        setReceiverAction("Online", event.getReceiverId());
                        break;
                    case EVENT_IS_ONLINE:
                        isOnline = event.getReceiverIds().length() != 0;
                        setReceiverAction(isOnline ? "Online" : null);
                        break;
                    case EVENT_OFFLINE:
                        isOnline = false;
                        setReceiverAction(null, event.getReceiverId());
                        break;
                    case EVENT_IS_BLOCKED:
                        if (event.getMessage().isStatus()) {
                            isBlocked = event.getMessage().isBlocked();
                            getView().userBlocked(isBlocked);
                        }
                        break;
                    case EVENT_BLOCK_USER:
                        isBlocked = event.getMessage().isStatus();
                        getView().userBlocked(isBlocked);
                        break;
                    case EVENT_UNBLOCK_USER:
                        isBlocked = !event.getMessage().isStatus();
                        getView().userBlocked(isBlocked);
                        break;
                }
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return ChatHelper.getChat(getContext(), getView().getSenderId(), getView().getReceiverId());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        getView().setMessageAdapter(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


}
