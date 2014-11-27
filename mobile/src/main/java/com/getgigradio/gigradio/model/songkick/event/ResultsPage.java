
package com.getgigradio.gigradio.model.songkick.event;

public class ResultsPage{
   	private ClientLocation clientLocation;
   	private Number page;
   	private Number perPage;
   	private Results results;
   	private String status;
   	private Number totalEntries;

    public ClientLocation getClientLocation() {
        return clientLocation;
    }

    public Number getPage() {
        return page;
    }

    public Number getPerPage() {
        return perPage;
    }

    public Results getResults() {
        return results;
    }

    public String getStatus() {
        return status;
    }

    public Number getTotalEntries() {
        return totalEntries;
    }
}
