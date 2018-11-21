package com.pws.pateast.activity.chat.message;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.ui.adapter.BaseCursorRecyclerAdapter;
import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.adapter.viewholder.BaseItemViewHolder;
import com.pws.pateast.R;
import com.pws.pateast.api.model.Message;
import com.pws.pateast.enums.MessageStatusType;
import com.pws.pateast.enums.MessageType;
import com.pws.pateast.provider.table.Chat;
import com.pws.pateast.utils.DateUtils;
import com.pws.pateast.widget.MessageStatusView;
import com.pws.pateast.widget.chat.ChatImageView;

import java.util.Calendar;

import static com.pws.pateast.utils.DateUtils.CHAT_DATE_TEMPLATE;
import static com.pws.pateast.utils.DateUtils.TIME_TEMPLATE_12_HOUR;

/**
 * Created by intel on 28-Jul-17.
 */

public class MessageAdapter extends BaseCursorRecyclerAdapter<MessageAdapter.MessageHolder> {
    private int ten, twenty, fifty;

    private MessageView messageView;
    private Calendar todayCalendar;
    private String todayDate;

    public MessageAdapter(Context context, Cursor cursor, BaseRecyclerAdapter.OnItemClickListener onItemClickListener) {
        super(context, cursor, onItemClickListener);
        float scale = context.getResources().getDisplayMetrics().density;
        ten = (int) (10 * scale + 0.5f);
        twenty = (int) (20 * scale + 0.5f);
        fifty = (int) (50 * scale + 0.5f);
    }


    public void setMessageView(MessageView messageView) {
        this.messageView = messageView;
    }

    public MessageView getMessageView() {
        return messageView;
    }


    @Override
    protected int getItemResourceLayout(int viewType) {
        switch (MessageType.getMessageType(viewType)) {
            case TEXT:
                return R.layout.item_text_message;
            case IMAGE:
                return R.layout.item_image_message;
            case AUDIO:
                return R.layout.item_file_message;
            case VIDEO:
                return R.layout.item_video_message;
            case OTHER:
                return R.layout.item_file_message;
            case NONE:
                return R.layout.item_text_message;

        }
        return R.layout.item_text_message;
    }

    @Override
    protected Object getSectionFromCursor(Cursor cursor) {
        long timeInMillis = cursor.getLong(Chat.INDEX_COLUMN_UPDATE_DATE);
        if (todayCalendar == null) {
            todayCalendar = Calendar.getInstance();
            todayDate = DateUtils.toTime(todayCalendar.getTime(), CHAT_DATE_TEMPLATE);
        }
        todayCalendar.setTimeInMillis(timeInMillis);
        String date = DateUtils.toTime(todayCalendar.getTime(), CHAT_DATE_TEMPLATE);
        if (todayDate.equalsIgnoreCase(date))
            return getString(R.string.title_today);
        else
            return date;
    }

    @Override
    protected boolean isSectioned() {
        return true;
    }

