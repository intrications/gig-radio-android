
package com.getgigradio.gigradio.model.songkickevent;

public class Location{
   	private String city;
   	private Number lat;
   	private Number lng;

 	public String getCity(){
		return this.city;
	}
	public void setCity(String city){
		this.city = city;
	}
 	public Number getLat(){
		return this.lat;
	}
	public void setLat(Number lat){
		this.lat = lat;
	}
 	public Number getLng(){
		return this.lng;
	}
	public void setLng(Number lng){
		this.lng = lng;
	}
}
