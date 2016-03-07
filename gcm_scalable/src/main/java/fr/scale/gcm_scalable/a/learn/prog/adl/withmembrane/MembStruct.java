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

public class MembStruct extends ASameStructure{

	Component composite, slave1, slave2, master,elm2;
	
	public boolean isAPI(){
		return false;
	}
	
	public MembStruct(){
		try {
			Factory factory = FactoryFactory.getFactory();
			
			final String adl = "fr.scale.gcm_scalable.a.learn.prog.commun.elements.adl.Composite";
			Map<String, Object> context = new HashMap<String, Object>();
			composite = (Component) factory.newComponent(
					adl, context);
		
		for(Component ca : GCM.getContentController(composite).getFcSubComponents()){
			String name = GCM.getNameController(ca).getFcName();
			if(name.equals("Slave")){
				slave1 = ca;
			}else if(name.equals("Master")){
				master = ca;
			}else if(name.equals("Slave2")){
				slave2 = ca;
			}else if(name.equals("Elm2")){
				elm2 = ca;
			}
		};
		
		} catch (ADLException e) {
			e.printStackTrace();
		} catch (NoSuchInterfaceException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Component createComposite() {
		return composite;
	}

	@Override
	public Component createMasterMulticast() {
		return master;
	}

	@Override
	public Component createSlave1() {
		return slave1;
	}

	@Override
	public Component createSlave2() {
		return slave2;
	}

	@Override
	public Component createElmt2() {
		return elm2;
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

}
