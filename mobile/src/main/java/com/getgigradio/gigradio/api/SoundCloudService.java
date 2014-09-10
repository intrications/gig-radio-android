package com.getgigradio.gigradio.api;

import com.getgigradio.gigradio.model.soundhoundartist.Artist;
import com.getgigradio.gigradio.model.soundhoundtrack.Track;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

public interface SoundCloudService {

    @GET("/users.json")
    Observable<List<Artist>> fetchArtist(@Query("q") String artistName);

    @GET("/users/{userid}/tracks.json?limit=3")
    Observable<List<Track>> fetchTracks(@Path("userid") Number userid);
}
