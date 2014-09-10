
package com.getgigradio.gigradio.model.songkickevent;

public class ResultsPage{
   	private ClientLocation clientLocation;
   	private Number page;
   	private Number perPage;
   	private Results results;
   	private String status;
   	private Number totalEntries;

 	public ClientLocation getClientLocation(){
		return this.clientLocation;
	}
	public void setClientLocation(ClientLocation clientLocation){
		this.clientLocation = clientLocation;
	}
 	public Number getPage(){
		return this.page;
	}
	public void setPage(Number page){
		this.page = page;
	}
 	public Number getPerPage(){
		return this.perPage;
	}
	public void setPerPage(Number perPage){
		this.perPage = perPage;
	}
 	public Results getResults(){
		return this.results;
	}
	public void setResults(Results results){
		this.results = results;
	}
 	public String getStatus(){
		return this.status;
	}
	public void setStatus(String status){
		this.status = status;
	}
 	public Number getTotalEntries(){
		return this.totalEntries;
	}
	public void setTotalEntries(Number totalEntries){
		this.totalEntries = totalEntries;
	}
}
