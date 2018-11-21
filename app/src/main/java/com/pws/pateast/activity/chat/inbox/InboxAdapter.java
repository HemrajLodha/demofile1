package com.pws.pateast.activity.chat.inbox;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.ui.adapter.BaseCursorRecyclerAdapter;
import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.adapter.viewholder.BaseItemViewHolder;
import com.pws.pateast.R;
import com.pws.pateast.api.model.Message;
import com.pws.pateast.chat.ChatHelper;
import com.pws.pateast.enums.MessageType;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.utils.DateUtils;
import com.pws.pateast.utils.ImageUtils;
import com.pws.pateast.utils.Utils;

import java.util.Calendar;

import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

import static com.pws.pateast.utils.DateUtils.TIME_TEMPLATE_12_HOUR;

/**
 * Created by intel on 27-Jul-17.
 */

public class InboxAdapter extends BaseCursorRecyclerAdapter<InboxAdapter.InboxHolder> {


    public InboxAdapter(Context context, Cursor cursor, BaseRecyclerAdapter.OnItemClickListener onItemClickListener) {
        super(context, cursor, onItemClickListener);
    }

    @Override
    protected int getItemResourceLayout(int viewType) {
        return R.layout.item_inbox;
    }

    @Override
    protected Object getSectionFromCursor(Cursor cursor) {
        return null;
    }

    @Override
    protected boolean isSectioned() {
        return false;
    }

    @Override
    protected int getViewType(int position) {
        return 0;
    }

    @Override
    public InboxHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new InboxHolder(getView(parent, viewType), mItemClickListener);
    }

    class InboxHolder extends BaseItemViewHolder<Cursor> {
        private ImageView imgProfile;
        private TextView tvSenderName;
        private TextView tvTime;
        private TextView tvSenderType;
        private TextView tvUnreadCount;
        private EmojiconTextView tvLastMessage;
        private Calendar start;

        public InboxHolder(View view, BaseRecyclerAdapter.OnItemClickListener onItemClickListener) {
            super(view, onItemClickListener);
            imgProfile = (ImageView) findViewById(R.id.img_profile);
            tvSenderName = (TextView) findViewById(R.id.tv_sender_name);
            tvTime = (TextView) findViewById(R.id.tv_time);
            tvSenderType = (TextView) findViewById(R.id.tv_sender_type);
            tvUnreadCount = (TextView) findViewById(R.id.tv_unread_count);
            tvLastMessage = (EmojiconTextView) findViewById(R.id.tv_last_message);
            start = Calendar.getInstance();

        }

        public void bindMessage(Message message) {

            ImageUtils.setImageUrl(getContext(), imgProfile, message.getSender().getUser_image(), R.drawable.avatar1);
            tvSenderName.setText(message.getSender().getFullname());
            tvSenderType.setText(UserType.getUserType(message.getSender().getUser_type()).getName());
            setMessage(message);
            if (message.getUpdatedAt() != 0) {
                start.setTimeInMillis(message.getUpdatedAt());
                tvTime.setText(DateUtils.toTime(start.getTime(), TIME_TEMPLATE_12_HOUR));
                tvTime.setVisibility(View.VISIBLE);
            } else {
                tvTime.setVisibility(View.GONE);
            }
            new UnreadCountTask().execute(message);
        }

        private void setMessage(Message message) {
            switch (MessageType.getMessageType(message.getType())) {
                case TEXT:
                    tvLastMessage.setText(message.getData());
                    break;
                case IMAGE:
                    tvLastMessage.setText(Utils.getEmoByUnicode(0x1F4F7) + " Image");
                    break;
                case VIDEO:
                    tvLastMessage.setText(Utils.getEmoByUnicode(0x1F4F9) + " Video");
                    break;
                case AUDIO:
                    tvLastMessage.setText(Utils.getEmoByUnicode(0x1F4E2) + " Audio");
                    break;
                case OTHER:
                    tvLastMessage.setText(Utils.getEmoByUnicode(0x1F4C2) + " Attachment");
                    break;
            }
        }

        @Override
        public void bind(Cursor cursor) {
            bindMessage(Message.getUserChat(cursor));
        }

        class UnreadCountTask extends AsyncTask<Message, Void, Message> {

            @Override
            protected Message doInBackground(Message... params) {
                Message message = params[0];
                message.setUnread(ChatHelper.getUnreadChatCount(getContext(), message.getSenderId(), message.getReceiverId()));
                return message;
            }

            @Override
            protected void onPostExecute(Message message) {
                tvUnreadCount.setText(String.valueOf(message.getUnread()));
                tvUnreadCount.setVisibility(message.getUnread() == 0 ? View.INVISIBLE : View.VISIBLE);
            }
        }
    }


}
