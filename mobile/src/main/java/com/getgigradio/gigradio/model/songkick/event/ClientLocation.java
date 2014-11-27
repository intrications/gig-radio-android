
package com.getgigradio.gigradio.model.songkick.event;

public class ClientLocation {
    private String ip;
    private Number lat;
    private Number lng;
    private Number metroAreaId;

    public String getIp() {
        return this.ip;
    }

    public Number getLat() {
        return this.lat;
    }

    public Number getLng() {
        return this.lng;
    }

    public Number getMetroAreaId() {
        return this.metroAreaId;
    }

}
