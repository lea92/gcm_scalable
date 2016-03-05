package fr.scale.gcm_scalable.controller.components;

import java.io.Serializable;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.objectweb.proactive.core.util.ProActiveInet;
import org.objectweb.proactive.core.util.log.ProActiveLogger;

import fr.scale.gcm_scalable.commun.CST;
import fr.scale.gcm_scalable.controller.interfaces.IScalabilityControl;


public class WrapScaleImpl implements  IScalabilityControl, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final static Logger logger = ProActiveLogger.getLogger(CST.APP_SCALE_LOGGER);

	protected long metricDelay = 30000; 
	
	private int nbInstance = 0;
	private int nbInstanceReal = 0;
	
	/** finds the name of the local machine */
	static String getHostName() {
		return ProActiveInet.getInstance().getInetAddress().getHostName();
	}
	
	
	public WrapScaleImpl(){
		//JOptionPane.showConfirmDialog(null,"a new wrap increased "+getHostName()+" from component ");		

	}
	
	
	

	@Override
	public void increase() {
		System.out.println("increase !!!! ");
		//JOptionPane.showConfirmDialog(null,"a new wrap increased "+getHostName()+" from component ");		
	}

	@Override
	public void decrease() {
		
	}



	public Integer getNbInst(){
		return nbInstance;
	}
	
	public void setNbInst(int nbInstance){
		this.nbInstance = nbInstance;
	}


	@Override
	public String getInfo() {
		StringBuilder strb = new StringBuilder();
		strb.append("hello it's me \n");

		strb.append("--------------------------------\n");
		strb.append("Identification....");
		strb.append("--------------------------------\n");
		strb.append("Host Name  = "+ getHostName()+"\n");
		strb.append("#Component = "+ this.getNbInst()+"\n");

		strb.append("--------------------------------\n");
		strb.append("Metrics ....");
		strb.append("--------------------------------\n");
		
		
		
		JOptionPane.showConfirmDialog(null,strb.toString());		
			
		return strb.toString();
	}


	@Override
	public void setNbInstReal(int nbInstance) {
		nbInstanceReal = nbInstance;
	}


	@Override
	public Integer getNbInstReal() {
		return nbInstanceReal;
	}


}
