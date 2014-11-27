
package com.getgigradio.gigradio.model.songkick.event;

public class Venue{
   	private String displayName;
   	private String id;
   	private String lat;
   	private String lng;
   	private MetroArea metroArea;
   	private String uri;

    public String getDisplayName() {
        return displayName;
    }

    public String getId() {
        return id;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public MetroArea getMetroArea() {
        return metroArea;
    }

    public String getUri() {
        return uri;
    }
}
