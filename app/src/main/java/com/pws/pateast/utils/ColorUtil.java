package com.pws.pateast.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ArrayRes;
import android.support.annotation.AttrRes;
import android.util.TypedValue;

import com.pws.pateast.R;

import java.sql.Array;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * Created by Hemraj on 8/30/2017.
 */

public final class ColorUtil {
    private static Queue<String> colorsQueue = new LinkedList<>();
    private static String previousContextClass;

    /**
     * get selected random color from colors array
     *
     * @param context
     * @return
     */
    public static int getSelectedRandomColor(Context context, @ArrayRes int random_colors) {
        if (!context.getClass().getCanonicalName().equals(previousContextClass)) {
            previousContextClass = context.getClass().getCanonicalName();
            colorsQueue.clear();
        }
        if (colorsQueue.size() == 0) {
            String[] colors = context.getResources().getStringArray(random_colors);
            colorsQueue.addAll(Arrays.asList(colors));
        }
        return Color.parseColor(colorsQueue.poll());
    }



    public static int getAttrColor(Context context,@AttrRes int color)
    {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(color, typedValue, true);
        return typedValue.data;
    }

}
