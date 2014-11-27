
package com.getgigradio.gigradio.model.songkick.location;

public class ResultsPage{
   	private Number page;
   	private Number perPage;
   	private Results results;
   	private String status;
   	private Number totalEntries;

 	public Number getPage(){
		return this.page;
	}
 	public Number getPerPage(){
		return this.perPage;
	}
 	public Results getResults(){
		return this.results;
	}
 	public String getStatus(){
		return this.status;
	}
 	public Number getTotalEntries(){
		return this.totalEntries;
	}
}
