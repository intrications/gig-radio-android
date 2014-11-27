
package com.getgigradio.gigradio.model.songkick.event;

import android.content.Context;

import com.colintmiller.simplenosql.NoSQL;
import com.colintmiller.simplenosql.NoSQLEntity;
import com.colintmiller.simplenosql.RetrievalCallback;
import com.google.gson.Gson;

import net.danlew.android.joda.DateUtils;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import java.util.List;

public class Event {

    public static final String EXTRA_JSON = "extra_event";

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

    public String toJson() {
        return new Gson().toJson(this);
    }

    public static Event fromJson(String json) {
        return new Gson().fromJson(json, Event.class);
    }

    public String getDisplayTime(Context context) {
        String datetimeString = getStart().getDatetime();
        if (datetimeString != null) {
            DateTime dateTime = DateTime.parse(datetimeString,
                    DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ"));
            LocalDate localDate = dateTime.toLocalDate();
            String text;
            if (localDate.equals(LocalDate.now())) {
                text = "Today!";
            } else if (localDate.equals(LocalDate.now().plusDays(1))) {
                text = "Tomorrow";
            } else {
                text = DateUtils.formatDateTime(context, dateTime, DateUtils.FORMAT_SHOW_TIME |
                        DateUtils
                        .FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_WEEKDAY);
            }
            return text;
        }
        return "NO START TIME! WHY?";
    }
}
