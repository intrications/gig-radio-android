
package com.getgigradio.gigradio.model.songkick.location;

public class MetroArea{
   	private Country country;
   	private String displayName;
   	private Number id;
   	private Number lat;
   	private Number lng;
   	private String uri;

 	public Country getCountry(){
		return this.country;
	}
 	public String getDisplayName(){
		return this.displayName;
	}
 	public Number getId(){
		return this.id;
	}
 	public Number getLat(){
		return this.lat;
	}
 	public Number getLng(){
		return this.lng;
	}
 	public String getUri(){
		return this.uri;
	}
}
