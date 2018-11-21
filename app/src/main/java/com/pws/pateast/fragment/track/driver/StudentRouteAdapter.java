package com.pws.pateast.fragment.track.driver;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.adapter.viewholder.BaseItemViewHolder;
import com.pws.pateast.R;
import com.pws.pateast.api.model.Trip;
import com.pws.pateast.enums.BoardStatus;
import com.pws.pateast.enums.TripStatus;
import com.pws.pateast.utils.ImageUtils;

import java.util.Collections;

/**
 * Created by intel on 18-Sep-17.
 */

public class StudentRouteAdapter extends BaseRecyclerAdapter<Trip, StudentRouteAdapter.StudentHolder> {

    private Trip trip;

    public StudentRouteAdapter(Context context, OnItemClickListener itemClickListener) {
        super(context, itemClickListener);
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public Trip getTrip() {
        return trip;
    }


    public void updateTripRecord(int tripRecordId, int tripRecordStatus) {
        if (tripRecordId == -1) {
            notifyDataSetChanged();
            return;
        }
        int position = 0;
        for (Trip tripRecord : getDatas()) {
            if (tripRecord.getId() == tripRecordId) {
                tripRecord.setStatus(tripRecordStatus);
                position = getDatas().indexOf(tripRecord);
                break;
            }
        }
        Collections.swap(getDatas(), 0, position);
        notifyItemMoved(position, 0);
    }


    @Override
    protected int getItemResourceLayout(int viewType) {
        return R.layout.item_student_route;
    }

    @Override
    public StudentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StudentHolder(getView(parent, viewType), mItemClickListener);
    }

    class StudentHolder extends BaseItemViewHolder<Trip> {
        private ImageView imgProfile;
        private TextView tvWardName, tvPhone, tvTripRecordStatus;
        private Button btnBoard, btnCancel;

        public StudentHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView, onItemClickListener);
            imgProfile = (ImageView) findViewById(R.id.img_profile);
            tvWardName = (TextView) findViewById(R.id.tv_ward_name);
            tvPhone = (TextView) findViewById(R.id.tv_phone);
            tvTripRecordStatus = (TextView) findViewById(R.id.tv_trip_record_status);
            btnBoard = (Button) findViewById(R.id.btn_board);
            btnCancel = (Button) findViewById(R.id.btn_cancel);

            btnBoard.setOnClickListener(this);
            btnCancel.setOnClickListener(this);
        }

        @Override
        public void bind(Trip trip) {
            String wardName = trip.getStudent().getUser().getUserdetails().get(0).getFullname();

            tvWardName.setText(wardName);
            tvPhone.setText(trip.getStudent().getUser().getMobile());
            ImageUtils.setImageUrl(getContext(), imgProfile, trip.getStudent().getUser().getUser_image(), R.drawable.avatar1);

            if (getTrip() != null)
                tvTripRecordStatus.setText(getString(getTripStatus(TripStatus.getStatus(getTrip().getStatus()),
                        BoardStatus.getStatus(trip.getStatus())), wardName));
            else
                tvTripRecordStatus.setText(getString(getTripStatus(TripStatus.NONE, BoardStatus.NONE), wardName));
        }


        private int getTripStatus(TripStatus tripStatus, BoardStatus boardStatus) {
            btnBoard.setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);
            switch (tripStatus) {
                case PICKUP_STARTED:
                    btnBoard.setText(R.string.title_on_board);
                    btnCancel.setText(R.string.cancel);
                    break;
                case PICKUP_STOPPED:
                    btnBoard.setText(R.string.title_on_board);
                    btnCancel.setText(R.string.leave_status_absent);
                    btnBoard.setVisibility(View.VISIBLE);
                    btnCancel.setVisibility(View.VISIBLE);
                    break;
                case DROP_STARTED:
                    btnBoard.setVisibility(View.VISIBLE);
                    btnBoard.setText(R.string.title_off_board);
                    btnCancel.setText(R.string.cancel);
                    break;
                case DROP_STOPPED:
                    btnBoard.setText(R.string.title_off_board);
                    btnCancel.setText(R.string.cancel);
                    break;
            }
            return getBoardStatus(boardStatus);
        }

        private int getBoardStatus(BoardStatus boardStatus) {
            switch (boardStatus) {
                case PARENT_ON_BOARD_DROP:
                    btnBoard.setVisibility(View.VISIBLE);
                    btnCancel.setVisibility(View.VISIBLE);
                    return R.string.label_parent_on_board_dropped;
                case DRIVER_ON_BOARD_PICKUP:
                    btnBoard.setText(R.string.title_off_board);
                    btnBoard.setVisibility(View.VISIBLE);
                    return R.string.label_driver_on_board_picked_up;
                case DRIVER_OFF_BOARD_PICKUP:
                    return R.string.label_driver_off_board_picked_up;
                case DRIVER_ON_BOARD_DROP:
                    btnCancel.setVisibility(View.GONE);
                    btnBoard.setText(R.string.title_off_board);
                    return R.string.label_driver_on_board_dropped;
                case PARENT_PICKUP_EARLY:
                    btnBoard.setVisibility(View.GONE);
                    btnCancel.setVisibility(View.GONE);
                    return R.string.label_parent_picked_up_early;
                case DRIVER_OFF_BOARD_DROP:
                    btnBoard.setVisibility(View.GONE);
                    return R.string.label_driver_off_board_dropped;
                default:
                    return R.string.label_parent_on_board_none;
            }
        }

    }
}
