package com.pws.pateast.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.pws.pateast.R;
import com.pws.pateast.utils.ColorUtil;

/**
 * Created by planet on 10/17/2017.
 */

public class FooterLoadingView extends ProgressBar {

    private String text;
    private Paint textPaint;
    private Rect bounds;

    public FooterLoadingView(Context context) {
        super(context);
        init();
    }

    public FooterLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FooterLoadingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        text = "";
        textPaint = new Paint();
        textPaint.setColor(ContextCompat.getColor(getContext(), R.color.textColor));
        textPaint.setTextSize(getContext().getResources().getDimension(R.dimen.text_size_small));
        bounds = new Rect();
        int color = ColorUtil.getAttrColor(getContext(), R.attr.colorPrimary);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ColorStateList stateList = ColorStateList.valueOf(color);
            setProgressTintList(stateList);
            setSecondaryProgressTintList(stateList);
            setIndeterminateTintList(stateList);
        } else {
            PorterDuff.Mode mode = PorterDuff.Mode.SRC_IN;
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
                mode = PorterDuff.Mode.MULTIPLY;
            }
            if (getIndeterminateDrawable() != null)
                getIndeterminateDrawable().setColorFilter(color, mode);
            if (getProgressDrawable() != null)
                getProgressDrawable().setColorFilter(color, mode);
        }
        setIndeterminate(true);
        getLayoutParams().width = ViewGroup.MarginLayoutParams.MATCH_PARENT;
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        int x = getWidth() / 2 - bounds.centerX();
        int y = getHeight() / 2 - bounds.centerY();
        canvas.drawText(text, x, y, textPaint);
    }

    public synchronized void setText(String text) {
        this.text = text;
        drawableStateChanged();
    }

    public void setTextColor(int color) {
        textPaint.setColor(color);
        drawableStateChanged();
    }
}
