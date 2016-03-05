package fr.scale.gcm_scalable.visualisation.content.model;

import fr.scale.gcm_scalable.visualisation.content.MyComponent;

/**
 * 
 * @author lelbeze
 *
 */
public class MyLink {


	/**
	 * Component client
	 */
	protected MyComponent source;
	
	/**
	 * Component server
	 */
	protected MyComponent destination;
	
	protected String name;
	
	public MyLink(String name){
		this.name = name;
	}
	
	
	//----------------------------------------------------------
	//GETTER AND SETTER
	//----------------------------------------------------------
	
	public MyComponent getSource(){
		return source;
	}
	
	public void setSource(MyComponent mo){
		this.source = mo;
	}
	
	public MyComponent getDestination(){
		return destination;
	}
	
	public void setDestination(MyComponent mo){
		this.destination = mo;
	}
	
	public String getName(){
		return name;
	}
}
