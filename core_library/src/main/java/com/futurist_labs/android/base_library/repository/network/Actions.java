package com.futurist_labs.android.base_library.repository.network;

import android.location.Location;
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Galeen on 1/2/2018.
 */

public class Actions {
    @NonNull
    static Action getTripsAction(int userId) {
        return new Action(Action.GET,
                "users/" + userId + "/trips",
                null);
    }


    @NonNull
    static Action nearestOffersAction(Location location) {
        Action action = new Action(Action.GET,
                "v1.0/offers/nearest",
                null);
        Map<String, String> params = new HashMap<>();

        params.put("latitude", String.valueOf(location.getLatitude()));
        params.put("longitude", String.valueOf(location.getLongitude()));

//        params.put("maxDistance", "2000");
        params.put("maxCount", "100");
        action.params = params;
        return action;
    }


}
