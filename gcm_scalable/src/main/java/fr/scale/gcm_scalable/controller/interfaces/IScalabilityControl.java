package fr.scale.gcm_scalable.controller.interfaces;

import java.io.Serializable;

public interface IScalabilityControl extends  Serializable{
	
	/**
	 * number instance of this component
	 * @return
	 */
	//public List<Integer> getNumberInstance();

	/**
	 * increases the number of instance 
	 */
	public void increase();
	
	/**
	 * decreases the number of instance
	 */
	public void decrease();
	
	
	/**
	 * get all informations .... 
	 * @return
	 */
	public String getInfo();
	
	public Integer getNbInst();
	
	/**
	 * Number instance choose
	 * @param nbInstance
	 */
	public void setNbInst(int nbInstance);
	

	/**
	 * Number instance real
	 * @param nbInstanceReal
	 */
	public void setNbInstReal(int nbInstanceReal);
	
	public Integer getNbInstReal();
}