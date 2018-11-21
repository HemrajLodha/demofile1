package com.pws.pateast.widget.chat;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.pws.calender.AnimatorListener;
import com.pws.pateast.R;
import com.pws.pateast.activity.chat.message.MessageView;
import com.pws.pateast.utils.FontManager;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

/**
 * Created by intel on 28-Jul-17.
 */

public class ChatMessageView extends FrameLayout implements TextWatcher
{
    private static final int TYPING_TIMER_LENGTH = 2000;

    private EmojiconEditText etMessage;
    private ImageButton btnSend;
    private ImageView imgEmo;
    private TextView tvAttachment;
    private EmojIconActions emoIcon;
    private CharSequence[] mTint;
    private int mColor;
    private MessageView messageView;
    private boolean mTyping = false;
    private Handler mTypingHandler = new Handler();

    public ChatMessageView(Context context) {
        super(context);
        initializeViews(context);
    }

    public ChatMessageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray;
        typedArray = context
                .obtainStyledAttributes(attrs, R.styleable.ChatMessageView);
        mTint = typedArray
                .getTextArray(R.styleable.ChatMessageView_messageBoxTint);

        mColor = typedArray
                .getColor(R.styleable.ChatMessageView_messageBoxTint, 0);

        typedArray.recycle();

        initializeViews(context);
    }

    public ChatMessageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray;
        typedArray = context
                .obtainStyledAttributes(attrs, R.styleable.ChatMessageView);
        mTint = typedArray
                .getTextArray(R.styleable.ChatMessageView_messageBoxTint);

        mColor = typedArray
                .getColor(R.styleable.ChatMessageView_messageBoxTint, 0);

        typedArray.recycle();

        initializeViews(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ChatMessageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initializeViews(context);
    }

    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.chat_message_view, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        imgEmo = (ImageView) this.findViewById(R.id.img_emo);
        tvAttachment = (TextView) this.findViewById(R.id.tv_attachment);
        etMessage = (EmojiconEditText) this.findViewById(R.id.et_message);
        btnSend = (ImageButton) this.findViewById(R.id.btn_send);
        etMessage.addTextChangedListener(this);
    }

    public void attachView(MessageView messageView)
    {
        this.messageView = messageView;
    }

    public void setRootView(View rootView)
    {
        emoIcon = new EmojIconActions(getContext(), rootView, etMessage, imgEmo);
        emoIcon.ShowEmojIcon();
    }

    public void setSendClickListener(OnClickListener listener) {
        btnSend.setOnClickListener(listener);
        tvAttachment.setOnClickListener(listener);
    }

    public void setMessageBoxHint(String text) {
        etMessage.setHint(text);
    }

    public String getMessageText() {
        String message = null;
        if (!TextUtils.isEmpty(etMessage.getText())) {
            message = etMessage.getText().toString();
            etMessage.setText("");
        }
        return message;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!mTyping) {
            mTyping = true;
            messageView.emitStartTyping();
        }

        mTypingHandler.removeCallbacks(onTypingTimeout);
        mTypingHandler.postDelayed(onTypingTimeout, TYPING_TIMER_LENGTH);
        animateAttachment(count);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    private Runnable onTypingTimeout = new Runnable() {
        @Override
        public void run() {
            if (!mTyping) return;

            mTyping = false;
            messageView.emitStopTyping();
        }
    };

    private void animateAttachment(int count)
    {
        if(count >= 1 && tvAttachment.getVisibility() == VISIBLE)
        {
            tvAttachment.animate()
                    .translationX(tvAttachment.getWidth())
                    .alpha(1.0f)
                    .setListener(new AnimatorListener() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationStart(animation);
                            tvAttachment.setVisibility(GONE);
                        }
                    });
        }
        else if(count < 1 && tvAttachment.getVisibility() == GONE)
        {
            tvAttachment.animate()
                    .translationX(0)
                    .alpha(1.0f)
                    .setListener(new AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);
                            tvAttachment.setVisibility(VISIBLE);
                        }
                    });
        }
    }
}
