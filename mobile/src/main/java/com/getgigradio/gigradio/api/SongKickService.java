package com.getgigradio.gigradio.api;

import com.getgigradio.gigradio.model.songkick.event.EventObject;
import com.getgigradio.gigradio.model.songkick.location.LocationObject;
import com.getgigradio.gigradio.util.SongKickDateTime;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

public interface SongKickService {
    @GET("/events.json?per_page=50")
    Observable<EventObject> fetchEventsByLocation(@Query("location") String location,
                                                  @Query("min_date") SongKickDateTime minDate,
                                                  @Query("max_date") SongKickDateTime maxDate);

    @GET("/events.json?location=clientip&per_page=50")
    Observable<EventObject> fetchEventsByClientIp(@Query("min_date") String minDate,
                                                  @Query("max_date") String maxDate);

    @GET("/search/locations.json?per_page=50")
    Observable<LocationObject> fetchLocationsByLocation(@Query("location") String location);

    @GET("/search/locations.json?per_page=50")
    Observable<LocationObject> fetchLocationsByQuery(@Query("query") String query);
}
