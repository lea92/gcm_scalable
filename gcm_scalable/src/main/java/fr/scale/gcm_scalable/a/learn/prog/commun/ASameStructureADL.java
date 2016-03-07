package fr.scale.gcm_scalable.a.learn.prog.commun;

import java.util.HashMap;
import java.util.Map;

import org.etsi.uri.gcm.util.GCM;
import org.objectweb.fractal.adl.ADLException;
import org.objectweb.fractal.adl.Factory;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;

public abstract class ASameStructureADL extends ASameStructure {

	public abstract Factory getFactory();
	public abstract Component newComposite(String adl, Map<String, Object> context);

	Component composite, slave1, slave2, master,elm2;
	protected Factory factory;
	
	protected String adl = "fr.scale.gcm_scalable.a.learn.prog.commun.elements.adl.Composite";
	
	public boolean isAPI(){
		return false;
	}
	
	public void init(){
		try {
			factory = getFactory();
			
			Map<String, Object> context = new HashMap<String, Object>();
			composite = newComposite(adl,context);
			System.out.println(composite);
		
		for(Component ca : GCM.getContentController(composite).getFcSubComponents()){
			String name = GCM.getNameController(ca).getFcName();
			if(name.equals("Slave")){
				slave1 = ca;
			}else if(name.equals("Master")){
				master = ca;
			}else if(name.equals("Slave2")){
				slave2 = ca;
			}else if(name.equals("Elmt2")){
				elm2 = ca;
			}
		};
		
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
	
	
}
