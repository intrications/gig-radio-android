
package com.getgigradio.gigradio.model.songkickevent;

public class ClientLocation{
   	private String ip;
   	private Number lat;
   	private Number lng;
   	private Number metroAreaId;

 	public String getIp(){
		return this.ip;
	}
	public void setIp(String ip){
		this.ip = ip;
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
 	public Number getMetroAreaId(){
		return this.metroAreaId;
	}
	public void setMetroAreaId(Number metroAreaId){
		this.metroAreaId = metroAreaId;
	}
}
