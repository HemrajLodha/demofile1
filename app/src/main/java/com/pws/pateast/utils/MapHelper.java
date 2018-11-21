package com.pws.pateast.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.google.maps.internal.StringJoin;

public class MapHelper {


    public static Intent dispatchShowDirections(Context context, String... placeIds) {
        String originPlaceId = placeIds[0];
        String destinationPlaceId = placeIds[placeIds.length - 1];
        Uri.Builder directionsBuilder = new Uri.Builder()
                .scheme("https")
                .authority("www.google.com")
                .appendPath("maps")
                .appendPath("dir")
                .appendPath("")
                .appendQueryParameter("api", "1")
                .appendQueryParameter("dir_action", "navigate")
                .appendQueryParameter("origin", originPlaceId)
                .appendQueryParameter("destination", destinationPlaceId)
                .appendQueryParameter("waypoints", StringJoin.join('|', placeIds));
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(directionsBuilder.build());
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            return intent;
        }
        return null;
    }
}
