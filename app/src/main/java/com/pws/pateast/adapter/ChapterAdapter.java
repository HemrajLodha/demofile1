package com.pws.pateast.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.base.ui.adapter.BaseSpinnerAdapter;
import com.base.ui.adapter.viewholder.BaseListViewHolder;
import com.pws.pateast.R;
import com.pws.pateast.api.model.Chapter;
import com.pws.pateast.api.model.Subject;

import java.util.List;

/**
 * Created by intel on 24-Jan-18.
 */

public class ChapterAdapter extends BaseSpinnerAdapter<Chapter, ChapterAdapter.ChapterHolder, ChapterAdapter.ChapterHolder> {

    public ChapterAdapter(Context context) {
        super(context);
    }

    public ChapterAdapter(Context context, List<Chapter> data) {
        super(context, data);
    }

    @Override
    public int getDropdownItemView() {
        return R.layout.item_spinner_dropdown;
    }

    @Override
    public ChapterHolder onCreateDropdownViewHolder(View itemView) {
        return new ChapterHolder(itemView);
    }

    @Override
    protected int getItemView() {
        return R.layout.item_spinner_view;
    }

    @Override
    public ChapterHolder onCreateViewHolder(View itemView) {
        return new ChapterHolder(itemView);
    }

    class ChapterHolder extends BaseListViewHolder<Chapter> {
        private TextView tvSpinner;

        public ChapterHolder(View itemView) {
            super(itemView);
            tvSpinner = (TextView) findViewById(R.id.tv_spinner);
        }

        @Override
        public void bind(Chapter chapter) {
            tvSpinner.setText(getChapterName(chapter));
        }
    }

    public String getChapterName(Chapter chapter) {
            return chapter.getLmschapterdetails().get(0).getName();
    }

    public Chapter getChapter(int position) {
        if (position > -1)
            return getDatas().get(position);
        return null;
    }
}
