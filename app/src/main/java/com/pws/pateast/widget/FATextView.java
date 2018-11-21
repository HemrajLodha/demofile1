package com.pws.pateast.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.pws.pateast.utils.FontManager;

/**
 * Created by planet on 5/3/2017.
 */

public class FATextView extends AppCompatTextView {

    public FATextView(Context context) {
        super(context);
        init(context, null);
    }

    public FATextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FATextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setTypeface(FontManager.getFontAwesomeFont(context));
    }
}
