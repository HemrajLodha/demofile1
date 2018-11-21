package com.pws.pateast.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.LongSparseArray;
import android.util.SparseArray;

import java.lang.reflect.Field;

public class TypefaceUtil {

    public final static String DEFAULT = "DEFAULT";
    public final static String MONOSPACE = "MONOSPACE";
    public final static String SERIF = "SERIF";
    public final static String SANS_SERIF = "SANS_SERIF";


    public static void overrideFont(Context context, String defaultFontNameToOverride) {
        Typeface customFontTypeface = FontManager.getAppFont(context);
        try {
            final Field staticField = Typeface.class.getDeclaredField(defaultFontNameToOverride);
            staticField.setAccessible(true);
            staticField.set(null, customFontTypeface);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
