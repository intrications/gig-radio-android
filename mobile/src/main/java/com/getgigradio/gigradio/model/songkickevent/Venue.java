
package com.getgigradio.gigradio.model.songkickevent;

public class Venue{
   	private String displayName;
   	private String id;
   	private String lat;
   	private String lng;
   	private MetroArea metroArea;
   	private String uri;

 	public String getDisplayName(){
		return this.displayName;
	}
	public void setDisplayName(String displayName){
		this.displayName = displayName;
	}
 	public String getId(){
		return this.id;
	}
	public void setId(String id){
		this.id = id;
	}
 	public String getLat(){
		return this.lat;
	}
	public void setLat(String lat){
		this.lat = lat;
	}
 	public String getLng(){
		return this.lng;
	}
	public void setLng(String lng){
		this.lng = lng;
	}
 	public MetroArea getMetroArea(){
		return this.metroArea;
	}
	public void setMetroArea(MetroArea metroArea){
		this.metroArea = metroArea;
	}
 	public String getUri(){
		return this.uri;
	}
	public void setUri(String uri){
		this.uri = uri;
	}
}