    @Override
    public MessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (MessageType.getMessageType(viewType)) {
            case TEXT:
                return new TextHolder(getView(parent, viewType), mItemClickListener);
            case IMAGE:
                return new ImageHolder(getView(parent, viewType), mItemClickListener);
            case AUDIO:
                return new AudioHolder(getView(parent, viewType), mItemClickListener);
            case VIDEO:
                return new VideoHolder(getView(parent, viewType), mItemClickListener);
            case OTHER:
                return new OtherHolder(getView(parent, viewType), mItemClickListener);
            case NONE:
                return new DateHolder(getView(parent, viewType), mItemClickListener);
        }
        return new MessageHolder(getView(parent, viewType), mItemClickListener);
    }

    class DateHolder extends MessageHolder {
        TextView labelMessage;

        public DateHolder(View itemView, BaseRecyclerAdapter.OnItemClickListener onItemClickListener) {
            super(itemView, onItemClickListener);
            labelMessage = (TextView) findViewById(R.id.label_message);
        }

        @Override
        public void bindMessage(Message message) {
            super.bindMessage(message);
            labelMessage.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            labelMessage.setText(message.getData());
        }
    }


    class TextHolder extends MessageHolder {
        TextView labelMessage;

        public TextHolder(View itemView, BaseRecyclerAdapter.OnItemClickListener onItemClickListener) {
            super(itemView, onItemClickListener);
            labelMessage = (TextView) findViewById(R.id.label_message);
        }

        @Override
        public void bindMessage(Message message) {
            super.bindMessage(message);
            labelMessage.setTextColor(ContextCompat.getColor(getContext(), R.color.textColor));
            labelMessage.setText(message.getData());
        }
    }

    class ImageHolder extends MessageHolder {
        ChatImageView imgMessage;

        public ImageHolder(View itemView, BaseRecyclerAdapter.OnItemClickListener onItemClickListener) {
            super(itemView, onItemClickListener);
            imgMessage = (ChatImageView) findViewById(R.id.img_message);
            imgMessage.setClickListener(this);
        }

        @Override
        public void bindMessage(Message message) {
            super.bindMessage(message);
            imgMessage.setMessage(message);
            imgMessage.apply();
        }
    }

    class AudioHolder extends MessageHolder {
        ChatImageView fileMessage;

        public AudioHolder(View itemView, BaseRecyclerAdapter.OnItemClickListener onItemClickListener) {
            super(itemView, onItemClickListener);
            fileMessage = (ChatImageView) findViewById(R.id.file_message);
            fileMessage.setClickListener(this);
        }

        @Override
        public void bindMessage(Message message) {
            super.bindMessage(message);
            fileMessage.setMessage(message);
            fileMessage.apply();
        }
    }

    class VideoHolder extends MessageHolder {
        ChatImageView videoMessage;

        public VideoHolder(View itemView, BaseRecyclerAdapter.OnItemClickListener onItemClickListener) {
            super(itemView, onItemClickListener);
            videoMessage = (ChatImageView) findViewById(R.id.video_message);
            videoMessage.setClickListener(this);
        }

        @Override
        public void bindMessage(Message message) {
            super.bindMessage(message);
            videoMessage.setMessage(message);
            videoMessage.apply();

        }
    }

    class OtherHolder extends MessageHolder {
        ChatImageView fileMessage;

        public OtherHolder(View itemView, BaseRecyclerAdapter.OnItemClickListener onItemClickListener) {
            super(itemView, onItemClickListener);
            fileMessage = (ChatImageView) findViewById(R.id.file_message);
            fileMessage.setClickListener(this);
        }

        @Override
        public void bindMessage(Message message) {
            super.bindMessage(message);
            fileMessage.setMessage(message);
            fileMessage.apply();

        }
    }


    class MessageHolder extends BaseItemViewHolder<Object> {
        TextView labelTime;
        MessageStatusView labelStatus;
        View containerSender, containerMessage, containerDate;
        Calendar start;

        public MessageHolder(View itemView, BaseRecyclerAdapter.OnItemClickListener onItemClickListener) {
            super(itemView, onItemClickListener);

            labelTime = (TextView) findViewById(R.id.label_date);
            labelStatus = (MessageStatusView) findViewById(R.id.label_status);
            containerSender = findViewById(R.id.container_sender);
            containerMessage = findViewById(R.id.container_message);
            containerDate = findViewById(R.id.container_date);
            start = Calendar.getInstance();
        }

        @Override
        public void bind(Object cursor) {
            if (cursor instanceof Cursor)
                bindMessage(Message.getChat((Cursor) cursor));
            else {
                Message message = new Message();
                message.setSenderId(0);
                message.setData(String.valueOf(cursor));
                bindMessage(message);
            }

        }

        public void bindMessage(Message message) {
            setParams(message);
        }

        private void setParams(Message message) {
            LinearLayout.LayoutParams messageParams = (LinearLayout.LayoutParams) containerMessage.getLayoutParams();
            if (message.isSystemMessage()) {
                messageParams.gravity = Gravity.CENTER;
                containerMessage.setBackgroundResource(R.drawable.shape_chip_selected);
                containerDate.setVisibility(View.GONE);
            } else {
                message.setIncomingMessage(message.getSenderId() != getMessageView().getSenderId());
                containerSender.setVisibility(View.VISIBLE);
                start.setTimeInMillis(message.getUpdatedAt());
                labelTime.setText(DateUtils.toTime(start.getTime(), TIME_TEMPLATE_12_HOUR));
                if (message.isIncomingMessage()) {
                    messageParams.gravity = Gravity.LEFT;
                    messageParams.setMargins(ten / 2, ten / 2, fifty * 2, ten / 2);
                    containerSender.setPadding(twenty, ten, ten, ten);
                    containerMessage.setBackgroundResource(R.drawable.ic_incoming);
                    labelStatus.setVisibility(View.GONE);
                    if (message.getMsg_status() != MessageStatusType.STATUS_SEEN.getValue()) {
                        message.setMsg_status(MessageStatusType.STATUS_SEEN.getValue());
                        getMessageView().emitMessageSeen(message);
                        Log.d("Status", String.valueOf(message.getMsg_status()));
                    }
                } else {
                    messageParams.gravity = Gravity.RIGHT;
                    messageParams.setMargins(fifty * 2, ten / 2, ten / 2, ten / 2);
                    containerSender.setPadding(ten, ten, twenty, ten);
                    containerMessage.setBackgroundResource(R.drawable.ic_outgoing);
                    labelStatus.setVisibility(View.VISIBLE);
                    labelStatus.setMessageStatus(message.getMsg_status());
                }

            }
        }
    }

    @Override
    protected int getViewType(int position) {
        return ((Cursor) getItem(position)).getInt(Chat.INDEX_COLUMN_MESSAGE_TYPE);
    }
}
