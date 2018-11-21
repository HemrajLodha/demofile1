package com.base.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class BasePreferenceUtils {

    public static SharedPreferences getSharedPreference(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static SharedPreferences getSharedPreference(Context context,String name) {
        return context.getSharedPreferences(name, Activity.MODE_PRIVATE);
    }

    public static void putString(Context context, String key, String isi) {
        getSharedPreference(context).edit().putString(key, isi).apply();
    }

    public static String getString(Context context, String key) {
        return getSharedPreference(context).getString(key, null);
    }

    public static void putDouble(Context context, String key, double isi) {
        getSharedPreference(context).edit().putFloat(key, (float) isi).apply();
    }

    public static Double getDouble(Context context, String key) {
        double value;
        switch (key) {
            case "lat":
                value = getSharedPreference(context).getFloat(key, -6.914744f);
                break;
            case "lng":
                value = getSharedPreference(context).getFloat(key, 107.609810f);
                break;
            default:
                value = getSharedPreference(context).getFloat(key, 0);
        }

        return value;
    }

    public static void putBoolean(Context context, String key, boolean isi) {
        getSharedPreference(context).edit().putBoolean(key, isi).apply();
    }

    public static boolean getBoolean(Context context, String key) {
        return getSharedPreference(context).getBoolean(key, false);
    }

    public static void clear(Context context) {
        getSharedPreference(context).edit().clear().apply();
    }

    public static void clear(Context context,String name) {
        getSharedPreference(context,name).edit().clear().apply();
    }
}
