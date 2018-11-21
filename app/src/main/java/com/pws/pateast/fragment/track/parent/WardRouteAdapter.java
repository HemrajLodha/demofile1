package com.pws.pateast.fragment.track.parent;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.ui.adapter.BaseExpandableRecyclerAdapter;
import com.base.ui.adapter.viewholder.ChildViewHolder;
import com.base.ui.adapter.viewholder.ParentViewHolder;
import com.pws.pateast.R;
import com.pws.pateast.activity.dashboard.ChildFooterItem;
import com.pws.pateast.api.model.RouteMap;
import com.pws.pateast.enums.BoardStatus;
import com.pws.pateast.enums.TripRecordType;
import com.pws.pateast.enums.TripStatus;
import com.pws.pateast.utils.ImageUtils;

import static com.pws.pateast.enums.BoardStatus.CANNOT_BOARD;
import static com.pws.pateast.enums.BoardStatus.NONE;
import static com.pws.pateast.enums.TripRecordType.DROP_OFF_ONLY;

/**
 * Created by intel on 13-Sep-17.
 */

public class WardRouteAdapter extends BaseExpandableRecyclerAdapter<RouteMap, RouteMap, ChildFooterItem, WardRouteAdapter.VehicleHolder, WardRouteAdapter.WardHolder> {

    public WardRouteAdapter(Context context, OnParentClickListener onParentClickListener, OnChildClickListener onChildClickListener) {
        super(context, onParentClickListener, onChildClickListener);
    }

    @Override
    protected int getParentResourceLayout(int viewType) {
        return R.layout.item_ward_route_parent;
    }

    @Override
    protected int getChildResourceLayout(int viewType) {
        return R.layout.item_ward_route_child;
    }

