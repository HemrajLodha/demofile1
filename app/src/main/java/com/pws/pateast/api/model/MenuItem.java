package com.pws.pateast.api.model;

import android.content.Context;
import android.support.annotation.StringRes;

/**
 * Created by intel on 31-Aug-17.
 */

public class MenuItem {
    private String text;
    private String icon;
    private Context context;
    private MenuItem(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public String getString(@StringRes int resId)
    {
        return getContext().getString(resId);
    }

    public String getString(@StringRes int resId, Object... formatArgs)
    {
        return getContext().getString(resId,formatArgs);
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public static class Builder {
        private MenuItem menuItem;
        public Builder(Context context) {
            menuItem = new MenuItem(context);
        }

        public Builder setText(String text) {
            menuItem.setText(text);
            return this;
        }

        public Builder setText(int text) {
            return setText(menuItem.getString(text));
        }

        public Builder setIcon(String icon) {
            menuItem.setIcon(icon);
            return this;
        }

        public Builder setIcon(int icon) {
            return setIcon(menuItem.getString(icon));
        }

        public MenuItem build() {
            return menuItem;
        }
    }
}
