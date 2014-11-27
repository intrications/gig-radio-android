package com.getgigradio.gigradio.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public final class SongKickDateTime {

    private static DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");

    public SongKickDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    private DateTime dateTime;

    @Override
    public String toString() {
        return formatter.print(dateTime);
    }
}
