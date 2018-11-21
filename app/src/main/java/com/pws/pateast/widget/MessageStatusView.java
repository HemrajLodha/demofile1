package com.pws.pateast.widget;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.pws.pateast.R;
import com.pws.pateast.enums.MessageStatusType;

import static android.util.TypedValue.COMPLEX_UNIT_SP;

/**
 * Created by intel on 31-Jul-17.
 */

public class MessageStatusView extends FTextView {
    private int textSize = 12;

    public MessageStatusView(Context context) {
        this(context, null);
    }

    public MessageStatusView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MessageStatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        setTextColor(ContextCompat.getColor(getContext(), R.color.unread));
        setTextSize(COMPLEX_UNIT_SP, textSize);
        setMessageStatus(MessageStatusType.STATUS_SAVE.getValue());
    }

    public void setMessageStatus(int messageStatus) {
        switch (MessageStatusType.getMessageStatusType(messageStatus)) {
            case STATUS_SAVE:
                setText(R.string.mdi_clock);
                setTextColor(ContextCompat.getColor(getContext(), R.color.unread));
                break;
            case STATUS_SENT:
                setText(R.string.mdi_check);
                setTextColor(ContextCompat.getColor(getContext(), R.color.unread));
                break;
            case STATUS_RECEIVED:
                setText(R.string.mdi_check_all);
                setTextColor(ContextCompat.getColor(getContext(), R.color.unread));
                break;
            case STATUS_SEEN:
                setText(R.string.mdi_check_all);
                setTextColor(ContextCompat.getColor(getContext(), R.color.read));
                break;
        }
    }
}
