package com.getgigradio.gigradio.util;


import android.content.Context;

import net.danlew.android.joda.DateUtils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

public class WeeklyDateTime {

    private Context context;
    private DateTime selectedWeek;



    private static final DateTime thisWeek = DateTime.now();

    public WeeklyDateTime(Context context, DateTime selectedWeek) {
        this.context = context;
        this.selectedWeek = selectedWeek;
    }

    public String formatAsDateRange() {
        if ((selectedWeek.weekOfWeekyear()).equals(thisWeek.weekOfWeekyear())) {
            return "This week";
        } else if ((selectedWeek.weekOfWeekyear()).equals(thisWeek.plusWeeks(1).weekOfWeekyear())) {
            return "Next week";
        } else {
            DateTime mondayOfSelectedWeek = new DateTime(selectedWeek).withDayOfWeek(DateTimeConstants.MONDAY);
            DateTime sundayOfSelectedWeek = mondayOfSelectedWeek.withDayOfWeek(DateTimeConstants.SUNDAY);
            return String.format("%s -\n%s",
                    DateUtils.formatDateTime(context, mondayOfSelectedWeek,
                            DateUtils.FORMAT_SHOW_DATE),
                    DateUtils.formatDateTime(context, sundayOfSelectedWeek, DateUtils.FORMAT_SHOW_DATE));
        }
    }
}
