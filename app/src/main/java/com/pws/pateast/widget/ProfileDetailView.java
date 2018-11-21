package com.pws.pateast.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.pws.pateast.R;
import com.pws.pateast.utils.FontManager;

/**
 * Created by intel on 05-Sep-17.
 */

public class ProfileDetailView extends FrameLayout implements View.OnClickListener {
    private Typeface mTypeFace;
    private final static int app = 1;
    private final static int fa = 2;
    private final static int mdi = 3;
    private int font = mdi;

    private TextView text1, text2, icon;

    private OnClickListener onClickListener;

    public ProfileDetailView(@NonNull Context context) {
        this(context, null);
    }

    public ProfileDetailView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProfileDetailView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeViews(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ProfileDetailView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initializeViews(attrs);
    }

    private void initializeViews(AttributeSet attrs) {
        inflate();
        try {
            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.ProfileDetailView);
            setDetailTag(array.getString(R.styleable.ProfileDetailView_tag));
            setDetail(array.getString(R.styleable.ProfileDetailView_detail));
            setFont(array.getInt(R.styleable.ProfileDetailView_fontFace, mdi));
            setFontIcon(array.getString(R.styleable.ProfileDetailView_fontIcon));
            array.recycle();
        } catch (Exception e) {

        }

    }

    private void inflate() {
        LayoutInflater.from(getContext()).inflate(R.layout.profile_detail_view, this);

        text1 = findViewById(android.R.id.text1);
        text2 = findViewById(android.R.id.text2);
        icon = findViewById(android.R.id.icon);
    }

    public void setDetail(CharSequence detail) {
        if (!TextUtils.isEmpty(detail))
            text2.setText(detail);

    }

    public void setDetail(@StringRes int detail) {
        setDetail(getContext().getString(detail));
    }

    public void setDetailTag(String detailTag) {
        if (!TextUtils.isEmpty(detailTag))
            text1.setText(detailTag);

    }

    public void setDetailTag(@StringRes int detailTag) {
        setDetailTag(getContext().getString(detailTag));
    }

    public void setFontIconVisibility(boolean visible) {
        icon.setVisibility(visible ? VISIBLE : GONE);
    }

    public void setFontIcon(String fontIcon) {
        if (!TextUtils.isEmpty(fontIcon)) {
            icon.setText(fontIcon);
            icon.setVisibility(VISIBLE);
            icon.setOnClickListener(this);
        }
    }

    public void setFontIcon(@StringRes int fontIcon) {
        setFontIcon(getContext().getString(fontIcon));
    }


    public void setFont(int font) {
        this.font = font;
        if (font == app)
            mTypeFace = FontManager.getAppFont(getContext());
        else if (font == fa)
            mTypeFace = FontManager.getFontAwesomeFont(getContext());
        else if (font == mdi)
            mTypeFace = FontManager.getMaterialDesignFont(getContext());
        icon.setTypeface(mTypeFace);
    }

    public void setOnIconClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public void onClick(View view) {
        if (onClickListener != null)
            onClickListener.onClick(this);
    }
}
