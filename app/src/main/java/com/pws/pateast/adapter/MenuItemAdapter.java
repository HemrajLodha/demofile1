package com.pws.pateast.adapter;

import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.base.ui.adapter.BaseSpinnerAdapter;
import com.base.ui.adapter.viewholder.BaseListViewHolder;
import com.pws.pateast.R;
import com.pws.pateast.api.model.MenuItem;
import com.pws.pateast.api.model.Tag;
import com.pws.pateast.utils.FontManager;

import java.util.List;

/**
 * Created by intel on 31-Aug-17.
 */

public class MenuItemAdapter extends BaseSpinnerAdapter<MenuItem, MenuItemAdapter.MenuItemHolder, MenuItemAdapter.MenuItemHolder> {
    public MenuItemAdapter(Context context) {
        super(context);
    }

    public MenuItemAdapter(Context context, List<MenuItem> data) {
        super(context, data);
    }

    @Override
    public int getDropdownItemView() {
        return R.layout.item_spinner_dropdown;
    }

    @Override
    public MenuItemHolder onCreateDropdownViewHolder(View itemView) {
        return new MenuItemHolder(itemView);
    }

    @Override
    protected int getItemView() {
        return R.layout.item_spinner_view;
    }

    @Override
    public MenuItemHolder onCreateViewHolder(View itemView) {
        return new MenuItemHolder(itemView);
    }

    class MenuItemHolder extends BaseListViewHolder<MenuItem> {
        private TextView tvSpinner;

        public MenuItemHolder(View itemView) {
            super(itemView);
            tvSpinner = (TextView) findViewById(R.id.tv_spinner);
            tvSpinner.setTypeface(FontManager.getFontAwesomeFont(getContext()));
        }

        @Override
        public void bind(MenuItem menuItem) {
            tvSpinner.setText(menuItem.getText());
        }
    }
}