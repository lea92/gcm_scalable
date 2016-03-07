package fr.scale.gcm_scalable.a.learn.oleksandra.p.classes;

import fr.scale.gcm_scalable.a.learn.oleksandra.p.types.*;
import fr.scale.gcm_scalable.a.learn.oleksandra.p.interfaces.*;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;

import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.api.control.IllegalBindingException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;
import org.objectweb.proactive.core.util.wrapper.IntWrapper;
import org.objectweb.proactive.core.util.wrapper.BooleanWrapper;


import org.objectweb.proactive.core.component.componentcontroller.AbstractPAComponentController;
import org.etsi.uri.gcm.util.GCM;

import java.util.List;
import java.io.Serializable;
import org.objectweb.proactive.Body;
import org.objectweb.proactive.core.component.body.ComponentRunActive;
import org.objectweb.fractal.api.control.AttributeController;
import org.objectweb.proactive.multiactivity.component.ComponentMultiActiveService;
import org.objectweb.proactive.core.component.Utils;

public class NodeClass1 extends AbstractPAComponentController implements Serializable, 
BindingController, ComponentRunActive, NFInterface{



	@Override
	public void bindFc(String myClientItf, Object serverItf) throws NoSuchInterfaceException, IllegalBindingException, IllegalLifeCycleException {
		switch(myClientItf) {
		default:
			throw new NoSuchInterfaceException(myClientItf);
		}
	}


	@Override
	public String[] listFc() {
		return new String[0];
	}
	@Override
	public Object lookupFc(String myClientItf) throws NoSuchInterfaceException {
		switch(myClientItf) {
		default:
			throw new NoSuchInterfaceException(myClientItf);
		}
	}


	@Override
	public void unbindFc(String myClientItf) throws NoSuchInterfaceException {
		switch(myClientItf) {
		default:
			throw new NoSuchInterfaceException(myClientItf);
		}
	}


 	@Override
	public void runComponentActivity(Body body) {
		ComponentMultiActiveService multiActiveService = new ComponentMultiActiveService(body);
		while (body.isActive()) {
			multiActiveService.multiActiveServing();
		};
	}

/*
	@Override
	public void stopAndStart() {
		System.out.println("The controller was accessed");
		
			Utils.getPAGCMLifeCycleController(this.hostComponent).stopFc();
			Utils.getPAGCMLifeCycleController(this.hostComponent).startFc();
	}*/
	
	@Override
	public void stopHost() {
		System.out.println("The controller will now stop the host ... ");
		try{
			Utils.getPAGCMLifeCycleController(this.hostComponent).stopFc();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void startHost() {
		System.out.println("The controller will now start the host ... ");
		try{
			Utils.getPAGCMLifeCycleController(this.hostComponent).startFc();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void bindP12() {
		System.out.println("The controller will now bind S1 to P12 ... ");
		try{
			org.objectweb.fractal.api.Component[] comps =
					Utils.getPAContentController(this.hostComponent).getFcSubComponents();
			ISingle tgtItf = null;
			for(org.objectweb.fractal.api.Component c : comps) {
				String cName = GCM.getNameController(c).getFcName();
				if(cName.equals("P12")) {
					tgtItf = (ISingle) c.getFcInterface("S1");
				}
			}
			Utils.getPAMulticastController(	this.hostComponent).
				bindGCMMulticast("S1", tgtItf);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
ISingle tgtItf = (ISingle)this.getInterface(Container.CONTENT, tgtCompName, tgtItfName );
	Utils.getPAGCMLifeCycleController(this.hostComponent).stopFc();
	Thread.sleep(1000);
	Utils.getPAMulticastController(	this.hostComponent).bindGCMMulticast("C1-controller", tgtItf);
	Utils.getPAGCMLifeCycleController(this.hostComponent).startFc();
	*/

/*
	public Object getInterface(Container compContainer, String compName, String itfName) throws Exception {
		if(compContainer.equals(Container.HOST)) {
			return Utils.getPABindingController(this.hostComponent).lookupFc(itfName);
		}
		Object result= null;
		result = this.getHostSubcomponent(compContainer, compName).getFcInterface(itfName);
		return result;
	}
*/
	/*
	public org.objectweb.fractal.api.Component getHostSubcomponent(Container compContainer, String compName) throws Exception {
		org.objectweb.fractal.api.Component[] comps = null;
		if(compContainer.equals(Container.CONTENT)) {
			comps = Utils.getPAContentController(this.hostComponent).getFcSubComponents();
		}
		else if(compContainer.equals(Container.MEMBRANE)) {
			comps = Utils.getPAMembraneController(this.hostComponent).nfGetFcSubComponents();
		}
		for(org.objectweb.fractal.api.Component c : comps) {
			String cName = GCM.getNameController(c).getFcName();
			if(cName.equals(compName)) {
				return c;
			}
		}
		return null;
	}

	public String getHostReconfBindings() {
		return this.itfsMap.toString() + " " + this.compsMap.toString();
	}

	public void setHostReconfBindings(String val) {
		this.itfsMap = new HashMap<String, List<String>>();
		this.compsMap = new HashMap<String, List<String>>();
		
		String[] reconfItfs = val.split(";");
		for(String reconfItf : reconfItfs) {
			String[] src_tgts = reconfItf.split(":");
			String src = src_tgts[0];
			String[] tgts = src_tgts[1].substring(1, src_tgts[1].length()-1).split(",");
			
			List<String> tgtItfs =  new ArrayList<String>(tgts.length);
			List<String> tgtComps = new ArrayList<String>(tgts.length);
			for(String tgt : tgts) {
				tgtItfs.add(tgt.split("\\.")[1]);
				tgtComps.add(tgt.split("\\.")[0]);
			}
			this.compsMap.put(src, tgtComps);
			this.itfsMap.put(src, tgtItfs);
		}
	} 
*/
}

