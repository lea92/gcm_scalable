package fr.scale.gcm_scalable.a.learn.prog.adl.withmape;

import java.util.Map;

import org.objectweb.fractal.adl.ADLException;
import org.objectweb.fractal.adl.Factory;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.Type;
import org.objectweb.fractal.api.control.IllegalBindingException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;
import org.objectweb.proactive.core.component.Constants;
import org.objectweb.proactive.core.component.Utils;
import org.objectweb.proactive.core.component.exceptions.NoSuchComponentException;
import org.objectweb.proactive.core.component.identity.PAComponent;
import org.objectweb.proactive.extensions.autonomic.adl.AFactory;
import org.objectweb.proactive.extensions.autonomic.adl.AFactoryFactory;
import org.objectweb.proactive.extensions.autonomic.controllers.remmos.Remmos;

import fr.scale.gcm_scalable.a.learn.prog.commun.ASameStructureADL;
import fr.scale.gcm_scalable.a.learn.prog.commun.CST;
import fr.scale.gcm_scalable.a.learn.prog.commun.elements.MasterImpl;

public class MapeStructureADL extends ASameStructureADL{


	public MapeStructureADL(){
		init();
	}
	
	public MapeStructureADL(String adl){
		this.adl = adl;
		init();
	}

	//-----------------------------------------------------------------------
	//fin SLAVE
	//-----------------------------------------------------------------------



	public boolean deb = true;
	@Override
	public void start(Component comp) {
		try {
			CST.log("start all ");    
			PAComponent composite = (PAComponent) comp;

			
			Utils.getPAMembraneController(composite).startMembrane();
			Type type = composite.getFcType();

			if(composite.getComponentParameters().getHierarchicalType().equals(Constants.COMPOSITE)){
				for( Component sub : Utils.getPAContentController(composite).getFcSubComponents()){
					Utils.getPAMembraneController(sub).startMembrane();
				}
			}
			CST.log("start");     
			Utils.getPAGCMLifeCycleController(composite).startFc();

			if(deb){//A faire une seule fois
				Remmos.enableMonitoring(composite);
				deb= false;
			}
		} catch (IllegalLifeCycleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchInterfaceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	public void stop(Component comp) {
		try {

			PAComponent composite = (PAComponent) comp;
			System.out.println("stopmb");    
			Utils.getPAMembraneController(composite).stopMembrane();
			if(composite.getComponentParameters().getHierarchicalType().equals(Constants.COMPOSITE)){
				for( Component sub : Utils.getPAContentController(composite).getFcSubComponents()){
					Utils.getPAMembraneController(sub).stopMembrane();
				}
			}
			System.out.println("stop");     
			Utils.getPAGCMLifeCycleController(composite).stopFc();
		} catch (IllegalLifeCycleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchInterfaceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	public void stopstart(Component composite) {
		stop(composite);
		try {
			Thread.sleep(1000);
		
		start(composite);

		Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	@Override
	public Factory getFactory() {
		try {
			return AFactoryFactory.getAFactory();
		} catch (ADLException e) {
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public Component newComposite(String adl, Map<String, Object> context) {
		AFactory fac = (AFactory) factory;
		Component composite = null;
		try {
			composite = (Component) fac.newAutonomicComponent(adl,context
					);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return composite;
	}
	
	
	
	
}