    @NonNull
    @Override
    public VehicleHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        return new VehicleHolder(getParentView(parentViewGroup, viewType), mOnParentClickListener);
    }

    @NonNull
    @Override
    public WardHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        return new WardHolder(getChildView(childViewGroup, viewType), mOnChildClickListener);
    }


    class VehicleHolder extends ParentViewHolder<RouteMap, RouteMap, ChildFooterItem> {
        private TextView tvDriverName, tvVehicleNumber, tvHelperName, tvTripStatus;
        private LinearLayout layoutTrackBus;


        public VehicleHolder(@NonNull View itemView, OnParentClickListener onParentClickListener) {
            super(itemView, onParentClickListener);
            tvDriverName = (TextView) findViewById(R.id.tv_driver_name);
            tvVehicleNumber = (TextView) findViewById(R.id.tv_vehicle_number);
            tvHelperName = (TextView) findViewById(R.id.tv_helper_name);
            tvTripStatus = (TextView) findViewById(R.id.tv_trip_status);
            layoutTrackBus = (LinearLayout) findViewById(R.id.layout_track_bus);

            layoutTrackBus.setOnClickListener(this);
        }

        @Override
        public void bind(RouteMap routeMap) {
            tvDriverName.setText(routeMap.getDriver().getUserdetails().get(0).getFullname());
            tvVehicleNumber.setText(routeMap.getVehicle().getVehicledetails().get(0).getName());
            if (routeMap.getHelper() != null)
                tvHelperName.setText(routeMap.getHelper().getUserdetails().get(0).getFullname());
            else
                tvHelperName.setText(getString(R.string.not_available));
            setTripStatus(routeMap.getTrip() != null ? TripStatus.getStatus(routeMap.getTrip().getStatus()) : TripStatus.NONE);

        }

        private void setTripStatus(TripStatus tripStatus) {
            switch (tripStatus) {
                case PICKUP_STARTED:
                case DROP_STARTED:
                    layoutTrackBus.setVisibility(View.VISIBLE);
                    break;
                default:
                    layoutTrackBus.setVisibility(View.GONE);
                    break;
            }
            tvTripStatus.setText(getTripStatus(tripStatus));
        }

        public int getTripStatus(TripStatus tripStatus) {
            switch (tripStatus) {
                case PICKUP_STARTED:
                    return R.string.label_trip_pickup_started;
                case PICKUP_STOPPED:
                    return R.string.label_trip_pickup_stopped;
                case DROP_STARTED:
                    return R.string.label_trip_drop_off_started;
                case DROP_STOPPED:
                    return R.string.label_trip_drop_off_stopped;
                default:
                    return R.string.label_trip_pickup_no_started;
            }
        }
    }

    class WardHolder extends ChildViewHolder<RouteMap, ChildFooterItem> {

        private ImageView imgProfile;
        private TextView tvWardName, tvPhone, tvTripRecordStatus;
        private Button btnOnBoard;

        public WardHolder(View itemView, OnChildClickListener onChildClickListener) {
            super(itemView, onChildClickListener);
            imgProfile = (ImageView) findViewById(R.id.img_profile);
            tvWardName = (TextView) findViewById(R.id.tv_ward_name);
            tvPhone = (TextView) findViewById(R.id.tv_phone);
            tvTripRecordStatus = (TextView) findViewById(R.id.tv_trip_record_status);
            btnOnBoard = (Button) findViewById(R.id.btn_on_board);

            btnOnBoard.setOnClickListener(this);
        }

        @Override
        public void bind(RouteMap child) {
            RouteMap parent = (RouteMap) getParent();
            String wardName = child.getStudent().getUser().getUserdetails().get(0).getFullname();
            tvWardName.setText(wardName);
            tvPhone.setText(child.getStudent().getUser().getMobile());
            ImageUtils.setImageUrl(getContext(), imgProfile, child.getStudent().getUser().getUser_image(), R.drawable.avatar1);

            if (parent.getTrip() != null && child.getStudent().getTriprecord() != null)
                tvTripRecordStatus.setText(getString(getTripStatus(TripStatus.getStatus(parent.getTrip().getStatus()),
                        BoardStatus.getStatus(child.getStudent().getTriprecord().getStatus()), TripRecordType.getStatus(child.getStudent().getTriprecord().getType())), wardName));
            else
                tvTripRecordStatus.setText(getString(getTripStatus(TripStatus.NONE, BoardStatus.CANNOT_BOARD, TripRecordType.NONE), wardName));
        }

        private int getTripStatus(TripStatus tripStatus, BoardStatus boardStatus, TripRecordType tripRecordType) {
            btnOnBoard.setVisibility(View.GONE);
            switch (tripStatus) {
                case NONE:
                    boardStatus = CANNOT_BOARD;
                    break;
                case PICKUP_STARTED:
                    if (tripRecordType != DROP_OFF_ONLY && boardStatus == NONE) {
                        btnOnBoard.setVisibility(View.VISIBLE);
                        btnOnBoard.setText(R.string.title_on_board);
                    }
                    break;
            }
            return getBoardStatus(boardStatus, tripRecordType);
        }

        private int getBoardStatus(BoardStatus boardStatus, TripRecordType tripRecordType) {
            switch (boardStatus) {
                case PARENT_ON_BOARD_DROP:
                    return R.string.label_parent_on_board_drop;
                case DRIVER_ON_BOARD_PICKUP:
                    return R.string.label_driver_on_board_pickup;
                case DRIVER_OFF_BOARD_PICKUP:
                    return R.string.label_driver_off_board_pickup;
                case DRIVER_ON_BOARD_DROP:
                    return R.string.label_driver_on_board_drop;
                case PARENT_PICKUP_EARLY:
                    return R.string.label_parent_pickup_early;
                case DRIVER_OFF_BOARD_DROP:
                    return R.string.label_driver_off_board_drop;
                case CANNOT_BOARD:
                    return tripRecordType == DROP_OFF_ONLY ? R.string.label_driver_not_on_board_drop : R.string.label_parent_on_board_none;
                default:
                    return tripRecordType == DROP_OFF_ONLY ? R.string.label_driver_not_on_board_drop : R.string.label_parent_on_board_none;
            }
        }
    }
}
