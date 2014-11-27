package com.getgigradio.gigradio.util;

import android.content.Context;
import android.location.Location;

public class PrefsUtils {

    private static final String RADIO_STATION_LOCATION_LATITUDE = "radio_station_location_latitude";
    private static final String RADIO_STATION_LOCATION_LONGITUDE = "radio_station_location_longitude";

    public static final String SELECTED_DATE_TIME = "selected_week_of_week_year";


    public static void saveRadioStationLocation(Context context, Location location) {
        Prefs.with(context).save(RADIO_STATION_LOCATION_LATITUDE, String.valueOf(location.getLatitude()));
        Prefs.with(context).save(RADIO_STATION_LOCATION_LONGITUDE, String.valueOf(location.getLongitude()));
    }

    public static Location getRadioStationLocation(Context context) {
        // Return null if no location saved
        if (!Prefs.with(context).getAll().containsKey(RADIO_STATION_LOCATION_LATITUDE)) {
            return null;
        }
        Location location = new Location("");
        location.setLatitude(Double.valueOf(Prefs.with(context).getString(RADIO_STATION_LOCATION_LATITUDE, null)));
        location.setLongitude(Double.valueOf(Prefs.with(context).getString(RADIO_STATION_LOCATION_LONGITUDE, null)));
        return location;
    }
}
