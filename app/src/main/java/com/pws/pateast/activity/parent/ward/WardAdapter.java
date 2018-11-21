package com.pws.pateast.activity.parent.ward;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.adapter.viewholder.BaseItemViewHolder;
import com.pws.pateast.R;
import com.pws.pateast.api.model.Leave;
import com.pws.pateast.api.model.User;
import com.pws.pateast.api.model.Ward;
import com.pws.pateast.enums.UserType;
import com.pws.pateast.utils.DateUtils;
import com.pws.pateast.utils.ImageUtils;
import com.pws.pateast.utils.Preference;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by intel on 28-Jun-17.
 */

public class WardAdapter extends BaseRecyclerAdapter<Ward, WardAdapter.ViewHolder> {

    public WardAdapter(Context context, OnItemClickListener onItemClickListener) {
        super(context, onItemClickListener);
    }

    @Override
    protected int getItemResourceLayout(int viewType) {
        return R.layout.ward_selection_item;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getView(parent, viewType), mItemClickListener);
    }

    class ViewHolder extends BaseItemViewHolder<Ward> {
        private TextView tvName, tvClass, tvSchool;
        private CircleImageView imgStudent;

        public ViewHolder(View itemView, OnItemClickListener itemClickListener) {
            super(itemView, itemClickListener);
            imgStudent = (CircleImageView) findViewById(R.id.ward_image);
            tvName = (TextView) findViewById(R.id.ward_name);
            tvClass = (TextView) findViewById(R.id.ward_class);
            tvSchool = (TextView) findViewById(R.id.ward_school);
        }

        @Override
        public void bind(Ward ward) {
            tvName.setText(ward.getFullname());
            tvClass.setText(ward.getClass_name());
            tvSchool.setText(ward.getInstitute());
            ImageUtils.setImageUrl(getContext(), imgStudent, ward.getUser_image(), R.drawable.avatar1);
        }
    }
}
