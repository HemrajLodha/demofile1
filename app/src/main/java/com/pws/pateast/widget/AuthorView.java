package com.pws.pateast.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.pws.pateast.R;

/**
 * Created by intel on 22-Aug-17.
 */

public class AuthorView extends FrameLayout {
    private TextView tvMessage, tvAuthor;

    public AuthorView(@NonNull Context context) {
        this(context, null);
    }

    public AuthorView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AuthorView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeViews(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AuthorView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initializeViews(attrs);
    }

    private void initializeViews(AttributeSet attrs) {
        inflate();
        try {
            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.AuthorView);
            setMessage(array.getString(R.styleable.AuthorView_message));
            setMessageColor(array.getColor(R.styleable.AuthorView_messageColor, 0));
            setAuthor(array.getString(R.styleable.AuthorView_author));
            setAuthorColor(array.getColor(R.styleable.AuthorView_authorColor, 0));
            array.recycle();
        } catch (Exception e) {

        }

    }

    private void inflate() {
        LayoutInflater.from(getContext()).inflate(R.layout.author_quote_item, this);

        tvMessage = (TextView) findViewById(R.id.message);
        tvAuthor = (TextView) findViewById(R.id.author);

    }


    public void setMessage(String message) {
        if (!TextUtils.isEmpty(message))
            tvMessage.setText(message);

    }

    public void setMessage(@StringRes int message) {
        setMessage(getContext().getString(message));
    }


    public void setAuthor(String author) {
        if (!TextUtils.isEmpty(author))
            tvAuthor.setText(author);
    }

    public void setAuthor(@StringRes int author) {
        setAuthor(getContext().getString(author));
    }


    public void setMessageColor(@ColorInt int messageColor) {
        tvMessage.setTextColor(messageColor);
    }


    public void setAuthorColor(@ColorInt int authorColor) {
        tvAuthor.setTextColor(authorColor);
    }
}
