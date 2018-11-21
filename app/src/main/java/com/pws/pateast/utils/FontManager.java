package com.pws.pateast.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;

import com.pws.pateast.R;

/**
 * Created by planet on 5/3/2017.
 */

public class FontManager {
    public static FontManager fontManager;
    public static Typeface materialTypeface;
    public static Typeface awesomeTypeface;
    public static Typeface appTypeface;
    private Context context;

    private FontManager(Context context) {
        this.context = context;
    }

    public static FontManager newInstance(Context context) {
        if (fontManager == null)
            fontManager = new FontManager(context);
        return fontManager;
    }


    public static Typeface getMaterialDesignFont(Context context) {
        if (materialTypeface == null)
            materialTypeface = ResourcesCompat.getFont(context, R.font.material_design);

        return materialTypeface;
    }

    public static Typeface getFontAwesomeFont(Context context) {
        if (awesomeTypeface == null)
            awesomeTypeface = ResourcesCompat.getFont(context, R.font.font_awesome);

        return awesomeTypeface;
    }

    public static Typeface getAppFont(Context context) {
        if (appTypeface == null)
            appTypeface = ResourcesCompat.getFont(context, R.font.proxima_regular);

        return appTypeface;
    }


}
