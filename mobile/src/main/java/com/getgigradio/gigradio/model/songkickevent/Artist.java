
package com.getgigradio.gigradio.model.songkickevent;

import java.util.List;

public class Artist{
   	private String displayName;
   	private Number id;
   	private List identifier;
   	private String uri;

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
 	public List getIdentifier(){
		return this.identifier;
	}
	public void setIdentifier(List identifier){
		this.identifier = identifier;
	}
 	public String getUri(){
		return this.uri;
	}
	public void setUri(String uri){
		this.uri = uri;
	}
}
