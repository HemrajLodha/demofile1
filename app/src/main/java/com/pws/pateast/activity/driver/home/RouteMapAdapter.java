package com.pws.pateast.activity.driver.home;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.base.ui.adapter.BaseRecyclerAdapter;
import com.base.ui.adapter.viewholder.BaseItemViewHolder;
import com.pws.pateast.R;
import com.pws.pateast.api.model.RouteMap;

/**
 * Created by intel on 12-Sep-17.
 */

public class RouteMapAdapter extends BaseRecyclerAdapter<RouteMap, RouteMapAdapter.RouteHolder> {

    public RouteMapAdapter(Context context, OnItemClickListener onItemClickListener) {
        super(context, onItemClickListener);
    }

    @Override
    protected int getItemResourceLayout(int viewType) {
        return R.layout.item_route_map;
    }

    @Override
    public RouteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RouteHolder(getView(parent, viewType), mItemClickListener);
    }

    class RouteHolder extends BaseItemViewHolder<RouteMap> {
        private TextView tvRouteTitle, tvVehicleNumber, tvHelperName;


        public RouteHolder(View view, OnItemClickListener onItemClickListener) {
            super(view, onItemClickListener);
            tvRouteTitle = (TextView) findViewById(R.id.tv_route_title);
            tvVehicleNumber = (TextView) findViewById(R.id.tv_vehicle_number);
            tvHelperName = (TextView) findViewById(R.id.tv_helper_name);
        }

        @Override
        public void bind(RouteMap routeMap) {
            tvRouteTitle.setText(routeMap.getRoute().getName());
            tvVehicleNumber.setText(routeMap.getVehicle().getNumber());
            if (routeMap.getHelper() != null)
                tvHelperName.setText(routeMap.getHelper().getUserdetails().get(0).getFullname());
            else
                tvHelperName.setText(getString(R.string.not_available));
        }
    }
}
