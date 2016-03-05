package fr.scale.gcm_scalable.a.learn.commun;

import org.objectweb.proactive.core.component.type.PAGCMTypeFactory;

public class CST {

	/**
	 * singleton
	 */
	public static String SC = PAGCMTypeFactory.SINGLETON_CARDINALITY;
	public static String MC = PAGCMTypeFactory.MULTICAST_CARDINALITY;
	public static boolean MND = PAGCMTypeFactory.MANDATORY;
	public static boolean OPT = PAGCMTypeFactory.OPTIONAL;
	public static boolean CLI = PAGCMTypeFactory.CLIENT;
	public static boolean SRV = PAGCMTypeFactory.SERVER;


	public static int NB = 0;


	public static void log(String msg){
		System.out.println("[LEA] >> "+NB+" : "+msg);
		NB++;
	}
	
}
