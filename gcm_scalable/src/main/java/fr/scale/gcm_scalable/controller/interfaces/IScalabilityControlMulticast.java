package fr.scale.gcm_scalable.controller.interfaces;

import java.io.Serializable;
import java.util.List;

public interface IScalabilityControlMulticast extends  Serializable{
	
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
	public List<String> getInfo();
	

	public List<Integer> getNbInst();
	
	public void setNbInst(int nbInstance);
	

	/**
	 * Number instance real
	 * @param nbInstanceReal
	 */
	public void setNbInstReal(int nbInstanceReal);
	
	public List<Integer> getNbInstReal();
}