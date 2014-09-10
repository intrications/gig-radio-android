package com.getgigradio.gigradio.api;

import com.getgigradio.gigradio.model.songkickevent.EventObject;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

public interface SongKickService {
    @GET("/events.json")
    Observable<EventObject> fetchEvents(@Query("location") String location);
}
