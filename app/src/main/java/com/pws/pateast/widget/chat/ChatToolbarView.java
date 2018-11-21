package com.pws.pateast.widget.chat;

import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pws.pateast.R;
import com.pws.pateast.api.model.UserInfo;
import com.pws.pateast.utils.ImageUtils;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by intel on 02-Aug-17.
 */

public class ChatToolbarView extends FrameLayout
{
    private LinearLayout layoutConversation;
    private CircleImageView imgProfile;
    private TextView tvTitle, tvSubTitle;


    public ChatToolbarView(@NonNull Context context) {
        this(context,null);
    }

    public ChatToolbarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ChatToolbarView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ChatToolbarView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init()
    {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.toolbar_conversation, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        layoutConversation = (LinearLayout) findViewById(R.id.conversation_image);
        imgProfile = (CircleImageView) findViewById(R.id.conversation_contact_photo);
        tvTitle = (TextView) findViewById(R.id.action_bar_title_1);
        tvSubTitle = (TextView) findViewById(R.id.action_bar_title_2);
    }

    public void setTitle(String title)
    {
        if(!TextUtils.isEmpty(title))
            tvTitle.setText(title);
        else
            tvTitle.setText(R.string.menu_messages);
    }

    public void setSubtitle(String subtitle)
    {
        tvSubTitle.setVisibility(GONE);
        tvSubTitle.setText(subtitle);
        if(!TextUtils.isEmpty(subtitle))
        {
            tvSubTitle.setText(subtitle);
            tvSubTitle.setVisibility(VISIBLE);
        }
    }

    public void setIcon(String iconUrl)
    {
        ImageUtils.setImageUrl(getContext(),imgProfile,iconUrl,R.drawable.user_placeholder);
    }
}
