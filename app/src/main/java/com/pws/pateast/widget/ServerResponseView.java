package com.pws.pateast.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pws.pateast.R;
import com.pws.pateast.utils.ColorUtil;


/**
 * Created by pws-A on 3/15/2017.
 */

public class ServerResponseView extends LinearLayout {
    private TextView tvMessage;
    private Button buttonAction;
    private ImageView imgIcon;


    public ServerResponseView(Context context) {
        super(context);
        init();
    }

    public ServerResponseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ServerResponseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ServerResponseView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.server_response_view, this, true);
        tvMessage = (TextView) mView.findViewById(R.id.tv_message);
        buttonAction = (Button) mView.findViewById(R.id.btn_action);
        imgIcon = (ImageView) mView.findViewById(R.id.img_icon);

        Drawable wrapDrawable = DrawableCompat.wrap(buttonAction.getBackground());
        DrawableCompat.setTint(wrapDrawable, ColorUtil.getAttrColor(getContext(), R.attr.colorPrimary));
    }

    public void setOnActionClickListener(OnClickListener onClickListener) {
        if (buttonAction != null) {
            buttonAction.setOnClickListener(onClickListener);
        }
    }

    public void setActionButtonText(String actionText) {
        if (buttonAction == null || TextUtils.isEmpty(actionText)) {
            return;
        }
        buttonAction.setText(actionText);
        setActionButtonVisibility(true);
    }

    public void setActionButtonVisibility(boolean visible) {
        if (buttonAction == null) {
            return;
        }
        buttonAction.setVisibility(visible ? VISIBLE : GONE);
    }

    public void setResponseMessage(String text) {
        if (tvMessage == null || TextUtils.isEmpty(text)) {
            return;
        }

        tvMessage.setText(text);
    }

    public void setResponseMessageVisibility(boolean visible) {
        if (tvMessage == null) {
            return;
        }
        tvMessage.setVisibility(visible ? VISIBLE : GONE);
    }

    public void setImgIcon(int icon) {
        if (imgIcon == null || icon == 0) {
            return;
        }

        imgIcon.setImageResource(icon);
    }

    public void setIconVisibility(boolean visible) {
        if (imgIcon == null) {
            return;
        }
        imgIcon.setVisibility(visible ? VISIBLE : GONE);
    }
}
