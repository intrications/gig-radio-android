
package com.getgigradio.gigradio.model.songkickevent;

import java.util.List;

public class Event{
   	private String ageRestriction;
   	private String displayName;
   	private End end;
   	private Number id;
   	private Location location;
   	private List<Performance> performance;
   	private Number popularity;
   	private Series series;
   	private Start start;
   	private String status;
   	private String type;
   	private String uri;
   	private Venue venue;

    public String getAgeRestriction() {
        return ageRestriction;
    }

    public String getDisplayName() {
        return displayName;
    }

    public End getEnd() {
        return end;
    }

    public Number getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public List<Performance> getPerformances() {
        return performance;
    }

    public Number getPopularity() {
        return popularity;
    }

    public Series getSeries() {
        return series;
    }

    public Start getStart() {
        return start;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public String getUri() {
        return uri;
    }

    public Venue getVenue() {
        return venue;
    }
}
