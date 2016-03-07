package fr.scale.gcm_scalable.a.learn.oleksandra;

import java.util.HashMap;
import java.util.Map;

import org.etsi.uri.gcm.util.GCM;
import org.objectweb.fractal.adl.Factory;
import org.objectweb.fractal.api.Component;
import org.objectweb.proactive.extra.component.fscript.Utils;

import fr.scale.gcm_scalable.a.learn.oleksandra.p.interfaces.*;


public class Main {
	public static void main (String[] args) {
		try {
			Component component = launchComponent("fr.scale.gcm_scalable.a.learn.oleksandra.Comp1");
		//	LifeCycleController lcc = Utils.getPAGCMLifeCycleController(component);
		//	lcc.stopFc();
			
			
			
			ICollect ic = (ICollect)(component.getFcInterface("S1"));
			ic.addSubcomp(0);
			Thread.sleep(1000);
			
			NFInterface nfItf =  (NFInterface) component.getFcInterface("S1-controller");
			//(NFInterface) Utils.getPAMembraneController(component).nfGetFcSubComponent("P1").getFcInterface("S1");
			
			Thread.sleep(1000);
			nfItf.bindP12();
			

			/*
			System.out.println("Trying to call F interface...");
			ic.addSubcomp(1);
			System.out.println("I will wait a bit to see if it works...");
			Thread.sleep(10000);
			
			System.out.println("I will start the functional part...");
			nfItf.startHost();
			Thread.sleep(1000);
			
			System.out.println("Trying to call F interface...");
			ic.addSubcomp(2);
	*/
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//launch a component from ADL, launch Monitor and KeyStorage and bind the component 
	//to the monitor and keyStorage
	private static Component launchComponent (String adl) throws Exception {
		  Factory f = org.objectweb.proactive.core.component.adl.FactoryFactory.getFactory();// getFactory();
		  Map<String, Object> context = new HashMap<String, Object>();
		  
		  //create components from ADL
		  Component mainComponent = (Component) f.newComponent(adl, context);
		  GCM.getLifeCycleController(mainComponent).startFc();
		  return mainComponent;
	}
	

}
