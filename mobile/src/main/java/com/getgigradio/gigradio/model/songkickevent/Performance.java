
package com.getgigradio.gigradio.model.songkickevent;

public class Performance{
   	private Artist artist;
   	private String billing;
   	private Number billingIndex;
   	private String displayName;
   	private Number id;

 	public Artist getArtist(){
		return this.artist;
	}
	public void setArtist(Artist artist){
		this.artist = artist;
	}
 	public String getBilling(){
		return this.billing;
	}
	public void setBilling(String billing){
		this.billing = billing;
	}
 	public Number getBillingIndex(){
		return this.billingIndex;
	}
	public void setBillingIndex(Number billingIndex){
		this.billingIndex = billingIndex;
	}
 	public String getDisplayName(){
		return this.displayName;
	}
	public void setDisplayName(String displayName){
		this.displayName = displayName;
	}
 	public Number getId(){
		return this.id;
	}
	public void setId(Number id){
		this.id = id;
	}
}
