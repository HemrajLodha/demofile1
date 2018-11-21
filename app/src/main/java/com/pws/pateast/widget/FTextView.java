package com.pws.pateast.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.TextView;

import com.pws.pateast.R;
import com.pws.pateast.utils.FontManager;

/**
 * Created by planet on 9/19/2017.
 */

public class FTextView extends AppCompatTextView {
    private Typeface mTypeFace;
    private final static int app = 1;
    private final static int fa = 2;
    private final static int mdi = 3;
    private int font = app;

    public FTextView(Context context) {
        super(context);
        init(null);
    }

    public FTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public FTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        try {
            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.FTextView);
            font = array.getInt(R.styleable.FTextView_fontFace, app);
            array.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (font == app)
            mTypeFace = FontManager.getAppFont(getContext());
        else if (font == fa)
            mTypeFace = FontManager.getFontAwesomeFont(getContext());
        else if (font == mdi)
            mTypeFace = FontManager.getMaterialDesignFont(getContext());
        setTypeface(mTypeFace);
    }
}
