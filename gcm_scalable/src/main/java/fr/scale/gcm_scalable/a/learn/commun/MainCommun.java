package fr.scale.gcm_scalable.a.learn.commun;

import java.util.Arrays;
import java.util.List;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.proactive.core.component.type.PAGCMTypeFactory;

import fr.scale.gcm.scalable.simple.masterslave.ItfRunner;

public class MainCommun {

	
	/**
	 * singleton
	 */
	public static String SC = PAGCMTypeFactory.SINGLETON_CARDINALITY;
	public static String MC = PAGCMTypeFactory.MULTICAST_CARDINALITY;
	public static boolean MND = PAGCMTypeFactory.MANDATORY;
	public static boolean OPT = PAGCMTypeFactory.OPTIONAL;
	public static boolean CLI = PAGCMTypeFactory.CLIENT;
	public static boolean SRV = PAGCMTypeFactory.SERVER;

	
	
	public static void test(Component composite,String nametest) throws NoSuchInterfaceException, InterruptedException{
		System.out.println("---------------------");
		System.out.println("Debut test : "+nametest);
		System.out.println("---------------------");

		ItfRunner i1 = (ItfRunner) composite.getFcInterface("runner");
		List<String> li = Arrays.asList(nametest);
		i1.run(li);
		Thread.sleep(1000);
		System.out.println("---------------------");
		System.out.println("Fin test :"+nametest);
		System.out.println("---------------------");

	}


}
