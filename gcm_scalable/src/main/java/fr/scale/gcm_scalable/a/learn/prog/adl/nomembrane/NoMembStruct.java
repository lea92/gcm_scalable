package fr.scale.gcm_scalable.a.learn.prog.adl.nomembrane;

import java.util.Map;
import org.objectweb.fractal.adl.ADLException;
import org.objectweb.fractal.adl.Factory;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;
import org.objectweb.proactive.core.component.Utils;
import org.objectweb.proactive.core.component.adl.FactoryFactory;
import fr.scale.gcm_scalable.a.learn.prog.commun.ASameStructureADL;

public class NoMembStruct extends ASameStructureADL{

	public NoMembStruct(){
		init();
	}
	
	public NoMembStruct(String adl){
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
