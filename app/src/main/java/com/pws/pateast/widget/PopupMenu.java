package com.pws.pateast.widget;

import android.content.Context;
import android.support.v7.widget.ListPopupWindow;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import com.pws.pateast.adapter.MenuItemAdapter;
import com.pws.pateast.api.model.MenuItem;

import java.util.List;

/**
 * Created by intel on 31-Aug-17.
 */

public class PopupMenu {
    private Context mContext;
    private ListPopupWindow popupWindow;
    private MenuItemAdapter adapter;

    private static PopupMenu popupMenu;


    private PopupMenu(Context context) {
        mContext = context;
        popupWindow = new ListPopupWindow(getContext());
    }

    public static PopupMenu getPopUpMenu(Context context) {
        if (popupMenu == null)
            popupMenu = new PopupMenu(context);
        return popupMenu;
    }

    public Context getContext() {
        return mContext;
    }


    public void setAnchorView(View anchorView) {
        if (popupWindow != null) {
            popupWindow.setAnchorView(anchorView);
            popupWindow.setContentWidth(ListPopupWindow.MATCH_PARENT);
        }

    }

    public void setAdapter(ListAdapter adapter) {
        if (popupWindow != null)
            popupWindow.setAdapter(adapter);
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        if (adapter == null)
            adapter = new MenuItemAdapter(getContext());
        adapter.update(menuItems);
        setAdapter(adapter);
    }


    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        if (popupWindow != null)
            popupWindow.setOnItemClickListener(onItemClickListener);
    }

    public void show() {
        if (popupWindow != null)
            popupWindow.show();
    }

    public void dismiss() {
        if (popupWindow != null)
            popupWindow.dismiss();
    }
}
