package com.pws.pateast.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pws.pateast.R;
import com.pws.pateast.utils.FontManager;

/**
 * Created by intel on 18-Aug-17.
 */

public class FontTabLayout extends TabLayout {
    private Typeface mTypeFace;
    private final static int app = 1;
    private final static int fa = 2;
    private final static int mdi = 3;
    private int font = app;

    public FontTabLayout(Context context) {
        this(context,null);
    }

    public FontTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FontTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(attrs);
    }


    private void initialize(AttributeSet attrs) {
        try {
            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.FontTabLayout);
            font = array.getInt(R.styleable.FontTabLayout_fontFace, app);
            array.recycle();
        } catch (Exception e) {

        }
        if (font == app)
            mTypeFace = FontManager.getAppFont(getContext());
        else if (font == fa)
            mTypeFace = FontManager.getFontAwesomeFont(getContext());
        else if (font == mdi)
            mTypeFace = FontManager.getMaterialDesignFont(getContext());
    }


    @Override
    public void addTab(@NonNull Tab tab, int position, boolean setSelected) {
        super.addTab(tab, position, setSelected);
        setTabTypeFace(tab);
    }

    private void setTabTypeFace(Tab tab)
    {
        ViewGroup tabLayoutView = (ViewGroup)getChildAt(0);
        ViewGroup tabView = (ViewGroup) tabLayoutView.getChildAt(tab.getPosition());
        int tabViewChildCount = tabView.getChildCount();
        for (int i = 0; i < tabViewChildCount; i++) {
            View tabViewChild = tabView.getChildAt(i);
            // Find the TextView in the tab
            if (tabViewChild instanceof TextView) {
                TextView tabTextView = (TextView)tabViewChild;
                // Set the TextView's font
                tabTextView .setTypeface(mTypeFace, Typeface.BOLD);
                tabTextView .setAllCaps(false);
            }
        }
    }
}
