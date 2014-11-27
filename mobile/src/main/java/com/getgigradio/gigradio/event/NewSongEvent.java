package com.getgigradio.gigradio.event;

import com.getgigradio.gigradio.model.songkick.event.Event;
import com.getgigradio.gigradio.model.soundcloud.track.Track;


public class NewSongEvent {

    private Event event;
    private Track currentSong;

    public NewSongEvent(Event event, Track currentSong) {
        this.event = event;
        this.currentSong = currentSong;
    }

    public Event getEvent() {
        return event;
    }

    public Track getCurrentSong() {
        return currentSong;
    }
}
