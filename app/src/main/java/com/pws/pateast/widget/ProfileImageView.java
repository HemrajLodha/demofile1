package com.pws.pateast.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.pws.pateast.R;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by planet on 5/22/2017.
 */

public class ProfileImageView extends CircleImageView {

    private double VIEW_ASPECT_RATIO;
    private ViewAspectRatioMeasurer varm;

    public ProfileImageView(Context context) {
        super(context);
    }

    public ProfileImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public ProfileImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet attrs)
    {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AspectRatio, 0, 0);
        try {
            this.VIEW_ASPECT_RATIO = (double) ta.getFloat(0, 2.0f);
            this.varm = new ViewAspectRatioMeasurer(this.VIEW_ASPECT_RATIO);
        } finally {
            ta.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.varm != null) {
            this.varm.measure(widthMeasureSpec, heightMeasureSpec);
            super.onMeasure(MeasureSpec.makeMeasureSpec(this.varm.getMeasuredWidth(), MeasureSpec.getMode(1073741824)), MeasureSpec.makeMeasureSpec(this.varm.getMeasuredHeight(), MeasureSpec.getMode(1073741824)));
            return;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void animateImage(boolean show) {
        new AnimationListener(show).start();
    }

    private class AnimationListener implements Animation.AnimationListener {
        private boolean show;

        AnimationListener(boolean show) {
            this.show = show;
        }

        public void start() {
            if (show) {
                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_object_show);
                animation.setAnimationListener(this);
                startAnimation(animation);
            } else {
                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_object_hide);
                animation.setAnimationListener(this);
                startAnimation(animation);
            }
        }

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

}
