package com.pws.pateast.widget;

import android.view.View.MeasureSpec;

public class ViewAspectRatioMeasurer {
    private double aspectRatio;
    private Integer measuredHeight = null;
    private Integer measuredWidth = null;

    public ViewAspectRatioMeasurer(double aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public void measure(int widthMeasureSpec, int heightMeasureSpec) {
        measure(widthMeasureSpec, heightMeasureSpec, this.aspectRatio);
    }

    public void measure(int widthMeasureSpec, int heightMeasureSpec, double aspectRatio) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = widthMode == 0 ? Integer.MAX_VALUE : MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = heightMode == 0 ? Integer.MAX_VALUE : MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode == 1073741824 && widthMode == 1073741824) {
            this.measuredWidth = Integer.valueOf(widthSize);
            this.measuredHeight = Integer.valueOf(heightSize);
        } else if (heightMode == 1073741824) {
            this.measuredHeight = Integer.valueOf((int) Math.min((double) heightSize, ((double) widthSize) / aspectRatio));
            this.measuredWidth = Integer.valueOf((int) (((double) this.measuredHeight.intValue()) * aspectRatio));
        } else if (widthMode == 1073741824) {
            this.measuredWidth = Integer.valueOf((int) Math.min((double) widthSize, ((double) heightSize) * aspectRatio));
            this.measuredHeight = Integer.valueOf((int) (((double) this.measuredWidth.intValue()) / aspectRatio));
        } else if (((double) widthSize) > ((double) heightSize) * aspectRatio) {
            this.measuredHeight = Integer.valueOf(heightSize);
            this.measuredWidth = Integer.valueOf((int) (((double) this.measuredHeight.intValue()) * aspectRatio));
        } else {
            this.measuredWidth = Integer.valueOf(widthSize);
            this.measuredHeight = Integer.valueOf((int) (((double) this.measuredWidth.intValue()) / aspectRatio));
        }
    }

    public int getMeasuredWidth() {
        if (this.measuredWidth != null) {
            return this.measuredWidth.intValue();
        }
        throw new IllegalStateException("You need to run measure() before trying to get measured dimensions");
    }

    public int getMeasuredHeight() {
        if (this.measuredHeight != null) {
            return this.measuredHeight.intValue();
        }
        throw new IllegalStateException("You need to run measure() before trying to get measured dimensions");
    }
}
