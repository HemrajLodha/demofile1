package com.pws.pateast.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.base.ui.adapter.BaseSpinnerAdapter;
import com.base.ui.adapter.viewholder.BaseListViewHolder;
import com.pws.pateast.R;
import com.pws.pateast.api.model.UserInfo;

/**
 * Created by intel on 25-Apr-17.
 */

public class TeacherAdapter extends BaseSpinnerAdapter<UserInfo, TeacherAdapter.TeacherHolder, TeacherAdapter.TeacherHolder> {


    public TeacherAdapter(Context context) {
        super(context);
    }

    @Override
    public int getDropdownItemView() {
        return R.layout.item_spinner_dropdown;
    }

    @Override
    public TeacherHolder onCreateDropdownViewHolder(View itemView) {
        return new TeacherHolder(itemView);
    }

    @Override
    protected int getItemView() {
        return R.layout.item_spinner_view;
    }

    @Override
    public TeacherHolder onCreateViewHolder(View itemView) {
        return new TeacherHolder(itemView);
    }

    class TeacherHolder extends BaseListViewHolder<UserInfo> {
        private TextView tvSpinner;

        public TeacherHolder(View itemView) {
            super(itemView);
            tvSpinner = (TextView) findViewById(R.id.tv_spinner);
        }

        @Override
        public void bind(UserInfo userInfo) {
            tvSpinner.setText(getTeacherName(userInfo));
        }
    }

    public String getTeacherName(UserInfo userInfo) {
        return userInfo.getUser().getUserdetails().get(0).getFullname();
    }

    public UserInfo getTeacher(int position) {
        if (position > -1)
            return getDatas().get(position);
        return new UserInfo();
    }

}
