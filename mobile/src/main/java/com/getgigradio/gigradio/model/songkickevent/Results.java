
package com.getgigradio.gigradio.model.songkickevent;

import java.util.List;

public class Results{
   	private List<Event> event;

 	public List<Event> getEvents(){
		return this.event;
	}
	public void setEvent(List<Event> event){
		this.event = event;
	}
}
