
package com.getgigradio.gigradio.model.songkick.location;

public class City{
   	private Country country;
   	private String displayName;
   	private Number lat;
   	private Number lng;

 	public Country getCountry(){
		return this.country;
	}
 	public String getDisplayName(){
		return this.displayName;
	}
 	public Number getLat(){
		return this.lat;
	}
 	public Number getLng(){
		return this.lng;
	}
}
