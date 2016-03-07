package fr.scale.gcm_scalable.a.learn.prog.adl.withmembrane;

import java.util.HashMap;
import java.util.Map;

import org.etsi.uri.gcm.util.GCM;
import org.objectweb.fractal.adl.ADLException;
import org.objectweb.fractal.adl.Factory;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;
import org.objectweb.proactive.core.component.Utils;
import org.objectweb.proactive.core.component.adl.FactoryFactory;
import org.objectweb.proactive.core.component.factory.PAGenericFactory;
import org.objectweb.proactive.core.component.type.PAGCMTypeFactory;

import fr.scale.gcm_scalable.a.learn.prog.commun.ASameStructure;
import fr.scale.gcm_scalable.a.learn.prog.commun.ASameStructureADL;

public class MembStruct extends ASameStructureADL{

	public MembStruct(){
		init();
	}
	
	public MembStruct(String adl){
		this.adl = adl;
		init();
	}
	

	@Override
	public void stop(Component composite) {
		try {
			Utils.getPAGCMLifeCycleController(composite).stopFc();
		} catch (IllegalLifeCycleException e) {
			e.printStackTrace();
		} catch (NoSuchInterfaceException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void start(Component composite) {
		try {
			Utils.getPAGCMLifeCycleController(composite).startFc();
		} catch (IllegalLifeCycleException e) {
			e.printStackTrace();
		} catch (NoSuchInterfaceException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stopstart(Component composite) {
		try {
			stop(composite);
			Thread.sleep(1000);
			start(composite);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public Factory getFactory() {
		try {
			return  FactoryFactory.getFactory();
		} catch (ADLException e) {
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public Component newComposite(String adl, Map<String, Object> context) {
		try {
			return (Component) factory.newComponent(adl, context);
		} catch (ADLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
