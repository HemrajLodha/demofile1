package com.pws.pateast.fragment.feeds.add;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.adapter.viewholder.BaseItemViewHolder;
import com.pws.pateast.R;
import com.pws.pateast.api.model.Feeds;
import com.pws.pateast.enums.MessageType;
import com.pws.pateast.utils.ImageUtils;

public class AddFileAdapter extends BaseRecyclerAdapter<Feeds, AddFileAdapter.FileHolder> {


    public AddFileAdapter(Context context, OnItemClickListener onItemClickListener) {
        super(context, onItemClickListener);
    }

    @Override
    protected int getItemResourceLayout(int viewType) {
        return R.layout.item_add_feed_file;
    }

    @Override
    public FileHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FileHolder(getView(parent, viewType), mItemClickListener);
    }


    class FileHolder extends BaseItemViewHolder<Feeds> {

        private ImageView imgFiles;
        private TextView imgDelete, imgFileType;

        public FileHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView, onItemClickListener);
            imgFiles = (ImageView) findViewById(R.id.img_files);
            imgDelete = (TextView) findViewById(R.id.img_delete);
            imgFileType = (TextView) findViewById(R.id.img_file_type);
        }

        @Override
        public void bind(Feeds file) {
            imgFileType.setVisibility(View.VISIBLE);
            switch (MessageType.getMessageType(file.getFeedType())) {
                case IMAGE:
                    imgFileType.setText(R.string.fa_image);
                    break;
                case VIDEO:
                    imgFileType.setText(R.string.fa_video);
                    break;
                default:
                    imgFileType.setVisibility(View.GONE);
                    break;
            }
            ImageUtils.setImageUri(getContext(), imgFiles, file.getFileUri(), R.drawable.ic_image_placeholder);
            imgDelete.setOnClickListener(this);
        }
    }
}
