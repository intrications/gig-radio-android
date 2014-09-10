
package com.getgigradio.gigradio.model.songkickevent;

public class MetroArea{
   	private Country country;
   	private String displayName;
   	private Number id;
   	private String uri;

 	public Country getCountry(){
		return this.country;
	}
	public void setCountry(Country country){
		this.country = country;
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
 	public String getUri(){
		return this.uri;
	}
	public void setUri(String uri){
		this.uri = uri;
	}
}
