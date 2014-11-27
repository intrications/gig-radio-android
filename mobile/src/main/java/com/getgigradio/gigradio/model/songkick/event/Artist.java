
package com.getgigradio.gigradio.model.songkick.event;

import java.util.List;

public class Artist {
    private String displayName;
    private Number id;
    private List identifier;
    private String uri;

    public String getDisplayName() {
        return this.displayName;
    }

    public Number getId() {
        return this.id;
    }

    public List getIdentifier() {
        return this.identifier;
    }

    public String getUri() {
        return this.uri;
    }

}
