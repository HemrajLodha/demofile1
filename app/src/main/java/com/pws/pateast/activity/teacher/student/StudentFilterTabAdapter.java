package com.pws.pateast.activity.teacher.student;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.base.ui.adapter.viewholder.BaseItemViewHolder;
import com.pws.pateast.R;
import com.pws.pateast.widget.RecyclerTabLayout;

/**
 * Created by intel on 09-Jun-17.
 */

public class StudentFilterTabAdapter extends RecyclerTabLayout.Adapter<StudentFilterTabAdapter.ViewHolder> {


    public StudentFilterTabAdapter(ViewPager viewPager) {
        super(viewPager);
    }

    public StudentFilterAdapter getAdapter() {
        return (StudentFilterAdapter) getViewPager().getAdapter();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.teacher_student_filter_item_selected, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position == getCurrentIndicatorPosition()) {
            holder.tvClassName.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.shape_chip_class_selected));
            holder.tvClassName.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorPrimaryTeacher));
        } else {
            holder.tvClassName.setBackground(null);
            holder.tvClassName.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.textColor));
        }
        holder.tvClassName.setText(getAdapter().getPageTitle(position));
    }

    @Override
    public int getItemCount() {
        return getAdapter().getCount();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvClassName;

        public ViewHolder(View itemView) {
            super(itemView);
            tvClassName = (TextView) itemView;
            tvClassName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setCurrentIndicatorPosition(getLayoutPosition());
                    getViewPager().setCurrentItem(getLayoutPosition());
                }
            });
        }
    }

}
