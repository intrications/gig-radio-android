package com.getgigradio.gigradio.model;

import com.getgigradio.gigradio.model.songkick.location.Location;
import com.getgigradio.gigradio.model.soundcloud.track.Track;

import java.util.List;

public class RadioStation {

    private Location location;

    // TODO deal with end of year.
    private int weekOfYear;

    private List<Track> playlist;

    public RadioStation(Location location, int weekOfYear, List<Track> playlist) {
        this.location = location;
        this.weekOfYear = weekOfYear;
        this.playlist = playlist;
    }

    public Location getLocation() {
        return location;
    }

    public int getWeekOfYear() {
        return weekOfYear;
    }

    public List<Track> getPlaylist() {
        return playlist;
    }
}
