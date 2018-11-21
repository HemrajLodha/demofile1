package com.pws.pateast.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.pws.pateast.R;

/**
 * Created by intel on 06-Mar-17.
 */

public class RectangleRelativeLayout extends RelativeLayout
{
    private double VIEW_ASPECT_RATIO;
    private ViewAspectRatioMeasurer varm;


    public RectangleRelativeLayout(Context context) {
        super(context);
    }

    public RectangleRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AspectRatio, 0, 0);
        try {
            this.VIEW_ASPECT_RATIO = (double) ta.getFloat(0, 2.0f);
            this.varm = new ViewAspectRatioMeasurer(this.VIEW_ASPECT_RATIO);
        } finally {
            ta.recycle();
        }
    }

    public RectangleRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RectangleRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.varm != null) {
            this.varm.measure(widthMeasureSpec, heightMeasureSpec);
            super.onMeasure(MeasureSpec.makeMeasureSpec(this.varm.getMeasuredWidth(), MeasureSpec.getMode(1073741824)), MeasureSpec.makeMeasureSpec(this.varm.getMeasuredHeight(), MeasureSpec.getMode(1073741824)));
            return;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
