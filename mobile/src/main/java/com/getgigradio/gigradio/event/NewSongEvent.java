package com.getgigradio.gigradio.event;

import com.getgigradio.gigradio.model.soundhoundtrack.Track;


public class NewSongEvent {
    private Track currentSong;

    public NewSongEvent(Track currentSong) {
        this.currentSong = currentSong;
    }

    public Track getCurrentSong() {
        return currentSong;
    }
}
