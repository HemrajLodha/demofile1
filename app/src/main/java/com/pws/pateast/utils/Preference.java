package com.pws.pateast.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.base.util.BasePreferenceUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pws.pateast.MyApplication;
import com.pws.pateast.activity.login.LoginActivity;
import com.pws.pateast.api.model.AccessToken;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.model.UserInfo;
import com.pws.pateast.api.model.Ward;

import java.util.ArrayList;

import static com.pws.pateast.utils.LocaleHelper.SELECTED_LANGUAGE;
import static com.pws.pateast.utils.LocaleHelper.SELECTED_LANGUAGE_ID;


public class Preference extends BasePreferenceUtils {

    public static String PREFERENCE_NAME = "PATEST_PREFERENCE";
    public static String KEY_APP_SHORTCUTS = "key_app_shortcuts";
    public static String KEY_TIMESTAMP = "timeStamp";
    public static String KEY_PERMISSION = "permissions";
    public static String KEY_WARD = "parent_ward";
    public static String KEY_NOTIFICATION = "notification";
    public static String KEY_DATE_FORMAT = "date_format";
    public static String KEY_NEXT_STOP = "driver_next_stop";

    public static final String LANG_EN = "en";
    public static final String LANG_AR = "ar";

    private static final String KEY_SESSION_ID = "academicSessionId";

    public Context mContext;
    private static SharedPreferences mPref;
    private static Preference preference;

    private Preference(Context context) {
        this.mContext = context;
        mPref = getSharedPreference(context, PREFERENCE_NAME);
    }

    public static Preference get(Context context) {
        if (preference == null) {
            preference = new Preference(context);
        }
        return preference;
    }

    public void logout() {
        clear();
        startLoginActivity();
    }

    public void clear() {
        clear(mContext);
        clear(mContext, PREFERENCE_NAME);
    }

    public void startLoginActivity() {
        Intent intentHome = new Intent(mContext, LoginActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intentHome);
    }


    public AccessToken getAccessToken() {
        String accessToken = mPref.getString("accessToken", null);
        if (accessToken == null)
            return null;

        return new Gson().fromJson(accessToken, AccessToken.class);
    }

    public void setAccessToken(AccessToken accessToken) {
        mPref.edit().putString("accessToken", new Gson().toJson(accessToken)).apply();
    }

    public boolean getNotification() {
        return mPref.getBoolean(KEY_NOTIFICATION, true);
    }

    public void setNotification(boolean notification) {
        mPref.edit().putBoolean(KEY_NOTIFICATION, notification).apply();
    }

    public User getUser() {
        String accessToken = mPref.getString("user", null);
        if (accessToken == null)
            return null;

        return new Gson().fromJson(accessToken, User.class);
    }

    public void setUser(User user) {
        mPref.edit().putString("user", new Gson().toJson(user)).apply();
    }

    public UserInfo getUserInfo() {
        String accessToken = mPref.getString("user_info", null);
        if (accessToken == null)
            return null;

        return new Gson().fromJson(accessToken, UserInfo.class);
    }

    public void setUserInfo(UserInfo user) {
        mPref.edit().putString("user_info", new Gson().toJson(user)).apply();
    }

    public String getLanguage(String defaultLanguage) {
        return mPref.getString(SELECTED_LANGUAGE, defaultLanguage);
    }

    public String getLanguage() {
        return mPref.getString(SELECTED_LANGUAGE, MyApplication.DEFAULT_LANGUAGE);
    }

    public void setLanguage(String lang) {
        mPref.edit().putString(SELECTED_LANGUAGE, lang).apply();
    }

    public String getLanguageID() {
        int langId = mPref.getInt(SELECTED_LANGUAGE_ID, MyApplication.DEFAULT_LANGUAGE_ID);
        return String.valueOf(langId);
    }

    public void setLanguageID(int id) {
        mPref.edit().putInt(SELECTED_LANGUAGE_ID, id).apply();
    }

    public String getDateFormat() {
        return mPref.getString(KEY_DATE_FORMAT, DateUtils.SERVER_DATE_TEMPLATE);
    }

    public void setDateFormat(String dateFormat) {
        if (dateFormat == null)
            return;
        switch (dateFormat) {
            case "DD/MM/YYYY":
                dateFormat = "dd/MM/yyyy";
                break;
            case "MM/DD/YYYY":
                dateFormat = "MM/dd/yyyy";
                break;
            case "YYYY/MM/DD":
                dateFormat = "yyyy/MM/dd";
                break;
        }
        mPref.edit().putString(KEY_DATE_FORMAT, dateFormat).apply();
    }

/*    public int getSessionId() {
        return mPref.getInt(KEY_SESSION_ID, -1);
    }

    public void setSessionId(int academicSessionId) {
        mPref.edit().putInt(KEY_SESSION_ID, academicSessionId).apply();
    }*/

    public void setWard(Ward ward) {
        mPref.edit().putString(KEY_WARD, new Gson().toJson(ward)).apply();
    }

    public Ward getWard() {
        String ward = mPref.getString(KEY_WARD, null);
        if (ward == null)
            return null;

        return new Gson().fromJson(ward, Ward.class);
    }

    public String getOrderBy() {
        return getLanguage().equals("en") ? "asc" : "desc";
    }

    public boolean isAppShortcutCreated() {
        return mPref.getBoolean(KEY_APP_SHORTCUTS, false);
    }

    public void setAppShortcutCreated(boolean created) {
        mPref.edit().putBoolean(KEY_APP_SHORTCUTS, created).apply();
    }

    public long getTimeStamp() {
        return mPref.getLong(KEY_TIMESTAMP, 0);
    }

    public void setTimeStamp(long timeStamp) {
        mPref.edit().putLong(KEY_TIMESTAMP, timeStamp).apply();
    }

    public ArrayList<String> getChatPermission() {
        String chatPermission = mPref.getString(KEY_PERMISSION, null);
        if (chatPermission == null || chatPermission.equalsIgnoreCase("null"))
            return new ArrayList<>();
        return new Gson().fromJson(chatPermission, new TypeToken<ArrayList<String>>() {
        }.getType());
    }

    public void setChatPermission(String chatPermission) {
        mPref.edit().putString(KEY_PERMISSION, chatPermission).apply();
    }

    public void setDriverNextStopId(int id) {
        mPref.edit().putInt(KEY_NEXT_STOP, id).apply();
    }

    public int getDriverNextStopId() {
        return mPref.getInt(KEY_NEXT_STOP, -1);
    }
}
