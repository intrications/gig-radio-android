package com.getgigradio.gigradio.event;

import com.getgigradio.gigradio.model.soundhoundtrack.Track;

public class SeekBarMoveEvent {

    private Track currentTrack;
    private int progress;
    private int bufferingProgress;
    private int currentMilliseconds;
    private int duration;

    public Track getCurrentTrack() {
        return currentTrack;
    }

    public void setCurrentTrack(Track currentTrack) {
        this.currentTrack = currentTrack;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getBufferingProgress() {
        return bufferingProgress;
    }

    public void setBufferingProgress(int bufferingProgress) {
        this.bufferingProgress = bufferingProgress;
    }

    public int getCurrentMilliseconds() {
        return currentMilliseconds;
    }

    public void setCurrentMilliseconds(int currentMilliseconds) {
        this.currentMilliseconds = currentMilliseconds;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

}
